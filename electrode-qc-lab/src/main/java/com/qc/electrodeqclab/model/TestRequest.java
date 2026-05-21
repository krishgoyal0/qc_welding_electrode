package com.qc.electrodeqclab.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_requests")
public class TestRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestNumber;
    private String batchNumber;
    private String electrodeType;
    private String requestedBy;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    private LocalDateTime requestedAt;

    public enum RequestStatus {
        PENDING, IN_PROGRESS, COMPLETED, REJECTED, RELEASED
    }

    public TestRequest() {}

    @PrePersist
    protected void onCreate() {
        requestedAt = LocalDateTime.now();
        requestNumber = "REQ-" + System.currentTimeMillis();
    }

    // Getters
    public Long getId() { return id; }
    public String getRequestNumber() { return requestNumber; }
    public String getBatchNumber() { return batchNumber; }
    public String getElectrodeType() { return electrodeType; }
    public String getRequestedBy() { return requestedBy; }
    public RequestStatus getStatus() { return status; }
    public LocalDateTime getRequestedAt() { return requestedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setRequestNumber(String requestNumber) { this.requestNumber = requestNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }
    public void setElectrodeType(String electrodeType) { this.electrodeType = electrodeType; }
    public void setRequestedBy(String requestedBy) { this.requestedBy = requestedBy; }
    public void setStatus(RequestStatus status) { this.status = status; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
}