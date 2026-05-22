package com.qc.electrodeqclab.repository;

import com.qc.electrodeqclab.model.TestResult;
import com.qc.electrodeqclab.model.TestRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByTestRequest(TestRequest testRequest);

    @Query("SELECT COUNT(r) FROM TestResult r WHERE r.testRequest = ?1 AND r.isPassed = false")
    long countFailedTests(TestRequest request);
}