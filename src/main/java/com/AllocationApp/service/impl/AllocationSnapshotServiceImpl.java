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
import com.AllocationApp.dao.AllocationSnapshotDAO;
import com.AllocationApp.domain.AllocationSnapshot;
import com.AllocationApp.dto.AllocationSnapshotDTO;
import com.AllocationApp.dto.AllocationSnapshotSearchDTO;
import com.AllocationApp.dto.AllocationSnapshotPageDTO;
import com.AllocationApp.dto.AllocationSnapshotConvertCriteriaDTO;
import com.AllocationApp.dto.common.RequestDTO;
import com.AllocationApp.dto.common.ResultDTO;
import com.AllocationApp.service.AllocationSnapshotService;
import com.AllocationApp.util.ControllerUtils;


@Service
public class AllocationSnapshotServiceImpl extends GenericServiceImpl<AllocationSnapshot, Integer> implements AllocationSnapshotService {

    private final static Logger logger = LoggerFactory.getLogger(AllocationSnapshotServiceImpl.class);

	@Autowired
	AllocationSnapshotDAO allocationSnapshotDao;

	

	@Override
	public GenericDAO<AllocationSnapshot, Integer> getDAO() {
		return (GenericDAO<AllocationSnapshot, Integer>) allocationSnapshotDao;
	}
	
	public List<AllocationSnapshot> findAll () {
		List<AllocationSnapshot> allocationSnapshots = allocationSnapshotDao.findAll();
		
		return allocationSnapshots;	
		
	}

	public ResultDTO addAllocationSnapshot(AllocationSnapshotDTO allocationSnapshotDTO, RequestDTO requestDTO) {

		AllocationSnapshot allocationSnapshot = new AllocationSnapshot();

		allocationSnapshot.setAllocationSnapshotId(allocationSnapshotDTO.getAllocationSnapshotId());

		allocationSnapshot.setAllocationId(allocationSnapshotDTO.getAllocationId());

		allocationSnapshot.setTargetReference(allocationSnapshotDTO.getTargetReference());

		allocationSnapshot.setActualPercent(allocationSnapshotDTO.getActualPercent());

		allocationSnapshot.setCapturedAt(allocationSnapshotDTO.getCapturedAt());

		LocalDate localDate = LocalDate.now();
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
		
		allocationSnapshot = allocationSnapshotDao.save(allocationSnapshot);
		
		ResultDTO result = new ResultDTO();
		return result;
	}
	
	public Page<AllocationSnapshot> getAllAllocationSnapshots(Pageable pageable) {
		return allocationSnapshotDao.findAll(pageable);
	}

	public Page<AllocationSnapshot> getAllAllocationSnapshots(Specification<AllocationSnapshot> spec, Pageable pageable) {
		return allocationSnapshotDao.findAll(spec, pageable);
	}

