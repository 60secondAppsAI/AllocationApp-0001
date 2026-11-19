package com.AllocationApp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;
import java.time.Year;
import jakarta.persistence.Transient;

@Entity
@Table(name="rebalance_instructions")
@Getter @Setter @NoArgsConstructor
public class RebalanceInstruction {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
  	@Column(name="rebalance_instruction_id")
	private Integer rebalanceInstructionId;
    
  	@Column(name="allocation_id")
	private Integer allocationId;
    
  	@Column(name="instrument_id")
	private Integer instrumentId;
    
  	@Column(name="target_quantity")
	private Double targetQuantity;
    
  	@Column(name="status")
	private String status;
    

}
