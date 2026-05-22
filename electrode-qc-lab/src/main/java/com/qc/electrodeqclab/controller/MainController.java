package com.qc.electrodeqclab.controller;

import com.qc.electrodeqclab.model.*;
import com.qc.electrodeqclab.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private TestService testService;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("pendingRequests", testService.getPendingRequests());
        model.addAttribute("inProgressRequests", testService.getInProgressRequests());
        model.addAttribute("completedRequests", testService.getCompletedRequests());
        model.addAttribute("releasedRequests", testService.getReleasedRequests());
        return "dashboard";
    }

    @GetMapping("/requests/new")
    public String newRequestForm(Model model) {
        model.addAttribute("testRequest", new TestRequest());
        return "new-request";
    }

    @PostMapping("/requests")
    public String createRequest(@ModelAttribute TestRequest request) {
        testService.createTestRequest(
                request.getBatchNumber(),
                request.getElectrodeType(),
                request.getRequestedBy()
        );
        return "redirect:/";
    }

    @GetMapping("/requests")
    public String listRequests(Model model) {
        model.addAttribute("requests", testService.getAllRequests());
        return "requests";
    }

    @GetMapping("/results/enter/{id}")
    public String enterResultsForm(@PathVariable Long id, Model model) {
        TestRequest request = testService.getRequestById(id);
        List<TestMaster> allTests = testService.getAllTestMasters();
        List<TestResult> existingResults = testService.getResultsForRequest(id);

        model.addAttribute("request", request);
        model.addAttribute("allTests", allTests);
        model.addAttribute("existingResults", existingResults);
        model.addAttribute("testResult", new TestResult());
        return "enter-results";
    }

    @PostMapping("/results")
    public String submitResult(@RequestParam Long requestId,
                               @RequestParam Long testMasterId,
                               @RequestParam Double measuredValue,
                               @RequestParam String testedBy) {

        testService.submitTestResult(requestId, testMasterId, measuredValue, testedBy);
        return "redirect:/results/enter/" + requestId;
    }

    @GetMapping("/init-tests")
    public String initTests() {
        testService.initializeDefaultTests();
        return "redirect:/";
    }
}