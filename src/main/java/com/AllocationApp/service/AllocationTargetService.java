package com.AllocationApp.service;

import java.util.List;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import com.AllocationApp.domain.AllocationTarget;
import com.AllocationApp.dto.AllocationTargetDTO;
import com.AllocationApp.dto.AllocationTargetSearchDTO;
import com.AllocationApp.dto.AllocationTargetPageDTO;
import com.AllocationApp.dto.AllocationTargetConvertCriteriaDTO;
import com.AllocationApp.service.GenericService;
import com.AllocationApp.dto.common.RequestDTO;
import com.AllocationApp.dto.common.ResultDTO;
import java.util.List;
import java.util.Optional;





public interface AllocationTargetService extends GenericService<AllocationTarget, Integer> {

	List<AllocationTarget> findAll();

	ResultDTO addAllocationTarget(AllocationTargetDTO allocationTargetDTO, RequestDTO requestDTO);

	ResultDTO updateAllocationTarget(AllocationTargetDTO allocationTargetDTO, RequestDTO requestDTO);

    Page<AllocationTarget> getAllAllocationTargets(Pageable pageable);

    Page<AllocationTarget> getAllAllocationTargets(Specification<AllocationTarget> spec, Pageable pageable);

	ResponseEntity<AllocationTargetPageDTO> getAllocationTargets(AllocationTargetSearchDTO allocationTargetSearchDTO);
	
	List<AllocationTargetDTO> convertAllocationTargetsToAllocationTargetDTOs(List<AllocationTarget> allocationTargets, AllocationTargetConvertCriteriaDTO convertCriteria);

	AllocationTargetDTO getAllocationTargetDTOById(Integer allocationTargetId);



	
}
