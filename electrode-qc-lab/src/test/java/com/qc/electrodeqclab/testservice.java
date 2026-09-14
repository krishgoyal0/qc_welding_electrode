package com.qc.electrodeqclab;

import com.qc.electrodeqclab.model.*;
import com.qc.electrodeqclab.repository.*;
import com.qc.electrodeqclab.service.PDFGenerationService;
import com.qc.electrodeqclab.service.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceTest {

    @Mock private TestRequestRepository requestRepository;
    @Mock private TestMasterRepository masterRepository;
    @Mock private TestResultRepository resultRepository;
    @Mock private BatchReleaseRepository releaseRepository;
    @Mock private PDFGenerationService pdfGeneratorService;

    @InjectMocks
    private TestService testService;

    private TestRequest request;
    private TestMaster tensileTest;

    @BeforeEach
    void setUp() {
        request = new TestRequest();
        request.setId(1L);
        request.setBatchNumber("B-101");
        request.setStatus(TestRequest.RequestStatus.PENDING);

        tensileTest = new TestMaster();
        tensileTest.setId(1L);
        tensileTest.setTestName("Tensile Strength");
        tensileTest.setMinStandardValue(400.0);
        tensileTest.setMaxStandardValue(500.0);
        tensileTest.setIsMandatory(true);

        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(masterRepository.findById(1L)).thenReturn(Optional.of(tensileTest));
        when(resultRepository.save(any(TestResult.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void testResult_withinRange_shouldPass() {
        // Measured value 450 falls within 400-500 range
        TestResult result = testService.submitTestResult(1L, 1L, 450.0, "krishna");

        assertTrue(result.getIsPassed(), "Value within range should PASS");
    }

    @Test
    void testResult_outsideRange_shouldFail() {
        // Measured value 350 falls below the 400 minimum
        TestResult result = testService.submitTestResult(1L, 1L, 350.0, "krishna");

        assertFalse(result.getIsPassed(), "Value outside range should FAIL");
    }
}