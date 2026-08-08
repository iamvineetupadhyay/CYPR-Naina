# ETHICAL SCRAPING & COMPLIANCE GUIDE

## 1. Ethical Principles & Zero Dark-Web Policy

AegisBreach enforces a strict **Zero Dark Web** policy:

1. **No Access to Stolen Credentials**: The system does NOT connect to Tor onion services, illegal Telegram leak channels, or dark web marketplaces selling stolen user data.
2. **Authorized Public Disclosures Only**: The scraper exclusively parses publicly published security advisories, Wikipedia historical data breach summaries, CISA known exploited vulnerability catalogs, and open vendor disclosures.
3. **No Storage of Plaintext Secrets**: The database stores aggregate metadata (breach entity name, breach year, victim count, exposed parameter categories). Individual plaintext passwords or raw credential dumps are **never stored**.

---

## 2. Web Scraper Implementation Mechanics

The Java JSoup Scraper (`PublicBreachScraperService.java`) follows compliant web crawling standards:

- **User-Agent Header**: `Mozilla/5.0 (Windows NT 10.0; Win64; x64) EthicalBreachMonitor/1.0`
- **Rate-Limiting & Polling Intervals**: Maximum 1 request every 10 seconds to prevent server overload.
- **De-duplication**: Checks title and domain against H2 database to avoid redundant storage.

---

## 3. k-Anonymity Privacy Guarantee

The Have I Been Pwned (HIBP) k-Anonymity model ensures 100% user privacy:
- A mathematical property ensuring that a target entity cannot be distinguished from at least $k-1$ other entities in a dataset.
- By sending only $5$ hex characters ($16^5 = 1,048,576$ possibilities), the API server receives a bucket containing thousands of hash suffixes and has zero knowledge of which specific password the user is testing.
