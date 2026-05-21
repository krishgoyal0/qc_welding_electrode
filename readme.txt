# Electrode QC Lab System

**Quality Control System for Welding Electrode Factory**

A Spring Boot application that manages test requests, auto-validates results, and generates Certificate of Analysis (COA) with QR codes for batch verification.

---

## Problem It Solves

Manual quality control in electrode manufacturing leads to:
- Delayed certificate generation (2+ hours per batch)
- Human error in pass/fail calculation
- Difficulty tracking batch release status
- No digital proof for customers

**This system reduces certificate generation time from 2 hours to under 2 minutes.**

---

## Features

| Feature | Description |
|---------|-------------|
| Test Master Catalog | Define 8+ test types with pass/fail thresholds |
| Test Request Workflow | QC officer requests → Lab tech tests → Auto validation |
| Auto Pass/Fail | System validates results against defined standards |
| Certificate of Analysis (COA) | PDF generated with complete test results |
| QR Code Verification | Scan to verify batch authenticity |
| Batch Release Control | Batch released only after ALL mandatory tests pass |

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Spring Boot 3.1 |
| Database | MySQL |
| Frontend | Thymeleaf + HTML/CSS |
| PDF Generation | OpenPDF |
| QR Code | ZXing |
| Build Tool | Maven |
| Java Version | 17 |

---

## Database Schema
┌─────────────────┐ ┌─────────────────┐
│ test_master │ │ test_requests │
├─────────────────┤ ├─────────────────┤
│ id (PK) │ │ id (PK) │
│ test_code │ │ request_number │
│ test_name │ │ batch_number │
│ min_value │◄────│ electrode_type │
│ max_value │ │ status │
│ unit │ │ requested_by │
│ is_mandatory │ │ requested_at │
└─────────────────┘ └────────┬────────┘
│
↓
┌─────────────────┐ ┌─────────────────┐
│ test_results │ │ batch_releases │
├─────────────────┤ ├─────────────────┤
│ id (PK) │ │ id (PK) │
│ request_id (FK) │ │ request_id (FK) │
│ test_master_id │ │ is_released │
│ measured_value │ │ coa_file_path │
│ is_passed │ │ qr_code_path │
│ tested_by │ │ released_at │
│ tested_at │ └─────────────────┘
└─────────────────┘

text

---

## Project Structure
electrode-qc-lab/
│
├── src/main/java/com/qc/
│ ├── ElectrodeQcLabApplication.java
│ ├── controller/
│ │ ├── DashboardController.java
│ │ ├── RequestController.java
│ │ ├── ResultController.java
│ │ └── ReportController.java
│ ├── model/
│ │ ├── TestMaster.java
│ │ ├── TestRequest.java
│ │ ├── TestResult.java
│ │ └── BatchRelease.java
│ ├── repository/
│ │ ├── TestMasterRepository.java
│ │ ├── TestRequestRepository.java
│ │ ├── TestResultRepository.java
│ │ └── BatchReleaseRepository.java
│ ├── service/
│ │ ├── TestService.java
│ │ ├── PDFGeneratorService.java
│ │ └── ReleaseService.java
│ └── scheduler/
│ └── ReleaseScheduler.java
│
├── src/main/resources/
│ ├── application.properties
│ ├── templates/
│ │ ├── dashboard.html
│ │ ├── requests.html
│ │ ├── results.html
│ │ └── certificate.html
│ └── static/css/
│ └── style.css
│
├── qr-codes/ (generated QR codes)
├── coa-reports/ (generated PDFs)
└── pom.xml

text

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/dashboard` | View dashboard |
| GET | `/tests/master` | View test master catalog |
| POST | `/tests/master` | Add new test type |
| GET | `/requests/new` | Create test request form |
| POST | `/requests` | Submit test request |
| GET | `/requests/list` | View all requests |
| GET | `/results/enter/{requestId}` | Enter results form |
| POST | `/results` | Submit test results |
| GET | `/certificate/{batchNumber}` | Download COA PDF |
| GET | `/api/verify/{batchNumber}` | QR code verification API |

---

## Workflow
QC Officer Lab Tech System
| | |
|--- Create Request ---->| |
| (batch #, type) | |
| | |
| |--- Enter Results ------>|
| | (measured values) |
| | |
| | Auto-validate
| | Pass/Fail
| | |
| | |--- Generate PDF
| | | + QR Code
| | |
|<-----------------------| Batch Released
(only if all tests pass)

text

---

## Setup Instructions

### Prerequisites

- Java 17
- MySQL 8+
- Maven

### Installation

```bash

# Create database
mysql -u root -p
CREATE DATABASE electrode_qc;

# Update application.properties with your MySQL credentials

# Build and run
mvn clean install
mvn spring-boot:run
Access the application at http://localhost:8080

Default Test Types (Pre-configured)
Test Name	Min Value	Max Value	Unit	Mandatory
Tensile Strength	400	500	MPa	Yes
Moisture Content	0	0.5	%	Yes
Diffusible Hydrogen	0	5	mL/100g	Yes
Yield Strength	330	420	MPa	Yes
Elongation	20	35	%	Yes
Impact Toughness	47	200	J	No
Coating Adhesion	80	100	%	Yes
Core Wire Diameter	3.15	4.0	mm	No
Future Enhancements
Email notifications on batch release

Role-based authentication (Admin, QC, Lab Tech)

REST API for external integration

Batch comparison reports

Export to Excel