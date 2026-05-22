package com.qc.electrodeqclab.repository;

import com.qc.electrodeqclab.model.BatchRelease;
import com.qc.electrodeqclab.model.TestRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BatchReleaseRepository extends JpaRepository<BatchRelease, Long> {
    Optional<BatchRelease> findByTestRequest(TestRequest testRequest);
}