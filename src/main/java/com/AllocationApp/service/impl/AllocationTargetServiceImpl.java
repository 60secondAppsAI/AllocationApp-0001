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
import com.AllocationApp.dao.AllocationTargetDAO;
import com.AllocationApp.domain.AllocationTarget;
import com.AllocationApp.dto.AllocationTargetDTO;
import com.AllocationApp.dto.AllocationTargetSearchDTO;
import com.AllocationApp.dto.AllocationTargetPageDTO;
import com.AllocationApp.dto.AllocationTargetConvertCriteriaDTO;
import com.AllocationApp.dto.common.RequestDTO;
import com.AllocationApp.dto.common.ResultDTO;
import com.AllocationApp.service.AllocationTargetService;
import com.AllocationApp.util.ControllerUtils;


@Service
public class AllocationTargetServiceImpl extends GenericServiceImpl<AllocationTarget, Integer> implements AllocationTargetService {

    private final static Logger logger = LoggerFactory.getLogger(AllocationTargetServiceImpl.class);

	@Autowired
	AllocationTargetDAO allocationTargetDao;

	

	@Override
	public GenericDAO<AllocationTarget, Integer> getDAO() {
		return (GenericDAO<AllocationTarget, Integer>) allocationTargetDao;
	}
	
	public List<AllocationTarget> findAll () {
		List<AllocationTarget> allocationTargets = allocationTargetDao.findAll();
		
		return allocationTargets;	
		
	}

	public ResultDTO addAllocationTarget(AllocationTargetDTO allocationTargetDTO, RequestDTO requestDTO) {

		AllocationTarget allocationTarget = new AllocationTarget();

		allocationTarget.setAllocationTargetId(allocationTargetDTO.getAllocationTargetId());

		allocationTarget.setAllocationId(allocationTargetDTO.getAllocationId());

		allocationTarget.setTargetType(allocationTargetDTO.getTargetType());

		allocationTarget.setTargetReference(allocationTargetDTO.getTargetReference());

		allocationTarget.setTargetPercent(allocationTargetDTO.getTargetPercent());

		LocalDate localDate = LocalDate.now();
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
		
		allocationTarget = allocationTargetDao.save(allocationTarget);
		
		ResultDTO result = new ResultDTO();
		return result;
	}
	
	public Page<AllocationTarget> getAllAllocationTargets(Pageable pageable) {
		return allocationTargetDao.findAll(pageable);
	}

	public Page<AllocationTarget> getAllAllocationTargets(Specification<AllocationTarget> spec, Pageable pageable) {
		return allocationTargetDao.findAll(spec, pageable);
	}

