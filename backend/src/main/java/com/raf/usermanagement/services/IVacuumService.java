package com.raf.usermanagement.services;

import com.raf.usermanagement.model.Vacuum;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IVacuumService {
    Vacuum createVacuum(String name, String userMail);
    Optional<Vacuum> findById(Long id);

    @Transactional
    Optional<Vacuum> findByIdAndCreatedByEmail(Long id, String userMail);

    Collection<Vacuum> getVacuumsByUser(String userMail);
    Collection<Vacuum> searchVacuums(String name, List<String> statuses, LocalDate dateFrom, LocalDate dateTo, String userMail);
    void destroyVacuum(Long id);
    void startVacuum(Long id,boolean scheduled) throws InterruptedException;
    void stopVacuum(Long id, boolean scheduled) throws InterruptedException;
    void dischargeVacuum(Long id, boolean scheduled) throws InterruptedException, ParseException;
    void scheduleVacuum(Long id, Date date, String action) throws ParseException;
}
