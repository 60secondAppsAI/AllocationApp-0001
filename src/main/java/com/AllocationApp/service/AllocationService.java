package com.AllocationApp.service;

import java.util.List;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import com.AllocationApp.domain.Allocation;
import com.AllocationApp.dto.AllocationDTO;
import com.AllocationApp.dto.AllocationSearchDTO;
import com.AllocationApp.dto.AllocationPageDTO;
import com.AllocationApp.dto.AllocationConvertCriteriaDTO;
import com.AllocationApp.service.GenericService;
import com.AllocationApp.dto.common.RequestDTO;
import com.AllocationApp.dto.common.ResultDTO;
import java.util.List;
import java.util.Optional;





public interface AllocationService extends GenericService<Allocation, Integer> {

	List<Allocation> findAll();

	ResultDTO addAllocation(AllocationDTO allocationDTO, RequestDTO requestDTO);

	ResultDTO updateAllocation(AllocationDTO allocationDTO, RequestDTO requestDTO);

    Page<Allocation> getAllAllocations(Pageable pageable);

    Page<Allocation> getAllAllocations(Specification<Allocation> spec, Pageable pageable);

	ResponseEntity<AllocationPageDTO> getAllocations(AllocationSearchDTO allocationSearchDTO);
	
	List<AllocationDTO> convertAllocationsToAllocationDTOs(List<Allocation> allocations, AllocationConvertCriteriaDTO convertCriteria);

	AllocationDTO getAllocationDTOById(Integer allocationId);



	
}
