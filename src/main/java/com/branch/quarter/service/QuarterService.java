package com.branch.quarter.service;

import com.branch.quarter.dto.QuarterEditRequest;
import com.branch.quarter.dto.QuarterRequest;
import com.branch.quarter.dto.ResponseModel;
import com.branch.quarter.mapper.QuarterMapper;
import com.branch.quarter.model.Quarter;
import com.branch.quarter.repository.QuarterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class QuarterService {
    //1800+ Logic Error REJECTED
    //1900+ DB Error FAILED
    //2000+ Success SUCCESS
    private final QuarterRepository quarterRepository;
    QuarterService(QuarterRepository quarterRepository){
        this.quarterRepository = quarterRepository;
    }
    //1890+,1990+,2090+
    public ResponseModel createQuarter(QuarterRequest quarterRequest){
        log.info("# Create Quarter Method Started Name:[" + quarterRequest.getQuarterName() + "] From:[" + quarterRequest.getFromDate() + "] To:[" + quarterRequest.getToDate() + "].");
        // Check If name Already Exists
        boolean duplicateFlag = false;
        try{
            log.info("# Creating Quarter Validating Duplicate Name.");
            duplicateFlag = duplicateNameValidation(quarterRequest.getQuarterName(),null);
        } catch (Exception ex){
            log.warn("# CODE:1990 DB Error [" + ex.getMessage() + "]");
            return ResponseModel.builder()
                    .message("FAILED")
                    .code(1990)
                    .data("Server Side Error, Please Contact Support For More Info.")
                    .build();
        }
        if(!duplicateFlag){
            log.warn("# CODE:1891 Duplicate Name While Creating Quarter Error [" + quarterRequest.getQuarterName() + "] Created By: ["+ quarterRequest.getCreatedBy()+"]");
            return ResponseModel.builder()
                    .message("REJECTED")
                    .code(1891)
                    .data("Quarter Already Exist With Name [" + quarterRequest.getQuarterName() + "]!")
                    .build();
        }

        Quarter periodFlag = null;
        // Check if There is a Quarter
        try {
            periodFlag = quarterPeriodValidation(quarterRequest.getFromDate(),quarterRequest.getToDate(),null);
        } catch (Exception ex){
            // DB error
            log.warn("# CODE:1991 DB Error [" + ex.getMessage() + "]");
            return ResponseModel.builder()
                    .message("FAILED")
                    .code(1991)
                    .data("Server Side Error, Please Contact Support For More Info.")
                    .build();

        }
        if (periodFlag == null) {
            log.info("# Create Quarter No Record Found For The Given Period Running...");
            Quarter createdQuarter;
            try {
                createdQuarter = quarterRepository.save(QuarterMapper.quarterRequestToQuarter(quarterRequest));
            }
            catch (Exception ex) {
                // DB error
                log.warn("# CODE:1992 DB Error [" + ex.getMessage() + "]");
                return ResponseModel.builder()
                        .message("FAILED")
                        .code(1992)
                        .data("Server Side Error, Please Contact Support For More Info.")
                        .build();
            }
            log.info("# CODE:2091 Quarter Created Successfully with ID: " + createdQuarter.getId());
            return ResponseModel.builder()
                    .message("SUCCESS")
                    .code(2091)
                    .data(createdQuarter)
                    .build();
        }
        else {
            log.warn("#  CODE:1892 Date Conflict While Creating Quarter Requested From [" + quarterRequest.getFromDate() + "] To [" + quarterRequest.getToDate()
             + "], With Conflicted Quarter ID: [" + periodFlag.getId() + "] From [" + periodFlag.getFromDate() + "] To [" + periodFlag.getToDate() + "]!");
            return ResponseModel.builder()
                    .message("REJECTED")
                    .code(1892)
                    .data("Quarter Already Exist Between [" + periodFlag.getFromDate() + "] To [" + periodFlag.getToDate() + "]!")
                    .build();
        }

    }

    //80+
    public ResponseModel getQuarterList(Pageable pageable){
        List<Quarter> quarterList = null;
        try{
            quarterList = quarterRepository.findAllByActiveIsTrue(pageable);
        }catch (Exception ex){
            // DB error
            log.warn("# CODE:1981 DB Error [" + ex.getMessage() + "]");
            return ResponseModel.builder()
                    .message("FAILED")
                    .code(1981)
                    .data("Server Side Error, Please Contact Support For More Info.")
                    .build();
        }
        log.info("# CODE:2081 Quarter List Fetched Successfully!");
        return ResponseModel.builder()
                .message("SUCCESS")
                .code(2081)
                .data(quarterList)
                .build();
    }
    // 70+
    public ResponseModel editQuarter(QuarterEditRequest quarterEditRequest){
        log.info("# Quarter Edit Request Initiated With ID: ["+ quarterEditRequest.getId() +"].");
        Quarter editedQuarter;
        try{
            log.info("# Validating Quarter ID And Fetching date");
            editedQuarter = quarterRepository.findQuarterById(quarterEditRequest.getId());
            log.info(editedQuarter.toString());
            if(editedQuarter == null) throw new ClassNotFoundException("Edit Request ID: ["+quarterEditRequest.getId()+"] Not Found.");
            log.info("# Starting Duplicate Name Validation From Editing Quarter");
            if(duplicateNameValidation(quarterEditRequest.getQuarterName(), quarterEditRequest.getId())){
                log.info("# Duplicate Name Validation Successful Starting Quarter Period Validation From Editing Quarter");
                Quarter periodFlag = quarterPeriodValidation(quarterEditRequest.getFromDate(),quarterEditRequest.getToDate(),quarterEditRequest.getId());
                if(periodFlag == null) {
                    log.info("# Period Validation Successful Editing...");
                    editedQuarter.setQuaraterName(quarterEditRequest.getQuarterName());
                    editedQuarter.setFromDate(quarterEditRequest.getFromDate());
                    editedQuarter.setToDate(quarterEditRequest.getToDate());
                    editedQuarter = quarterRepository.save(editedQuarter);
                    log.info("# Quarter Edited Successfully With ID: [" + quarterEditRequest.getId() +"].");
                } else {
                    log.warn("# CODE:1871 Edit Failed Date Conflict Found Edited Date From:[" + quarterEditRequest.getFromDate() + "] To:[" + quarterEditRequest.getToDate() +
                             "]. Conflicted Date From:[" + periodFlag.getFromDate() + "] To:[" + periodFlag.getToDate() + "] !");
                    return ResponseModel.builder()
                            .message("REJECTED")
                            .code(1871)
                            .data("Quarter Exist With Duration From " + periodFlag.getFromDate() + " To " + periodFlag.getToDate()+ "!")
                            .build();
                }
            } else {
                log.warn("# CODE:1872 Quarter Name Already Exist (" + quarterEditRequest.getQuarterName() +")!");
                return ResponseModel.builder()
                        .message("REJECTED")
                        .code(1872)
                        .data("Quarter Name Already Exist (" + quarterEditRequest.getQuarterName() +")!")
                        .build();
            }
            log.info("# CODE:2071 Quarter Edited Successfully With ID:["+quarterEditRequest.getId()+"].");
            return ResponseModel.builder()
                    .message("SUCCESS")
                    .code(2071)
                    .data(editedQuarter)
                    .build();
        } catch (Exception ex){
            log.warn("# CODE:1971 Quarter Editing Failed For ID: [" + quarterEditRequest.getId() + "]. " + ex.getMessage());
            return ResponseModel.builder()
                    .message("FAILED")
                    .code(1971)
                    .data("Server Side Error, Please Contact Support For More Info.")
                    .build();

        }
    }
    //60+
    public ResponseModel softDeleteQuarter(Long id){
        log.info("# Quarter Deletion Initiated With ID:["+ id + "].");
        try {
            if(quarterRepository.findQuarterById(id)==null) throw new ClassNotFoundException("No Record Found With ID:["+id+"].");
            if(visitsForQuarterCheckerIsEmpty(id)){
                log.info("### CODE:2060 Empty Quarter Soft Deleted With ID:[" + id + "].");
                quarterRepository.softDeleteQuarterById(id);
                return ResponseModel.builder()
                        .message("SUCCESS")
                        .code(2060)
                        .data("Quarter Deleted Successfully!")
                        .build();
            }
        } catch (Exception ex){
            log.warn("# CODE:1960 Quarter Deletion Failed For ID: [" + id + "]. " + ex.getMessage());
            return ResponseModel.builder()
                    .message("FAILED")
                    .code(1960)
                    .data("Server Side Error, Please Contact Support For More Info.")
                    .build();
        }
        log.warn("# CODE:1860 Quarter Deletion Canceled Due To Visits Exists Quarter ID:[" + id + "].");
        return ResponseModel.builder()
                .message("REJECTED")
                .code(1860)
                .data("Quarter Deletion Canceled, Visits Exist With In The Quarter.")
                .build();
    }
    // 50+
    public ResponseModel closeQuarter(Long id){
        log.info("# Closing Quarter Initiated With ID:["+ id + "].");
        try {
            if(quarterRepository.findQuarterById(id)==null) throw new ClassNotFoundException("No Record Found With ID:["+id+"].");
            if(visitsForQuarterIsCloseable(id)){
                log.info("### CODE:2050 Closing Quarter With ID:[" + id + "].");
                quarterRepository.closeQuarterById(id);
                return ResponseModel.builder()
                        .message("SUCCESS")
                        .code(2050)
                        .data("Quarter Closed Successfully!")
                        .build();
            }
        } catch (Exception ex){
            log.warn("# CODE:1950 Closing Quarter Failed For ID: [" + id + "]. " + ex.getMessage());
            return ResponseModel.builder()
                    .message("FAILED")
                    .code(1950)
                    .data("Server Side Error, Please Contact Support For More Info.")
                    .build();
        }
        // TODO
        log.warn("# CODE:1850 Closing Quarter Canceled Due To Visits Exists Quarter ID:[" + id + "].");
        return ResponseModel.builder()
                .message("REJECTED")
                .code(1850)
                .data("Closing Quarter Failed, Visits Exist With In The Quarter.")
                .build();
    }
    // 40+
    public ResponseModel getPageCount(Integer size){
        log.info("# Getting Page Count For Page Size:[" + size + "].");
        double pageCount ;
        try {
            pageCount = quarterRepository.getPageCount();
        } catch (Exception ex){
            log.warn("# CODE:1940 DB Error While Getting Page Count ["+ ex.getMessage() + "].");
            return ResponseModel.builder()
                    .message("FAILED")
                    .code(1940)
                    .data("Server Side Error, Please Contact Support For More Info.")
                    .build();
        }

        pageCount = Math.ceil(pageCount / size);
        log.info("# CODE:2040 Page Count Of [" + pageCount + "] Returned Successfully!");
        return ResponseModel.builder()
                .message("SUCCESS")
                .code(2040)
                .data(pageCount)
                .build();
    }
    // 30-34
    private boolean duplicateNameValidation(String quarterName,Long id){
        log.info("# Checking For Duplicate Name Validation!");
        boolean duplicateName = true ;
        try {
            Object flag;
            if(id==null) {
                flag = quarterRepository.existsByQuaraterNameAndActiveIsTrue(quarterName);
            }
                    else{
                flag = quarterRepository.existsByQuaraterNameAndActiveIsTrue(quarterName,id);
            };

            if(flag != null && flag.toString().equals("true") ) duplicateName = false;
        } catch (Exception ex){
            log.warn("# CODE:1930 DB Error [" + ex.getMessage() + "]");
            return false;
        }
        if(!duplicateName){
            log.warn("# CODE:1830 Duplicate Name Validation Failed For Name:[" + quarterName + "] Already Exists.");
            return duplicateName;
        }else{
            log.info("# CODE:2030 Duplicate Name Validation Successful For [" + quarterName +"].");
            return duplicateName;
        }
    }
    // 35-39
    private Quarter quarterPeriodValidation(LocalDate fromDate, LocalDate toDate, Long id){
        Quarter periodFlag = null;
        // Check if There is a Quarter That Conflicts With The Same Period
        if(id==null) {
            periodFlag = quarterRepository.findByFromDateLessThanEqualAndToDateGreaterThanEqualAndIsActiveIsTrue(fromDate, toDate);
        }
        else {
            periodFlag = quarterRepository.findByFromDateLessThanEqualAndToDateGreaterThanEqualAndIsActiveIsTrueAndIdIsNot(fromDate,toDate,id);
        }
        if (periodFlag == null) {
            log.info("# CODE:2035 Period Validation Successful From:["+ fromDate + "] To:[" + toDate + "].");
            return periodFlag;
            }
        log.warn("# CODE:1835 Period Validation Failed From:["+ fromDate + "] To:[" + toDate + "]. Conflicted Period From:["+ periodFlag.getFromDate() + "] To:[" + periodFlag.getToDate() +"] ID:[" + periodFlag.getId() + "].");
        return periodFlag;

        }
        // TODO
        private boolean visitsForQuarterCheckerIsEmpty(Long id){
            return id % 2 == 0;
        }
        // TODO
        private boolean visitsForQuarterIsCloseable(Long id){
            return id % 2 == 0;
        }
}
