package com.branch.quarter.mapper;

import com.branch.quarter.dto.QuarterDto;
import com.branch.quarter.dto.QuarterEditRequest;
import com.branch.quarter.dto.QuarterRequest;
import com.branch.quarter.model.Quarter;

import java.time.LocalDate;
import java.util.Date;

public class QuarterMapper {
    public static Quarter quarterDtoToQuarter(QuarterDto quarterDto){
        return Quarter.builder()
                .id(quarterDto.getId())
                .quaraterName(quarterDto.getQuarterName())
                .fromDate(quarterDto.getFromDate())
                .toDate(quarterDto.getToDate())
                .creationDate(quarterDto.getCreationDate())
                .createdBy(quarterDto.getCreatedBy())
                .isOpen(quarterDto.isOpen())
                .build();
    }
    public static Quarter quarterEditRequestToQuarter(QuarterEditRequest quarterEditRequest){
        return Quarter.builder()
                .id(quarterEditRequest.getId())
                .quaraterName(quarterEditRequest.getQuarterName())
                .fromDate(quarterEditRequest.getFromDate())
                .toDate(quarterEditRequest.getToDate())
                .build();
    }
    public static Quarter quarterRequestToQuarter(QuarterRequest quarterRequest){
        return Quarter.builder()
                .quaraterName(quarterRequest.getQuarterName())
                .fromDate(quarterRequest.getFromDate())
                .toDate(quarterRequest.getToDate())
                .creationDate(LocalDate.now())
                .createdBy(quarterRequest.getCreatedBy())
                .isActive(true)
                .isOpen(true)
                .build();
    }

    public static QuarterDto quarterToQuarterDto(Quarter quarter){
        return QuarterDto.builder()
                .id(quarter.getId())
                .quarterName(quarter.getQuaraterName())
                .fromDate(quarter.getFromDate())
                .toDate(quarter.getToDate())
                .creationDate(quarter.getCreationDate())
                .createdBy(quarter.getCreatedBy())
                .isOpen(quarter.isOpen())
                .build();
    }
}
