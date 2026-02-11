# Complete Seed Data — All 204 Tasks
# Format for DataSeeder.java

This file contains all 204 tasks in a format ready for Java implementation.
The DataSeeder should use a CommandLineRunner to insert these if the tasks table is empty.

## Implementation Notes

The DataSeeder.java should:
1. Check if tasks table is empty: `taskRepository.count() == 0`
2. Look up each sprint by sprint_number
3. Insert each task with all fields
4. The `deliverables` and `validation_criteria` fields are JSON arrays stored as strings
5. Calculate `week_number` based on task date relative to sprint start date
6. Set `sort_order` as sequential within each sprint

## Java Helper Method Signature
```java
private Task createTask(Sprint sprint, String code, String date, String day,
    int hours, int weekNum, String title, List<String> deliverables,
    String validation, String coverage) {
    Task t = new Task();
    t.setSprint(sprint);
    t.setTaskCode(code);
    t.setSessionDate(LocalDate.parse(date));
    t.setDayOfWeek(day);
    t.setPlannedHours(BigDecimal.valueOf(hours));
    t.setWeekNumber(weekNum);
    t.setTitle(title);
    t.setDeliverables(objectMapper.writeValueAsString(deliverables));
    t.setValidationCriteria(objectMapper.writeValueAsString(List.of(validation)));
    t.setCoverageTarget(coverage);
    t.setStatus(TaskStatus.PLANNED);
    return t;
}
```

---

## Sprint 1 (36 tasks)

### S1-01
```
code:       S1-01
date:       2026-03-02
day:        Mon
hours:      2
week:       W1
title:      Create GitHub organization and repositories
coverage:   N/A
deliverables:
  - Organization 'sgcd-angola': sgcd-backend, sgcd-frontend-backoffice, sgcd-frontend-portal, sgcd-infra, sgcd-docs
  - Branch protection rules and merge policies
  - PR and Issue templates in each repo
validation: Clone OK, push blocked without PR
```

### S1-02
```
code:       S1-02
date:       2026-03-03
day:        Tue
hours:      2
week:       W1
title:      Maven multi-module structure
coverage:   N/A
deliverables:
  - Maven parent with modules: commons, gateway, registration-svc, scheduling-svc, passport-svc, civil-registry-svc,
  - Shared dependencies in parent POM (Spring Boot 3.x, Java 21)
  - .gitignore, .editorconfig, README.md
validation: mvn clean compile without errors
```

### S1-03
```
code:       S1-03
date:       2026-03-04
day:        Wed
hours:      2
week:       W1
title:      Linting, hooks and contribution guidelines
coverage:   N/A
deliverables:
  - Checkstyle with Google Style
  - Commitlint + Husky (conventional commits)
  - CONTRIBUTING.md, CODE_OF_CONDUCT.md
validation: Non-conventional commit rejected
```

### S1-04
```
code:       S1-04
date:       2026-03-05
day:        Thu
hours:      2
week:       W1
title:      Angular Backoffice structure
coverage:   N/A
deliverables:
  - Angular 17+ with standalone components
  - ESLint + Prettier configured
  - Jest/Karma with 80% threshold
validation: ng build and ng test without errors
```

### S1-05
```
code:       S1-05
date:       2026-03-06
day:        Fri
hours:      2
week:       W1
title:      Angular Citizen Portal structure
coverage:   N/A
deliverables:
  - Angular Portal with i18n (@angular/localize)
  - Translations: PT, DE, EN, CS
  - PWA with basic service worker
validation: ng build with each locale without errors
```

### S1-06
```
code:       S1-06
date:       2026-03-15
day:        Sun
hours:      10
week:       W2
title:      Complete CI/CD Pipeline
coverage:   N/A
deliverables:
  - ci-backend.yml: checkout → Java 21 → mvn test → JaCoCo
  - ci-frontend.yml: checkout → Node → npm ci → ng test → ng lint
  - SonarQube Cloud in workflows
  - Cache Maven and npm dependencies
  - Mandatory status checks
  - cd-staging.yml: Docker build → push GHCR → deploy
  - Document workflows in sgcd-infra
  - Test pipelines with test PR
validation: Workflows OK, quality gates blocking
```

### S1-07
```
code:       S1-07
date:       2026-03-16
day:        Mon
hours:      2
week:       W3
title:      Multi-stage Backend Dockerfile
coverage:   N/A
deliverables:
  - Multi-stage Dockerfile for Spring Boot
  - Docker layers optimized for cache
  - .dockerignore
validation: docker build OK, image < 300MB
```

### S1-08
```
code:       S1-08
date:       2026-03-17
day:        Tue
hours:      2
week:       W3
title:      Frontend Dockerfile + Nginx
coverage:   N/A
deliverables:
  - Multi-stage Dockerfile for Angular
  - nginx.conf: gzip, cache, SPA fallback, security headers
  - Build for backoffice + portal
validation: Nginx serves app, secure headers
```

### S1-09
```
code:       S1-09
date:       2026-03-18
day:        Wed
hours:      2
week:       W3
title:      Docker Compose — Infrastructure
coverage:   N/A
deliverables:
  - docker-compose.yml: MySQL 8.0, Redis 7, Kafka + Zookeeper, MinIO
  - Healthchecks and depends_on
  - Persistent volumes
validation: docker-compose up all healthy
```

### S1-10
```
code:       S1-10
date:       2026-03-19
day:        Thu
hours:      2
week:       W3
title:      Docker Compose — Application
coverage:   N/A
deliverables:
  - Services: backend, backoffice, portal, Keycloak
  - Networking between containers
  - Script start-dev.sh
validation: start-dev.sh starts everything
```

### S1-11
```
code:       S1-11
date:       2026-03-20
day:        Fri
hours:      2
week:       W3
title:      Commons Module and Spring profiles
coverage:   N/A
deliverables:
  - Base DTOs: ApiResponse, PageResponse, ErrorResponse
  - Exceptions: ResourceNotFoundException, BusinessException
  - Spring profiles: dev, staging, prod
validation: Unit tests DTOs and exceptions
```

### S1-12
```
code:       S1-12
date:       2026-03-22
day:        Sun
hours:      10
week:       W3
title:      GlobalExceptionHandler, Flyway, Actuator
coverage:   N/A
deliverables:
  - GlobalExceptionHandler: exceptions → HTTP status
  - Flyway with V1__create_audit_table.sql
  - Seed script for initial data
  - Actuator: health, info, metrics, prometheus
  - Structured RequestLoggingFilter
  - Logback with JSON formatter
  - CorrelationID (X-Request-ID)
  - Unit and integration tests (8+ scenarios)
validation: GlobalExceptionHandler OK, Flyway Testcontainers
```

### S1-13
```
code:       S1-13
date:       2026-03-23
day:        Mon
hours:      2
week:       W4
title:      Spring Cloud Gateway — Routes
coverage:   N/A
deliverables:
  - API Gateway with Spring Cloud Gateway
  - Routes: /api/v1/registration/**, /scheduling/**, /passport/**, /civil-registry/**
  - Request/response logging
validation: Routing correct
```

### S1-14
```
code:       S1-14
date:       2026-03-24
day:        Tue
hours:      2
week:       W4
title:      Rate Limiting and CORS
coverage:   N/A
deliverables:
  - Rate limiting Redis (bucket4j): 100 req/min per IP
  - CORS policy configured
  - Request size limits (max 10MB)
validation: 429 after exceeding limit
```

### S1-15
```
code:       S1-15
date:       2026-03-25
day:        Wed
hours:      2
week:       W4
title:      Keycloak — Realm and Clients
coverage:   N/A
deliverables:
  - Keycloak in Docker Compose
  - Realm 'sgcd': token lifetime, password policy, brute force
  - Clients: sgcd-backend, sgcd-backoffice, sgcd-portal
validation: Realm OK, clients functional
```

### S1-16
```
code:       S1-16
date:       2026-03-26
day:        Thu
hours:      2
week:       W4
title:      Keycloak — Roles and Users
coverage:   N/A
deliverables:
  - Roles: ADMIN, CONSUL, ATTENDANCE_OFFICER, FINANCIAL_OFFICER, CIVIL_OFFICER, CITIZEN
  - Test users per role
  - Export realm config JSON
validation: Login OK, token contains roles
```

### S1-17
```
code:       S1-17
date:       2026-03-27
day:        Fri
hours:      2
week:       W4
title:      Circuit Breaker and Resilience
coverage:   N/A
deliverables:
  - Resilience4j: circuit breaker, retry, timeout
  - Fallback responses
  - Failure tests
validation: Circuit breaker opens after failures
```

### S1-18
```
code:       S1-18
date:       2026-04-05
day:        Sun
hours:      10
week:       W5
title:      OAuth2, JWT and Complete Authorization
coverage:   N/A
deliverables:
  - OAuth2 Resource Server with JWT via Keycloak
  - JwtAuthenticationConverter for roles/claims
  - SecurityConfig with @PreAuthorize per endpoint
  - Annotations: @AdminOnly, @ConsulOrAbove, @Authenticated
  - SecurityContextService
  - AuthInterceptor Angular (JWT), ErrorInterceptor (401 → login)
  - AuthGuard and RoleGuard Angular
  - Directives *hasRole and *hasAnyRole
validation: Authorized/denied per role, login/logout E2E
```

### S1-19
```
code:       S1-19
date:       2026-04-07
day:        Tue
hours:      2
week:       W6
title:      Angular Backoffice Layout
coverage:   N/A
deliverables:
  - Shell: responsive sidebar, header user info, footer
  - Angular Material theme
  - Global loading spinner and toast notifications
validation: Layout, sidebar toggle, responsive
```

### S1-20
```
code:       S1-20
date:       2026-04-08
day:        Wed
hours:      2
week:       W6
title:      Routing and Lazy Loading
coverage:   N/A
deliverables:
  - Lazy loading per functional module
  - Dynamic breadcrumbs
  - 404 page and post-login redirect
validation: Lazy loading chunks, breadcrumbs OK
```

