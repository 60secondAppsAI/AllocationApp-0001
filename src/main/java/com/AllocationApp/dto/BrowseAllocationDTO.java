package com.AllocationApp.dto;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class BrowseAllocationDTO {

	private Integer ownerId;

	private Integer allocationId;

	private Integer allocationStatus;
	
	private Integer nextOrPrevious;
}

