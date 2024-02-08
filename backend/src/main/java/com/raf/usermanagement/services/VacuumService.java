package com.raf.usermanagement.services;

import com.raf.usermanagement.model.ErrorMessage;
import com.raf.usermanagement.model.User;
import com.raf.usermanagement.model.Vacuum;
import com.raf.usermanagement.model.enums.Status;
import com.raf.usermanagement.repositories.ErrorMessageRepository;
import com.raf.usermanagement.repositories.UserRepository;
import com.raf.usermanagement.repositories.VacuumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VacuumService implements IVacuumService {

    private VacuumRepository vacuumRepository;
    private UserRepository userRepository;
    private TaskScheduler taskScheduler;
    private ErrorMessageRepository errorMessageRepository;

    @Autowired
    public VacuumService(VacuumRepository vacuumRepository, UserRepository userRepository, TaskScheduler taskScheduler, ErrorMessageRepository errorMessageRepository) {
        this.vacuumRepository = vacuumRepository;
        this.userRepository = userRepository;
        this.taskScheduler = taskScheduler;
        this.errorMessageRepository = errorMessageRepository;
    }

    @Override
    @Transactional
    public Optional<Vacuum> findById(Long id) {
        return vacuumRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Vacuum> findByIdAndCreatedByEmail(Long id, String userMail) {
        return vacuumRepository.findByIdAndCreatedByEmail(id, userMail);
    }

    @Override
    @Transactional
    public Collection<Vacuum> getVacuumsByUser(String userMail) {
        return vacuumRepository.findAllByCreatedBy(userRepository.findByEmail(userMail));
    }


    @Override
    @Transactional
    public Collection<Vacuum> searchVacuums(String name, List<String> statuses, LocalDate dateFrom, LocalDate dateTo, String userMail) {
        List<Status> statusesInt = new ArrayList<>();
        if (statuses.isEmpty()) {
            statusesInt = Arrays.asList(Status.OFF, Status.ON, Status.DISCHARGING);
        }else{
            // convert intigers to Status
            for (String status : statuses) {
                switch (status) {
                    case "0":
                        statusesInt.add(Status.OFF);
                        break;
                    case "1":
                        statusesInt.add(Status.ON);
                        break;
                    case "2":
                        statusesInt.add(Status.DISCHARGING);
                        break;
                }
            }
        }
        if(dateFrom == null) dateFrom = LocalDate.of(1970, 1, 1);
        if(dateTo == null) dateTo = LocalDate.now();

        return vacuumRepository.searchVacuums(name, statusesInt, dateFrom, dateTo, userMail);
    }

    @Override
    public Vacuum createVacuum(String name, String userMail) {
        User user = userRepository.findByEmail(userMail);
        return vacuumRepository.save(new Vacuum(0L, Status.OFF, user, true, name, LocalDate.now()/*, 0*/));
    }

    @Transactional
    public Collection<ErrorMessage> findAllErrorsForUser(String userMail){
        return errorMessageRepository.findAllByVacuum_CreatedBy(userRepository.findByEmail(userMail));
    }

    @Override
    @Transactional
    public void destroyVacuum(Long id) {
        Optional<Vacuum> optionalVacuum = this.findById(id);
        if (optionalVacuum.isPresent()) {
            Vacuum vacuum = optionalVacuum.get();
            if (vacuum.getStatus() != Status.OFF) return;
            vacuum.setActive(false);
            vacuumRepository.save(vacuum);
        }
    }

    @Override
    @Async
    @Transactional
    public void startVacuum(Long id, boolean scheduled) throws InterruptedException {
        Optional<Vacuum> optionalVacuum = vacuumRepository.findById(id);
        if(optionalVacuum.isPresent()) {
            Vacuum vacuum = optionalVacuum.get();
            if(vacuum.isActive()) {
                if (vacuum.getStatus() == Status.OFF) {
                    Thread.sleep((long) (Math.random() * (15000 - 10000) + 10000));
                    vacuum.setStatus(Status.ON);
                    vacuumRepository.save(vacuum);
                } else
                if(scheduled)
                    errorMessageRepository.save(new ErrorMessage(0L, "The vacuum's status is not 'OFF'.", "ON", LocalDate.now(), vacuum));
            } else
            if(scheduled)
                errorMessageRepository.save(new ErrorMessage(0L, "The vacuum is deactivated.", "ON",LocalDate.now(), vacuum));
        }
    }


    @Override
    @Async
    @Transactional
    public void stopVacuum(Long id, boolean scheduled) throws InterruptedException {
        Optional<Vacuum> optionalVacuum = vacuumRepository.findById(id);
        if(optionalVacuum.isPresent()) {
            Vacuum vacuum = optionalVacuum.get();
            if(vacuum.isActive()) {
                if (vacuum.getStatus() == Status.ON) {
                    Thread.sleep((long) (Math.random() * (15000 - 10000) + 10000));
                    vacuum.setStatus(Status.OFF);
                    vacuumRepository.save(vacuum);
                } else
                if(scheduled)
                    errorMessageRepository.save(new ErrorMessage(0L, "The vacuum's status is not 'ON'.", "OFF", LocalDate.now(), vacuum));
            } else
            if(scheduled)
                errorMessageRepository.save(new ErrorMessage(0L, "The vacuum is deactivated.", "OFF",LocalDate.now(), vacuum));
        }
    }

    @Override
    @Async
    @Transactional
    public void dischargeVacuum(Long id, boolean scheduled) throws InterruptedException {
        Optional<Vacuum> optionalVacuum = vacuumRepository.findById(id);
        if(optionalVacuum.isPresent()) {
            Vacuum vacuum = optionalVacuum.get();
            if(vacuum.isActive()) {
                if (vacuum.getStatus() == Status.ON) {
                    Thread.sleep((long) (Math.random() * (10000 - 5000) + 5000));
                    vacuum.setStatus(Status.DISCHARGING);
                    vacuumRepository.save(vacuum);

                    vacuum = this.findById(id).get();

                    Thread.sleep((long) (Math.random() * (10000 - 5000) + 5000));
                    vacuum.setStatus(Status.OFF);
                    vacuumRepository.save(vacuum);
                } else
                if(scheduled)
                    errorMessageRepository.save(new ErrorMessage(0L, "The vacuum's status is not 'ON'.", "DISCHARGE", LocalDate.now(), vacuum));
            } else
            if(scheduled)
                errorMessageRepository.save(new ErrorMessage(0L, "The vacuum is deactivated.", "DISCHARGE",LocalDate.now(), vacuum));
        }
    }

    @Override
    @Transactional
    public void scheduleVacuum(Long id, Date date, String action) throws ParseException {
        this.taskScheduler.schedule(() -> {
            try {
                switch (action.toUpperCase()) {
                    case "ON":
                        startVacuum(id, true);
                        break;
                    case "OFF":
                        stopVacuum(id, true);
                        break;
                    case "DISCHARGE":
                        dischargeVacuum(id, true);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, date);
    }

}
