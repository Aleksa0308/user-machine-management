package com.raf.usermanagement.controllers;

import com.raf.usermanagement.model.User;
import com.raf.usermanagement.model.Vacuum;
import com.raf.usermanagement.model.enums.Status;
import com.raf.usermanagement.repositories.VacuumRepository;
import com.raf.usermanagement.requests.CreateRequest;
import com.raf.usermanagement.requests.ScheduleRequest;
import com.raf.usermanagement.responses.UsersResponse;
import com.raf.usermanagement.responses.VacuumsResponse;
import com.raf.usermanagement.services.UserService;
import com.raf.usermanagement.services.VacuumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/vacuums")
public class VacuumController {
    private final VacuumService vacuumService;
    private final UserService userService;
    private final VacuumRepository vacuumRepository;

    @Autowired
    public VacuumController(VacuumService vacuumService, UserService userService, VacuumRepository vacuumRepository) {
        this.vacuumService = vacuumService;
        this.userService = userService;
        this.vacuumRepository = vacuumRepository;
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VacuumsResponse> getVacuumsFiltered(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathParam("name") String name,
            @PathParam("status") String status,
            @PathParam("dateFrom") String dateFrom,
            @PathParam("dateTo") String dateTo){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedFrom = null;
        LocalDate parsedTo = null;
        List<String> vacuumFilterStatuses = new ArrayList<>();

        if (!dateFrom.isEmpty()) parsedFrom = LocalDate.parse(dateFrom, formatter);
        if (!dateTo.isEmpty()) parsedTo = LocalDate.parse(dateTo, formatter);
        if (!status.isEmpty()) vacuumFilterStatuses = new ArrayList<>(Arrays.asList(status.split(",")));

        Collection<Vacuum> vacuums = vacuumService.searchVacuums(name, vacuumFilterStatuses, parsedFrom, parsedTo, userDetails.getUsername());
        VacuumsResponse vacuumsResponse = new VacuumsResponse();
        vacuumsResponse.setVacuums(vacuums);
        return ResponseEntity.ok().body(vacuumsResponse);
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VacuumsResponse> getVacuumsByUser(@AuthenticationPrincipal UserDetails userDetails){
        Collection<Vacuum> vacuums = vacuumService.getVacuumsByUser(userDetails.getUsername());
        VacuumsResponse vacuumsResponse = new VacuumsResponse();
        vacuumsResponse.setVacuums(vacuums);
        return ResponseEntity.ok().body(vacuumsResponse);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vacuum> getVacuumById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        Optional<Vacuum> vacuum = vacuumService.findByIdAndCreatedByEmail(id, userDetails.getUsername());
        return vacuum.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public Vacuum createVacuum(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CreateRequest createRequest){
        String name = createRequest.getName();
        String userMail = userDetails.getUsername();
        Vacuum vacuum = vacuumService.createVacuum(name, userMail);
        return vacuum;
    }

    @DeleteMapping(value = "/destroy/{id}")
    private ResponseEntity<?> destroyVacuum(@PathVariable("id") Long id){
        vacuumService.destroyVacuum(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/start/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> startVacuum(@PathVariable Long id) throws InterruptedException {

        Optional<Vacuum> optionalVacuum = vacuumService.findById(id);

        if(optionalVacuum.isPresent() && optionalVacuum.get().getStatus() == Status.OFF) {
            vacuumService.startVacuum(id, false);
            return ResponseEntity.ok(optionalVacuum.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/stop/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stopVacuum(@PathVariable Long id) throws InterruptedException {

        Optional<Vacuum> optionalVacuum = vacuumService.findById(id);

        if(optionalVacuum.isPresent() && optionalVacuum.get().getStatus() == Status.ON) {
            vacuumService.stopVacuum(id, false);
            return ResponseEntity.ok(optionalVacuum.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/discharge/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> restartVacuum(@PathVariable Long id) throws InterruptedException {

        Optional<Vacuum> optionalVacuum = vacuumService.findById(id);

        if(optionalVacuum.isPresent() && optionalVacuum.get().getStatus() == Status.ON) {
            vacuumService.dischargeVacuum(id, false);
            return ResponseEntity.ok(optionalVacuum.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/schedule")
    public ResponseEntity<?> scheduleVacuum(@RequestBody ScheduleRequest scheduleRequest) throws ParseException {
        vacuumService.scheduleVacuum(scheduleRequest.getId(),scheduleRequest.getDate(),scheduleRequest.getAction());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/errors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getErrorHistory(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(vacuumService.findAllErrorsForUser(userDetails.getUsername()));
    }

}