	public ResponseEntity<AllocationTargetPageDTO> getAllocationTargets(AllocationTargetSearchDTO allocationTargetSearchDTO) {
	
			Integer allocationTargetId = allocationTargetSearchDTO.getAllocationTargetId(); 
 			Integer allocationId = allocationTargetSearchDTO.getAllocationId(); 
 			String targetType = allocationTargetSearchDTO.getTargetType(); 
 			String targetReference = allocationTargetSearchDTO.getTargetReference(); 
  			String sortBy = allocationTargetSearchDTO.getSortBy();
			String sortOrder = allocationTargetSearchDTO.getSortOrder();
			String searchQuery = allocationTargetSearchDTO.getSearchQuery();
			Integer page = allocationTargetSearchDTO.getPage();
			Integer size = allocationTargetSearchDTO.getSize();

	        Specification<AllocationTarget> spec = Specification.where(null);

			spec = ControllerUtils.andIfNecessary(spec, allocationTargetId, "allocationTargetId"); 
			
			spec = ControllerUtils.andIfNecessary(spec, allocationId, "allocationId"); 
			
			spec = ControllerUtils.andIfNecessary(spec, targetType, "targetType"); 
			
			spec = ControllerUtils.andIfNecessary(spec, targetReference, "targetReference"); 
			
			

		if (searchQuery != null && !searchQuery.isEmpty()) {
			spec = spec.and((root, query, cb) -> cb.or(

             cb.like(cb.lower(root.get("targetType")), "%" + searchQuery.toLowerCase() + "%") 
             , cb.like(cb.lower(root.get("targetReference")), "%" + searchQuery.toLowerCase() + "%") 
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

		Page<AllocationTarget> allocationTargets = this.getAllAllocationTargets(spec, pageable);
		
		//System.out.println(String.valueOf(allocationTargets.getTotalElements()) + " total ${classNamelPlural}, viewing page X of " + String.valueOf(allocationTargets.getTotalPages()));
		
		List<AllocationTarget> allocationTargetsList = allocationTargets.getContent();
		
		AllocationTargetConvertCriteriaDTO convertCriteria = new AllocationTargetConvertCriteriaDTO();
		List<AllocationTargetDTO> allocationTargetDTOs = this.convertAllocationTargetsToAllocationTargetDTOs(allocationTargetsList,convertCriteria);
		
		AllocationTargetPageDTO allocationTargetPageDTO = new AllocationTargetPageDTO();
		allocationTargetPageDTO.setAllocationTargets(allocationTargetDTOs);
		allocationTargetPageDTO.setTotalElements(allocationTargets.getTotalElements());
		return ResponseEntity.ok(allocationTargetPageDTO);
	}

	public List<AllocationTargetDTO> convertAllocationTargetsToAllocationTargetDTOs(List<AllocationTarget> allocationTargets, AllocationTargetConvertCriteriaDTO convertCriteria) {
		
		List<AllocationTargetDTO> allocationTargetDTOs = new ArrayList<AllocationTargetDTO>();
		
		for (AllocationTarget allocationTarget : allocationTargets) {
			allocationTargetDTOs.add(convertAllocationTargetToAllocationTargetDTO(allocationTarget,convertCriteria));
		}
		
		return allocationTargetDTOs;

	}
	
	public AllocationTargetDTO convertAllocationTargetToAllocationTargetDTO(AllocationTarget allocationTarget, AllocationTargetConvertCriteriaDTO convertCriteria) {
		
		AllocationTargetDTO allocationTargetDTO = new AllocationTargetDTO();

		allocationTargetDTO.setAllocationTargetId(allocationTarget.getAllocationTargetId());

		allocationTargetDTO.setAllocationId(allocationTarget.getAllocationId());

		allocationTargetDTO.setTargetType(allocationTarget.getTargetType());

		allocationTargetDTO.setTargetReference(allocationTarget.getTargetReference());

		allocationTargetDTO.setTargetPercent(allocationTarget.getTargetPercent());
		
		return allocationTargetDTO;
	}

	public ResultDTO updateAllocationTarget(AllocationTargetDTO allocationTargetDTO, RequestDTO requestDTO) {
		
		AllocationTarget allocationTarget = allocationTargetDao.getById(allocationTargetDTO.getAllocationTargetId());
		
		allocationTarget.setAllocationTargetId(ControllerUtils.setValue(allocationTarget.getAllocationTargetId(), allocationTargetDTO.getAllocationTargetId()));
		
		allocationTarget.setAllocationId(ControllerUtils.setValue(allocationTarget.getAllocationId(), allocationTargetDTO.getAllocationId()));
		
		allocationTarget.setTargetType(ControllerUtils.setValue(allocationTarget.getTargetType(), allocationTargetDTO.getTargetType()));
		
		allocationTarget.setTargetReference(ControllerUtils.setValue(allocationTarget.getTargetReference(), allocationTargetDTO.getTargetReference()));
		
		allocationTarget.setTargetPercent(ControllerUtils.setValue(allocationTarget.getTargetPercent(), allocationTargetDTO.getTargetPercent()));

        allocationTarget = allocationTargetDao.save(allocationTarget);
		
		ResultDTO result = new ResultDTO();
		return result;
	}

	public AllocationTargetDTO getAllocationTargetDTOById(Integer allocationTargetId) {
	
		AllocationTarget allocationTarget = allocationTargetDao.getById(allocationTargetId);
		
		AllocationTargetConvertCriteriaDTO convertCriteria = new AllocationTargetConvertCriteriaDTO();
		return(this.convertAllocationTargetToAllocationTargetDTO(allocationTarget,convertCriteria));
	}

}
