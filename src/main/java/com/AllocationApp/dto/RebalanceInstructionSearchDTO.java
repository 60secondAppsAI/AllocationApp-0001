package com.AllocationApp.dto;

import java.sql.Timestamp;
import java.time.Year;
import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RebalanceInstructionSearchDTO {

	private Integer page = 0;
	private Integer size;
	private String sortBy;
	private String sortOrder;
	private String searchQuery;

	private Integer rebalanceInstructionId;
	
	private Integer allocationId;
	
	private Integer instrumentId;
	
	private Double targetQuantity;
	
	private String status;
	
}
