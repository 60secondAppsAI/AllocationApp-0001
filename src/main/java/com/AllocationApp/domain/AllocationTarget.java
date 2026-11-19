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
@Table(name="allocation_targets")
@Getter @Setter @NoArgsConstructor
public class AllocationTarget {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
  	@Column(name="allocation_target_id")
	private Integer allocationTargetId;
    
  	@Column(name="allocation_id")
	private Integer allocationId;
    
  	@Column(name="target_type")
	private String targetType;
    
  	@Column(name="target_reference")
	private String targetReference;
    
  	@Column(name="target_percent")
	private Double targetPercent;
    

}
