package com.qc.electrodeqclab.service;

import com.qc.electrodeqclab.model.*;
import com.qc.electrodeqclab.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestRequestRepository requestRepository;

    @Autowired
    private TestMasterRepository masterRepository;

    @Autowired
    private TestResultRepository resultRepository;

    @Autowired
    private BatchReleaseRepository releaseRepository;

    // Create a new test request
    public TestRequest createTestRequest(String batchNumber, String electrodeType, String requestedBy) {
        TestRequest request = new TestRequest();
        request.setBatchNumber(batchNumber);
        request.setElectrodeType(electrodeType);
        request.setRequestedBy(requestedBy);
        request.setStatus(TestRequest.RequestStatus.PENDING);
        return requestRepository.save(request);
    }

    // Submit a test result and auto validate
    public TestResult submitTestResult(Long requestId, Long testMasterId, Double measuredValue, String testedBy) {
        TestRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        TestMaster testMaster = masterRepository.findById(testMasterId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        // Business logic: Validate pass/fail
        boolean isPassed = validateResult(measuredValue, testMaster);

        TestResult result = new TestResult();
        result.setTestRequest(request);
        result.setTestMaster(testMaster);
        result.setMeasuredValue(measuredValue);
        result.setIsPassed(isPassed);
        result.setTestedBy(testedBy);

        resultRepository.save(result);

        // Update request status after each result
        updateRequestStatus(request);

        return result;
    }

    // Business logic: Check if value passes
    private boolean validateResult(Double value, TestMaster testMaster) {
        if (value == null || testMaster.getMinStandardValue() == null) {
            return false;
        }
        return value >= testMaster.getMinStandardValue() &&
                value <= testMaster.getMaxStandardValue();
    }

    // Business logic: Update request status based on completed tests
    private void updateRequestStatus(TestRequest request) {
        List<TestMaster> mandatoryTests = masterRepository.findByIsMandatoryTrue();
        List<TestResult> results = resultRepository.findByTestRequest(request);

        long completedMandatory = results.stream()
                .filter(r -> r.getTestMaster().getIsMandatory())
                .count();

        if (completedMandatory >= mandatoryTests.size()) {
            long failedTests = resultRepository.countFailedTests(request);
            if (failedTests > 0) {
                request.setStatus(TestRequest.RequestStatus.REJECTED);
            } else {
                request.setStatus(TestRequest.RequestStatus.COMPLETED);
                // Auto release if all pass
                autoReleaseBatch(request);
            }
        } else {
            request.setStatus(TestRequest.RequestStatus.IN_PROGRESS);
        }
        requestRepository.save(request);
    }

    // Business logic: Auto release batch
    private void autoReleaseBatch(TestRequest request) {
        BatchRelease release = new BatchRelease();
        release.setTestRequest(request);
        release.setIsReleased(true);
        release.setReleasedAt(LocalDateTime.now());
        release.setReleasedBy("SYSTEM");
        release.setCoaFilePath("COA_" + request.getBatchNumber() + ".pdf");
        release.setQrCodePath("QR_" + request.getBatchNumber() + ".png");
        releaseRepository.save(release);

        request.setStatus(TestRequest.RequestStatus.RELEASED);
        requestRepository.save(request);
    }

    // Check if batch can be released
    public boolean canReleaseBatch(Long requestId) {
        TestRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != TestRequest.RequestStatus.COMPLETED) {
            return false;
        }

        long failedTests = resultRepository.countFailedTests(request);
        return failedTests == 0;
    }

    // Get all pending requests
    public List<TestRequest> getPendingRequests() {
        return requestRepository.findByStatus(TestRequest.RequestStatus.PENDING);
    }

    // Get all in-progress requests
    public List<TestRequest> getInProgressRequests() {
        return requestRepository.findByStatus(TestRequest.RequestStatus.IN_PROGRESS);
    }

    // Get all completed requests
    public List<TestRequest> getCompletedRequests() {
        return requestRepository.findByStatus(TestRequest.RequestStatus.COMPLETED);
    }

    // Get all released requests
    public List<TestRequest> getReleasedRequests() {
        return requestRepository.findByStatus(TestRequest.RequestStatus.RELEASED);
    }

    // Get all test masters
    public List<TestMaster> getAllTestMasters() {
        return masterRepository.findAll();
    }

    // Get existing results for a request
    public List<TestResult> getResultsForRequest(Long requestId) {
        TestRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        return resultRepository.findByTestRequest(request);
    }

    // Get test request by ID
    public TestRequest getRequestById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
    }

    // Get all requests
    public List<TestRequest> getAllRequests() {
        return requestRepository.findAll();
    }
    // Initialize default tests
    public void initializeDefaultTests() {
        if (masterRepository.count() == 0) {
            createDefaultTest("T001", "Tensile Strength", 400.0, 500.0, "MPa", true);
            createDefaultTest("T002", "Moisture Content", 0.0, 0.5, "%", true);
            createDefaultTest("T003", "Diffusible Hydrogen", 0.0, 5.0, "mL/100g", true);
            createDefaultTest("T004", "Yield Strength", 330.0, 420.0, "MPa", true);
            createDefaultTest("T005", "Elongation", 20.0, 35.0, "%", true);
            createDefaultTest("T006", "Impact Toughness", 47.0, 200.0, "J", false);
            createDefaultTest("T007", "Coating Adhesion", 80.0, 100.0, "%", true);
            createDefaultTest("T008", "Core Wire Diameter", 3.15, 4.0, "mm", false);
        }
    }

    private void createDefaultTest(String code, String name, Double min, Double max, String unit, boolean mandatory) {
        TestMaster master = new TestMaster();
        master.setTestCode(code);
        master.setTestName(name);
        master.setMinStandardValue(min);
        master.setMaxStandardValue(max);
        master.setUnit(unit);
        master.setIsMandatory(mandatory);
        masterRepository.save(master);
    }
}