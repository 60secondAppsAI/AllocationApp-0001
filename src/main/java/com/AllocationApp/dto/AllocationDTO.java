package com.AllocationApp.dto;

import java.util.List;
import java.util.Date;
import java.sql.Timestamp;
import java.time.Year;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class AllocationDTO {

	private Integer allocationId;

	private Integer portfolioId;

	private String name;

	private String allocationType;

	private Date effectiveDate;






}
