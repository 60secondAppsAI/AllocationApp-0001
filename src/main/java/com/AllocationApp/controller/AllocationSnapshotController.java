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

import com.AllocationApp.domain.AllocationSnapshot;
import com.AllocationApp.dto.AllocationSnapshotDTO;
import com.AllocationApp.dto.AllocationSnapshotSearchDTO;
import com.AllocationApp.dto.AllocationSnapshotPageDTO;
import com.AllocationApp.service.AllocationSnapshotService;
import com.AllocationApp.dto.common.RequestDTO;
import com.AllocationApp.dto.common.ResultDTO;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;




@CrossOrigin(origins = "*")
@RequestMapping("/allocationSnapshot")
@RestController
public class AllocationSnapshotController {

	private final static Logger logger = LoggerFactory.getLogger(AllocationSnapshotController.class);

	@Autowired
	AllocationSnapshotService allocationSnapshotService;



	@RequestMapping(value="/", method = RequestMethod.GET)
	public List<AllocationSnapshot> getAll() {

		List<AllocationSnapshot> allocationSnapshots = allocationSnapshotService.findAll();
		
		return allocationSnapshots;	
	}

	@GetMapping(value = "/{allocationSnapshotId}")
	@ResponseBody
	public AllocationSnapshotDTO getAllocationSnapshot(@PathVariable Integer allocationSnapshotId) {
		
		return (allocationSnapshotService.getAllocationSnapshotDTOById(allocationSnapshotId));
	}

 	@RequestMapping(value = "/addAllocationSnapshot", method = RequestMethod.POST)
	public ResponseEntity<?> addAllocationSnapshot(@RequestBody AllocationSnapshotDTO allocationSnapshotDTO, HttpServletRequest request) {

		RequestDTO requestDTO = new RequestDTO(request);
		ResultDTO result = allocationSnapshotService.addAllocationSnapshot(allocationSnapshotDTO, requestDTO);
		
		return result.asResponseEntity();
	}

	@GetMapping("/allocationSnapshots")
	public ResponseEntity<AllocationSnapshotPageDTO> getAllocationSnapshots(AllocationSnapshotSearchDTO allocationSnapshotSearchDTO) {
 
		return allocationSnapshotService.getAllocationSnapshots(allocationSnapshotSearchDTO);
	}	

	@RequestMapping(value = "/updateAllocationSnapshot", method = RequestMethod.POST)
	public ResponseEntity<?> updateAllocationSnapshot(@RequestBody AllocationSnapshotDTO allocationSnapshotDTO, HttpServletRequest request) {
		RequestDTO requestDTO = new RequestDTO(request);
		ResultDTO result = allocationSnapshotService.updateAllocationSnapshot(allocationSnapshotDTO, requestDTO);
		
//		if (result.isSuccessful()) {
//		}

		return result.asResponseEntity();
	}





}
