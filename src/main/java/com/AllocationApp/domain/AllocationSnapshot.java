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
@Table(name="allocation_snapshots")
@Getter @Setter @NoArgsConstructor
public class AllocationSnapshot {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
  	@Column(name="allocation_snapshot_id")
	private Integer allocationSnapshotId;
    
  	@Column(name="allocation_id")
	private Integer allocationId;
    
  	@Column(name="target_reference")
	private String targetReference;
    
  	@Column(name="actual_percent")
	private Double actualPercent;
    
  	@Column(name="captured_at")
	private Date capturedAt;
    

}
