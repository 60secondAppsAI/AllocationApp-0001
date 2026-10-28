CREATE DATABASE IF NOT EXISTS AllocationApp;

CREATE TABLE AllocationApp.allocations (
    `allocation_id` INT AUTO_INCREMENT,
    `portfolio_id` INT,
    `name` VARCHAR(255),
    `allocation_type` VARCHAR(255),
    `effective_date` DATETIME,
PRIMARY KEY (allocation_id)) ENGINE=MyISAM DEFAULT CHARSET=latin1;

CREATE TABLE AllocationApp.allocation_targets (
    `allocation_target_id` INT AUTO_INCREMENT,
    `allocation_id` INT,
    `target_type` VARCHAR(255),
    `target_reference` VARCHAR(255),
    `target_percent` DOUBLE,
PRIMARY KEY (allocation_target_id)) ENGINE=MyISAM DEFAULT CHARSET=latin1;

CREATE TABLE AllocationApp.allocation_snapshots (
    `allocation_snapshot_id` INT AUTO_INCREMENT,
    `allocation_id` INT,
    `target_reference` VARCHAR(255),
    `actual_percent` DOUBLE,
    `captured_at` DATETIME,
PRIMARY KEY (allocation_snapshot_id)) ENGINE=MyISAM DEFAULT CHARSET=latin1;

CREATE TABLE AllocationApp.rebalance_instructions (
    `rebalance_instruction_id` INT AUTO_INCREMENT,
    `allocation_id` INT,
    `instrument_id` INT,
    `target_quantity` DOUBLE,
    `status` VARCHAR(255),
PRIMARY KEY (rebalance_instruction_id)) ENGINE=MyISAM DEFAULT CHARSET=latin1;

USE drcvelocity;
