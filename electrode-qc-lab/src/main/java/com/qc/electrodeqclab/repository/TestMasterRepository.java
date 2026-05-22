package com.qc.electrodeqclab.repository;

import com.qc.electrodeqclab.model.TestMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestMasterRepository extends JpaRepository<TestMaster, Long> {
    List<TestMaster> findByIsMandatoryTrue();
}