### S1-21
```
code:       S1-21
date:       2026-04-09
day:        Thu
hours:      2
week:       W6
title:      Angular Base HTTP Services
coverage:   N/A
deliverables:
  - Generic typed ApiService: get, post, put, patch
  - Reusable pagination and filters
  - Local cache service
validation: ApiService with HttpClientTestingModule
```

### S1-22
```
code:       S1-22
date:       2026-04-10
day:        Fri
hours:      2
week:       W6
title:      Prometheus and Grafana
coverage:   N/A
deliverables:
  - Docker Compose with Prometheus + Grafana
  - Scraping Actuator and Kafka metrics
  - Dashboards: JVM, HTTP, MySQL
validation: Targets up, dashboards with data
```

### S1-23
```
code:       S1-23
date:       2026-04-13
day:        Mon
hours:      2
week:       W7
title:      Alerts and Custom Metrics
coverage:   N/A
deliverables:
  - Micrometer: requests, latency
  - Grafana alerts: CPU > 80%, mem > 85%, 5xx > 1%
  - Overview dashboard
validation: Metrics visible, alerts trigger
```

### S1-24
```
code:       S1-24
date:       2026-04-19
day:        Sun
hours:      10
week:       W7
title:      E2E Integration and Sprint 1 Review
coverage:   N/A
deliverables:
  - Cypress with fixtures and commands
  - E2E: login → dashboard → role
  - Complete cd-staging.yml
  - Staging environment
  - Database seeding staging
  - Deploy staging and validate services
  - Smoke tests staging
  - Code review and final merge
validation: Staging operational, smoke tests OK
```

### S1-25
```
code:       S1-25
date:       2026-04-20
day:        Mon
hours:      2
week:       W8
title:      Base document upload service
coverage:   N/A
deliverables:
  - DocumentStorageService with MinIO: upload, download, delete
  - File type validation by magic bytes
  - Size validation (max 5MB) and formats (PDF, JPG, PNG)
validation: Upload/download MinIO functional
```

### S1-26
```
code:       S1-26
date:       2026-04-21
day:        Tue
hours:      2
week:       W8
title:      Base notification service
coverage:   N/A
deliverables:
  - NotificacaoService with Spring Mail + Thymeleaf
  - Base templates: confirmation, error, info
  - Asynchronous sending via Kafka
validation: GreenMail: email with template
```

### S1-27
```
code:       S1-27
date:       2026-04-22
day:        Wed
hours:      2
week:       W8
title:      Kafka — Configuration and Topics
coverage:   N/A
deliverables:
  - Kafka topics: registration, scheduling, passport, civil-registry, payment, workflow, audit
  - JSON serialization with schema
  - Dead letter queue for failures
validation: Topics created, produce/consume OK
```

### S1-28
```
code:       S1-28
date:       2026-04-23
day:        Thu
hours:      2
week:       W8
title:      AuditService — Audit Trail
coverage:   N/A
deliverables:
  - AuditConsumer: Kafka events → audit table
  - GET /audit?entity=...&action;=...&from;=...&to;=...
  - Retention: >1 year → archive, >3 years → delete
validation: Consumer OK, filters functional
```

### S1-29
```
code:       S1-29
date:       2026-04-24
day:        Fri
hours:      2
week:       W8
title:      Integration Tests Configuration
coverage:   N/A
deliverables:
  - Testcontainers for MySQL, Kafka, MinIO, Keycloak
  - TestDataFactory for test data
  - Test profiles: @ActiveProfiles(\"test\")
validation: Testcontainers start in < 30s
```

### S1-30
```
code:       S1-30
date:       2026-04-26
day:        Sun
hours:      10
week:       W8
title:      Base Workflow Module (State Machine)
coverage:   N/A
deliverables:
  - ConsularProcess base entity
  - WorkflowStateMachine: generic transitions for all modules
  - ProcessState enum: DRAFT, SUBMITTED, UNDER_REVIEW, VALIDATED, PENDING_CITIZEN,
  - ProcessService: create, transition, comment
  - ApprovalService: multi-level routing (SIMPLE, INTERMEDIATE, COMPLEX)
  - Kafka Event ProcessStateChanged
  - GET /processes/{id}/history
  - Tests: valid and invalid transitions (20+ scenarios)
validation: Generic state machine, 20+ scenarios
```

### S1-31
```
code:       S1-31
date:       2026-04-27
day:        Mon
hours:      2
week:       W9
title:      Frontend — Process Inbox (generic)
coverage:   N/A
deliverables:
  - Inbox: pending by urgency and age
  - Badges: urgent (red), >48h (yellow), normal
  - Filters: type, state, post, module
validation: Inbox renders, filters, badges
```

### S1-32
```
code:       S1-32
date:       2026-04-28
day:        Tue
hours:      2
week:       W9
title:      Frontend — Process Timeline
coverage:   N/A
deliverables:
  - Visual timeline of process states
  - Stepper: past (green), current (blue), future (gray)
  - Documents section with upload
validation: Timeline, stepper per state
```

### S1-33
```
code:       S1-33
date:       2026-04-29
day:        Wed
hours:      2
week:       W9
title:      Frontend — Approval/Return Modal
coverage:   N/A
deliverables:
  - Modal: approve, return (mandatory reason), reject
  - Confirmation before irreversible actions
  - Toast notifications feedback
validation: Modal, reason validation, actions
```

### S1-34
```
code:       S1-34
date:       2026-04-30
day:        Thu
hours:      2
week:       W9
title:      User Management
coverage:   N/A
deliverables:
  - UserService: CRUD + Keycloak sync
  - Role management: assign/remove
  - Access profile per consular post
validation: CRUD → Keycloak sync, role, post
```

### S1-35
```
code:       S1-35
date:       2026-05-04
day:        Mon
hours:      2
week:       W10
title:      Session Log and Security
coverage:   N/A
deliverables:
  - Session logs: login, logout, refresh, IP, user-agent
  - Active sessions for admin
  - Lock after 5 failed attempts
validation: Login recorded, lock works
```

### S1-36
```
code:       S1-36
date:       2026-05-10
day:        Sun
hours:      10
week:       W10
title:      Final Integration and Sprint 1 Buffer
coverage:   N/A
deliverables:
  - Delegation: absent officer → substitute
  - Escalation > 48h → upper level
  - @Scheduled SLA alerts: >24h, >48h, >72h
  - Complete regression suite all base modules
  - Deploy staging with full infra
  - Smoke tests all endpoints
  - Documentation: ADRs, setup guide, architecture overview
  - Prepare seed data for Sprint 2
validation: Staging stable, base workflow functional
```

---

## Sprint 2 (30 tasks)

### S2-01
```
code:       S2-01
date:       2026-05-11
day:        Mon
hours:      2
week:       W1
title:      JPA Entities — Citizen
coverage:   N/A
deliverables:
  - Citizen: id, name, dateOfBirth, parentage, maritalStatus, profession, qualifications
  - ForeignAddress: street, city, postalCode, country
  - Enums: MaritalStatus, Jurisdiction, DocumentType
validation: JPA persistence with Testcontainers
```

### S2-02
```
code:       S2-02
date:       2026-05-12
day:        Tue
hours:      2
week:       W1
title:      JPA Entities — Registration and Documents
coverage:   N/A
deliverables:
  - ConsularRegistration: registrationNumber, status, jurisdiction, consularPost
  - DigitalDocument: type, originalName, path, hash, size, validated
  - Flyway: V2, V3 create tables with indexes
validation: Migrations OK, foreign keys, indexes
```

### S2-03
```
code:       S2-03
date:       2026-05-13
day:        Wed
hours:      2
week:       W1
title:      DTOs and MapStruct Mappers
coverage:   N/A
deliverables:
  - CitizenCreateRequest, CitizenUpdateRequest, CitizenResponse
  - MapStruct bidirectional mappers
  - Validators: @ValidBI, @ValidPassport, @ValidNIF
validation: Tests each mapper and validator (10+)
```

### S2-04
```
code:       S2-04
date:       2026-05-15
day:        Fri
hours:      2
week:       W1
title:      Repository and Specification Pattern
coverage:   N/A
deliverables:
  - CitizenRepository: findByIdNumber, findByPassportNumber
  - ConsularRegistrationRepository: by jurisdiction, status, date
  - Specification pattern for dynamic multi-criteria search
validation: Integration tests Testcontainers
```

### S2-05
```
code:       S2-05
date:       2026-05-18
day:        Mon
hours:      2
week:       W2
title:      Service Layer — Business Validations
coverage:   N/A
deliverables:
  - CitizenService: ID/passport uniqueness, mandatory fields
  - Registration number generation: YEAR/JURISDICTION/SEQ
  - Age validation, eligibility, prior registration check
validation: Mockito: duplicate, invalid, generation (8+)
```

### S2-06
```
code:       S2-06
date:       2026-05-24
day:        Sun
hours:      10
week:       W2
title:      Complete REST API and Upload
coverage:   N/A
deliverables:
  - POST /api/v1/registrations (create registration)
  - GET (by ID), GET (paginated listing), PATCH, DELETE (soft delete)
  - POST /registrations/{id}/documents (multipart, 5MB, PDF/JPG/PNG)
  - Photo validation: ICAO dimensions, format
  - ClamAV virus scan on uploads
  - Workflow integration: registration → automatic process
  - CRUD + upload tests (20+ scenarios)
  - Error tests: large file, invalid type, duplicate
validation: Testcontainers suite MySQL + MinIO
```

### S2-07
```
code:       S2-07
date:       2026-05-26
day:        Tue
hours:      2
week:       W3
title:      Automatic Jurisdiction Mapping
coverage:   N/A
deliverables:
  - JurisdictionService: postal code → consular post
  - Table: DE codes → Berlin/Bremen/Düsseldorf/Munich; CZ → Prague
  - Manual fallback when code not recognized
validation: 20+ postal codes, fallback
```

### S2-08
```
code:       S2-08
date:       2026-05-27
day:        Wed
hours:      2
week:       W3
title:      Consular Card — PDF Generation
coverage:   N/A
deliverables:
  - ConsularCardService: A6 PDF with data, photo, coat of arms
  - iText/OpenPDF template: name, ID, passport, jurisdiction, validity
  - GET /registrations/{id}/card
validation: Correct PDF, A6, photo
```

