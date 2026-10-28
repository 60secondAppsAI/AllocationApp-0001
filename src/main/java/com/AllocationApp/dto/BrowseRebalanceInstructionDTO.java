package com.AllocationApp.dto;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class BrowseRebalanceInstructionDTO {

	private Integer ownerId;

	private Integer rebalanceInstructionId;

	private Integer rebalanceInstructionStatus;
	
	private Integer nextOrPrevious;
}

