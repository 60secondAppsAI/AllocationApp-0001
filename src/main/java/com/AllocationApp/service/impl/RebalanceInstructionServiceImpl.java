package com.AllocationApp.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.AllocationApp.dao.GenericDAO;
import com.AllocationApp.service.GenericService;
import com.AllocationApp.service.impl.GenericServiceImpl;
import com.AllocationApp.dao.RebalanceInstructionDAO;
import com.AllocationApp.domain.RebalanceInstruction;
import com.AllocationApp.dto.RebalanceInstructionDTO;
import com.AllocationApp.dto.RebalanceInstructionSearchDTO;
import com.AllocationApp.dto.RebalanceInstructionPageDTO;
import com.AllocationApp.dto.RebalanceInstructionConvertCriteriaDTO;
import com.AllocationApp.dto.common.RequestDTO;
import com.AllocationApp.dto.common.ResultDTO;
import com.AllocationApp.service.RebalanceInstructionService;
import com.AllocationApp.util.ControllerUtils;


@Service
public class RebalanceInstructionServiceImpl extends GenericServiceImpl<RebalanceInstruction, Integer> implements RebalanceInstructionService {

    private final static Logger logger = LoggerFactory.getLogger(RebalanceInstructionServiceImpl.class);

	@Autowired
	RebalanceInstructionDAO rebalanceInstructionDao;

	

	@Override
	public GenericDAO<RebalanceInstruction, Integer> getDAO() {
		return (GenericDAO<RebalanceInstruction, Integer>) rebalanceInstructionDao;
	}
	
	public List<RebalanceInstruction> findAll () {
		List<RebalanceInstruction> rebalanceInstructions = rebalanceInstructionDao.findAll();
		
		return rebalanceInstructions;	
		
	}

	public ResultDTO addRebalanceInstruction(RebalanceInstructionDTO rebalanceInstructionDTO, RequestDTO requestDTO) {

		RebalanceInstruction rebalanceInstruction = new RebalanceInstruction();

		rebalanceInstruction.setRebalanceInstructionId(rebalanceInstructionDTO.getRebalanceInstructionId());

		rebalanceInstruction.setAllocationId(rebalanceInstructionDTO.getAllocationId());

		rebalanceInstruction.setInstrumentId(rebalanceInstructionDTO.getInstrumentId());

		rebalanceInstruction.setTargetQuantity(rebalanceInstructionDTO.getTargetQuantity());

		rebalanceInstruction.setStatus(rebalanceInstructionDTO.getStatus());

		LocalDate localDate = LocalDate.now();
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
		
		rebalanceInstruction = rebalanceInstructionDao.save(rebalanceInstruction);
		
		ResultDTO result = new ResultDTO();
		return result;
	}
	
	public Page<RebalanceInstruction> getAllRebalanceInstructions(Pageable pageable) {
		return rebalanceInstructionDao.findAll(pageable);
	}

	public Page<RebalanceInstruction> getAllRebalanceInstructions(Specification<RebalanceInstruction> spec, Pageable pageable) {
		return rebalanceInstructionDao.findAll(spec, pageable);
	}

	public ResponseEntity<RebalanceInstructionPageDTO> getRebalanceInstructions(RebalanceInstructionSearchDTO rebalanceInstructionSearchDTO) {
	
			Integer rebalanceInstructionId = rebalanceInstructionSearchDTO.getRebalanceInstructionId(); 
 			Integer allocationId = rebalanceInstructionSearchDTO.getAllocationId(); 
 			Integer instrumentId = rebalanceInstructionSearchDTO.getInstrumentId(); 
  			String status = rebalanceInstructionSearchDTO.getStatus(); 
 			String sortBy = rebalanceInstructionSearchDTO.getSortBy();
			String sortOrder = rebalanceInstructionSearchDTO.getSortOrder();
			String searchQuery = rebalanceInstructionSearchDTO.getSearchQuery();
			Integer page = rebalanceInstructionSearchDTO.getPage();
			Integer size = rebalanceInstructionSearchDTO.getSize();

	        Specification<RebalanceInstruction> spec = Specification.where(null);

			spec = ControllerUtils.andIfNecessary(spec, rebalanceInstructionId, "rebalanceInstructionId"); 
			
			spec = ControllerUtils.andIfNecessary(spec, allocationId, "allocationId"); 
			
			spec = ControllerUtils.andIfNecessary(spec, instrumentId, "instrumentId"); 
			
			
			spec = ControllerUtils.andIfNecessary(spec, status, "status"); 
			

		if (searchQuery != null && !searchQuery.isEmpty()) {
			spec = spec.and((root, query, cb) -> cb.or(

             cb.like(cb.lower(root.get("status")), "%" + searchQuery.toLowerCase() + "%") 
		));}
		
		Sort sort = Sort.unsorted();
		if (sortBy != null && !sortBy.isEmpty() && sortOrder != null && !sortOrder.isEmpty()) {
			if (sortOrder.equalsIgnoreCase("asc")) {
				sort = Sort.by(sortBy).ascending();
			} else if (sortOrder.equalsIgnoreCase("desc")) {
				sort = Sort.by(sortBy).descending();
			}
		}
		Pageable pageable = PageRequest.of(page, size, sort);

		Page<RebalanceInstruction> rebalanceInstructions = this.getAllRebalanceInstructions(spec, pageable);
		
		//System.out.println(String.valueOf(rebalanceInstructions.getTotalElements()) + " total ${classNamelPlural}, viewing page X of " + String.valueOf(rebalanceInstructions.getTotalPages()));
		
		List<RebalanceInstruction> rebalanceInstructionsList = rebalanceInstructions.getContent();
		
		RebalanceInstructionConvertCriteriaDTO convertCriteria = new RebalanceInstructionConvertCriteriaDTO();
		List<RebalanceInstructionDTO> rebalanceInstructionDTOs = this.convertRebalanceInstructionsToRebalanceInstructionDTOs(rebalanceInstructionsList,convertCriteria);
		
		RebalanceInstructionPageDTO rebalanceInstructionPageDTO = new RebalanceInstructionPageDTO();
		rebalanceInstructionPageDTO.setRebalanceInstructions(rebalanceInstructionDTOs);
		rebalanceInstructionPageDTO.setTotalElements(rebalanceInstructions.getTotalElements());
		return ResponseEntity.ok(rebalanceInstructionPageDTO);
	}

