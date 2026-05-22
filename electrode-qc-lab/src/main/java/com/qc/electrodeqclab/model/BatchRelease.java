package com.qc.electrodeqclab.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "batch_releases")
public class BatchRelease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "request_id")
    private TestRequest testRequest;

    private Boolean isReleased = false;
    private LocalDateTime releasedAt;
    private String coaFilePath;
    private String qrCodePath;
    private String releasedBy;

    public BatchRelease() {}

    // Getters
    public Long getId() { return id; }
    public TestRequest getTestRequest() { return testRequest; }
    public Boolean getIsReleased() { return isReleased; }
    public LocalDateTime getReleasedAt() { return releasedAt; }
    public String getCoaFilePath() { return coaFilePath; }
    public String getQrCodePath() { return qrCodePath; }
    public String getReleasedBy() { return releasedBy; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTestRequest(TestRequest testRequest) { this.testRequest = testRequest; }
    public void setIsReleased(Boolean isReleased) { this.isReleased = isReleased; }
    public void setReleasedAt(LocalDateTime releasedAt) { this.releasedAt = releasedAt; }
    public void setCoaFilePath(String coaFilePath) { this.coaFilePath = coaFilePath; }
    public void setQrCodePath(String qrCodePath) { this.qrCodePath = qrCodePath; }
    public void setReleasedBy(String releasedBy) { this.releasedBy = releasedBy; }
}