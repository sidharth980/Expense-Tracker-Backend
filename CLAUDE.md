# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an **Expense Tracker Backend API** built with Spring Boot 3.3.11 and Java 17. It manages shared expenses, account balances, and expense splitting between users (similar to Splitwise).

## Development Commands

### Database Setup
```bash
# Start PostgreSQL with Docker Compose (recommended)
#docker-compose up -d

# Or run PostgreSQL container manually
docker run -d --name postgres -p 5432:5432 \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=expense_tracker \
  postgres:13
```

### Application Commands
```bash
# Build the application
./gradlew build

# Run the application (starts on port 8080)
./gradlew bootRun

# Run tests
./gradlew test

# Build and run full stack with Docker
docker-compose up --build
```

## Architecture

**Layered Spring Boot Architecture:**
- **Controllers** (`/controller`): REST endpoints with CORS enabled for localhost:3000
- **Services** (`/service` + `/service/impl`): Business logic with interface-implementation pattern
- **Repositories** (`/repository`): Data access using Spring Data JPA
- **Entities** (`/entity`): JPA entities for database tables
- **DTOs**: Request/Response objects (ExpenseRequest, ExpenseResponse)

## Core Domain Model

1. **Users**: Authentication and user management
2. **Accounts**: User financial accounts (SAVINGS, CREDIT_CARD) with balances and credit limits
3. **Expenses**: Expense tracking with categories, amounts, dates, and payment tracking
4. **ExpenseSplits**: Expense sharing - tracks who owes what amounts for each expense
5. **Role**: User role management (partially implemented)

## Database

- **PostgreSQL** with **Flyway migrations** in `/src/main/resources/db/migration/`
- **Connection**: `jdbc:postgresql://localhost:5432/expense_tracker`
- **Migrations**: V1 creates tables, V2-V4 contain seed data
- **Custom ENUM**: `account_type` for SAVINGS/CREDIT_CARD

## Key Business Logic

- **Expense Creation**: Users create expenses and split them with other users
- **Automatic Balance Updates**: Account balances update when expenses are created
- **Monthly Reporting**: Category-wise expense summaries
- **Multi-Account Support**: Users can have multiple accounts (savings, credit cards)
- **Settlement Tracking**: Tracks who owes money and settlement status

## Configuration

- **Main Config**: `/src/main/resources/application.properties`
- **Database**: PostgreSQL on localhost:5432
- **Server Port**: 8080
- **CORS**: Configured for React frontend on localhost:3000

## Important Patterns

- **@Transactional**: Used for data consistency in service methods
- **Lombok @Builder**: Extensive use for entity creation
- **Service Interface Pattern**: All services have interface definitions
- **Repository Pattern**: Spring Data JPA for data access

## Schema 
- **Main Schema**: resources/db/migration/V1__create_tables.sql