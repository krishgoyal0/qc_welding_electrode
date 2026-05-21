package com.qc.electrodeqclab.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_results")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private TestRequest testRequest;

    @ManyToOne
    @JoinColumn(name = "test_master_id")
    private TestMaster testMaster;

    private Double measuredValue;
    private Boolean isPassed;
    private String testedBy;
    private LocalDateTime testedAt;

    public TestResult() {}

    @PrePersist
    protected void onCreate() {
        testedAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public TestRequest getTestRequest() { return testRequest; }
    public TestMaster getTestMaster() { return testMaster; }
    public Double getMeasuredValue() { return measuredValue; }
    public Boolean getIsPassed() { return isPassed; }
    public String getTestedBy() { return testedBy; }
    public LocalDateTime getTestedAt() { return testedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTestRequest(TestRequest testRequest) { this.testRequest = testRequest; }
    public void setTestMaster(TestMaster testMaster) { this.testMaster = testMaster; }
    public void setMeasuredValue(Double measuredValue) { this.measuredValue = measuredValue; }
    public void setIsPassed(Boolean isPassed) { this.isPassed = isPassed; }
    public void setTestedBy(String testedBy) { this.testedBy = testedBy; }
    public void setTestedAt(LocalDateTime testedAt) { this.testedAt = testedAt; }
}