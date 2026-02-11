# SGCD-PM — Sistema de Gestão de Projecto

## What is this?
An independent Spring Boot + Angular web application for managing the SGCD MVP development project. It tracks 204 development sessions across 6 sprints (680 hours total) and provides:
- **Developer Dashboard**: real-time progress, today's task, Claude prompt generator
- **Stakeholder Dashboard**: read-only executive view for Embassy stakeholders
- **Auto-update**: metrics recalculate on task/sprint completion
- **Sprint Reports**: auto-generated at sprint end

## Architecture
- **Backend**: Spring Boot 3.2.x, Java 21, Maven, MySQL 8.0, Flyway, MapStruct, JWT
- **Frontend**: Angular 17+ standalone components, Angular Material, SCSS
- **Deploy**: Docker Compose (MySQL 3307, Backend 8090, Frontend 4201)

## How to implement
Read `docs/SGCD-PM-SPECIFICATION.md` for the complete specification including:
- Full MySQL schema
- All REST API endpoints  
- Complete frontend page designs
- Prompt generation system
- All 204 tasks with deliverables and validation criteria

Read `docs/SEED-DATA.md` for the complete Java seed data for all 204 tasks.

## Implementation Order
1. Backend: Schema → Entities → Repos → Services → Controllers → Seeder
2. Frontend: Routing → Services → Dashboard → Sprint/Task views → Stakeholder → Calendar
3. Docker Compose
4. Tests

## Key Design Decisions
- MySQL (not PostgreSQL)
- JWT auth (not Keycloak — this is a management tool, not the SGCD itself)
- Two roles only: DEVELOPER (full access) and STAKEHOLDER (read-only dashboard)
- Angola visual identity: Red #CC092F, Black #1A1A1A, Gold #F4B400
- Fonts: Source Sans 3 + Playfair Display
- All text in Portuguese (PT)

## Ports
- Backend: 8090 (/api)
- Frontend: 4201
- MySQL: 3307

## Default Credentials
- Developer: admin / admin123
- Stakeholder: stakeholder / stakeholder2026  
- Token URL: /stakeholder?token=sgcd-stakeholder-2026