### S2-09
```
code:       S2-09
date:       2026-05-28
day:        Thu
hours:      2
week:       W3
title:      QR Code Verification
coverage:   N/A
deliverables:
  - QRCodeService: QR with HMAC signature
  - QR on consular card PDF
  - GET /verify/{code} (public endpoint)
validation: QR encode/decode, valid/invalid HMAC
```

### S2-10
```
code:       S2-10
date:       2026-05-29
day:        Fri
hours:      2
week:       W3
title:      Full-Text Search
coverage:   N/A
deliverables:
  - MySQL FULLTEXT on name and birthPlace
  - Combined filters: name, jurisdiction, status, date
  - GET /registrations?search=...&page;=...
validation: Partial search, combined, pagination
```

### S2-11
```
code:       S2-11
date:       2026-06-01
day:        Mon
hours:      2
week:       W4
title:      Excel/CSV Export
coverage:   N/A
deliverables:
  - Apache POI (Excel) and CSV
  - GET /registrations/export?format=xlsx|csv with filters
  - Correct download headers and content-type
validation: Valid Excel, CSV, filters
```

### S2-12
```
code:       S2-12
date:       2026-06-07
day:        Sun
hours:      10
week:       W4
title:      Frontend — Complete Consular Registration
coverage:   N/A
deliverables:
  - Multi-step form: Personal Data → Address → Upload → Confirmation
  - Drag-and-drop upload (ID, passport, photo, proof of address)
  - Reactive validation per step
  - Citizen listing: Material table with filters, search, pagination
  - Citizen details: tabs Data, Documents, Card, Process, History
  - Inline document preview
  - Consular card download
  - Component tests (15+)
validation: Form, upload, table, details
```

### S2-13
```
code:       S2-13
date:       2026-06-08
day:        Mon
hours:      2
week:       W5
title:      Partial Update (PATCH)
coverage:   N/A
deliverables:
  - PATCH /registrations/{id} with merge patch
  - Protected fields (ID, birth) → approval required
  - Optimistic versioning @Version
validation: Patch, protected rejected, conflict
```

### S2-14
```
code:       S2-14
date:       2026-06-09
day:        Tue
hours:      2
week:       W5
title:      Change History
coverage:   N/A
deliverables:
  - ChangeHistory: field, previousValue, newValue, date, user
  - Hibernate Envers/JPA listener
  - GET /registrations/{id}/history
validation: Change → history recorded
```

### S2-15
```
code:       S2-15
date:       2026-06-10
day:        Wed
hours:      2
week:       W5
title:      Expiration Notification
coverage:   N/A
deliverables:
  - @Scheduled: registrations expiring (90, 30, 7 days)
  - Renewal email template
  - Renewal with pre-filled data
validation: Scheduler notifies, template OK
```

### S2-16
```
code:       S2-16
date:       2026-06-11
day:        Thu
hours:      2
week:       W5
title:      Kafka Producer — Events
coverage:   N/A
deliverables:
  - Events: RegistrationCreated, Updated, Expiring, DocumentUploaded
  - JSON serialization with schema
  - Publication after each CRUD
validation: EmbeddedKafka: event published
```

### S2-17
```
code:       S2-17
date:       2026-06-12
day:        Fri
hours:      2
week:       W5
title:      Consular Registration Renewal
coverage:   N/A
deliverables:
  - Renewal workflow: pre-fill existing data
  - Update expired documents
  - New consular card after renewal
validation: Renewal preserves history
```

### S2-18
```
code:       S2-18
date:       2026-06-14
day:        Sun
hours:      10
week:       W5
title:      E2E Tests and Sprint 2 Review
coverage:   N/A
deliverables:
  - E2E Cypress: register → upload → search → details
  - E2E: generate card → verify QR
  - E2E: update → history
  - Integration: registration-svc → Kafka → audit
  - Security tests: CITIZEN ≠ ADMIN
  - Fix E2E and integration bugs
  - Deploy staging with 50+ test citizens
  - Sprint 2 Demo
validation: E2E suite 10+ scenarios, staging OK
```

### S2-19
```
code:       S2-19
date:       2026-06-15
day:        Mon
hours:      2
week:       W6
title:      Edge Cases and Validations
coverage:   N/A
deliverables:
  - Names with ç, ã, ü; boundary dates
  - Rate limiting uploads (10/hour)
  - Total document limit (50MB)
validation: Edge cases, rate limiting
```

### S2-20
```
code:       S2-20
date:       2026-06-16
day:        Tue
hours:      2
week:       W6
title:      Performance and Queries
coverage:   N/A
deliverables:
  - EXPLAIN all queries + indexes
  - Redis cache for frequent queries
  - Optimize N+1: @EntityGraph, JOIN FETCH
validation: Queries < 200ms, indexes used
```

### S2-21
```
code:       S2-21
date:       2026-06-17
day:        Wed
hours:      2
week:       W6
title:      API Documentation OpenAPI
coverage:   N/A
deliverables:
  - Swagger/OpenAPI annotations all registration endpoints
  - Request/response examples
  - Grouped Swagger UI
validation: Valid schema, examples OK
```

### S2-22
```
code:       S2-22
date:       2026-06-18
day:        Thu
hours:      2
week:       W6
title:      Frontend UX Improvements
coverage:   N/A
deliverables:
  - Skeleton loaders
  - Upload progress bar, preview thumbnail
  - PT error messages
validation: Loading states, upload feedback
```

### S2-23
```
code:       S2-23
date:       2026-06-19
day:        Fri
hours:      2
week:       W6
title:      Code Quality and Refactoring
coverage:   N/A
deliverables:
  - SonarQube: 0 bugs, 0 vulnerabilities
  - Refactoring duplicated code
  - README registration module
validation: Quality gate passes
```

### S2-24
```
code:       S2-24
date:       2026-06-21
day:        Sun
hours:      10
week:       W6
title:      Staging Deploy and Sprint 2 Validation
coverage:   N/A
deliverables:
  - Deploy staging with 50+ test citizens
  - Edge cases: ç, ã, ü; simultaneous uploads
  - Performance: EXPLAIN queries + indexes
  - Redis cache for frequent queries
  - Optimize N+1: @EntityGraph, JOIN FETCH
  - Swagger/OpenAPI registration endpoints
  - Complete smoke tests
  - Prepare citizen portal for Sprint 2 Week 5
validation: Staging validated, queries < 200ms
```

### S2-25
```
code:       S2-25
date:       2026-06-22
day:        Mon
hours:      2
week:       W7
title:      Citizen Portal — Online Registration
coverage:   N/A
deliverables:
  - Multi-step public registration form on portal
  - Document upload with preview
  - Confirmation and protocol number
validation: Registration via portal functional
```

### S2-26
```
code:       S2-26
date:       2026-06-23
day:        Tue
hours:      2
week:       W7
title:      Citizen Portal — Personal Registration Area
coverage:   N/A
deliverables:
  - 'My Registration': data, status, documents
  - Data update via portal
  - Consular card download
validation: Personal area functional
```

### S2-27
```
code:       S2-27
date:       2026-06-24
day:        Wed
hours:      2
week:       W7
title:      Workflow Integration — Registration
coverage:   N/A
deliverables:
  - Registration → automatic process (SIMPLE workflow)
  - Process approved → registration active → card issued
  - End-to-end flow: submit → approve → card
validation: Complete flow functional
```

### S2-28
```
code:       S2-28
date:       2026-06-25
day:        Thu
hours:      2
week:       W7
title:      Consular Registration Reports
coverage:   N/A
deliverables:
  - Monthly report: registrations by jurisdiction, status
  - Trend chart
  - Export PDF and Excel
validation: Report, export OK
```

### S2-29
```
code:       S2-29
date:       2026-06-26
day:        Fri
hours:      2
week:       W7
title:      Registration Dashboard
coverage:   N/A
deliverables:
  - KPIs: total registered, new/month, expiring
  - Distribution by jurisdiction and post
  - Latest registrations
validation: Dashboard functional
```

### S2-30
```
code:       S2-30
date:       2026-06-28
day:        Sun
hours:      10
week:       W7
title:      Buffer / Sprint 3 Preparation
coverage:   N/A
deliverables:
  - Complete delayed tasks
  - Complete regression suite
  - Prepare scheduling seed data
  - Documentation and ADRs
  - Sprint 2 Retrospective
  - Deploy staging with citizen portal
  - Verify i18n on portal (4 languages)
  - Smoke tests all registration flows
validation: 100% green regression, staging OK
```

---

## Sprint 3 (30 tasks)

### S3-01
```
code:       S3-01
date:       2026-06-29
day:        Mon
hours:      2
week:       W1
title:      JPA Entities — Scheduling
coverage:   N/A
deliverables:
  - Appointment, TimeSlot, PostConfiguration, ServiceType
  - Enums: AppointmentStatus, AttendanceType, Priority
  - Flyway: V4__create_scheduling_tables.sql
validation: Migrations OK, constraints
```

### S3-02
```
code:       S3-02
date:       2026-06-30
day:        Tue
hours:      2
week:       W1
title:      Seed Data — Posts and Configuration
coverage:   N/A
deliverables:
  - 4 posts: Berlin, Bremen, Düsseldorf, Munich + Prague
  - Service types with duration (30/45/60min)
  - DE and CZ holidays for exclusion
validation: Seed OK, slots excluded on holidays
```

### S3-03
```
code:       S3-03
date:       2026-07-01
day:        Wed
hours:      2
week:       W1
title:      SlotService — Slot Calculation
coverage:   N/A
deliverables:
  - Slots per post, day, service type
  - Exclusion: holidays, occupied, lunch
  - Pessimistic locking for conflicts
validation: Correct generation, no conflicts
```

