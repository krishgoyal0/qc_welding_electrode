package com.qc.electrodeqclab.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_master")
public class TestMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String testCode;
    private String testName;
    private Double minStandardValue;
    private Double maxStandardValue;
    private String unit;
    private Boolean isMandatory = true;
    private LocalDateTime createdAt;

    public TestMaster() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public String getTestCode() { return testCode; }
    public String getTestName() { return testName; }
    public Double getMinStandardValue() { return minStandardValue; }
    public Double getMaxStandardValue() { return maxStandardValue; }
    public String getUnit() { return unit; }
    public Boolean getIsMandatory() { return isMandatory; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTestCode(String testCode) { this.testCode = testCode; }
    public void setTestName(String testName) { this.testName = testName; }
    public void setMinStandardValue(Double minStandardValue) { this.minStandardValue = minStandardValue; }
    public void setMaxStandardValue(Double maxStandardValue) { this.maxStandardValue = maxStandardValue; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setIsMandatory(Boolean isMandatory) { this.isMandatory = isMandatory; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}