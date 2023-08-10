package com.branch.quarter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuarterEditRequest {
    private Long id;
    private String quarterName;
    private LocalDate fromDate;
    private LocalDate toDate;
}
