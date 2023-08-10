package com.branch.quarter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "quarter")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Quarter{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quarter_name")
    private String quaraterName;
    @Column(name= "from_date")
    private LocalDate fromDate;
    @Column(name= "to_date")
    private LocalDate toDate;

    @Column(name = "creation_date")
    private LocalDate creationDate = LocalDate.now();
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "status",columnDefinition = "boolean default true")
    private boolean isOpen = true;

    @Column(name = "active",columnDefinition = "boolean default true")
    private boolean isActive = true;
}