	public List<RebalanceInstructionDTO> convertRebalanceInstructionsToRebalanceInstructionDTOs(List<RebalanceInstruction> rebalanceInstructions, RebalanceInstructionConvertCriteriaDTO convertCriteria) {
		
		List<RebalanceInstructionDTO> rebalanceInstructionDTOs = new ArrayList<RebalanceInstructionDTO>();
		
		for (RebalanceInstruction rebalanceInstruction : rebalanceInstructions) {
			rebalanceInstructionDTOs.add(convertRebalanceInstructionToRebalanceInstructionDTO(rebalanceInstruction,convertCriteria));
		}
		
		return rebalanceInstructionDTOs;

	}
	
	public RebalanceInstructionDTO convertRebalanceInstructionToRebalanceInstructionDTO(RebalanceInstruction rebalanceInstruction, RebalanceInstructionConvertCriteriaDTO convertCriteria) {
		
		RebalanceInstructionDTO rebalanceInstructionDTO = new RebalanceInstructionDTO();

		rebalanceInstructionDTO.setRebalanceInstructionId(rebalanceInstruction.getRebalanceInstructionId());

		rebalanceInstructionDTO.setAllocationId(rebalanceInstruction.getAllocationId());

		rebalanceInstructionDTO.setInstrumentId(rebalanceInstruction.getInstrumentId());

		rebalanceInstructionDTO.setTargetQuantity(rebalanceInstruction.getTargetQuantity());

		rebalanceInstructionDTO.setStatus(rebalanceInstruction.getStatus());
		
		return rebalanceInstructionDTO;
	}

	public ResultDTO updateRebalanceInstruction(RebalanceInstructionDTO rebalanceInstructionDTO, RequestDTO requestDTO) {
		
		RebalanceInstruction rebalanceInstruction = rebalanceInstructionDao.getById(rebalanceInstructionDTO.getRebalanceInstructionId());
		
		rebalanceInstruction.setRebalanceInstructionId(ControllerUtils.setValue(rebalanceInstruction.getRebalanceInstructionId(), rebalanceInstructionDTO.getRebalanceInstructionId()));
		
		rebalanceInstruction.setAllocationId(ControllerUtils.setValue(rebalanceInstruction.getAllocationId(), rebalanceInstructionDTO.getAllocationId()));
		
		rebalanceInstruction.setInstrumentId(ControllerUtils.setValue(rebalanceInstruction.getInstrumentId(), rebalanceInstructionDTO.getInstrumentId()));
		
		rebalanceInstruction.setTargetQuantity(ControllerUtils.setValue(rebalanceInstruction.getTargetQuantity(), rebalanceInstructionDTO.getTargetQuantity()));
		
		rebalanceInstruction.setStatus(ControllerUtils.setValue(rebalanceInstruction.getStatus(), rebalanceInstructionDTO.getStatus()));

        rebalanceInstruction = rebalanceInstructionDao.save(rebalanceInstruction);
		
		ResultDTO result = new ResultDTO();
		return result;
	}

	public RebalanceInstructionDTO getRebalanceInstructionDTOById(Integer rebalanceInstructionId) {
	
		RebalanceInstruction rebalanceInstruction = rebalanceInstructionDao.getById(rebalanceInstructionId);
		
		RebalanceInstructionConvertCriteriaDTO convertCriteria = new RebalanceInstructionConvertCriteriaDTO();
		return(this.convertRebalanceInstructionToRebalanceInstructionDTO(rebalanceInstruction,convertCriteria));
	}

}
