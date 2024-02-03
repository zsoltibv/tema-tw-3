package com.lab.model.model;

import com.lab.model.config.util.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * JPA Entities
 */
@Entity
@Getter @Setter
@Table(name="days_off")
public class DaysOff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name="end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Column(name="status")
    private Status status;

    @Column(name="message")
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;
}
