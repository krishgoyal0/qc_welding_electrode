package com.qc.electrodeqclab.repository;

import com.qc.electrodeqclab.model.TestRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestRequestRepository extends JpaRepository<TestRequest, Long> {
    List<TestRequest> findByStatus(TestRequest.RequestStatus status);
    List<TestRequest> findByBatchNumber(String batchNumber);
}