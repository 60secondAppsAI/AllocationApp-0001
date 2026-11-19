package com.AllocationApp.dto;

import java.util.List;
import java.util.Date;
import java.sql.Timestamp;
import java.time.Year;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class AllocationTargetDTO {

	private Integer allocationTargetId;

	private Integer allocationId;

	private String targetType;

	private String targetReference;

	private Double targetPercent;






}
