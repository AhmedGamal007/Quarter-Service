package com.branch.quarter.controller;

import com.branch.quarter.dto.QuarterDto;
import com.branch.quarter.dto.QuarterEditRequest;
import com.branch.quarter.dto.QuarterRequest;
import com.branch.quarter.service.QuarterService;
import com.branch.quarter.dto.ResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/quarter")
@RequiredArgsConstructor
@Slf4j
public class QuarterController {

    private final QuarterService quarterService;

    @PostMapping("/create")
    public ResponseModel createQuarter(QuarterRequest quarterRequest){
        return quarterService.createQuarter(quarterRequest);
    }
    @GetMapping("/page-count")
    public ResponseModel getPages(@RequestParam("size") Integer size){
        return quarterService.getPageCount(size);
    }
    @GetMapping("/quarter-list")
    public ResponseModel getQuarterList(@RequestParam("page") Integer page,@RequestParam("size") Integer size){
        Pageable pageable = PageRequest.of(page-1,size);
        return quarterService.getQuarterList(pageable);
    }
    @PutMapping("/edit")
    public ResponseModel editQuarter(QuarterEditRequest quarterEditRequest){
        return quarterService.editQuarter(quarterEditRequest);
    }
    @DeleteMapping("/delete-quarter/{id}")
    public ResponseModel deleteQuarter(@PathVariable Long id){
        return quarterService.softDeleteQuarter(id);
    }
    @PutMapping("/close-quarter/{id}")
    public ResponseModel closeQuarter(@PathVariable Long id){
        return quarterService.closeQuarter(id);
    }

}
