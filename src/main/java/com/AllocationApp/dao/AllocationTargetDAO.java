package com.AllocationApp.dao;

import java.util.List;
import java.util.Date;

import com.AllocationApp.dao.GenericDAO;
import com.AllocationApp.domain.AllocationTarget;




import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface AllocationTargetDAO extends GenericDAO<AllocationTarget, Integer> {
  
	List<AllocationTarget> findAll();
	


}

