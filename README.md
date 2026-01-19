# Tiering Machine

A dystopian Social Credit System simulation where citizens are ranked, judged, and tiered based on their behavior.

> [!WARNING]
> This was a project built during the Odoo Hackathon 5th edition in **48 hours**, expect code to be messy or bugs to be found.

## Overview

Tiering Machine simulates a society where every action is monitored and scored. It features an AI-powered judicial system that evaluates reports and assigns point penalties or rewards, determining a citizen's social standing.

## Architecture

The project is divided into three main components:

- **[Client](./client/README.md)**: A React-based frontend providing the user interface for citizens and administrators.
- **[Server](./server/README.md)**: A Spring Boot backend handling logic, AI integration, and data persistence.
- **[Database](./database/README.md)**: PostgreSQL database schema and data.

## Standalone Client

A standalone client for Tiering Machine is used for Deployment. This is to save costs because hosting a database and server on Azure are expensive.

## Tech Stack

- **Frontend**: React, TypeScript, Vite, Material UI, React Three Fiber
- **Backend**: Java 21, Spring Boot 3.2, Gradle (Kotlin DSL)
- **Database**: PostgreSQL
- **AI**: Groq API (Llama 3.1)
- **DevOps**: Docker

> [!NOTE]
> Development commits were made in other repositories, this repository is a bundled version of the project.
>
> - **Frontend**: Tiering-Machine-FE (DELETED)
> - **Backend**: Tiering-Machine-BE (PRIVATE)
> - **Database**: No Git

## Deployment

- **Frontend**: Combell Web Hosting
- **Backend**: Azure App Service (Container)
- **Database**: Azure Database for PostgreSQL flexible servers

> [!NOTE]
> Web application is deployed on the cloud on low tiers of Azure. Expect some latency.

## Getting Started

Please refer to the specific READMEs for each component to get started:

- [Client Setup](./client/README.md)
- [Server Setup](./server/README.md)
- [Database Setup](./database/README.md)

## Credits

- Frontend/DevOps Lead: [Aryan (Github)](https://github.com/l145dev)
- Backend Lead: [MJ (Github)](https://github.com/Mougembo)
- Database Lead: [Sahir (Github)](https://github.com/elciguapuuu)