### S3-04
```
code:       S3-04
date:       2026-07-02
day:        Thu
hours:      2
week:       W1
title:      Scheduling API — CRUD
coverage:   N/A
deliverables:
  - POST create, GET by ID, GET list, DELETE cancel
  - GET /scheduling/slots?postId=...&date;=...&serviceType;=...
  - Advance: min 24h, max 90 days
validation: CRUD + slot occupied + cancel
```

### S3-05
```
code:       S3-05
date:       2026-07-03
day:        Fri
hours:      2
week:       W1
title:      Cancellation and Rescheduling
coverage:   N/A
deliverables:
  - Cancellation min 4h before
  - Rescheduling: release + reserve + history
  - 3 no-shows → 30-day block
validation: Deadline/past deadline, no-show block
```

### S3-06
```
code:       S3-06
date:       2026-07-05
day:        Sun
hours:      10
week:       W1
title:      Queues, Priorities and WebSocket
coverage:   N/A
deliverables:
  - QueueService: URGENT > ELDERLY/PREGNANT > MINOR > NORMAL
  - Digital check-in with QR code
  - WebSocket STOMP over SockJS
  - Broadcast: new, called, started, completed
  - Dynamic wait time estimate
  - POST /queue/next (call next)
  - Attendance record: start, end, result
  - Queue tests (15+ scenarios)
validation: Queue 15+ scenarios, WebSocket OK
```

### S3-07
```
code:       S3-07
date:       2026-07-06
day:        Mon
hours:      2
week:       W2
title:      Email Notifications
coverage:   N/A
deliverables:
  - Templates: confirmation, 24h reminder, cancellation
  - Asynchronous sending via Kafka
  - Retry with backoff
validation: Email with correct template
```

### S3-08
```
code:       S3-08
date:       2026-07-07
day:        Tue
hours:      2
week:       W2
title:      Reminder Scheduler
coverage:   N/A
deliverables:
  - @Scheduled: tomorrow's appointments → reminder
  - Log: sent, failed, bounced
  - Retry failed
validation: Automatic reminder, retry
```

### S3-09
```
code:       S3-09
date:       2026-07-08
day:        Wed
hours:      2
week:       W2
title:      Frontend — Backoffice Calendar
coverage:   N/A
deliverables:
  - FullCalendar Angular: weekly view per post
  - Post selector
  - Click slot → detail modal
validation: Calendar, filter, modal
```

### S3-10
```
code:       S3-10
date:       2026-07-09
day:        Thu
hours:      2
week:       W2
title:      Frontend — Calendar Interactions
coverage:   N/A
deliverables:
  - Modal: confirm, cancel, reschedule, view citizen
  - Drag-and-drop rescheduling
  - Color legend per state
validation: Actions, drag-and-drop
```

### S3-11
```
code:       S3-11
date:       2026-07-10
day:        Fri
hours:      2
week:       W2
title:      Frontend — Queue Dashboard
coverage:   N/A
deliverables:
  - WebSocket queue by priority
  - 'Call Next' button
  - Wait time, priority badge, status
validation: Queue updates, call works
```

### S3-12
```
code:       S3-12
date:       2026-07-12
day:        Sun
hours:      10
week:       W2
title:      Citizen Portal — Scheduling
coverage:   N/A
deliverables:
  - Wizard: Service → Post → Date/Time → Confirmation
  - 'My Appointments': listing, cancel/reschedule
  - i18n (PT, DE, EN, CS)
  - QR check-in on detail
  - E2E: schedule → confirm → cancel → reschedule
  - Mobile/tablet PWA responsiveness
validation: E2E portal 4 languages, mobile
```

### S3-13
```
code:       S3-13
date:       2026-07-13
day:        Mon
hours:      2
week:       W3
title:      Daily Dashboard — Statistics
coverage:   N/A
deliverables:
  - GET /dashboard/daily: total, attended, no-shows
  - Average wait/attendance time
  - Day/week comparison
validation: Correct calculations
```

### S3-14
```
code:       S3-14
date:       2026-07-14
day:        Tue
hours:      2
week:       W3
title:      Dashboard — Frontend
coverage:   N/A
deliverables:
  - KPI cards: attended, waiting, average time, no-shows
  - Appointments/hour chart
  - Upcoming appointments with countdown
validation: KPIs OK, chart present
```

### S3-15
```
code:       S3-15
date:       2026-07-15
day:        Wed
hours:      2
week:       W3
title:      Attendance Record
coverage:   N/A
deliverables:
  - POST /attendances: start, end, observations, result
  - Auto-start when officer calls next
  - Categorization: completed, requires docs, forwarded
validation: Record, auto-start, categorization
```

### S3-16
```
code:       S3-16
date:       2026-07-16
day:        Thu
hours:      2
week:       W3
title:      Attendance Reports
coverage:   N/A
deliverables:
  - Weekly report: by type, post, officer
  - Export PDF and Excel
  - Redis cache (TTL: 1h)
validation: Report OK, export, cache
```

### S3-17
```
code:       S3-17
date:       2026-07-17
day:        Fri
hours:      2
week:       W3
title:      Frontend — Reports
coverage:   N/A
deliverables:
  - Filters: period, post, type
  - Trend charts
  - Export PDF/Excel
validation: Filters, export works
```

### S3-18
```
code:       S3-18
date:       2026-07-19
day:        Sun
hours:      10
week:       W3
title:      E2E Tests and Sprint 3 Review
coverage:   N/A
deliverables:
  - E2E: schedule → check-in → queue → called → attendance
  - k6: 50 simultaneous appointments
  - Integration: scheduling → Kafka → email
  - Concurrency: two citizens same slot
  - Fix bugs, deploy staging
  - Sprint 3 Demo, retrospective
validation: E2E 10+, k6 p95 < 500ms, staging OK
```

### S3-19
```
code:       S3-19
date:       2026-07-20
day:        Mon
hours:      2
week:       W4
title:      Advanced Post Configuration
coverage:   N/A
deliverables:
  - Admin interface for schedules per post
  - Holiday management per country
validation: Schedule → slots updated
```

### S3-20
```
code:       S3-20
date:       2026-07-21
day:        Tue
hours:      2
week:       W4
title:      Push Notifications (PWA)
coverage:   N/A
deliverables:
  - Web Push Portal
  - Opt-in/opt-out by citizen
validation: Push on state change
```

### S3-21
```
code:       S3-21
date:       2026-07-22
day:        Wed
hours:      2
week:       W4
title:      Scheduling Performance
coverage:   N/A
deliverables:
  - Composite indexes date + post + status
  - Cache slots with Kafka invalidation
validation: Slots < 50ms, cache > 80%
```

### S3-22
```
code:       S3-22
date:       2026-07-23
day:        Thu
hours:      2
week:       W4
title:      Regression Tests
coverage:   N/A
deliverables:
  - Complete suite registration + scheduling
  - Resolve flaky tests
validation: 100% green, zero flaky
```

### S3-23
```
code:       S3-23
date:       2026-07-24
day:        Fri
hours:      2
week:       W4
title:      Documentation and ADRs
coverage:   N/A
deliverables:
  - Scheduling API OpenAPI
  - ADR-003 (WebSocket), ADR-004 (prioritization)
validation: Valid OpenAPI, ADRs
```

### S3-24
```
code:       S3-24
date:       2026-07-26
day:        Sun
hours:      10
week:       W4
title:      Staging Deploy and Sprint 3 Validation
coverage:   N/A
deliverables:
  - Deploy staging with complete scheduling
  - E2E: schedule → check-in → queue → attendance → report
  - k6: 50 simultaneous appointments
  - Concurrency: two citizens same slot
  - Performance tuning scheduling queries
  - Smoke tests registration + scheduling integrated
  - Fix found bugs
  - Sprint 3 mid-point demo
validation: E2E 10+, k6 p95 < 500ms
```

### S3-25
```
code:       S3-25
date:       2026-07-27
day:        Mon
hours:      2
week:       W5
title:      Registration → Scheduling Integration
coverage:   N/A
deliverables:
  - Verify active consular registration before scheduling
  - Prerequisites per service type
  - Citizen data pre-filled from registration
validation: Without registration → blocked
```

### S3-26
```
code:       S3-26
date:       2026-07-28
day:        Tue
hours:      2
week:       W5
title:      Capacity Management per Post
coverage:   N/A
deliverables:
  - Max appointments/day per post configuration
  - Capacity > 80% alerts
  - Weekly occupancy dashboard
validation: Limits respected, alert OK
```

### S3-27
```
code:       S3-27
date:       2026-07-29
day:        Wed
hours:      2
week:       W5
title:      Agenda Export
coverage:   N/A
deliverables:
  - Weekly agenda PDF export per post
  - Daily list export for reception
  - iCalendar (.ics) for officers
validation: Export PDF, iCal functional
```

### S3-28
```
code:       S3-28
date:       2026-07-30
day:        Thu
hours:      2
week:       W5
title:      Portal Improvements — Scheduling
coverage:   N/A
deliverables:
  - Service type search with auto-complete
  - Confirmation with consular post map
  - Simplified cancellation and rescheduling
validation: Improved UX, map OK
```

### S3-29
```
code:       S3-29
date:       2026-07-31
day:        Fri
hours:      2
week:       W5
title:      Cross-Module Regression Tests
coverage:   N/A
deliverables:
  - Suite: registration + scheduling integrated
  - Verify Sprint 3 didn't break registration
  - Resolve flaky tests
validation: 100% green, zero flaky
```

### S3-30
```
code:       S3-30
date:       2026-08-02
day:        Sun
hours:      10
week:       W5
title:      Buffer / Sprint 4 Preparation
coverage:   N/A
deliverables:
  - Delayed tasks
  - Research passport workflows: states, documents, SMEP
  - Passport seed data
  - Regression and staging deploy
  - Scheduling API OpenAPI documentation
  - Prepare passport data model
  - Verify staging with 2 integrated modules
  - Sprint 3 Retrospective
validation: Staging stable, 2 modules
```

---

## Sprint 4 (36 tasks)

