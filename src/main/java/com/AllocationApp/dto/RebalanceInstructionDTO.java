package com.AllocationApp.dto;

import java.util.List;
import java.util.Date;
import java.sql.Timestamp;
import java.time.Year;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class RebalanceInstructionDTO {

	private Integer rebalanceInstructionId;

	private Integer allocationId;

	private Integer instrumentId;

	private Double targetQuantity;

	private String status;






}
