# SGCD-PM — Sistema de Gestão de Projecto
## Especificação Completa para Implementação via Claude Code

**Versão:** 1.0  
**Data:** 11 Fevereiro 2026  
**Objectivo:** Blueprint completo para o Claude Code construir um sistema web de gestão de projecto  
**Projecto-alvo:** SGCD — Sistema de Gestão Consular Digital (MVP)

---

## ÍNDICE

1. [Visão Geral](#1-visão-geral)
2. [Arquitectura e Stack](#2-arquitectura-e-stack)
3. [Modelo de Dados (MySQL)](#3-modelo-de-dados)
4. [Backend Spring Boot](#4-backend-spring-boot)
5. [Frontend Angular](#5-frontend-angular)
6. [Sistema de Prompts](#6-sistema-de-prompts)
7. [Dados Seed (204 Tarefas)](#7-dados-seed)
8. [Docker e Deploy](#8-docker-e-deploy)
9. [Instruções de Implementação](#9-instruções-de-implementação)

---

## 1. VISÃO GERAL

### O que é
Uma aplicação web **independente** que corre em paralelo com o desenvolvimento do SGCD MVP. Substitui o anterior sistema CLI Python, oferecendo:

- **Dashboard Developer:** progresso em tempo real, tarefa do dia, gerador de prompts Claude
- **Dashboard Stakeholder:** visão executiva read-only para a Embaixada de Angola
- **Actualização automática:** métricas recalculadas ao completar cada tarefa/sprint
- **Relatórios:** gerados automaticamente ao fim de cada sprint (PDF)

### Quem usa
| Role | Acesso | Funcionalidades |
|------|--------|----------------|
| DEVELOPER | Total | CRUD tarefas, prompts, relatórios, gestão completa |
| STAKEHOLDER | Leitura | Dashboard executivo, relatórios, progresso |

### O projecto que está a ser gerido
O SGCD é um Sistema de Gestão Consular Digital para a Embaixada de Angola na Alemanha e República Checa. Digitaliza 60+ serviços consulares em 13 módulos. O MVP tem:
- **204 sessões** de desenvolvimento
- **6 sprints**, 680 horas totais
- **Período:** 2 Março 2026 → 20 Dezembro 2026
- **Horário:** Seg-Sex 20:00-22:00 (2h) + Domingo 08:00-18:00 (10h)
- **12 dias bloqueados** (feriados e eventos desportivos)

---

## 2. ARQUITECTURA E STACK

### Backend
```
Framework: Spring Boot 3.2.x
Java:      21 (LTS)
Build:     Maven (single module)
DB:        MySQL 8.0
Migrations: Flyway
Mapping:   MapStruct
Lombok:    Sim
PDF:       iText 5.5 / OpenPDF
Auth:      Spring Security + JWT simples
API Docs:  SpringDoc OpenAPI 2.3
```

### Frontend
```
Framework: Angular 17+ (standalone components)
CSS:       Angular Material + SCSS custom
Charts:    ngx-charts ou Chart.js
HTTP:      HttpClient com interceptors
Auth:      JWT guard simples
i18n:      Não necessário (apenas PT)
```

### Infraestrutura
```
Container: Docker Compose
DB:        MySQL 8.0 (container)
Portas:    Backend 8090, Frontend 4201, MySQL 3307
```

### Estrutura do Projecto
```
sgcd-pm/
├── backend/
│   ├── pom.xml
│   └── src/main/
│       ├── java/ao/gov/sgcd/pm/
│       │   ├── SgcdPmApplication.java
│       │   ├── config/
│       │   │   ├── SecurityConfig.java
│       │   │   ├── CorsConfig.java
│       │   │   └── JwtTokenProvider.java
│       │   ├── entity/
│       │   │   ├── Sprint.java
│       │   │   ├── Task.java
│       │   │   ├── TaskExecution.java
│       │   │   ├── TaskNote.java
│       │   │   ├── SprintReport.java
│       │   │   ├── BlockedDay.java
│       │   │   ├── Prototype.java
│       │   │   └── ProjectConfig.java
│       │   ├── dto/
│       │   │   ├── SprintDTO.java
│       │   │   ├── SprintProgressDTO.java
│       │   │   ├── TaskDTO.java
│       │   │   ├── TaskUpdateDTO.java
│       │   │   ├── DashboardDTO.java
│       │   │   ├── StakeholderDashboardDTO.java
│       │   │   ├── CalendarDTO.java
│       │   │   ├── PromptDTO.java
│       │   │   └── ReportDTO.java
│       │   ├── repository/
│       │   │   ├── SprintRepository.java
│       │   │   ├── TaskRepository.java
│       │   │   ├── TaskExecutionRepository.java
│       │   │   ├── BlockedDayRepository.java
│       │   │   ├── SprintReportRepository.java
│       │   │   └── ProjectConfigRepository.java
│       │   ├── service/
│       │   │   ├── SprintService.java
│       │   │   ├── TaskService.java
│       │   │   ├── DashboardService.java
│       │   │   ├── PromptService.java
│       │   │   ├── ReportService.java
│       │   │   ├── CalendarService.java
│       │   │   └── PdfExportService.java
│       │   ├── controller/
│       │   │   ├── SprintController.java
│       │   │   ├── TaskController.java
│       │   │   ├── DashboardController.java
│       │   │   ├── PromptController.java
│       │   │   ├── ReportController.java
│       │   │   ├── CalendarController.java
│       │   │   └── StakeholderController.java
│       │   └── seed/
│       │       └── DataSeeder.java  (CommandLineRunner)
│       └── resources/
│           ├── application.yml
│           └── db/migration/
│               └── V1__create_schema.sql
├── frontend/
│   ├── angular.json
│   ├── package.json
│   └── src/app/
│       ├── app.component.ts
│       ├── app.routes.ts
│       ├── core/
│       │   ├── services/
│       │   │   ├── api.service.ts
│       │   │   ├── sprint.service.ts
│       │   │   ├── task.service.ts
│       │   │   ├── dashboard.service.ts
│       │   │   └── auth.service.ts
│       │   ├── models/
│       │   │   ├── sprint.model.ts
│       │   │   ├── task.model.ts
│       │   │   └── dashboard.model.ts
│       │   ├── guards/
│       │   │   └── auth.guard.ts
│       │   └── interceptors/
│       │       └── jwt.interceptor.ts
│       ├── features/
│       │   ├── dashboard/
│       │   │   └── dashboard.component.ts    (Developer)
│       │   ├── stakeholder/
│       │   │   └── stakeholder.component.ts  (Stakeholder read-only)
│       │   ├── sprints/
│       │   │   ├── sprint-list.component.ts
│       │   │   └── sprint-detail.component.ts
│       │   ├── tasks/
│       │   │   ├── task-list.component.ts
│       │   │   └── task-detail.component.ts
│       │   ├── prompts/
│       │   │   └── prompt-generator.component.ts
│       │   ├── calendar/
│       │   │   └── calendar.component.ts
│       │   └── reports/
│       │       └── reports.component.ts
│       └── shared/
│           ├── components/
│           │   ├── header.component.ts
│           │   ├── sidebar.component.ts
│           │   ├── status-badge.component.ts
│           │   ├── progress-bar.component.ts
│           │   └── sprint-card.component.ts
│           └── pipes/
│               ├── hours.pipe.ts
│               └── date-pt.pipe.ts
└── docker-compose.yml
```

---

## 3. MODELO DE DADOS

### Schema MySQL completo (Flyway V1)

```sql
-- ═══════════════════════════════════════════
-- SGCD-PM: Schema V1
-- ═══════════════════════════════════════════

CREATE TABLE sprints (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    sprint_number   INT NOT NULL UNIQUE,
    name            VARCHAR(120) NOT NULL,
    name_en         VARCHAR(120) NOT NULL,
    description     TEXT,
    weeks           INT NOT NULL,
    total_hours     INT NOT NULL,
    total_sessions  INT NOT NULL,
    start_date      DATE NOT NULL,
    end_date        DATE NOT NULL,
    focus           VARCHAR(60),
    color           VARCHAR(7) DEFAULT '#3884F4',
    status          ENUM('PLANNED','ACTIVE','COMPLETED') NOT NULL DEFAULT 'PLANNED',
    actual_hours    DECIMAL(6,1) DEFAULT 0,
    completed_sessions INT DEFAULT 0,
    completion_notes TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_sprint_status (status),
    INDEX idx_sprint_dates (start_date, end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tasks (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    sprint_id           BIGINT NOT NULL,
    task_code           VARCHAR(10) NOT NULL UNIQUE COMMENT 'Ex: S1-01, S2-15',
    session_date        DATE NOT NULL,
    day_of_week         VARCHAR(3) NOT NULL COMMENT 'Mon/Tue/Wed/Thu/Fri/Sun',
    week_number         INT NOT NULL COMMENT 'Semana dentro do sprint',
    planned_hours       DECIMAL(4,1) NOT NULL,
    title               VARCHAR(200) NOT NULL,
    title_en            VARCHAR(200),
    description         TEXT,
    deliverables        JSON COMMENT 'Lista de entregáveis',
    validation_criteria JSON COMMENT 'Critérios de validação',
    coverage_target     VARCHAR(10) DEFAULT 'N/A',
    status              ENUM('PLANNED','IN_PROGRESS','COMPLETED','BLOCKED','SKIPPED') NOT NULL DEFAULT 'PLANNED',
    actual_hours        DECIMAL(4,1) DEFAULT NULL,
    started_at          DATETIME DEFAULT NULL,
    completed_at        DATETIME DEFAULT NULL,
    completion_notes    TEXT,
    blockers            TEXT,
    prompt_template     TEXT COMMENT 'Template do prompt Claude',
    sort_order          INT DEFAULT 0,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (sprint_id) REFERENCES sprints(id) ON DELETE CASCADE,
    INDEX idx_task_sprint (sprint_id),
    INDEX idx_task_date (session_date),
    INDEX idx_task_status (status),
    INDEX idx_task_code (task_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE task_executions (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id         BIGINT NOT NULL,
    started_at      DATETIME NOT NULL,
    ended_at        DATETIME DEFAULT NULL,
    hours_spent     DECIMAL(4,1) DEFAULT NULL,
    prompt_used     LONGTEXT,
    response_summary TEXT,
    notes           TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    INDEX idx_exec_task (task_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE task_notes (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id     BIGINT NOT NULL,
    note_type   ENUM('INFO','WARNING','BLOCKER','DECISION','OBSERVATION') DEFAULT 'INFO',
    content     TEXT NOT NULL,
    author      VARCHAR(60) DEFAULT 'developer',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    INDEX idx_note_task (task_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE sprint_reports (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    sprint_id       BIGINT NOT NULL,
    report_type     ENUM('WEEKLY','SPRINT_END','CUSTOM') NOT NULL,
    week_number     INT DEFAULT NULL,
    generated_at    DATETIME NOT NULL,
    summary_pt      TEXT,
    summary_en      TEXT,
    metrics_json    JSON,
    pdf_path        VARCHAR(500),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sprint_id) REFERENCES sprints(id) ON DELETE CASCADE,
    INDEX idx_report_sprint (sprint_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE blocked_days (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    blocked_date DATE NOT NULL UNIQUE,
    day_of_week VARCHAR(3) NOT NULL,
    block_type  ENUM('HOLIDAY','SCC_EVENT') NOT NULL,
    reason      VARCHAR(200) NOT NULL,
    hours_lost  DECIMAL(4,1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE prototypes (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(120) NOT NULL,
    module      VARCHAR(60) NOT NULL,
    file_path   VARCHAR(500),
    file_type   VARCHAR(10),
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE task_prototypes (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id         BIGINT NOT NULL,
    prototype_id    BIGINT NOT NULL,
    relevance       ENUM('primary','reference','inspiration') DEFAULT 'reference',
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (prototype_id) REFERENCES prototypes(id) ON DELETE CASCADE,
    UNIQUE KEY uk_task_proto (task_id, prototype_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE project_config (
    config_key   VARCHAR(60) PRIMARY KEY,
    config_value TEXT NOT NULL,
    description  VARCHAR(200),
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### Seed Dados Fixos (dentro da migração)

```sql
-- Dias Bloqueados
INSERT INTO blocked_days (blocked_date, day_of_week, block_type, reason, hours_lost) VALUES
('2026-03-08', 'Sun', 'HOLIDAY',   'Internationaler Frauentag', 10),
('2026-03-29', 'Sun', 'SCC_EVENT', 'Generali Berlin Half Marathon', 10),
('2026-04-03', 'Fri', 'HOLIDAY',   'Karfreitag (Good Friday)', 2),
('2026-04-06', 'Mon', 'HOLIDAY',   'Ostermontag (Easter Monday)', 2),
('2026-05-01', 'Fri', 'HOLIDAY',   'Tag der Arbeit (Labour Day)', 2),
('2026-05-14', 'Thu', 'HOLIDAY',   'Christi Himmelfahrt (Ascension)', 2),
('2026-05-25', 'Mon', 'HOLIDAY',   'Pfingstmontag (Whit Monday)', 2),
('2026-06-02', 'Tue', 'SCC_EVENT', '5x5km Team-Staffel (Day 1)', 2),
('2026-06-03', 'Wed', 'SCC_EVENT', '5x5km Team-Staffel (Day 2)', 2),
('2026-06-04', 'Thu', 'SCC_EVENT', '5x5km Team-Staffel (Day 3)', 2),
('2026-08-23', 'Sun', 'SCC_EVENT', 'die Generalprobe', 10),
('2026-09-27', 'Sun', 'SCC_EVENT', 'BMW Berlin Marathon', 10);

-- Sprints
INSERT INTO sprints (sprint_number, name, name_en, description, weeks, total_hours, total_sessions, start_date, end_date, focus, color, status) VALUES
(1, 'Fundação Técnica', 'Technical Foundation',
 'Infraestrutura completa: repos, CI/CD, Docker, Keycloak, API Gateway, Spring Boot+Angular, monitorização.',
 6, 120, 36, '2026-03-02', '2026-05-10', 'Infra/DevOps', '#3884F4', 'PLANNED'),
(2, 'Registo Consular', 'Consular Registration & Citizen Mgmt',
 'Módulo core de inscrição consular, upload docs, cartão digital, pesquisa, jurisdição automática.',
 5, 100, 30, '2026-05-11', '2026-06-28', 'Registo', '#CC092F', 'PLANNED'),
(3, 'Agendamento e Filas', 'Scheduling, Queues & Service Desk',
 'Agendamento online multi-posto, filas com prioridades, notificações, dashboard tempo real.',
 5, 100, 30, '2026-06-29', '2026-08-02', 'Atendimento', '#F4B400', 'PLANNED'),
(4, 'Passaportes', 'Passports & Travel Documents',
 'Passaportes: pedido, workflow multi-nível, tracking produção, menores, salvo-condutos.',
 6, 120, 36, '2026-08-03', '2026-09-20', 'Passaportes', '#2EA043', 'PLANNED'),
(5, 'Registo Civil e Pagamentos', 'Civil Registry & Payments',
 'Actos civis (nascimento, casamento, óbito), pagamentos multi-moeda, emolumentos.',
 6, 120, 36, '2026-09-21', '2026-11-08', 'Civil/Finanças', '#8957E5', 'PLANNED'),
(6, 'Portal e Go-Live', 'Portal, Quality & Go-Live',
 'Portal cidadão completo, OWASP/RGPD, performance, deploy produção, UAT, lançamento.',
 6, 120, 36, '2026-11-09', '2026-12-20', 'Go-Live', '#009688', 'PLANNED');

-- Configuração do Projecto
INSERT INTO project_config (config_key, config_value, description) VALUES
('project.name', 'SGCD — Sistema de Gestão Consular Digital', 'Nome do projecto'),
('project.client', 'Embaixada da República de Angola — Alemanha & Rep. Checa', 'Cliente'),
('project.start_date', '2026-03-02', 'Data de início'),
('project.end_date', '2026-12-20', 'Go-Live'),
('project.total_hours', '680', 'Horas totais planeadas'),
('project.total_sessions', '204', 'Sessões totais'),
('project.schedule.weekday', '2h (20:00-22:00)', 'Horário dias úteis'),
('project.schedule.sunday', '10h (08:00-18:00)', 'Horário Domingo'),
('project.stack.backend', 'Spring Boot 3.x, Java 21, Maven', 'Stack backend'),
('project.stack.frontend', 'Angular 17+, TypeScript 5.x', 'Stack frontend'),
('project.stack.database', 'MySQL 8.0', 'Base de dados');
```

---

## 4. BACKEND SPRING BOOT

### 4.1 Dependências Maven (pom.xml)
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.3</version>
</parent>
<properties>
    <java.version>21</java.version>
    <mapstruct.version>1.5.5.Final</mapstruct.version>
</properties>
<dependencies>
    spring-boot-starter-web
    spring-boot-starter-data-jpa
    spring-boot-starter-validation
    spring-boot-starter-security
    mysql-connector-j (runtime)
    flyway-core + flyway-mysql
    mapstruct
    lombok (provided)
    itextpdf 5.5.13.3
    springdoc-openapi-starter-webmvc-ui 2.3.0
    spring-boot-starter-test (test)
    h2 (test)
</dependencies>
```

### 4.2 application.yml
```yaml
server:
  port: 8090
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://localhost:3307/sgcd_pm?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Berlin
    username: sgcd_pm
    password: ${DB_PASSWORD:sgcd_pm_dev}
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true

sgcd-pm:
  jwt:
    secret: ${JWT_SECRET:sgcd-pm-secret-key-change-in-production-2026}
    expiration: 86400000  # 24h
  stakeholder:
    token: ${STAKEHOLDER_TOKEN:sgcd-stakeholder-2026}
```

### 4.3 REST API (todos os endpoints)

#### Sprints
```
GET    /api/v1/sprints                     → List<SprintDTO>
GET    /api/v1/sprints/{id}                → SprintDTO (com tasks count)
GET    /api/v1/sprints/{id}/progress       → SprintProgressDTO
GET    /api/v1/sprints/active              → SprintDTO (sprint actual)
PATCH  /api/v1/sprints/{id}                → SprintDTO (actualizar notas/status)
```

#### Tasks
```
GET    /api/v1/tasks                       → Page<TaskDTO> (?sprint=&status=&from=&to=)
GET    /api/v1/tasks/{id}                  → TaskDTO (com execuções e notas)
GET    /api/v1/tasks/today                 → TaskDTO (tarefa de hoje, ou próxima)
GET    /api/v1/tasks/next                  → TaskDTO (próxima pendente)
PATCH  /api/v1/tasks/{id}                  → TaskDTO (actualizar campos)
POST   /api/v1/tasks/{id}/start            → TaskDTO (status→IN_PROGRESS, startedAt=now)
POST   /api/v1/tasks/{id}/complete         → TaskDTO (status→COMPLETED, completedAt=now, recalcula sprint)
POST   /api/v1/tasks/{id}/block            → TaskDTO (status→BLOCKED, blocker reason)
POST   /api/v1/tasks/{id}/skip             → TaskDTO (status→SKIPPED)
GET    /api/v1/tasks/{id}/prompt           → PromptDTO (prompt Claude completo)
POST   /api/v1/tasks/{id}/notes            → TaskNoteDTO (adicionar nota)
```

**IMPORTANTE — Lógica de POST /tasks/{id}/complete:**
1. Marca task como COMPLETED com completedAt=now()
2. Calcula actualHours (se não fornecido, usa plannedHours)
3. Incrementa sprint.completedSessions
4. Soma sprint.actualHours
5. Se última task do sprint → sprint.status=COMPLETED, gera relatório automático
6. Se última task do sprint → próximo sprint.status=ACTIVE

#### Dashboard
```
GET    /api/v1/dashboard                   → DashboardDTO
GET    /api/v1/dashboard/stakeholder       → StakeholderDashboardDTO
```

**DashboardDTO contém:**
```json
{
  "projectProgress": 0.0,          // % global
  "totalSessions": 204,
  "completedSessions": 0,
  "totalHoursPlanned": 680,
  "totalHoursSpent": 0,
  "activeSprint": { SprintDTO },
  "todayTask": { TaskDTO },
  "recentTasks": [ TaskDTO x5 ],
  "sprintSummaries": [
    { "sprintNumber": 1, "name": "...", "progress": 0, "status": "ACTIVE", "color": "#3884F4" }
  ],
  "upcomingBlockedDays": [ BlockedDayDTO ],
  "weekProgress": {
    "weekTasks": 6,
    "weekCompleted": 0,
    "weekHoursPlanned": 20,
    "weekHoursSpent": 0
  }
}
```

**StakeholderDashboardDTO contém:**
```json
{
  "projectName": "SGCD — Sistema de Gestão Consular Digital",
  "client": "Embaixada da República de Angola",
  "overallProgress": 0.0,
  "totalSessions": 204,
  "completedSessions": 0,
  "totalHoursPlanned": 680,
  "totalHoursSpent": 0,
  "startDate": "2026-03-02",
  "targetDate": "2026-12-20",
  "daysRemaining": 312,
  "sprints": [
    {
      "number": 1, "name": "Fundação Técnica", "nameEn": "Technical Foundation",
      "progress": 0.0, "status": "PLANNED", "startDate": "...", "endDate": "...",
      "sessions": 36, "completedSessions": 0, "hours": 120, "hoursSpent": 0,
      "color": "#3884F4", "focus": "Infra/DevOps"
    }
  ],
  "milestones": [
    { "name": "Sprint 1 Complete", "targetDate": "2026-05-10", "status": "FUTURE" },
    { "name": "Go-Live", "targetDate": "2026-12-20", "status": "FUTURE" }
  ],
  "weeklyActivity": {
    "sessionsThisWeek": 0,
    "hoursThisWeek": 0,
    "tasksCompletedThisWeek": 0
  },
  "lastUpdated": "2026-02-11T..."
}
```

#### Calendar
```
GET    /api/v1/calendar                    → CalendarDTO (?month=&year=)
GET    /api/v1/calendar/blocked            → List<BlockedDayDTO>
```

#### Prompts
```
GET    /api/v1/prompts/today               → PromptDTO (prompt de hoje)
GET    /api/v1/prompts/task/{taskId}       → PromptDTO (prompt para tarefa específica)
GET    /api/v1/prompts/context             → String (contexto base do projecto)
```

#### Reports
```
GET    /api/v1/reports                     → List<ReportDTO>
GET    /api/v1/reports/sprint/{sprintId}   → ReportDTO
POST   /api/v1/reports/sprint/{sprintId}/generate → ReportDTO (gera PDF)
GET    /api/v1/reports/{id}/pdf            → byte[] (download PDF)
```

#### Auth
```
POST   /api/v1/auth/login                 → { token, role, expiresIn }
GET    /api/v1/auth/me                     → { username, role }
```

### 4.4 PromptService — Motor de Prompts

O PromptService gera prompts Claude contextualizados. Estrutura:

```
═══════════════════════════════════════════
SGCD Development Session
Sprint {N}: {SprintName} · {TaskCode} · {Date}
Session {X} of 204 · {Hours}h ({DayOfWeek})
═══════════════════════════════════════════

PROJECT CONTEXT:
• Stack: Spring Boot 3.x (Java 21), Angular 17+, MySQL 8.0
• Components: Kafka, Redis, MinIO, Keycloak, Docker
• Repos: sgcd-backend, sgcd-frontend-backoffice, sgcd-frontend-portal, sgcd-infra
• Conventions: Google Java Style, Conventional Commits, 80% coverage, MapStruct, Flyway

SPRINT {N} STATUS:
• Focus: {SprintFocus}
• Progress: {completed}/{total} sessions ({%})
• Recently completed: {last 3 completed tasks}

TODAY'S TASK: {Title}
{Description}

DELIVERABLES:
■ {bullet 1}
■ {bullet 2}
■ ...

VALIDATION:
✓ {validation criteria}

Coverage target: {coverageTarget}

DELIVERY RULES:
1. Production-quality code only
2. Unit tests for all new code
3. Integration tests where applicable
4. Portuguese for comments, English for technical names
5. Follow existing patterns and conventions
```

### 4.5 DataSeeder (CommandLineRunner)

O DataSeeder corre ao startup e insere as 204 tarefas se a tabela estiver vazia. Usa os dados do ficheiro JSON (ver Secção 7).

---

## 5. FRONTEND ANGULAR

### 5.1 Design Visual

**Identidade visual:** Consistente com o SGCD (Angola government style)
- **Cores principais:** Angola Red (#CC092F), Black (#1A1A1A), Gold (#F4B400)
- **Cores de estado:** Blue (#3884F4), Green (#2EA043), Purple (#8957E5), Teal (#009688)
- **Font body:** 'Source Sans 3' (Google Fonts)
- **Font display:** 'Playfair Display' (Google Fonts)
- **Background:** #FFFFFF (surface), #F6F8FA (surface-alt)
- **Border:** #D0D7DE, #E8EBED (light)
- **Text:** #24292F (primary), #57606A (secondary), #8B949E (muted)

### 5.2 Páginas / Rotas

```
/                          → Developer Dashboard
/stakeholder               → Stakeholder Dashboard (read-only, sem sidebar)
/stakeholder?token=xxx     → Acesso por token
/login                     → Login simples
/sprints                   → Sprint List (cards com progresso)
/sprints/:id               → Sprint Detail (tasks por semana)
/tasks                     → Task List (tabela com filtros)
/tasks/:id                 → Task Detail (completo + prompt)
/prompts                   → Prompt Generator (hoje + copiar)
/calendar                  → Calendário (mensal com tarefas)
/reports                   → Reports list + geração
```

### 5.3 Layout

**Developer Layout (/, /sprints, /tasks, /prompts, /calendar, /reports):**
```
┌──────────────────────────────────────────────┐
│ HEADER: Logo SGCD-PM · Nav · User dropdown   │
├──────────┬───────────────────────────────────┤
│ SIDEBAR  │                                   │
│          │           MAIN CONTENT             │
│ Dashboard│                                   │
│ Sprints  │                                   │
│ Tasks    │                                   │
│ Prompts  │                                   │
│ Calendar │                                   │
│ Reports  │                                   │
│          │                                   │
│ ──────── │                                   │
│ Stakeh.  │                                   │
│ ──────── │                                   │
│ Settings │                                   │
└──────────┴───────────────────────────────────┘
```

**Stakeholder Layout (/stakeholder):**
```
┌──────────────────────────────────────────────┐
│ HEADER: Logo Embaixada · "Relatório SGCD"    │
├──────────────────────────────────────────────┤
│                                              │
│          FULL-WIDTH READ-ONLY CONTENT        │
│                                              │
│  Progress bars, sprint cards, milestones,    │
│  charts, last updated timestamp              │
│                                              │
└──────────────────────────────────────────────┘
```

### 5.4 Developer Dashboard Components

**KPI Row (4 cards):**
```
[ Sprint Actual   ] [ Progresso Global ] [ Tarefa de Hoje    ] [ Horas Esta Semana ]
  Sprint 1           12/204 (5.8%)        S1-12 · Flyway       4h / 20h
  Fundação Técnica   ████░░░░░░           10h Domingo          ██░░░░░░░░░
```

**Tarefa de Hoje (card grande):**
```
┌─────────────────────────────────────────────────┐
│ 🔵 S1-12 · 22/03/2026 · Domingo · 10h          │
│ GlobalExceptionHandler, Flyway, Actuator         │
│                                                  │
│ ■ GlobalExceptionHandler: exceptions → HTTP      │
│ ■ Flyway com V1__create_audit_table.sql          │
│ ■ Seed script para dados iniciais                │
│ ✓ GlobalExceptionHandler OK, Flyway Testcontain. │
│                                                  │
│ [▶ Iniciar Tarefa]  [📋 Copiar Prompt]  [→ Ver] │
└─────────────────────────────────────────────────┘
```

**Sprint Progress (6 mini-cards horizontais):**
```
S1 ████████░░ 33%  S2 ░░░░░░░░░░ 0%  S3 ░░░░░░░░░░ 0%
S4 ░░░░░░░░░░ 0%   S5 ░░░░░░░░░░ 0%  S6 ░░░░░░░░░░ 0%
```

**Tarefas Recentes (lista 5):**
```
✅ S1-11 · Commons Module          · 20/03 · 2h
✅ S1-10 · Docker Compose App      · 19/03 · 2h
✅ S1-09 · Docker Compose Infra    · 18/03 · 2h
🔵 S1-12 · GlobalExceptionHandler · 22/03 · 10h ← EM PROGRESSO
⬜ S1-13 · Spring Cloud Gateway    · 23/03 · 2h
```

### 5.5 Stakeholder Dashboard Components

**Header executivo:**
```
┌──────────────────────────────────────────────────┐
│ 🇦🇴 SGCD — Relatório de Progresso do Projecto    │
│ Embaixada da República de Angola                  │
│ Actualizado: 11/02/2026 14:30                     │
└──────────────────────────────────────────────────┘
```

**KPI row:**
```
[ Progresso Global ] [ Sessões ] [ Horas    ] [ Prazo      ]
  5.8%               12 / 204    24h / 680h   312 dias
  ████░░░░░░░░░░░░   ██░░░░░░   ██░░░░░░░░   20 Dez 2026
```

**Sprint Timeline (visual):**
```
  ●━━━━━━━● ○─────────○ ○─────────○ ○─────────○ ○─────────○ ○─────────○
  Sprint 1   Sprint 2    Sprint 3    Sprint 4    Sprint 5    Sprint 6
  33% ⏳     0% 📅        0% 📅       0% 📅       0% 📅       0% 📅
  Mar-Mai    Mai-Jun     Jun-Ago     Ago-Set     Set-Nov     Nov-Dez
```

**Sprint cards (accordion, cada sprint expandível):**
- Barra de progresso colorida
- % conclusão, sessões, horas
- Foco do sprint
- Datas início/fim

**Sem funcionalidades de edição. Apenas visualização.**

### 5.6 Task Detail Page

```
┌─────────────────────────────────────────────────────────┐
│ ← Voltar                                S1-12 · PLANNED │
│                                                          │
│ GlobalExceptionHandler, Flyway, Actuator                │
│ 22 Março 2026 · Domingo · 10h · Semana 3                │
│ Sprint 1: Fundação Técnica · Cobertura: >80%            │
│                                                          │
│ ENTREGÁVEIS:                                            │
│ ■ GlobalExceptionHandler: exceptions → HTTP status       │
│ ■ Flyway com V1__create_audit_table.sql                  │
│ ■ Seed script para dados iniciais                        │
│                                                          │
│ VALIDAÇÃO:                                              │
│ ✓ GlobalExceptionHandler OK, Flyway Testcontainers       │
│                                                          │
│ ┌──────────┐ ┌──────────────┐ ┌────────────────┐       │
│ │▶ Iniciar │ │📋 Ver Prompt │ │🔴 Bloquear    │       │
│ └──────────┘ └──────────────┘ └────────────────┘       │
│                                                          │
│ ─── PROMPT CLAUDE ──────────────────────────────        │
│ [Texto completo do prompt]                [📋 Copiar]   │
│                                                          │
│ ─── NOTAS ──────────────────────────────────────        │
│ [Adicionar nota...]                                     │
│                                                          │
│ ─── HISTÓRICO DE EXECUÇÕES ─────────────────────        │
│ (vazio - nenhuma execução registada)                    │
└─────────────────────────────────────────────────────────┘
```

---

## 6. SISTEMA DE PROMPTS

### Contexto fixo do projecto (injectado em todos os prompts)

```
SGCD — Sistema de Gestão Consular Digital
Embaixada da República de Angola — Alemanha & Rep. Checa

STACK: Spring Boot 3.x (Java 21), Angular 17+, MySQL 8.0
       Kafka, Redis, MinIO, Keycloak, Docker Compose
       Hetzner Cloud (Alemanha, RGPD)

REPOS: sgcd-backend (Maven multi-module), sgcd-frontend-backoffice,
       sgcd-frontend-portal, sgcd-infra, sgcd-docs

MODULES: commons, gateway, registration-svc, scheduling-svc,
         passport-svc, civil-registry-svc, financial-svc, workflow-svc

CONVENTIONS:
- Google Java Style (Checkstyle)
- Conventional Commits (commitlint + Husky)
- Coverage: minimum 80%
- MapStruct for DTO mapping
- Flyway for DB migrations
- OpenAPI/Swagger for API docs
- PT for comments, EN for technical names
- Testcontainers for integration tests
```

---

## 7. DADOS SEED — TODAS AS 204 TAREFAS

O DataSeeder deve inserir todas as 204 tarefas. Abaixo está a lista completa.
O campo `deliverables` é um JSON array com as bullets (■).
O campo `validation_criteria` é um JSON array com a validação (✓).

### Sprint 1: Fundação Técnica (36 sessões, 120h)
```
S1-01 | 2026-03-02 | Mon | 2h  | W1 | Create GitHub organization and repositories
  ■ Organization 'sgcd-angola': sgcd-backend, sgcd-frontend-backoffice, sgcd-frontend-portal, sgcd-infra, sgcd-docs
  ■ Branch protection rules and merge policies
  ■ PR and Issue templates in each repo
  ✓ Clone OK, push blocked without PR | Coverage: N/A

S1-02 | 2026-03-03 | Tue | 2h  | W1 | Maven multi-module structure
  ■ Maven parent with modules: commons, gateway, registration-svc, scheduling-svc, passport-svc, civil-registry-svc, financial-svc, workflow-svc
  ■ Shared dependencies in parent POM (Spring Boot 3.x, Java 21)
  ■ .gitignore, .editorconfig, README.md
  ✓ mvn clean compile without errors | Coverage: N/A

S1-03 | 2026-03-04 | Wed | 2h  | W1 | Linting, hooks and contribution guidelines
  ■ Checkstyle with Google Style
  ■ Commitlint + Husky (conventional commits)
  ■ CONTRIBUTING.md, CODE_OF_CONDUCT.md
  ✓ Non-conventional commit rejected | Coverage: N/A

S1-04 | 2026-03-05 | Thu | 2h  | W1 | Angular Backoffice structure
  ■ Angular 17+ with standalone components
  ■ ESLint + Prettier configured
  ■ Jest/Karma with 80% threshold
  ✓ ng build and ng test without errors | Coverage: N/A

S1-05 | 2026-03-06 | Fri | 2h  | W1 | Angular Citizen Portal structure
  ■ Angular Portal with i18n (@angular/localize)
  ■ Translations: PT, DE, EN, CS
  ■ PWA with basic service worker
  ✓ ng build with each locale without errors | Coverage: N/A

S1-06 | 2026-03-15 | Sun | 10h | W2 | Complete CI/CD Pipeline
  ■ ci-backend.yml: checkout → Java 21 → mvn test → JaCoCo
  ■ ci-frontend.yml: checkout → Node → npm ci → ng test → ng lint
  ■ SonarQube Cloud in workflows
  ✓ Workflows OK, quality gates blocking | Coverage: N/A

S1-07 | 2026-03-16 | Mon | 2h  | W2 | Multi-stage Backend Dockerfile
  ■ Multi-stage Dockerfile for Spring Boot
  ■ Docker layers optimized for cache
  ■ .dockerignore
  ✓ docker build OK, image < 300MB | Coverage: N/A

S1-08 | 2026-03-17 | Tue | 2h  | W2 | Frontend Dockerfile + Nginx
  ■ Multi-stage Dockerfile for Angular
  ■ nginx.conf: gzip, cache, SPA fallback, security headers
  ■ Build for backoffice + portal
  ✓ Nginx serves app, secure headers | Coverage: N/A

S1-09 | 2026-03-18 | Wed | 2h  | W2 | Docker Compose — Infrastructure
  ■ docker-compose.yml: MySQL 8.0, Redis 7, Kafka + Zookeeper, MinIO
  ■ Healthchecks and depends_on
  ■ Persistent volumes
  ✓ docker-compose up all healthy | Coverage: N/A

S1-10 | 2026-03-19 | Thu | 2h  | W2 | Docker Compose — Application
  ■ Services: backend, backoffice, portal, Keycloak
  ■ Networking between containers
  ■ Script start-dev.sh
  ✓ start-dev.sh starts everything | Coverage: N/A

S1-11 | 2026-03-20 | Fri | 2h  | W2 | Commons Module and Spring profiles
  ■ Base DTOs: ApiResponse, PageResponse, ErrorResponse
  ■ Exceptions: ResourceNotFoundException, BusinessException
  ■ Spring profiles: dev, staging, prod
  ✓ Unit tests DTOs and exceptions | Coverage: N/A

S1-12 | 2026-03-22 | Sun | 10h | W3 | GlobalExceptionHandler, Flyway, Actuator
  ■ GlobalExceptionHandler: exceptions → HTTP status
  ■ Flyway with V1__create_audit_table.sql
  ■ Seed script for initial data
  ✓ GlobalExceptionHandler OK, Flyway Testcontainers | Coverage: N/A

S1-13 | 2026-03-23 | Mon | 2h  | W3 | Spring Cloud Gateway — Routes
  ■ API Gateway with Spring Cloud Gateway
  ■ Routes: /api/v1/registration/**, /scheduling/**, /passport/**, /civil-registry/**
  ■ Request/response logging
  ✓ Routing correct | Coverage: N/A

S1-14 | 2026-03-24 | Tue | 2h  | W3 | Rate Limiting and CORS
  ■ Rate limiting Redis (bucket4j): 100 req/min per IP
  ■ CORS policy configured
  ■ Request size limits (max 10MB)
  ✓ 429 after exceeding limit | Coverage: N/A

S1-15 | 2026-03-25 | Wed | 2h  | W3 | Keycloak — Realm and Clients
  ■ Keycloak in Docker Compose
  ■ Realm 'sgcd': token lifetime, password policy, brute force
  ■ Clients: sgcd-backend, sgcd-backoffice, sgcd-portal
  ✓ Realm OK, clients functional | Coverage: N/A

S1-16 | 2026-03-26 | Thu | 2h  | W3 | Keycloak — Roles and Users
  ■ Roles: ADMIN, CONSUL, ATTENDANCE_OFFICER, FINANCIAL_OFFICER, CIVIL_OFFICER, CITIZEN
  ■ Test users per role
  ■ Export realm config JSON
  ✓ Login OK, token contains roles | Coverage: N/A

S1-17 | 2026-03-27 | Fri | 2h  | W3 | Circuit Breaker and Resilience
  ■ Resilience4j: circuit breaker, retry, timeout
  ■ Fallback responses
  ■ Failure tests
  ✓ Circuit breaker opens after failures | Coverage: N/A

S1-18 | 2026-04-05 | Sun | 10h | W4 | OAuth2, JWT and Complete Authorization
  ■ OAuth2 Resource Server with JWT via Keycloak
  ■ JwtAuthenticationConverter for roles/claims
  ■ SecurityConfig with @PreAuthorize per endpoint
  ✓ Authorized/denied per role, login/logout E2E | Coverage: N/A

S1-19 | 2026-04-07 | Tue | 2h  | W4 | Angular Backoffice Layout
  ■ Shell: responsive sidebar, header user info, footer
  ■ Angular Material theme
  ■ Global loading spinner and toast notifications
  ✓ Layout, sidebar toggle, responsive | Coverage: N/A

S1-20 | 2026-04-08 | Wed | 2h  | W4 | Routing and Lazy Loading
  ■ Lazy loading per functional module
  ■ Dynamic breadcrumbs
  ■ 404 page and post-login redirect
  ✓ Lazy loading chunks, breadcrumbs OK | Coverage: N/A

S1-21 | 2026-04-09 | Thu | 2h  | W4 | Angular Base HTTP Services
  ■ Generic typed ApiService: get, post, put, patch
  ■ Reusable pagination and filters
  ■ Local cache service
  ✓ ApiService with HttpClientTestingModule | Coverage: N/A

S1-22 | 2026-04-10 | Fri | 2h  | W4 | Prometheus and Grafana
  ■ Docker Compose with Prometheus + Grafana
  ■ Scraping Actuator and Kafka metrics
  ■ Dashboards: JVM, HTTP, MySQL
  ✓ Targets up, dashboards with data | Coverage: N/A

S1-23 | 2026-04-13 | Mon | 2h  | W4 | Alerts and Custom Metrics
  ■ Micrometer: requests, latency
  ■ Grafana alerts: CPU > 80%, mem > 85%, 5xx > 1%
  ■ Overview dashboard
  ✓ Metrics visible, alerts trigger | Coverage: N/A

S1-24 | 2026-04-19 | Sun | 10h | W5 | E2E Integration and Sprint 1 Review
  ■ Cypress with fixtures and commands
  ■ E2E: login → dashboard → role
  ■ Complete cd-staging.yml
  ✓ Staging operational, smoke tests OK | Coverage: N/A

S1-25 | 2026-04-20 | Mon | 2h  | W5 | Base document upload service
  ■ DocumentStorageService with MinIO: upload, download, delete
  ■ File type validation by magic bytes
  ■ Size validation (max 5MB) and formats (PDF, JPG, PNG)
  ✓ Upload/download MinIO functional | Coverage: N/A

S1-26 | 2026-04-21 | Tue | 2h  | W5 | Base notification service
  ■ NotificacaoService with Spring Mail + Thymeleaf
  ■ Base templates: confirmation, error, info
  ■ Asynchronous sending via Kafka
  ✓ GreenMail: email with template | Coverage: N/A

S1-27 | 2026-04-22 | Wed | 2h  | W5 | Kafka — Configuration and Topics
  ■ Kafka topics: registration, scheduling, passport, civil-registry, payment, workflow
  ■ JSON serialization with schema
  ■ Dead letter queue for failures
  ✓ Topics created, produce/consume OK | Coverage: N/A

S1-28 | 2026-04-23 | Thu | 2h  | W5 | AuditService — Audit Trail
  ■ AuditConsumer: Kafka events → audit table
  ■ GET /audit?entity=&action=&from=&to=
  ■ Retention: >1 year → archive, >3 years → delete
  ✓ Consumer OK, filters functional | Coverage: N/A

S1-29 | 2026-04-24 | Fri | 2h  | W5 | Integration Tests Configuration
  ■ Testcontainers for MySQL, Kafka, MinIO, Keycloak
  ■ TestDataFactory for test data
  ■ Test profiles: @ActiveProfiles("test")
  ✓ Testcontainers start in < 30s | Coverage: N/A

S1-30 | 2026-04-26 | Sun | 10h | W5 | Base Workflow Module (State Machine)
  ■ ConsularProcess base entity
  ■ WorkflowStateMachine: generic transitions for all modules
  ■ ProcessState enum: DRAFT, SUBMITTED, UNDER_REVIEW, VALIDATED, PENDING_CITIZEN, PENDING_PAYMENT, APPROVED, IN_PRODUCTION, AVAILABLE, DELIVERED, REJECTED, CANCELLED
  ✓ Generic state machine, 20+ scenarios | Coverage: N/A

S1-31 | 2026-04-27 | Mon | 2h  | W5 | Frontend — Process Inbox (generic)
  ■ Inbox: pending by urgency and age
  ■ Badges: urgent (red), >48h (yellow), normal
  ■ Filters: type, state, post, module
  ✓ Inbox renders, filters, badges | Coverage: N/A

S1-32 | 2026-04-28 | Tue | 2h  | W6 | Frontend — Process Timeline
  ■ Visual timeline of process states
  ■ Stepper: past (green), current (blue), future (gray)
  ■ Documents section with upload
  ✓ Timeline, stepper per state | Coverage: N/A

S1-33 | 2026-04-29 | Wed | 2h  | W6 | Frontend — Approval/Return Modal
  ■ Modal: approve, return (mandatory reason), reject
  ■ Confirmation before irreversible actions
  ■ Toast notifications feedback
  ✓ Modal, reason validation, actions | Coverage: N/A

S1-34 | 2026-04-30 | Thu | 2h  | W6 | User Management
  ■ UserService: CRUD + Keycloak sync
  ■ Role management: assign/remove
  ■ Access profile per consular post
  ✓ CRUD → Keycloak sync, role, post | Coverage: N/A

S1-35 | 2026-05-04 | Mon | 2h  | W6 | Session Log and Security
  ■ Session logs: login, logout, refresh, IP, user-agent
  ■ Active sessions for admin
  ■ Lock after 5 failed attempts
  ✓ Login recorded, lock works | Coverage: N/A

S1-36 | 2026-05-10 | Sun | 10h | W6 | Final Integration and Sprint 1 Buffer
  ■ Delegation: absent officer → substitute
  ■ Escalation > 48h → upper level
  ■ @Scheduled SLA alerts: >24h, >48h, >72h
  ■ Documentation: ADRs, setup guide, architecture overview
  ■ Prepare seed data for Sprint 2
  ✓ Staging stable, base workflow functional | Coverage: N/A
```

### Sprints 2-6: Dados no ficheiro JSON

As tarefas dos sprints 2-6 seguem o mesmo formato. O DataSeeder deve carregar todas as 204 tarefas a partir de um ficheiro JSON embebido ou de um método Java com os dados hardcoded.

**Ficheiro de referência completo:** `/home/claude/all_tasks.json` (gerado pela extracção do MVP plan)

O DataSeeder deve:
1. Verificar se a tabela `tasks` está vazia
2. Se vazia, inserir todas as 204 tarefas
3. Calcular `week_number` com base na data (semana dentro do sprint)
4. Gerar `sort_order` sequencial por sprint

---

## 8. DOCKER COMPOSE

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    container_name: sgcd-pm-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: sgcd_pm
      MYSQL_USER: sgcd_pm
      MYSQL_PASSWORD: sgcd_pm_dev
    ports:
      - "3307:3306"
    volumes:
      - sgcd-pm-mysql-data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  backend:
    build: ./backend
    container_name: sgcd-pm-backend
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/sgcd_pm?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Berlin
      SPRING_DATASOURCE_USERNAME: sgcd_pm
      SPRING_DATASOURCE_PASSWORD: sgcd_pm_dev
    ports:
      - "8090:8090"
    depends_on:
      mysql:
        condition: service_healthy

  frontend:
    build: ./frontend
    container_name: sgcd-pm-frontend
    ports:
      - "4201:80"
    depends_on:
      - backend

volumes:
  sgcd-pm-mysql-data:
```

---

## 9. INSTRUÇÕES DE IMPLEMENTAÇÃO

### Para o Claude Code

Este documento é a especificação completa. Implementa o sistema seguindo estas prioridades:

**Fase 1 — Backend Core (Sessões 1-5):**
1. Criar projecto Maven com todas as dependências
2. Schema MySQL via Flyway (copiar da Secção 3)
3. Entidades JPA com Lombok e MapStruct
4. Repositories com queries customizadas
5. Services: SprintService, TaskService, DashboardService
6. Controllers REST (todos os endpoints da Secção 4.3)
7. DataSeeder: inserir 204 tarefas ao startup
8. PromptService: gerar prompts contextualizados
9. SecurityConfig: JWT simples, 2 roles
10. CORS configurado para Angular (porta 4201)

**Fase 2 — Frontend Core (Sessões 6-10):**
1. Angular 17+ com standalone components
2. Angular Material + SCSS com variáveis Angola
3. Routing com lazy loading
4. Services HTTP com interceptor JWT
5. Developer Dashboard (KPIs, tarefa hoje, progresso sprints)
6. Sprint List + Sprint Detail (tarefas por semana)
7. Task Detail com prompt viewer e acções (iniciar/completar)
8. Stakeholder Dashboard (read-only, executivo)
9. Calendar com tarefas e dias bloqueados
10. Reports list

**Fase 3 — Polish:**
1. Docker Compose funcional
2. Testes unitários backend (Services)
3. OpenAPI annotations
4. README com instruções de setup

### Comandos de Setup Esperados
```bash
# Backend
cd backend
mvn clean install
mvn spring-boot:run

# Frontend
cd frontend
npm install
ng serve --port 4201

# Docker
docker-compose up -d
```

### Credenciais Default
```
Developer:   admin / admin123
Stakeholder: stakeholder / stakeholder2026
Token URL:   /stakeholder?token=sgcd-stakeholder-2026
```

---

**FIM DA ESPECIFICAÇÃO**

Este documento contém tudo o que o Claude Code precisa para implementar o sistema completo.
O ficheiro `/home/claude/all_tasks.json` contém os dados das 204 tarefas em formato JSON.
