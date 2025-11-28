# Tiering Machine Database

The PostgreSQL database for the Tiering Machine system.

## Overview

This directory contains the SQL schema and initial data for the application. The database stores citizen records, social credit scores, reports, and other system data.

## Schema

The database schema includes the following key tables (inferred from `tieringmachine.sql`):

- **citizens**: Stores user information, social credit scores, and tiers.
- **reports**: Stores user-submitted reports for the AI judge.
- **bets**: Stores betting information.
- **events**: Stores system events.

## Setup

The database is automatically provisioned when running the application via Docker Compose (if applicable) or needs to be set up manually using the provided SQL dump.

### Import Data (UNTESTED)

To import the schema and data into a running PostgreSQL instance:

```bash
psql -U <username> -d <database_name> -f tieringmachine.sql
```

## Configuration

The server connects to this database using the following environment variables (defined in `server/.env`):

- `DATABASE_URL`
- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`
