package com.AllocationApp.dto;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class BrowseAllocationSnapshotDTO {

	private Integer ownerId;

	private Integer allocationSnapshotId;

	private Integer allocationSnapshotStatus;
	
	private Integer nextOrPrevious;
}

