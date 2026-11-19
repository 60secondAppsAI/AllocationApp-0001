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

import com.AllocationApp.domain.Allocation;
import com.AllocationApp.dto.AllocationDTO;
import com.AllocationApp.dto.AllocationSearchDTO;
import com.AllocationApp.dto.AllocationPageDTO;
import com.AllocationApp.service.AllocationService;
import com.AllocationApp.dto.common.RequestDTO;
import com.AllocationApp.dto.common.ResultDTO;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;




@CrossOrigin(origins = "*")
@RequestMapping("/allocation")
@RestController
public class AllocationController {

	private final static Logger logger = LoggerFactory.getLogger(AllocationController.class);

	@Autowired
	AllocationService allocationService;



	@RequestMapping(value="/", method = RequestMethod.GET)
	public List<Allocation> getAll() {

		List<Allocation> allocations = allocationService.findAll();
		
		return allocations;	
	}

	@GetMapping(value = "/{allocationId}")
	@ResponseBody
	public AllocationDTO getAllocation(@PathVariable Integer allocationId) {
		
		return (allocationService.getAllocationDTOById(allocationId));
	}

 	@RequestMapping(value = "/addAllocation", method = RequestMethod.POST)
	public ResponseEntity<?> addAllocation(@RequestBody AllocationDTO allocationDTO, HttpServletRequest request) {

		RequestDTO requestDTO = new RequestDTO(request);
		ResultDTO result = allocationService.addAllocation(allocationDTO, requestDTO);
		
		return result.asResponseEntity();
	}

	@GetMapping("/allocations")
	public ResponseEntity<AllocationPageDTO> getAllocations(AllocationSearchDTO allocationSearchDTO) {
 
		return allocationService.getAllocations(allocationSearchDTO);
	}	

	@RequestMapping(value = "/updateAllocation", method = RequestMethod.POST)
	public ResponseEntity<?> updateAllocation(@RequestBody AllocationDTO allocationDTO, HttpServletRequest request) {
		RequestDTO requestDTO = new RequestDTO(request);
		ResultDTO result = allocationService.updateAllocation(allocationDTO, requestDTO);
		
//		if (result.isSuccessful()) {
//		}

		return result.asResponseEntity();
	}





}
