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

-- ═══════════════════════════════════════════
-- SEED DATA
-- ═══════════════════════════════════════════

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
