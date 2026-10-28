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
import com.AllocationApp.dao.AllocationDAO;
import com.AllocationApp.domain.Allocation;
import com.AllocationApp.dto.AllocationDTO;
import com.AllocationApp.dto.AllocationSearchDTO;
import com.AllocationApp.dto.AllocationPageDTO;
import com.AllocationApp.dto.AllocationConvertCriteriaDTO;
import com.AllocationApp.dto.common.RequestDTO;
import com.AllocationApp.dto.common.ResultDTO;
import com.AllocationApp.service.AllocationService;
import com.AllocationApp.util.ControllerUtils;


@Service
public class AllocationServiceImpl extends GenericServiceImpl<Allocation, Integer> implements AllocationService {

    private final static Logger logger = LoggerFactory.getLogger(AllocationServiceImpl.class);

	@Autowired
	AllocationDAO allocationDao;

	

	@Override
	public GenericDAO<Allocation, Integer> getDAO() {
		return (GenericDAO<Allocation, Integer>) allocationDao;
	}
	
	public List<Allocation> findAll () {
		List<Allocation> allocations = allocationDao.findAll();
		
		return allocations;	
		
	}

	public ResultDTO addAllocation(AllocationDTO allocationDTO, RequestDTO requestDTO) {

		Allocation allocation = new Allocation();

		allocation.setAllocationId(allocationDTO.getAllocationId());

		allocation.setPortfolioId(allocationDTO.getPortfolioId());

		allocation.setName(allocationDTO.getName());

		allocation.setAllocationType(allocationDTO.getAllocationType());

		allocation.setEffectiveDate(allocationDTO.getEffectiveDate());

		LocalDate localDate = LocalDate.now();
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
		
		allocation = allocationDao.save(allocation);
		
		ResultDTO result = new ResultDTO();
		return result;
	}
	
	public Page<Allocation> getAllAllocations(Pageable pageable) {
		return allocationDao.findAll(pageable);
	}

	public Page<Allocation> getAllAllocations(Specification<Allocation> spec, Pageable pageable) {
		return allocationDao.findAll(spec, pageable);
	}

	public ResponseEntity<AllocationPageDTO> getAllocations(AllocationSearchDTO allocationSearchDTO) {
	
			Integer allocationId = allocationSearchDTO.getAllocationId(); 
 			Integer portfolioId = allocationSearchDTO.getPortfolioId(); 
 			String name = allocationSearchDTO.getName(); 
 			String allocationType = allocationSearchDTO.getAllocationType(); 
 			Date effectiveDate = allocationSearchDTO.getEffectiveDate();
 			String sortBy = allocationSearchDTO.getSortBy();
			String sortOrder = allocationSearchDTO.getSortOrder();
			String searchQuery = allocationSearchDTO.getSearchQuery();
			Integer page = allocationSearchDTO.getPage();
			Integer size = allocationSearchDTO.getSize();

	        Specification<Allocation> spec = Specification.where(null);

			spec = ControllerUtils.andIfNecessary(spec, allocationId, "allocationId"); 
			
			spec = ControllerUtils.andIfNecessary(spec, portfolioId, "portfolioId"); 
			
			spec = ControllerUtils.andIfNecessary(spec, name, "name"); 
			
			spec = ControllerUtils.andIfNecessary(spec, allocationType, "allocationType"); 
			
			spec = ControllerUtils.andIfNecessary(spec, effectiveDate, "effectiveDate");
			

		if (searchQuery != null && !searchQuery.isEmpty()) {
			spec = spec.and((root, query, cb) -> cb.or(

             cb.like(cb.lower(root.get("name")), "%" + searchQuery.toLowerCase() + "%") 
             , cb.like(cb.lower(root.get("allocationType")), "%" + searchQuery.toLowerCase() + "%") 
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

		Page<Allocation> allocations = this.getAllAllocations(spec, pageable);
		
		//System.out.println(String.valueOf(allocations.getTotalElements()) + " total ${classNamelPlural}, viewing page X of " + String.valueOf(allocations.getTotalPages()));
		
		List<Allocation> allocationsList = allocations.getContent();
		
		AllocationConvertCriteriaDTO convertCriteria = new AllocationConvertCriteriaDTO();
		List<AllocationDTO> allocationDTOs = this.convertAllocationsToAllocationDTOs(allocationsList,convertCriteria);
		
		AllocationPageDTO allocationPageDTO = new AllocationPageDTO();
		allocationPageDTO.setAllocations(allocationDTOs);
		allocationPageDTO.setTotalElements(allocations.getTotalElements());
		return ResponseEntity.ok(allocationPageDTO);
	}

	public List<AllocationDTO> convertAllocationsToAllocationDTOs(List<Allocation> allocations, AllocationConvertCriteriaDTO convertCriteria) {
		
		List<AllocationDTO> allocationDTOs = new ArrayList<AllocationDTO>();
		
		for (Allocation allocation : allocations) {
			allocationDTOs.add(convertAllocationToAllocationDTO(allocation,convertCriteria));
		}
		
		return allocationDTOs;

	}
	
	public AllocationDTO convertAllocationToAllocationDTO(Allocation allocation, AllocationConvertCriteriaDTO convertCriteria) {
		
		AllocationDTO allocationDTO = new AllocationDTO();

		allocationDTO.setAllocationId(allocation.getAllocationId());

		allocationDTO.setPortfolioId(allocation.getPortfolioId());

		allocationDTO.setName(allocation.getName());

		allocationDTO.setAllocationType(allocation.getAllocationType());

		allocationDTO.setEffectiveDate(allocation.getEffectiveDate());
		
		return allocationDTO;
	}

	public ResultDTO updateAllocation(AllocationDTO allocationDTO, RequestDTO requestDTO) {
		
		Allocation allocation = allocationDao.getById(allocationDTO.getAllocationId());
		
		allocation.setAllocationId(ControllerUtils.setValue(allocation.getAllocationId(), allocationDTO.getAllocationId()));
		
		allocation.setPortfolioId(ControllerUtils.setValue(allocation.getPortfolioId(), allocationDTO.getPortfolioId()));
		
		allocation.setName(ControllerUtils.setValue(allocation.getName(), allocationDTO.getName()));
		
		allocation.setAllocationType(ControllerUtils.setValue(allocation.getAllocationType(), allocationDTO.getAllocationType()));
		
		allocation.setEffectiveDate(ControllerUtils.setValue(allocation.getEffectiveDate(), allocationDTO.getEffectiveDate()));

        allocation = allocationDao.save(allocation);
		
		ResultDTO result = new ResultDTO();
		return result;
	}

	public AllocationDTO getAllocationDTOById(Integer allocationId) {
	
		Allocation allocation = allocationDao.getById(allocationId);
		
		AllocationConvertCriteriaDTO convertCriteria = new AllocationConvertCriteriaDTO();
		return(this.convertAllocationToAllocationDTO(allocation,convertCriteria));
	}

}
