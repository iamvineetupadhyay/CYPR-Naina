# REST API DOCUMENTATION

Base URL: `http://localhost:8080/api`

---

## Endpoints Summary

| Method | Endpoint | Description | Query / Body Parameters |
|---|---|---|---|
| `GET` | `/breach/search` | Search email or username against indexed public breaches | `query=user@domain.com` |
| `POST` | `/password/check` | Test password hash exposure via k-Anonymity HIBP API | `{"password": "secret"}` |
| `GET` | `/domain/audit` | Perform DNS-over-HTTPS security & breach audit on domain | `domain=adobe.com` |
| `GET` | `/breaches/all` | Retrieve all scraped & indexed breach records | None |
| `POST` | `/scraper/run` | On-demand trigger for Java JSoup public web scraper | None |
| `GET` | `/stats` | Fetch engine metrics & database status | None |

---

## Endpoint Details & Samples

### 1. Identity Search
`GET /api/breach/search?query=admin@adobe.com`

**Response `200 OK`**:
```json
{
  "query": "admin@adobe.com",
  "isExposed": true,
  "exposureCount": 2,
  "totalAffectedAccounts": 289724279,
  "breaches": [
    {
      "id": 1,
      "title": "Adobe Historical Leak",
      "domain": "adobe.com",
      "breachDate": "2013-10-04",
      "pwnCount": 152445165,
      "description": "In October 2013, 153 million Adobe accounts were publicly exposed.",
      "dataClasses": "Email addresses, Passwords",
      "isVerified": true,
      "severity": "CRITICAL",
      "sourceUrl": "https://en.wikipedia.org/wiki/Adobe_Inc.#2013_security_breach"
    }
  ],
  "searchedAt": "2026-08-08T03:40:00"
}
```

### 2. Password Check (k-Anonymity)
`POST /api/password/check`
**Request Body**:
```json
{
  "password": "password123"
}
```

**Response `200 OK`**:
```json
{
  "sha1Prefix": "CBFDA",
  "sha1SuffixMasked": "****2A97",
  "pwnCount": 4539120,
  "isExposed": true,
  "entropyScore": 32.5,
  "strengthRating": "WEAK",
  "timestamp": "2026-08-08T03:40:00"
}
```