### S4-01
```
code:       S4-01
date:       2026-08-03
day:        Mon
hours:      2
week:       W1
title:      JPA Entities — Passport
coverage:   N/A
deliverables:
  - PassportRequest: type (issuance/renewal/extension/replacement), modality, urgency
  - BiometricData: photo, fingerprints (placeholder), signature
  - Flyway: V5__create_passport_tables.sql
validation: Migrations OK, constraints
```

### S4-02
```
code:       S4-02
date:       2026-08-04
day:        Tue
hours:      2
week:       W1
title:      JPA Entities — Emergency Document and Tracking
coverage:   N/A
deliverables:
  - EmergencyDocument: reason, itinerary, validityHours
  - PassportTracking: state, transitionDate, observation
  - Enums: RequestType, ProductionState, ReplacementReason
validation: Enums mapped, FK correct
```

### S4-03
```
code:       S4-03
date:       2026-08-05
day:        Wed
hours:      2
week:       W1
title:      DTOs and Specific Validations
coverage:   N/A
deliverables:
  - PassportRequestDTO: validations per type
  - EmergencyDocumentRequest: urgency fields
  - RequiredDocumentValidator: document list per request type
validation: Validators per type (10+)
```

### S4-04
```
code:       S4-04
date:       2026-08-06
day:        Thu
hours:      2
week:       W1
title:      Service — Passport Business Rules
coverage:   N/A
deliverables:
  - PassportRequestService: active consular registration check
  - Previous passport verification (renewal/extension)
  - Parental authorization validation (minors)
validation: Registration mandatory, parental check
```

### S4-05
```
code:       S4-05
date:       2026-08-07
day:        Fri
hours:      2
week:       W1
title:      Passport Workflow — Configuration
coverage:   N/A
deliverables:
  - INTERMEDIATE workflow: Officer → Consul
  - States: SUBMITTED → UNDER_REVIEW → VALIDATED → PENDING_PAYMENT → APPROVED →
  - SLA per state: review 48h, production 15 days
validation: Transitions configured
```

### S4-06
```
code:       S4-06
date:       2026-08-09
day:        Sun
hours:      10
week:       W1
title:      Complete Passport REST API
coverage:   N/A
deliverables:
  - POST /api/v1/passports (create request)
  - GET by ID, GET listing with filters
  - POST /passports/{id}/documents (upload required docs)
  - POST /passports/{id}/transition (state transition)
  - GET /passports/{id}/tracking (production timeline)
  - Required docs check before submit
  - Workflow integration: request → automatic process
  - CRUD + workflow tests (20+ scenarios)
validation: API complete, workflow integrated
```

### S4-07
```
code:       S4-07
date:       2026-08-10
day:        Mon
hours:      2
week:       W2
title:      Emergency Travel Document — API and Service
coverage:   N/A
deliverables:
  - POST /passports/emergency (emergency document)
  - Simplified urgency form
  - Limited validity, single use
validation: Create, validity, itinerary
```

### S4-08
```
code:       S4-08
date:       2026-08-11
day:        Tue
hours:      2
week:       W2
title:      Required Documents per Type
coverage:   N/A
deliverables:
  - Document list: issuance (birth certificate, ID, photo, proof of address)
  - Renewal: previous passport mandatory
  - Replacement (theft): police report mandatory
validation: Validation per type, missing docs rejected
```

### S4-09
```
code:       S4-09
date:       2026-08-12
day:        Wed
hours:      2
week:       W2
title:      Minor Management
coverage:   N/A
deliverables:
  - Parental authorization verification
  - Mandatory presence of both parents
  - Notarized authorization for absent parent
validation: Minor without authorization → blocked
```

### S4-10
```
code:       S4-10
date:       2026-08-13
day:        Thu
hours:      2
week:       W2
title:      Production Tracking
coverage:   N/A
deliverables:
  - Visual timeline: request → review → production → available
  - Automatic notification on state change
  - Estimated deadline per type
validation: Timeline updates, notification OK
```

### S4-11
```
code:       S4-11
date:       2026-08-14
day:        Fri
hours:      2
week:       W2
title:      Scheduling → Passport Integration
coverage:   N/A
deliverables:
  - 'Passport' appointment type → request pre-creation
  - Active registration check before scheduling
  - Document prerequisites informed
validation: Schedule → request, without registration → blocked
```

### S4-12
```
code:       S4-12
date:       2026-08-16
day:        Sun
hours:      10
week:       W2
title:      Frontend — Complete Passport Request
coverage:   N/A
deliverables:
  - Multi-step form: Type → Data → Documents → Minors → Confirmation
  - Document upload with checklist per type
  - Parental validation for minors
  - Request listing with filters and states
  - Details: tracking timeline, documents, process
  - Actions: approve, return, reject (backoffice)
  - Citizen portal: 'My Passports' with tracking
  - Component tests (15+)
validation: Form, upload, tracking, portal
```

### S4-13
```
code:       S4-13
date:       2026-08-17
day:        Mon
hours:      2
week:       W3
title:      Passport Extension
coverage:   N/A
deliverables:
  - Rules: only valid or recently expired passports
  - Automatic eligibility check
  - Database annotation
validation: Eligible/not eligible, annotation
```

### S4-14
```
code:       S4-14
date:       2026-08-18
day:        Tue
hours:      2
week:       W3
title:      Numbering and Registry
coverage:   N/A
deliverables:
  - Request number generation: YEAR/TYPE/POST/SEQ
  - Digital registry book: complete history
  - Search by previous passport number
validation: Sequential numbering, search OK
```

### S4-15
```
code:       S4-15
date:       2026-08-19
day:        Wed
hours:      2
week:       W3
title:      Passport-Specific Notifications
coverage:   N/A
deliverables:
  - Templates: request received, approved, in production, available
  - Pickup reminder (7 days after available)
  - Passport expiration alert (180, 90, 30 days)
validation: Notifications per state
```

### S4-16
```
code:       S4-16
date:       2026-08-20
day:        Thu
hours:      2
week:       W3
title:      Passport Reports
coverage:   N/A
deliverables:
  - Monthly report: by type, post, state
  - Average processing time per type
  - Export PDF and Excel
validation: Report OK, correct times
```

### S4-17
```
code:       S4-17
date:       2026-08-21
day:        Fri
hours:      2
week:       W3
title:      Frontend — Passport Reports
coverage:   N/A
deliverables:
  - Dashboard: requests by state, average time
  - Trend charts
  - Integrated export
validation: Dashboard OK, export
```

### S4-18
```
code:       S4-18
date:       2026-08-30
day:        Sun
hours:      10
week:       W4
title:      Passport E2E Tests
coverage:   N/A
deliverables:
  - E2E: issuance request → upload → review → approve → tracking
  - E2E: minor request with parental authorization
  - E2E: emergency document urgency
  - E2E: renewal with previous passport
  - Integration: passport → Kafka → notification
  - Security tests: OFFICER vs CONSUL vs CITIZEN
  - Fix bugs and UX adjustments
  - Deploy staging with passport data
validation: E2E suite 10+ scenarios
```

### S4-19
```
code:       S4-19
date:       2026-08-31
day:        Mon
hours:      2
week:       W5
title:      Delivery Record
coverage:   N/A
deliverables:
  - POST /passports/{id}/deliver: date, digital signature
  - Previous passport return mandatory (renewal)
  - Delivery receipt PDF
validation: Delivery recorded, receipt
```

### S4-20
```
code:       S4-20
date:       2026-09-01
day:        Tue
hours:      2
week:       W5
title:      Expired Passport Management
coverage:   N/A
deliverables:
  - @Scheduled: monthly expired check
  - Automatic renewal notification
  - Dashboard: passports expiring by period
validation: Expired detected, notified
```

### S4-21
```
code:       S4-21
date:       2026-09-02
day:        Wed
hours:      2
week:       W5
title:      Restriction Verification
coverage:   N/A
deliverables:
  - Restriction list check (mock)
  - Impediments: warrants, state debts
  - Workflow blocked until resolution
validation: Restriction → process blocked
```

### S4-22
```
code:       S4-22
date:       2026-09-03
day:        Thu
hours:      2
week:       W5
title:      Portal — Public Tracking
coverage:   N/A
deliverables:
  - Simplified timeline on portal
  - Push notifications on state change
  - Download delivery receipt
validation: Portal tracking, push OK
```

### S4-23
```
code:       S4-23
date:       2026-09-04
day:        Fri
hours:      2
week:       W5
title:      Performance and Indexes
coverage:   N/A
deliverables:
  - EXPLAIN passport queries + indexes
  - Redis cache for frequent queries
  - N+1 optimization
validation: Queries < 100ms
```

### S4-24
```
code:       S4-24
date:       2026-09-06
day:        Sun
hours:      10
week:       W5
title:      Cross-Module Integration and Sprint 4 Review
coverage:   N/A
deliverables:
  - E2E cross-module: registration → schedule passport → request → pay → approve → deliver
  - Rejection flow: submit → reject → citizen notification
  - Return flow: review → return → re-submit → approve
  - k6: 50 simultaneous requests
  - Complete staging deploy
  - Sprint 4 Demo
  - Retrospective and Sprint 5 planning
  - Passport API OpenAPI documentation
validation: E2E cross-module 10+, staging OK
```

### S4-25
```
code:       S4-25
date:       2026-09-07
day:        Mon
hours:      2
week:       W6
title:      Passport Edge Cases
coverage:   N/A
deliverables:
  - Duplicate request (valid passport exists)
  - Minor turning 18 during process
  - Emergency doc with valid passport → rejection
validation: Edge cases 8+
```

### S4-26
```
code:       S4-26
date:       2026-09-08
day:        Tue
hours:      2
week:       W6
title:      Cross-Module Regression Tests
coverage:   N/A
deliverables:
  - Suite: registration + scheduling + passport
  - Verify Sprint 4 didn't break previous modules
validation: 100% green all modules
```

### S4-27
```
code:       S4-27
date:       2026-09-09
day:        Wed
hours:      2
week:       W6
title:      SonarQube and Code Quality
coverage:   N/A
deliverables:
  - Resolve bugs and vulnerabilities
  - Refactoring duplicated code
validation: Quality gate passes
```

