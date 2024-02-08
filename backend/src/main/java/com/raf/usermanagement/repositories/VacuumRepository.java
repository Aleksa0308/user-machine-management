package com.raf.usermanagement.repositories;

import com.raf.usermanagement.model.User;
import com.raf.usermanagement.model.Vacuum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import javax.persistence.LockModeType;
import javax.transaction.Status;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface VacuumRepository extends JpaRepository<Vacuum, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Collection<Vacuum> findAllByCreatedBy(User user);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    public Optional<Vacuum> findById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    public Optional<Vacuum> findByIdAndCreatedByEmail(Long id, String userMail);

    //Find All by Name And CreatedBy
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Collection<Vacuum> findAllByNameAndCreatedBy(String name, User user);

    // search vacuums by name, status, dateFrom, dateTo and createdBy
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT v FROM Vacuum v WHERE v.name LIKE %:name% AND v.status in :statuses AND v.creationDate >= :dateFrom AND v.creationDate <= :dateTo AND v.createdBy.email = :userMail")
    public Collection<Vacuum> searchVacuums(@Param("name") String name, @Param("statuses") List<com.raf.usermanagement.model.enums.Status> statuses, @Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo, @Param("userMail") String userMail);

}
