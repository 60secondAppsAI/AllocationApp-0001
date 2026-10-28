package com.AllocationApp.dto;

import java.util.List;
import java.util.Date;
import java.sql.Timestamp;
import java.time.Year;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class AllocationSnapshotDTO {

	private Integer allocationSnapshotId;

	private Integer allocationId;

	private String targetReference;

	private Double actualPercent;

	private Date capturedAt;






}
