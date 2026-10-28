package com.AllocationApp.controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.ZoneId;


import com.AllocationApp.util.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.Date;

import com.AllocationApp.domain.AllocationTarget;
import com.AllocationApp.dto.AllocationTargetDTO;
import com.AllocationApp.dto.AllocationTargetSearchDTO;
import com.AllocationApp.dto.AllocationTargetPageDTO;
import com.AllocationApp.service.AllocationTargetService;
import com.AllocationApp.dto.common.RequestDTO;
import com.AllocationApp.dto.common.ResultDTO;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;




@CrossOrigin(origins = "*")
@RequestMapping("/allocationTarget")
@RestController
public class AllocationTargetController {

	private final static Logger logger = LoggerFactory.getLogger(AllocationTargetController.class);

	@Autowired
	AllocationTargetService allocationTargetService;



	@RequestMapping(value="/", method = RequestMethod.GET)
	public List<AllocationTarget> getAll() {

		List<AllocationTarget> allocationTargets = allocationTargetService.findAll();
		
		return allocationTargets;	
	}

	@GetMapping(value = "/{allocationTargetId}")
	@ResponseBody
	public AllocationTargetDTO getAllocationTarget(@PathVariable Integer allocationTargetId) {
		
		return (allocationTargetService.getAllocationTargetDTOById(allocationTargetId));
	}

 	@RequestMapping(value = "/addAllocationTarget", method = RequestMethod.POST)
	public ResponseEntity<?> addAllocationTarget(@RequestBody AllocationTargetDTO allocationTargetDTO, HttpServletRequest request) {

		RequestDTO requestDTO = new RequestDTO(request);
		ResultDTO result = allocationTargetService.addAllocationTarget(allocationTargetDTO, requestDTO);
		
		return result.asResponseEntity();
	}

	@GetMapping("/allocationTargets")
	public ResponseEntity<AllocationTargetPageDTO> getAllocationTargets(AllocationTargetSearchDTO allocationTargetSearchDTO) {
 
		return allocationTargetService.getAllocationTargets(allocationTargetSearchDTO);
	}	

	@RequestMapping(value = "/updateAllocationTarget", method = RequestMethod.POST)
	public ResponseEntity<?> updateAllocationTarget(@RequestBody AllocationTargetDTO allocationTargetDTO, HttpServletRequest request) {
		RequestDTO requestDTO = new RequestDTO(request);
		ResultDTO result = allocationTargetService.updateAllocationTarget(allocationTargetDTO, requestDTO);
		
//		if (result.isSuccessful()) {
//		}

		return result.asResponseEntity();
	}





}
