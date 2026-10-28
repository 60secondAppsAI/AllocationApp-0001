package com.AllocationApp.dto;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class BrowseAllocationTargetDTO {

	private Integer ownerId;

	private Integer allocationTargetId;

	private Integer allocationTargetStatus;
	
	private Integer nextOrPrevious;
}

