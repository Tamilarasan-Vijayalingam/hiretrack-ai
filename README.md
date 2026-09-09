# HireTrack AI – Campus Placement Analytics System

HireTrack AI is an enterprise-grade campus placement intelligence and recruitment management platform built with React, Spring Boot, and PostgreSQL.

---

## Architecture Overview

- **Frontend**: React 19, Vite, Tailwind CSS, Recharts, Lucide Icons (Deployed on **Vercel**)
- **Backend**: Spring Boot 3.2, Spring Security (JWT), Spring Data JPA, HikariCP (Deployed on **Render**)
- **Database**: PostgreSQL 13+ (Hosted on **Render PostgreSQL** or external managed database)
- **AI Engine**: OpenAI GPT-4o-mini / GPT-3.5 API with strict JSON schema validation (Backend-only integration)

---

## Production Deployment Guide

### 1. Database Deployment (PostgreSQL on Render)

1. Log into your [Render Dashboard](https://dashboard.render.com).
2. Click **New +** → **PostgreSQL**.
3. Set:
   - **Name**: `hiretrack-db`
   - **Database**: `hiretrack`
   - **User**: `hiretrack_admin`
   - **Region**: Same region as backend (e.g. `Oregon (US West)`)
4. Click **Create Database**.
5. Once created, copy the **Internal Database URL** (or **External Database URL**).

---

### 2. Backend Deployment (Render)

#### Option A: Deploy via Blueprint (`render.yaml`)
1. In Render, click **New +** → **Blueprint**.
2. Connect your Git repository.
3. Render automatically discovers `render.yaml` and provisions both the Web Service and PostgreSQL database.
4. Provide the required environment variables:
   - `FRONTEND_URL`: Your Vercel frontend URL (e.g., `https://hiretrack.vercel.app`)
   - `OPENAI_API_KEY`: Your OpenAI API secret key

#### Option B: Deploy Manually as a Docker Web Service
1. Click **New +** → **Web Service**.
2. Connect your Git repository.
3. Select **Docker** environment.
4. Set **Docker Command**: Leave default (uses root `Dockerfile`).
5. Configure the following environment variables:
   | Variable | Value / Description | Example |
   | :--- | :--- | :--- |
   | `DATABASE_URL` | PostgreSQL JDBC connection URL | `jdbc:postgresql://<host>:5432/<database>` |
   | `DATABASE_USER` | Database username | `hiretrack_admin` |
   | `DATABASE_PASSWORD` | Database user password | `<secure_password>` |
   | `SPRING_PROFILES_ACTIVE` | Set to `prod` | `prod` |
   | `PORT` | Web server port | `8080` |
   | `JWT_SECRET` | 256-bit secure secret key | `<min_32_character_string>` |
   | `FRONTEND_URL` | Vercel production and preview URLs | `https://your-app.vercel.app,https://*.vercel.app` |
   | `OPENAI_API_KEY` | OpenAI secret API key | `sk-proj-...` |
6. Set **Health Check Path** to: `/api/health`
7. Click **Create Web Service**.

---

### 3. Frontend Deployment (Vercel)

1. Log into your [Vercel Dashboard](https://vercel.com).
2. Click **Add New...** → **Project**.
3. Import your Git repository.
4. In Project Settings:
   - **Root Directory**: Select `frontend`
   - **Framework Preset**: `Vite`
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist`
5. Configure Environment Variable:
   | Variable | Value | Example |
   | :--- | :--- | :--- |
   | `VITE_API_URL` | Backend API URL with `/api` | `https://hiretrack-backend.onrender.com/api` |
6. Click **Deploy**.
7. Once deployed, copy your production domain (e.g. `https://hiretrack.vercel.app`) and update the `FRONTEND_URL` environment variable on Render.

---

## Security & Best Practices

1. **Zero Secret Exposure**:
   - `OPENAI_API_KEY` is strictly confined to the Spring Boot backend environment.
   - Frontend accesses AI intelligence solely through authenticated `/api/ai/...` endpoints.
2. **Deterministic Rules**:
   - Placement eligibility cutoffs (CGPA, backlogs, required skills) are computed using deterministic business logic in the backend engine, never delegated to LLM hallucination.
3. **CORS Security**:
   - Backend restricts origin access to the declared Vercel frontend domain with credentials support.
4. **Resilient Routing**:
   - `frontend/vercel.json` provides rewrite rules ensuring client-side React Router navigation works without 404 errors on browser refresh or direct URL access.
