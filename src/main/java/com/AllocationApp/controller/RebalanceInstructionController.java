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

import com.AllocationApp.domain.RebalanceInstruction;
import com.AllocationApp.dto.RebalanceInstructionDTO;
import com.AllocationApp.dto.RebalanceInstructionSearchDTO;
import com.AllocationApp.dto.RebalanceInstructionPageDTO;
import com.AllocationApp.service.RebalanceInstructionService;
import com.AllocationApp.dto.common.RequestDTO;
import com.AllocationApp.dto.common.ResultDTO;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;




@CrossOrigin(origins = "*")
@RequestMapping("/rebalanceInstruction")
@RestController
public class RebalanceInstructionController {

	private final static Logger logger = LoggerFactory.getLogger(RebalanceInstructionController.class);

	@Autowired
	RebalanceInstructionService rebalanceInstructionService;



	@RequestMapping(value="/", method = RequestMethod.GET)
	public List<RebalanceInstruction> getAll() {

		List<RebalanceInstruction> rebalanceInstructions = rebalanceInstructionService.findAll();
		
		return rebalanceInstructions;	
	}

	@GetMapping(value = "/{rebalanceInstructionId}")
	@ResponseBody
	public RebalanceInstructionDTO getRebalanceInstruction(@PathVariable Integer rebalanceInstructionId) {
		
		return (rebalanceInstructionService.getRebalanceInstructionDTOById(rebalanceInstructionId));
	}

 	@RequestMapping(value = "/addRebalanceInstruction", method = RequestMethod.POST)
	public ResponseEntity<?> addRebalanceInstruction(@RequestBody RebalanceInstructionDTO rebalanceInstructionDTO, HttpServletRequest request) {

		RequestDTO requestDTO = new RequestDTO(request);
		ResultDTO result = rebalanceInstructionService.addRebalanceInstruction(rebalanceInstructionDTO, requestDTO);
		
		return result.asResponseEntity();
	}

	@GetMapping("/rebalanceInstructions")
	public ResponseEntity<RebalanceInstructionPageDTO> getRebalanceInstructions(RebalanceInstructionSearchDTO rebalanceInstructionSearchDTO) {
 
		return rebalanceInstructionService.getRebalanceInstructions(rebalanceInstructionSearchDTO);
	}	

	@RequestMapping(value = "/updateRebalanceInstruction", method = RequestMethod.POST)
	public ResponseEntity<?> updateRebalanceInstruction(@RequestBody RebalanceInstructionDTO rebalanceInstructionDTO, HttpServletRequest request) {
		RequestDTO requestDTO = new RequestDTO(request);
		ResultDTO result = rebalanceInstructionService.updateRebalanceInstruction(rebalanceInstructionDTO, requestDTO);
		
//		if (result.isSuccessful()) {
//		}

		return result.asResponseEntity();
	}





}
