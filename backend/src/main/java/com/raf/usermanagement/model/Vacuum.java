package com.raf.usermanagement.model;

import com.raf.usermanagement.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacuum {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    private User createdBy;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate creationDate;
}
