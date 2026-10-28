package com.AllocationApp.service;

import java.util.List;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import com.AllocationApp.domain.AllocationSnapshot;
import com.AllocationApp.dto.AllocationSnapshotDTO;
import com.AllocationApp.dto.AllocationSnapshotSearchDTO;
import com.AllocationApp.dto.AllocationSnapshotPageDTO;
import com.AllocationApp.dto.AllocationSnapshotConvertCriteriaDTO;
import com.AllocationApp.service.GenericService;
import com.AllocationApp.dto.common.RequestDTO;
import com.AllocationApp.dto.common.ResultDTO;
import java.util.List;
import java.util.Optional;





public interface AllocationSnapshotService extends GenericService<AllocationSnapshot, Integer> {

	List<AllocationSnapshot> findAll();

	ResultDTO addAllocationSnapshot(AllocationSnapshotDTO allocationSnapshotDTO, RequestDTO requestDTO);

	ResultDTO updateAllocationSnapshot(AllocationSnapshotDTO allocationSnapshotDTO, RequestDTO requestDTO);

    Page<AllocationSnapshot> getAllAllocationSnapshots(Pageable pageable);

    Page<AllocationSnapshot> getAllAllocationSnapshots(Specification<AllocationSnapshot> spec, Pageable pageable);

	ResponseEntity<AllocationSnapshotPageDTO> getAllocationSnapshots(AllocationSnapshotSearchDTO allocationSnapshotSearchDTO);
	
	List<AllocationSnapshotDTO> convertAllocationSnapshotsToAllocationSnapshotDTOs(List<AllocationSnapshot> allocationSnapshots, AllocationSnapshotConvertCriteriaDTO convertCriteria);

	AllocationSnapshotDTO getAllocationSnapshotDTOById(Integer allocationSnapshotId);



	
}