### S4-28
```
code:       S4-28
date:       2026-09-10
day:        Thu
hours:      2
week:       W6
title:      Passport Documentation
coverage:   N/A
deliverables:
  - Complete API OpenAPI
  - ADR-005 (passport workflow), ADR-006 (minors)
validation: Valid OpenAPI, ADRs
```

### S4-29
```
code:       S4-29
date:       2026-09-11
day:        Fri
hours:      2
week:       W6
title:      Complete Registration → Passport Integration
coverage:   N/A
deliverables:
  - Consular registration → enables passport request
  - Citizen data pre-filled in form
  - Consular card as verified prerequisite
validation: Integrated flow functional
```

### S4-30
```
code:       S4-30
date:       2026-09-13
day:        Sun
hours:      10
week:       W6
title:      E2E Cross-Module Tests Sprint 4
coverage:   N/A
deliverables:
  - E2E: registration → schedule passport → request → approve
  - E2E: minor with parental authorization
  - Concurrency: two officers same request
  - Performance: 100 active requests
  - Fix bugs, deploy staging
  - Sprint 4 Demo (passports)
  - Sprint 4 mid-point retrospective
validation: E2E cross-module 8+
```

### S4-31
```
code:       S4-31
date:       2026-09-14
day:        Mon
hours:      2
week:       W7
title:      Migration Declarations
coverage:   N/A
deliverables:
  - Automatic generation of standard declarations (nationality, marital status)
  - Bilingual templates PT/DE and PT/CS
  - Pre-filled data from consular registration
validation: Declaration generated, bilingual
```

### S4-32
```
code:       S4-32
date:       2026-09-15
day:        Tue
hours:      2
week:       W7
title:      Fee Management per Passport Type
coverage:   N/A
deliverables:
  - Passport-specific fee schedule
  - Differentiated prices: issuance vs renewal vs urgent
  - Financial module integration (Sprint 5 preparation)
validation: Correct prices per type
```

### S4-33
```
code:       S4-33
date:       2026-09-16
day:        Wed
hours:      2
week:       W7
title:      Passport Analytical Reports
coverage:   N/A
deliverables:
  - Average time per process phase
  - Rejection/return rate with reasons
  - Load forecast (seasonality)
validation: Analytical reports OK
```

### S4-34
```
code:       S4-34
date:       2026-09-17
day:        Thu
hours:      2
week:       W7
title:      Portal Improvements — Passports
coverage:   N/A
deliverables:
  - Interactive FAQ: required documents per type
  - Deadline and cost estimate before submitting
  - Dynamic document checklist
validation: FAQ, estimate, checklist
```

### S4-35
```
code:       S4-35
date:       2026-09-18
day:        Fri
hours:      2
week:       W7
title:      Civil Registry Preparation
coverage:   N/A
deliverables:
  - Research act types: birth, marriage, death, paternity acknowledgment
  - Map required documents per type
  - Prepare seed data
validation: Research completed
```

### S4-36
```
code:       S4-36
date:       2026-09-20
day:        Sun
hours:      10
week:       W7
title:      Buffer / Sprint 4 Stabilization
coverage:   N/A
deliverables:
  - Sprint 4 delayed tasks
  - End-to-end regression: registration → scheduling → passport
  - Stable staging with 3 modules
  - Civil registry and payments seed data
  - Start civil registry data model
  - Passport API OpenAPI documentation
  - ADR-005 (passport workflow), ADR-006 (minors)
  - Sprint 4 Retrospective
validation: Staging stable, 3 modules integrated
```

---

## Sprint 5 (36 tasks)

### S5-01
```
code:       S5-01
date:       2026-09-21
day:        Mon
hours:      2
week:       W1
title:      JPA Entities — Civil Registry
coverage:   N/A
deliverables:
  - CivilRegistryAct: type (BIRTH, MARRIAGE, DEATH, PATERNITY_ACKNOWLEDGMENT)
  - BirthRecord: fullName, date, place, hospital, sex, parents
  - Flyway: V6__create_civil_registry_tables.sql
validation: Migrations OK, constraints
```

### S5-02
```
code:       S5-02
date:       2026-09-22
day:        Tue
hours:      2
week:       W1
title:      Entities — Marriage and Death
coverage:   N/A
deliverables:
  - MarriageRecord: spouses, regime, place, date, witnesses
  - DeathRecord: deceased, date, place, cause (optional), declarant
  - Enums: ActType, RegistrationModality (INSCRIPTION, TRANSCRIPTION)
validation: Entities mapped, enums OK
```

### S5-03
```
code:       S5-03
date:       2026-09-23
day:        Wed
hours:      2
week:       W1
title:      DTOs and Civil Registry Validations
coverage:   N/A
deliverables:
  - BirthRequest: newborn data + parents
  - MarriageRequest: spouses + documents + regime
  - DeathRequest: deceased + declarant + documents
validation: Validators per type (12+)
```

### S5-04
```
code:       S5-04
date:       2026-09-24
day:        Thu
hours:      2
week:       W1
title:      Service — Civil Business Rules
coverage:   N/A
deliverables:
  - CivilRegistryService: parent registration verification
  - Birth rules: legal deadline, automatic nationality
  - Marriage rules: matrimonial capacity, impediments
validation: Rules per act type
```

### S5-05
```
code:       S5-05
date:       2026-09-25
day:        Fri
hours:      2
week:       W1
title:      Civil Registry Workflow
coverage:   N/A
deliverables:
  - SIMPLE workflow for birth (Officer only)
  - INTERMEDIATE workflow for marriage (Officer → Consul)
  - Configurable required documents per type
validation: Workflows configured per type
```

### S5-06
```
code:       S5-06
date:       2026-10-04
day:        Sun
hours:      10
week:       W2
title:      Complete Civil Registry REST API
coverage:   N/A
deliverables:
  - POST /api/v1/civil-registry/births
  - POST /civil-registry/marriages
  - POST /civil-registry/deaths
  - GET by ID, GET listing with filters per type
  - POST /{id}/documents (upload)
  - POST /{id}/transition (workflow)
  - Record number generation: YEAR/TYPE/BOOK/FOLIO/POST
  - CRUD + workflow tests (20+ scenarios)
validation: Civil API complete, workflow integrated
```

### S5-07
```
code:       S5-07
date:       2026-10-05
day:        Mon
hours:      2
week:       W3
title:      Certificates — PDF Generation
coverage:   N/A
deliverables:
  - CertificateService: birth, marriage, death
  - A4 PDF template: coat of arms, data, numbering, signature
  - Verification QR on certificate
validation: Correct PDF, QR functional
```

### S5-08
```
code:       S5-08
date:       2026-10-06
day:        Tue
hours:      2
week:       W3
title:      Certificates — Types and Formats
coverage:   N/A
deliverables:
  - Full narrative certificate vs extract
  - Bilingual certificate (PT/DE or PT/CS)
  - GET /civil-registry/{id}/certificate?type=〈=
validation: Types, formats, bilingual
```

### S5-09
```
code:       S5-09
date:       2026-10-07
day:        Wed
hours:      2
week:       W3
title:      Frontend — Civil Registry Forms
coverage:   N/A
deliverables:
  - Birth form: baby data, parents, hospital
  - Marriage form: spouses, regime, witnesses
  - Death form: deceased, cause, declarant
validation: Forms with validation
```

### S5-10
```
code:       S5-10
date:       2026-10-08
day:        Thu
hours:      2
week:       W3
title:      Frontend — Listing and Details
coverage:   N/A
deliverables:
  - Civil acts listing with type/date/state filters
  - Details with timeline, documents, certificate
  - Download certificate PDF
validation: Listing, details, download
```

### S5-11
```
code:       S5-11
date:       2026-10-09
day:        Fri
hours:      2
week:       W3
title:      Portal — Citizen Civil Registry
coverage:   N/A
deliverables:
  - 'My Civil Records': listing
  - Online certificate request
  - Request tracking
validation: Portal civil functional
```

### S5-12
```
code:       S5-12
date:       2026-10-11
day:        Sun
hours:      10
week:       W3
title:      Civil Registry E2E Tests
coverage:   N/A
deliverables:
  - E2E: birth registration → upload → approve → certificate
  - E2E: birth transcription
  - E2E: marriage → matrimonial capacity → approve
  - E2E: death → certificate
  - E2E: bilingual certificate request via portal
  - Security tests: CIVIL_OFFICER role
  - Fix bugs, UX adjustments
  - Deploy staging with civil data
validation: E2E civil 8+ scenarios
```

### S5-13
```
code:       S5-13
date:       2026-10-12
day:        Mon
hours:      2
week:       W4
title:      JPA Entities — Financial Model
coverage:   N/A
deliverables:
  - Fee, Payment, Receipt, PaymentItem, ExchangeRate
  - Enums: PaymentMethod, PaymentStatus, Currency (EUR, AOA)
  - Flyway: V7__create_financial_tables.sql
validation: Migrations OK, decimal(19,4)
```

### S5-14
```
code:       S5-14
date:       2026-10-13
day:        Tue
hours:      2
week:       W4
title:      Seed — Official Fee Schedule
coverage:   N/A
deliverables:
  - 60+ services with EUR and AOA prices
  - Table versioning (validity, history)
  - Exemptions: minors, elderly, students
validation: 60+ fees, exemptions
```

### S5-15
```
code:       S5-15
date:       2026-10-14
day:        Wed
hours:      2
week:       W4
title:      FeeService and Exchange Rate
coverage:   N/A
deliverables:
  - Price per service, exemptions/discounts
  - @Scheduled: ECB rate / manual fallback
  - Rate cache TTL 24h
validation: Price, exemption, exchange OK
```

### S5-16
```
code:       S5-16
date:       2026-10-15
day:        Thu
hours:      2
week:       W4
title:      Payments API
coverage:   N/A
deliverables:
  - POST /payments (create)
  - PENDING → PROCESSING → CONFIRMED/FAILED
  - Idempotency with X-Idempotency-Key
validation: Create, states, idempotency
```

