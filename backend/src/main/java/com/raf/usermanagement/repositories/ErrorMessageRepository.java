package com.raf.usermanagement.repositories;

import com.raf.usermanagement.model.ErrorMessage;
import com.raf.usermanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Collection;

public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Collection<ErrorMessage> findAllByVacuum_CreatedBy (User user);
}
