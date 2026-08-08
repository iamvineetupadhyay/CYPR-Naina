<div align="center">

  # 🛡️ CYPR NAINA ENGINE
  ### **Public Breach Monitoring Platform**

  [![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg?style=for-the-badge&logo=openjdk)](https://www.oracle.com/java/)
  [![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.3-6DB33F.svg?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
  [![React](https://img.shields.io/badge/React-18.2-61DAFB.svg?style=for-the-badge&logo=react)](https://reactjs.org/)
  [![Vite](https://img.shields.io/badge/Vite-5.0-646CFF.svg?style=for-the-badge&logo=vite)](https://vitejs.dev/)
  [![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-3.4-38B2AC.svg?style=for-the-badge&logo=tailwind-css)](https://tailwindcss.com/)
  [![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED.svg?style=for-the-badge&logo=docker)](https://www.docker.com/)
  [![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)

  <p align="center">
    <b>Next-Generation Ethical Threat Intelligence, Zero-Knowledge Credential Auditing & Automated Web Scraping Platform</b>
  </p>

</div>

---

## 📌 Executive Summary

**CYPR NAINA ENGINE** is a full-stack, enterprise-grade ethical breach monitoring and threat intelligence platform. Built to address global credential harvesting, identity exposure, and domain security risks, CYPR NAINA combines zero-knowledge **k-Anonymity Cryptography**, a toggleable **Dual Engine Architecture** (XposedOrNot SDK ↔ Local H2 Database), an automated **JSoup Web Crawler**, and mandatory **Google OAuth2 Access Control**.

The system dynamically aggregates over **8.98 Billion+ Exposed Records** harvested from major historical disclosures (Yahoo 3B, Aadhaar 1.1B, First American 885M, Verifications.io 763M, LinkedIn 700M, Facebook 533M) directly from live database queries with sub-50ms latency.

---

## ✨ Key Features

- 🔑 **Mandatory Google OAuth2 Gate**: Enterprise-grade identity access control powered by Google Identity Services (`gsi/client`) to prevent unauthenticated bot abuse.
- 🛡️ **Dual-Engine Identity Scanner**: Seamless toggle between:
  - **XposedOrNot Global SDK Engine**: Official `com.xposedornot` Java SDK connection governed by a 10 req/min sliding-window rate limiter.
  - **Local Engine H2 Database**: Zero-latency, unrestricted local fuzzy matching against indexed breach repositories.
- 🔐 **Zero-Knowledge Password Auditor**: Implements **k-Anonymity Cryptography** (5-character SHA-1 range queries against 800M+ pwned passwords). Plaintext passwords and full hashes are never transmitted over network sockets.
- 🕸️ **Automated JSoup Breach Crawler**: Background scraper service (`PublicBreachScraperService.java`) parsing open web disclosures from Wikipedia and CISA feeds into JPA storage.
- 🌐 **Domain Security Auditor**: Real-time DNS-over-HTTPS (DoH) audits validating domain SPF (`v=spf1`) and DMARC (`v=DMARC1`) security policies.
- 📊 **Dynamic Database Aggregation**: Live aggregate metric calculation (`sum(pwnCount)`) computing exact historical exposures (**8.98B+ accounts**).
- ⏱️ **Real-Time Quota Meter**: Live rate-limit monitoring visual meters and reset countdown timers in user profile dashboards.

---

## 🏗️ System Architecture

```
                               ┌────────────────────────────────────────┐
                               │            React 18 SPA                │
                               │   (Tailwind CSS + Lucide Icons + GSI)   │
                               └──────────────────┬─────────────────────┘
                                                  │ REST / JSON API
                                                  ▼
                               ┌────────────────────────────────────────┐
                               │        Spring Boot 3 REST API          │
                               │     `BreachApiController.java`         │
                               └───────┬────────────────────┬───────────┘
                                       │                    │
                    ┌──────────────────┴───┐            ┌───┴──────────────────┐
                    ▼                      ▼            ▼                      ▼
┌───────────────────────┐ ┌──────────────────┐ ┌────────────────┐ ┌─────────────────┐
│ RateLimitingService   │ │ JSoup Scraper    │ │ XposedOrNot    │ │ k-Anonymity     │
│ (Sliding Bucket 10/m) │ │ (Web Crawler)    │ │ Java SDK v1.1  │ │ SHA-1 5-Char    │
└───────────────────────┘ └────────┬─────────┘ └───────┬────────┘ └────────┬────────┘
                                   │                   │                   │
                                   ▼                   ▼                   ▼
                          ┌──────────────────┐ ┌───────────────┐ ┌──────────────────┐
                          │ H2 Database      │ │ XposedOrNot   │ │ HIBP Pwned       │
                          │ `public_breaches`│ │ Cloud API     │ │ Passwords API    │
                          └──────────────────┘ └───────────────┘ └──────────────────┘
```

---

## 🚀 Quick Start & Installation

### Option 1: One-Click Docker Deployment (Recommended)

Ensure Docker & Docker Compose are installed, then run:

```bash
# 1. Clone repository
git clone https://github.com/iamvineetupadhyay/CYPR-Naina.git
cd CYPR-Naina

# 2. Build and launch containers
docker-compose up --build -d
```

- **Frontend Application**: `http://localhost:5173`
- **Spring Boot API**: `http://localhost:8080`
- **H2 Database Console**: `http://localhost:8080/h2-console`

---

### Option 2: Local Development Setup

#### Prerequisites
- **Java Development Kit (JDK 21 LTS)**
- **Apache Maven 3.8+**
- **Node.js v18+ & npm**

#### 1. Backend Service (Spring Boot 3)
```bash
cd backend
mvn clean compile
mvn spring-boot:run
```
*Backend runs at `http://localhost:8080`.*

#### 2. Frontend Web Application (React + Vite)
```bash
cd frontend
npm install
npm run dev
```
*Frontend runs at `http://localhost:5173`.*

---

## 📡 REST API Documentation

| Method | Endpoint | Description | Engine Mode |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/breach/search` | Search identity exposure | Dual (`SDK` / `LOCAL`) |
| `POST` | `/api/password/check` | Zero-knowledge password audit | k-Anonymity HIBP |
| `GET` | `/api/stats` | Live database aggregate metrics | H2 Database JPA |
| `GET` | `/api/domain/audit?domain=example.com` | Check SPF/DMARC records | DNS-over-HTTPS |
| `POST` | `/api/scraper/scrape` | Trigger JSoup web scraper | Background Task |

---

## 🛠️ Technology Stack

- **Backend**: Java 21 LTS, Spring Boot 3.2.3, Spring Data JPA, H2 In-Memory DB, JSoup 1.17+, Apache Commons Codec, `com.xposedornot` Java SDK.
- **Frontend**: React 18.2, Vite 5.0, Tailwind CSS 3.4, Lucide React Icons, Google Identity Services SDK (`gsi/client`), Axios.
- **DevOps**: Docker, Docker Compose, Nginx, GitHub Actions CI/CD ready.

---

## 🎓 Academic Attribution & Credits

This project was developed as a **B.Tech Major Project** at **IEC College of Engineering & Technology, Greater Noida**.

- **Author / Developer**: **Vineet Kumar Upadhyay** (Roll No.: `2300900100170`)
- **Department**: Department of Computer Science and Engineering (CSE)
- **Project Guide**: **Mr. Arvind Kumar** *(Assistant Professor)*
- **Head of Department**: **Prof. Vipin Kr. Kushwaha** *(HOD CSE/IT)*
- **Affiliation**: Dr. A.P.J. Abdul Kalam Technical University (AKTU), Lucknow

---

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