### S5-17
```
code:       S5-17
date:       2026-10-16
day:        Fri
hours:      2
week:       W4
title:      ReceiptService — PDF and Email
coverage:   N/A
deliverables:
  - PDF numbering YEAR/POST/SEQ, coat of arms, QR
  - Automatic email sending
  - GET /payments/{id}/receipt
validation: Receipt PDF, email, download
```

### S5-18
```
code:       S5-18
date:       2026-10-18
day:        Sun
hours:      10
week:       W4
title:      Processing and Workflow Integration
coverage:   N/A
deliverables:
  - PaymentGateway interface + MockGateway
  - Mock: 80% approval, 15% decline, 5% timeout
  - Asynchronous Kafka confirmation
  - Workflow integration: PENDING_PAYMENT → pay → process advances
  - Timeout 48h → process suspended
  - Cancellation/refund with credit note
  - Payment → workflow tests (15+ scenarios)
  - Financial audit trail
validation: Payment + workflow integrated
```

### S5-19
```
code:       S5-19
date:       2026-10-19
day:        Mon
hours:      2
week:       W5
title:      Frontend — Fee Management
coverage:   N/A
deliverables:
  - Fee CRUD: listing, editing
  - Price change preview
  - Last update indicator
validation: CRUD, preview
```

### S5-20
```
code:       S5-20
date:       2026-10-20
day:        Tue
hours:      2
week:       W5
title:      Frontend — Payment Registration
coverage:   N/A
deliverables:
  - Manual registration (cash/transfer)
  - Services + total form
  - History per citizen
validation: Form, payment registered
```

### S5-21
```
code:       S5-21
date:       2026-10-21
day:        Wed
hours:      2
week:       W5
title:      Frontend — Financial Dashboard
coverage:   N/A
deliverables:
  - KPIs: day/month revenue, transactions, success rate
  - Revenue by period chart
  - Distribution by service (pie chart)
validation: KPIs OK, charts
```

### S5-22
```
code:       S5-22
date:       2026-10-22
day:        Thu
hours:      2
week:       W5
title:      Portal — Online Payments
coverage:   N/A
deliverables:
  - Checkout: items → summary → method → confirmation
  - 'My Payments': history with filters
  - Public cost simulator
validation: Portal payments functional
```

### S5-23
```
code:       S5-23
date:       2026-10-23
day:        Fri
hours:      2
week:       W5
title:      Financial Reports
coverage:   N/A
deliverables:
  - Daily, monthly, multi-currency
  - Reconciliation: pending vs confirmed
  - Export PDF and Excel
validation: Reports, export OK
```

### S5-24
```
code:       S5-24
date:       2026-10-25
day:        Sun
hours:      10
week:       W5
title:      Financial E2E Tests and Sprint 5 Review
coverage:   N/A
deliverables:
  - E2E: request → pay → process advances → receipt
  - E2E: minor exemption → zero payment → direct approval
  - E2E: multi-currency EUR + AOA
  - k6: 100 simultaneous payments
  - Integrity: item sum = total, no numbering gaps
  - Complete audit trail
  - Deploy staging with financial data
  - Sprint 5 Demo, retrospective
validation: E2E financial 10+, k6 OK
```

### S5-25
```
code:       S5-25
date:       2026-10-26
day:        Mon
hours:      2
week:       W6
title:      Limits and Financial Control
coverage:   N/A
deliverables:
  - Per-role limits: OFFICER ≤ €5000, CONSUL unlimited
  - Alerts > €1000 → consul
  - Separation of duties: creator ≠ approver
validation: Limit, alert, separation
```

### S5-26
```
code:       S5-26
date:       2026-10-27
day:        Tue
hours:      2
week:       W6
title:      Automatic Reconciliation
coverage:   N/A
deliverables:
  - @Scheduled daily reconciliation
  - Discrepancy report
  - Anomaly detection
validation: Inconsistency → discrepancy
```

### S5-27
```
code:       S5-27
date:       2026-10-28
day:        Wed
hours:      2
week:       W6
title:      Financial Edge Cases
coverage:   N/A
deliverables:
  - Partial payment, refund
  - Consistent rounding (2 decimals)
  - Rate changed during payment
validation: Edge cases 8+
```

### S5-28
```
code:       S5-28
date:       2026-10-29
day:        Thu
hours:      2
week:       W6
title:      5-Module Regression Tests
coverage:   N/A
deliverables:
  - Complete suite: registration + scheduling + passport + civil + payments
  - Cross-module integrity verification
validation: 100% green
```

### S5-29
```
code:       S5-29
date:       2026-10-30
day:        Fri
hours:      2
week:       W6
title:      Sprint 5 Documentation
coverage:   N/A
deliverables:
  - Civil and financial API OpenAPI
  - ADR-007 (payments), ADR-008 (certificates)
validation: OpenAPI, ADRs
```

### S5-30
```
code:       S5-30
date:       2026-11-01
day:        Sun
hours:      10
week:       W6
title:      Buffer / Sprint 6 Preparation
coverage:   N/A
deliverables:
  - Delayed tasks
  - End-to-end 5-module regression
  - Prepare quality and security checklist
  - Prepare k6 scripts for load testing
  - Start production deploy checklist
validation: Staging stable, 5 modules
```

### S5-31
```
code:       S5-31
date:       2026-11-02
day:        Mon
hours:      2
week:       W7
title:      SonarQube — All Modules
coverage:   N/A
deliverables:
  - Resolve bugs and vulnerabilities
  - Priority code smells
  - Outdated dependencies
validation: Quality gate passes
```

### S5-32
```
code:       S5-32
date:       2026-11-03
day:        Tue
hours:      2
week:       W7
title:      Global UX Improvements
coverage:   N/A
deliverables:
  - Skeleton loaders on all listings
  - Visual feedback on all actions
  - Consistent PT error messages
validation: Consistent UX
```

### S5-33
```
code:       S5-33
date:       2026-11-04
day:        Wed
hours:      2
week:       W7
title:      Complete API Documentation
coverage:   N/A
deliverables:
  - Verify ALL endpoints OpenAPI
  - Request/response examples
  - Published Swagger UI
validation: Complete OpenAPI
```

### S5-34
```
code:       S5-34
date:       2026-11-05
day:        Thu
hours:      2
week:       W7
title:      User Manual — Structure
coverage:   N/A
deliverables:
  - PT manual structure for staff
  - Sections: Login, Citizens, Scheduling, Passports, Civil, Payments
  - First screenshots
validation: Structure created
```

### S5-35
```
code:       S5-35
date:       2026-11-06
day:        Fri
hours:      2
week:       W7
title:      Production Infra Preparation
coverage:   N/A
deliverables:
  - Hetzner specs: 3×CX41 + CCX31 + CX21
  - DNS and SSL plan
  - Backup and disaster recovery plan
validation: Specs defined
```

### S5-36
```
code:       S5-36
date:       2026-11-08
day:        Sun
hours:      10
week:       W7
title:      Final Staging Deploy and Validation
coverage:   N/A
deliverables:
  - Complete 5-module staging deploy
  - Smoke tests all flows
  - Realistic data: 100+ citizens, 50+ passports, 30+ civil acts
  - Cross-module end-to-end validation
  - Bug fixes
  - Prepare UAT
  - Staging deployment documentation
  - Checkpoint: ready for Sprint 6
validation: Staging complete and stable
```

---

## Sprint 6 (36 tasks)

### S6-01
```
code:       S6-01
date:       2026-11-09
day:        Mon
hours:      2
week:       W1
title:      OWASP ZAP — Scan
coverage:   N/A
deliverables:
  - OWASP ZAP spider + active scan staging
  - Catalog vulnerabilities
  - Prioritize: Critical > High > Medium
validation: Scan complete, report
```

### S6-02
```
code:       S6-02
date:       2026-11-10
day:        Tue
hours:      2
week:       W1
title:      Vulnerability Remediation (1/2)
coverage:   N/A
deliverables:
  - SQL Injection: parameterized queries
  - XSS: sanitization, CSP header
  - CSRF protection
validation: Re-scan confirms fixes
```

### S6-03
```
code:       S6-03
date:       2026-11-11
day:        Wed
hours:      2
week:       W1
title:      Vulnerability Remediation (2/2)
coverage:   N/A
deliverables:
  - Security headers: HSTS, X-Frame-Options
  - Rate limiting public endpoints
  - JWT: adequate expiration, no sensitive data
validation: Headers, rate limiting OK
```

### S6-04
```
code:       S6-04
date:       2026-11-12
day:        Thu
hours:      2
week:       W1
title:      GDPR — Export (DSAR)
coverage:   N/A
deliverables:
  - GET /gdpr/export/{citizenId}: JSON/PDF
  - Include all data: personal, docs, processes, payments
  - DSAR request logging
validation: Complete export, log OK
```

### S6-05
```
code:       S6-05
date:       2026-11-13
day:        Fri
hours:      2
week:       W1
title:      GDPR — Erasure
coverage:   N/A
deliverables:
  - DELETE /gdpr/erase/{citizenId}: anonymize
  - Consent management
  - Retention: >5 years → anonymized
validation: Anonymize, consent, retention
```

### S6-06
```
code:       S6-06
date:       2026-11-15
day:        Sun
hours:      10
week:       W1
title:      Performance and Load Testing
coverage:   N/A
deliverables:
  - k6: login → search → schedule (realistic)
  - 50 virtual users 10 min
  - 100 virtual users 5 min (stress)
  - Identify and optimize bottlenecks
  - Endurance: 20 users 1 hour (leaks)
  - Document p50, p95, p99, throughput
validation: p95 < 500ms, 0 errors, no leaks
```

### S6-07
```
code:       S6-07
date:       2026-11-16
day:        Mon
hours:      2
week:       W2
title:      Accessibility — Audit
coverage:   N/A
deliverables:
  - axe-core + Lighthouse all pages
  - Min contrast 4.5:1
  - ARIA labels on interactive elements, forms, tables
validation: Lighthouse A11y > 90
```

