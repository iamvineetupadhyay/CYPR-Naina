# AEGIS BREACH: SYSTEM ARCHITECTURE & TECHNICAL BLUEPRINT

## 1. System Overview

AegisBreach is a full-stack **Ethical Public Breach Monitoring Engine** designed to identify exposed credentials, vulnerable enterprise domains, and critical security disclosures using **XposedOrNot Official Java SDK (v1.1.0)**, **100% free public security APIs**, and an **ethical JSoup web scraping engine**.

```mermaid
graph TD
    subgraph Client Layer [React Cyberpunk Dashboard]
        UI[React Vite SPA]
        IDScan[Identity Scanner View]
        PassAud[k-Anonymity Password Checker]
        DomAud[Domain & DNS Auditor]
        ScrapCtrl[Web Scraper Control]
        ThreatFeed[Live Vulnerability Ticker]
        Analytics[Chart.js Threat Analytics]
    end

    subgraph Backend Layer [Java Spring Boot REST Engine]
        Controller[BreachApiController]
        ScraperSvc[PublicBreachScraperService / JSoup]
        ApiSvc[FreeSecurityApiService]
        XposedSDK[XposedOrNot Official Java SDK v1.1.0]
        DB[(H2 Embedded Database)]
    end

    subgraph External Free APIs & Sources
        XposedAPI[XposedOrNot Free API v1/v3]
        HIBP[HIBP Pwned Passwords API - k-Anonymity SHA-1]
        DoH[Google / Cloudflare DNS-over-HTTPS]
        Wiki[Wikipedia List of Data Breaches]
    end

    UI <-->|HTTP REST JSON| Controller
    Controller --> ScraperSvc
    Controller --> ApiSvc
    ApiSvc <--> XposedSDK
    XposedSDK <-->|Free HTTPS REST| XposedAPI
    ScraperSvc -->|JSoup HTML Parse| Wiki
    ScraperSvc -->|JPA Persist| DB
    ApiSvc -->|SHA-1 5-Char Prefix| HIBP
    ApiSvc -->|DoH TXT Lookup| DoH
    ApiSvc <-->|Local Query| DB
```

---

## 2. Component Specifications

### 2.1 Java Backend Layer (Spring Boot 3.x)
- **Framework**: Spring Boot 3.2.3 (Java 17)
- **Official SDK**: `com.xposedornot` Java SDK v1.1.0 for free breach API operations
- **Database**: H2 In-Memory / Embedded Relational Database
- **Web Scraping Engine**: JSoup 1.17.2 for DOM parsing of public breach directories
- **Cryptography**: BouncyCastle 1.78.1 & Apache Commons Codec for SHA-1 digests
- **CORS Configuration**: Open cross-origin REST configuration for Vite dev server (`http://localhost:5173`)

### 2.2 Frontend Layer (React + Vite)
- **Build Tool**: Vite 5.1
- **UI Styling**: TailwindCSS + Custom CSS tokens (glassmorphism, cyber neon accents, animated grid background, scanlines)
- **Data Visualization**: Chart.js 4.4 + React-Chartjs-2
- **Icons**: Lucide React
