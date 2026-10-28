package com.AllocationApp.service;

import java.util.List;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import com.AllocationApp.domain.RebalanceInstruction;
import com.AllocationApp.dto.RebalanceInstructionDTO;
import com.AllocationApp.dto.RebalanceInstructionSearchDTO;
import com.AllocationApp.dto.RebalanceInstructionPageDTO;
import com.AllocationApp.dto.RebalanceInstructionConvertCriteriaDTO;
import com.AllocationApp.service.GenericService;
import com.AllocationApp.dto.common.RequestDTO;
import com.AllocationApp.dto.common.ResultDTO;
import java.util.List;
import java.util.Optional;





public interface RebalanceInstructionService extends GenericService<RebalanceInstruction, Integer> {

	List<RebalanceInstruction> findAll();

	ResultDTO addRebalanceInstruction(RebalanceInstructionDTO rebalanceInstructionDTO, RequestDTO requestDTO);

	ResultDTO updateRebalanceInstruction(RebalanceInstructionDTO rebalanceInstructionDTO, RequestDTO requestDTO);

    Page<RebalanceInstruction> getAllRebalanceInstructions(Pageable pageable);

    Page<RebalanceInstruction> getAllRebalanceInstructions(Specification<RebalanceInstruction> spec, Pageable pageable);

	ResponseEntity<RebalanceInstructionPageDTO> getRebalanceInstructions(RebalanceInstructionSearchDTO rebalanceInstructionSearchDTO);
	
	List<RebalanceInstructionDTO> convertRebalanceInstructionsToRebalanceInstructionDTOs(List<RebalanceInstruction> rebalanceInstructions, RebalanceInstructionConvertCriteriaDTO convertCriteria);

	RebalanceInstructionDTO getRebalanceInstructionDTOById(Integer rebalanceInstructionId);



	
}