### S6-08
```
code:       S6-08
date:       2026-11-17
day:        Tue
hours:      2
week:       W2
title:      Accessibility — Keyboard
coverage:   N/A
deliverables:
  - Keyboard navigation all flows
  - Skip-to-content, focus modals
  - Test screen reader
validation: Complete keyboard, reader OK
```

### S6-09
```
code:       S6-09
date:       2026-11-18
day:        Wed
hours:      2
week:       W2
title:      Operational Runbooks
coverage:   N/A
deliverables:
  - Deploy: step by step + checklist
  - Rollback: revert in < 30 min
  - Disaster recovery: restore backup
validation: Runbooks reviewed
```

### S6-10
```
code:       S6-10
date:       2026-11-19
day:        Thu
hours:      2
week:       W2
title:      Complete User Manual
coverage:   N/A
deliverables:
  - Complete all sections
  - Screenshots of each flow
  - FAQ and troubleshooting
validation: Manual complete
```

### S6-11
```
code:       S6-11
date:       2026-11-20
day:        Fri
hours:      2
week:       W2
title:      Portal — Final Consolidation
coverage:   N/A
deliverables:
  - Verify all modules on citizen portal
  - Complete i18n 4 languages
  - PWA: offline, install prompt
validation: Portal complete, i18n OK
```

### S6-12
```
code:       S6-12
date:       2026-11-22
day:        Sun
hours:      10
week:       W2
title:      Production Infrastructure
coverage:   N/A
deliverables:
  - Provision Hetzner: 3×CX41 + CCX31 MySQL + CX21 Redis
  - k3s Kubernetes cluster
  - Helm charts with production values
  - DNS + SSL Let's Encrypt (cert-manager)
  - Velero backup: daily MySQL, weekly full
  - Test deploy, healthchecks, SSL, backup/restore
validation: Cluster OK, SSL, backup verified
```

### S6-13
```
code:       S6-13
date:       2026-11-23
day:        Mon
hours:      2
week:       W3
title:      GitHub Actions — Production Deploy
coverage:   N/A
deliverables:
  - cd-production.yml: build → push → deploy K8s with approval
  - Blue-green deployment
  - Slack/email notification
validation: Manual trigger, approval, deploy
```

### S6-14
```
code:       S6-14
date:       2026-11-24
day:        Tue
hours:      2
week:       W3
title:      Rollback and Health Checks
coverage:   N/A
deliverables:
  - Automatic rollback if health fails 5 min
  - Readiness and liveness probes all pods
  - Emergency manual rollback script
validation: Automatic rollback works
```

### S6-15
```
code:       S6-15
date:       2026-11-25
day:        Wed
hours:      2
week:       W3
title:      Production DB Migrations
coverage:   N/A
deliverables:
  - Flyway automatic on deploy
  - Backward-compatible (never remove columns)
  - Test migration rollback
validation: Staging migration = prod
```

### S6-16
```
code:       S6-16
date:       2026-11-26
day:        Thu
hours:      2
week:       W3
title:      Production Seed Data
coverage:   N/A
deliverables:
  - Official fees, posts, configs
  - Keycloak users: admin, consul (MFA)
  - No test data
validation: Seed verified
```

### S6-17
```
code:       S6-17
date:       2026-11-27
day:        Fri
hours:      2
week:       W3
title:      Production Monitoring
coverage:   N/A
deliverables:
  - Prometheus + Grafana production
  - Dashboards: system, application, business
  - Alerts: disk > 80%, CPU > 85%, 5xx > 0.5%
validation: Dashboards, alerts OK
```

### S6-18
```
code:       S6-18
date:       2026-11-29
day:        Sun
hours:      10
week:       W3
title:      Production MVP Deploy
coverage:   N/A
deliverables:
  - Pre-deploy checklist: backups, rollback, communication
  - Deploy: migrations → backend → frontend → gateway
  - Health checks all services
  - Smoke tests: login, citizen, schedule, passport, civil, payment
  - SSL, CORS, rate limiting, security headers
  - Automatic backup + restore test
  - Manual: production screenshots
validation: Production operational, backup OK
```

### S6-19
```
code:       S6-19
date:       2026-11-30
day:        Mon
hours:      2
week:       W4
title:      Production Security Checks
coverage:   N/A
deliverables:
  - OWASP ZAP passive scan production
  - Dependabot: zero vulnerabilities
  - Sensitive endpoints authenticated
validation: Zero high/critical
```

### S6-20
```
code:       S6-20
date:       2026-12-01
day:        Tue
hours:      2
week:       W4
title:      UAT — Session 1
coverage:   N/A
deliverables:
  - Session with 2-3 staff: registration and scheduling
  - Document feedback
  - Prioritize issues
validation: Feedback documented
```

### S6-21
```
code:       S6-21
date:       2026-12-02
day:        Wed
hours:      2
week:       W4
title:      UAT — Session 2
coverage:   N/A
deliverables:
  - Session: passports and civil registry
  - Document feedback
  - Classify: blocker, important, minor
validation: Feedback documented
```

### S6-22
```
code:       S6-22
date:       2026-12-03
day:        Thu
hours:      2
week:       W4
title:      UAT Fixes — Blockers
coverage:   N/A
deliverables:
  - Fix blocker issues
  - Deploy hotfixes if needed
  - Re-test with users
validation: Blockers resolved
```

### S6-23
```
code:       S6-23
date:       2026-12-04
day:        Fri
hours:      2
week:       W4
title:      UAT Fixes — Important
coverage:   N/A
deliverables:
  - Fix important issues
  - UX improvements based on feedback
  - Deploy update
validation: Important issues resolved
```

### S6-24
```
code:       S6-24
date:       2026-12-06
day:        Sun
hours:      10
week:       W4
title:      Training and Go-Live Preparation
coverage:   N/A
deliverables:
  - 3h remote training session: all modules
  - Practical exercises: registration → passport → payment
  - Session recording
  - Printed/PDF manual distributed
  - FAQ based on training questions
  - Support channel configuration
  - Final verification all modules
  - Internal communication: go-live date
validation: Training completed, channel ready
```

### S6-25
```
code:       S6-25
date:       2026-12-07
day:        Mon
hours:      2
week:       W5
title:      Official Go-Live
coverage:   N/A
deliverables:
  - Public access Citizen Portal
  - Support channel: email + form
  - Official launch communication
validation: Portal public, support active
```

### S6-26
```
code:       S6-26
date:       2026-12-08
day:        Tue
hours:      2
week:       W5
title:      Day 1 Monitoring
coverage:   N/A
deliverables:
  - Monitor logs, metrics, alerts
  - Resolve emerging issues
  - Backup verified with real data
validation: Zero incidents day 1
```

### S6-27
```
code:       S6-27
date:       2026-12-09
day:        Wed
hours:      2
week:       W5
title:      Day 2-3 Monitoring
coverage:   N/A
deliverables:
  - Track usage patterns
  - Performance with real load
  - Adjust rate limiting if needed
validation: Stable metrics
```

### S6-28
```
code:       S6-28
date:       2026-12-10
day:        Thu
hours:      2
week:       W5
title:      Post-Launch Fixes
coverage:   N/A
deliverables:
  - Fix bugs reported day 1-3
  - Deploy hotfix
  - User communication
validation: Bugs resolved
```

### S6-29
```
code:       S6-29
date:       2026-12-11
day:        Fri
hours:      2
week:       W5
title:      Post-Launch Optimizations
coverage:   N/A
deliverables:
  - Cache tuning with real data
  - Slow queries identified → indexes
  - UX adjustments based on real usage
validation: Performance optimized
```

### S6-30
```
code:       S6-30
date:       2026-12-13
day:        Sun
hours:      10
week:       W5
title:      First Week Stabilization
coverage:   N/A
deliverables:
  - First week report: metrics, issues, feedback
  - Verify backups with real data
  - Resolve pending issues
  - Monitor trends
  - Configuration adjustments based on usage
  - Document lessons learned
  - Plan maintenance: 5-10h/week
  - Stakeholder status communication
validation: Production stable, report OK
```

### S6-31
```
code:       S6-31
date:       2026-12-14
day:        Mon
hours:      2
week:       W6
title:      Final MVP Report
coverage:   N/A
deliverables:
  - Metrics: users, transactions, uptime
  - Resolved vs pending issues
  - Performance: p50, p95 in production
validation: Report complete
```

### S6-32
```
code:       S6-32
date:       2026-12-15
day:        Tue
hours:      2
week:       W6
title:      Technical Debt and Backlog
coverage:   N/A
deliverables:
  - Catalog technical debt
  - Prioritize items for Phase 2
  - Effort estimates
validation: Backlog prioritized
```

### S6-33
```
code:       S6-33
date:       2026-12-16
day:        Wed
hours:      2
week:       W6
title:      Phase 2 Backlog
coverage:   N/A
deliverables:
  - Future modules: Visas, Notarization, Chatbot, Video Call
  - Angola integrations (MIREX, SMEP)
  - Estimate: 6-9 additional months
validation: Roadmap defined
```

### S6-34
```
code:       S6-34
date:       2026-12-17
day:        Thu
hours:      2
week:       W6
title:      Formal Handover
coverage:   N/A
deliverables:
  - Complete technical documentation
  - Access and credentials
  - Runbooks and manuals
  - Source code and repositories
validation: Handover complete
```

### S6-35
```
code:       S6-35
date:       2026-12-18
day:        Fri
hours:      2
week:       W6
title:      Maintenance Plan
coverage:   N/A
deliverables:
  - Maintenance contract: 5-10h/week
  - SLA: critical 4h, high 24h, medium 72h
  - Reporting and escalation process
validation: Plan agreed
```

### S6-36
```
code:       S6-36
date:       2026-12-20
day:        Sun
hours:      10
week:       W6
title:      Final Retrospective and Closure
coverage:   N/A
deliverables:
  - Final MVP retrospective with stakeholders
  - What worked, what to improve
  - Consular team feedback
  - Celebration: MVP delivered and operational
  - Final verification: all modules, backup, monitoring
  - Update final documentation
  - Formal closure of MVP project
  - Transition to maintenance mode
validation: MVP closed, maintenance active
```

---
**Total: 204 tasks across 6 sprints**