	public ResponseEntity<AllocationSnapshotPageDTO> getAllocationSnapshots(AllocationSnapshotSearchDTO allocationSnapshotSearchDTO) {
	
			Integer allocationSnapshotId = allocationSnapshotSearchDTO.getAllocationSnapshotId(); 
 			Integer allocationId = allocationSnapshotSearchDTO.getAllocationId(); 
 			String targetReference = allocationSnapshotSearchDTO.getTargetReference(); 
  			Date capturedAt = allocationSnapshotSearchDTO.getCapturedAt();
 			String sortBy = allocationSnapshotSearchDTO.getSortBy();
			String sortOrder = allocationSnapshotSearchDTO.getSortOrder();
			String searchQuery = allocationSnapshotSearchDTO.getSearchQuery();
			Integer page = allocationSnapshotSearchDTO.getPage();
			Integer size = allocationSnapshotSearchDTO.getSize();

	        Specification<AllocationSnapshot> spec = Specification.where(null);

			spec = ControllerUtils.andIfNecessary(spec, allocationSnapshotId, "allocationSnapshotId"); 
			
			spec = ControllerUtils.andIfNecessary(spec, allocationId, "allocationId"); 
			
			spec = ControllerUtils.andIfNecessary(spec, targetReference, "targetReference"); 
			
			
			spec = ControllerUtils.andIfNecessary(spec, capturedAt, "capturedAt");
			

		if (searchQuery != null && !searchQuery.isEmpty()) {
			spec = spec.and((root, query, cb) -> cb.or(

             cb.like(cb.lower(root.get("targetReference")), "%" + searchQuery.toLowerCase() + "%") 
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

		Page<AllocationSnapshot> allocationSnapshots = this.getAllAllocationSnapshots(spec, pageable);
		
		//System.out.println(String.valueOf(allocationSnapshots.getTotalElements()) + " total ${classNamelPlural}, viewing page X of " + String.valueOf(allocationSnapshots.getTotalPages()));
		
		List<AllocationSnapshot> allocationSnapshotsList = allocationSnapshots.getContent();
		
		AllocationSnapshotConvertCriteriaDTO convertCriteria = new AllocationSnapshotConvertCriteriaDTO();
		List<AllocationSnapshotDTO> allocationSnapshotDTOs = this.convertAllocationSnapshotsToAllocationSnapshotDTOs(allocationSnapshotsList,convertCriteria);
		
		AllocationSnapshotPageDTO allocationSnapshotPageDTO = new AllocationSnapshotPageDTO();
		allocationSnapshotPageDTO.setAllocationSnapshots(allocationSnapshotDTOs);
		allocationSnapshotPageDTO.setTotalElements(allocationSnapshots.getTotalElements());
		return ResponseEntity.ok(allocationSnapshotPageDTO);
	}

	public List<AllocationSnapshotDTO> convertAllocationSnapshotsToAllocationSnapshotDTOs(List<AllocationSnapshot> allocationSnapshots, AllocationSnapshotConvertCriteriaDTO convertCriteria) {
		
		List<AllocationSnapshotDTO> allocationSnapshotDTOs = new ArrayList<AllocationSnapshotDTO>();
		
		for (AllocationSnapshot allocationSnapshot : allocationSnapshots) {
			allocationSnapshotDTOs.add(convertAllocationSnapshotToAllocationSnapshotDTO(allocationSnapshot,convertCriteria));
		}
		
		return allocationSnapshotDTOs;

	}
	
	public AllocationSnapshotDTO convertAllocationSnapshotToAllocationSnapshotDTO(AllocationSnapshot allocationSnapshot, AllocationSnapshotConvertCriteriaDTO convertCriteria) {
		
		AllocationSnapshotDTO allocationSnapshotDTO = new AllocationSnapshotDTO();

		allocationSnapshotDTO.setAllocationSnapshotId(allocationSnapshot.getAllocationSnapshotId());

		allocationSnapshotDTO.setAllocationId(allocationSnapshot.getAllocationId());

		allocationSnapshotDTO.setTargetReference(allocationSnapshot.getTargetReference());

		allocationSnapshotDTO.setActualPercent(allocationSnapshot.getActualPercent());

		allocationSnapshotDTO.setCapturedAt(allocationSnapshot.getCapturedAt());
		
		return allocationSnapshotDTO;
	}

	public ResultDTO updateAllocationSnapshot(AllocationSnapshotDTO allocationSnapshotDTO, RequestDTO requestDTO) {
		
		AllocationSnapshot allocationSnapshot = allocationSnapshotDao.getById(allocationSnapshotDTO.getAllocationSnapshotId());
		
		allocationSnapshot.setAllocationSnapshotId(ControllerUtils.setValue(allocationSnapshot.getAllocationSnapshotId(), allocationSnapshotDTO.getAllocationSnapshotId()));
		
		allocationSnapshot.setAllocationId(ControllerUtils.setValue(allocationSnapshot.getAllocationId(), allocationSnapshotDTO.getAllocationId()));
		
		allocationSnapshot.setTargetReference(ControllerUtils.setValue(allocationSnapshot.getTargetReference(), allocationSnapshotDTO.getTargetReference()));
		
		allocationSnapshot.setActualPercent(ControllerUtils.setValue(allocationSnapshot.getActualPercent(), allocationSnapshotDTO.getActualPercent()));
		
		allocationSnapshot.setCapturedAt(ControllerUtils.setValue(allocationSnapshot.getCapturedAt(), allocationSnapshotDTO.getCapturedAt()));

        allocationSnapshot = allocationSnapshotDao.save(allocationSnapshot);
		
		ResultDTO result = new ResultDTO();
		return result;
	}

	public AllocationSnapshotDTO getAllocationSnapshotDTOById(Integer allocationSnapshotId) {
	
		AllocationSnapshot allocationSnapshot = allocationSnapshotDao.getById(allocationSnapshotId);
		
		AllocationSnapshotConvertCriteriaDTO convertCriteria = new AllocationSnapshotConvertCriteriaDTO();
		return(this.convertAllocationSnapshotToAllocationSnapshotDTO(allocationSnapshot,convertCriteria));
	}

}
