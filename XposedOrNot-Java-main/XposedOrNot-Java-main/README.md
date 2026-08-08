<p align="center">
  <a href="https://xposedornot.com">
    <img src="https://xposedornot.com/static/logos/xon.png" alt="XposedOrNot" width="200">
  </a>
</p>

<h1 align="center">xposedornot-java</h1>

<p align="center">
  Official Java SDK for the <a href="https://xposedornot.com">XposedOrNot</a> API<br>
  <em>Check if your email has been exposed in data breaches</em>
</p>

<p align="center">
  <a href="https://central.sonatype.com/artifact/com.xposedornot/xposedornot"><img src="https://img.shields.io/maven-central/v/com.xposedornot/xposedornot.svg" alt="Maven Central"></a>
  <a href="https://opensource.org/licenses/MIT"><img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License: MIT"></a>
  <a href="https://www.oracle.com/java/"><img src="https://img.shields.io/badge/Java-11%2B-blue.svg" alt="Java Version"></a>
</p>

---

> **Note:** This SDK uses the free public API from [XposedOrNot.com](https://xposedornot.com) - a free service to check if your email has been compromised in data breaches. Visit the [XposedOrNot website](https://xposedornot.com) to learn more about the service and check your email manually.

---

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [API Reference](#api-reference)
  - [Email Breach Check](#emailcheckemail)
  - [Email Breach Check (Detailed)](#emailcheckdetailedemail)
  - [List Breaches](#breacheslist)
  - [Domain Breach Monitoring](#breachesdomainbreaches)
  - [Breach Analytics](#emailgetanalyticsemail)
  - [Password Check](#passwordcheckpassword)
- [Error Handling](#error-handling)
- [Rate Limits](#rate-limits)
- [Configuration](#configuration)
- [Contributing](#contributing)
- [License](#license)
- [Links](#links)

---

## Features

- **Simple API** - Fluent builder pattern and endpoint-based method grouping
- **Comprehensive Coverage** - Email breach checks, breach listings, analytics, and password exposure
- **Plus API Support** - Optional API key for detailed breach information
- **Error Handling** - Typed exception classes for every error scenario
- **Configurable** - Timeout, retries, custom headers, and base URL overrides
- **Secure** - HTTPS enforced, input validation, k-anonymity for password checks
- **Lightweight** - Built on Java's built-in `HttpClient` with minimal dependencies

## Installation

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("com.xposedornot:xposedornot:1.1.0")
}
```

### Maven

```xml
<dependency>
    <groupId>com.xposedornot</groupId>
    <artifactId>xposedornot</artifactId>
    <version>1.1.0</version>
</dependency>
```

## Requirements

- Java 11 or higher

## Quick Start

```java
import com.xposedornot.XposedOrNot;
import com.xposedornot.models.EmailBreachResponse;

try (XposedOrNot xon = XposedOrNot.builder().build()) {

    // Check if an email has been breached
    EmailBreachResponse result = xon.email().check("test@example.com");

    if (!result.getBreachNames().isEmpty()) {
        System.out.println("Email found in " + result.getBreachNames().size() + " breaches:");
        result.getBreachNames().forEach(b -> System.out.println("  - " + b));
    } else {
        System.out.println("Good news! Email not found in any known breaches.");
    }
}
```

## API Reference

### Constructor

```java
XposedOrNot xon = XposedOrNot.builder()
    .timeout(Duration.ofSeconds(15))
    .maxRetries(2)
    .build();
```

See [Configuration](#configuration) for all builder options.

### Methods

#### `email().check(email)`

Check if an email address has been exposed in any data breaches using the free API.
Pass `true` as the second argument to request detailed breach information.

```java
EmailBreachResponse result = xon.email().check("user@example.com");
// result.getBreachNames() -> List<String>
// result.getEmail()       -> String
// result.getStatus()      -> String

EmailBreachResponse detailed = xon.email().check("user@example.com", true);
```

#### `email().checkDetailed(email)`

Check an email using the Plus API with detailed breach information. Requires an API key.

```java
XposedOrNot xon = XposedOrNot.builder()
    .apiKey("your-api-key")
    .build();

EmailBreachDetailedResponse result = xon.email().checkDetailed("user@example.com");
```

#### `breaches().list()`

Get a list of all known data breaches.

```java
List<BreachInfo> breaches = xon.breaches().list();
```

#### `breaches().listByDomain(domain)`

Filter breaches by domain.

```java
List<BreachInfo> adobeBreaches = xon.breaches().listByDomain("adobe.com");
```

#### `breaches().listByBreachId(breachId)`

Fetch a specific breach by its ID.

```java
List<BreachInfo> adobe = xon.breaches().listByBreachId("Adobe");
```

**`BreachInfo` properties:** `breachID`, `breachedDate`, `domain`, `industry`, `exposedData`, `exposedRecords`, `verified`, and more.

#### `breaches().domainBreaches()`

Get breach information for domains verified against your API key. Requires an API key
with domains verified at the [CXO dashboard](https://xposedornot.com/dashboard).

```java
XposedOrNot xon = XposedOrNot.builder()
    .apiKey("your-api-key")
    .build();

DomainBreachesResponse result = xon.breaches().domainBreaches();
// result.getBreachesDetails()    -> List<DomainBreachDetail> (email, domain, breach)
// result.getYearlyMetrics()      -> Map<String, Object>
// result.getDomainSummary()      -> Map<String, Object>
// result.getBreachSummary()      -> Map<String, Object>
// result.getTop10Breaches()      -> Map<String, Object>
// result.getDetailedBreachInfo() -> Map<String, Object>
```

#### `email().getAnalytics(email)`

Get detailed breach analytics for an email address, including breach metrics and summaries.
Pass an optional token as the second argument to access sensitive breach data.

```java
BreachAnalyticsResponse analytics = xon.email().getAnalytics("user@example.com");
// analytics.getBreachNames()    -> List<String>
// analytics.getBreachesCount()  -> int
// analytics.getExposuresCount() -> int
// analytics.getFirstBreach()    -> String
// analytics.getPastesCount()    -> int

BreachAnalyticsResponse sensitive = xon.email().getAnalytics("user@example.com", "your-token");
```

#### `password().check(password)`

Check if a password has been exposed in any known data breach. Uses k-anonymity: the password is hashed locally with Keccak-512 and only the first 10 hex characters are sent to the API.

```java
PasswordCheckResponse result = xon.password().check("password123");
```

## Error Handling

The library provides typed exception classes for different failure scenarios:

```java
import com.xposedornot.XposedOrNot;
import com.xposedornot.exceptions.*;

try (XposedOrNot xon = XposedOrNot.builder().build()) {
    var result = xon.email().check("invalid-email");
} catch (ValidationException e) {
    System.err.println("Invalid input: " + e.getMessage());
} catch (RateLimitException e) {
    System.err.println("Rate limited: " + e.getMessage());
} catch (NetworkException e) {
    System.err.println("Network error: " + e.getMessage());
} catch (AuthenticationException e) {
    System.err.println("Authentication failed: " + e.getMessage());
} catch (XposedOrNotException e) {
    System.err.println("API error: " + e.getMessage() + " (code: " + e.getStatusCode() + ")");
}
```

### Exception Types

| Exception Class | Description |
|-----------------|-------------|
| `XposedOrNotException` | Base exception class for all errors |
| `ValidationException` | Invalid input (e.g., malformed email, empty password) |
| `RateLimitException` | API rate limit exceeded (HTTP 429) |
| `NotFoundException` | Resource not found (HTTP 404) |
| `AuthenticationException` | Authentication failed (invalid or missing API key) |
| `NetworkException` | Network connectivity issues |
| `ApiException` | General API or response parsing error |

## Rate Limits

The XposedOrNot API has the following rate limits:

- 2 requests per second
- 50-100 requests per hour
- 100-1000 requests per day

The client includes automatic retry with exponential backoff for `429` responses.

## Configuration

Use the builder pattern to configure the client:

```java
XposedOrNot xon = XposedOrNot.builder()
    .apiKey("your-api-key")          // API key for Plus API access
    .timeout(Duration.ofSeconds(15)) // HTTP request timeout
    .maxRetries(5)                   // Retry count on 429 responses
    .baseUrl("https://...")          // Override free API base URL
    .plusBaseUrl("https://...")       // Override Plus API base URL
    .passwordBaseUrl("https://...")   // Override password API base URL
    .header("X-Custom", "value")     // Add custom header to all requests
    .build();
```

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `apiKey` | `String` | `null` | API key for Plus API access |
| `timeout` | `Duration` | `30s` | HTTP request timeout |
| `maxRetries` | `int` | `3` | Max retries on 429 responses |
| `baseUrl` | `String` | `https://api.xposedornot.com` | Free API base URL |
| `plusBaseUrl` | `String` | `https://plus-api.xposedornot.com` | Plus API base URL |
| `passwordBaseUrl` | `String` | `https://passwords.xposedornot.com/api` | Password API base URL |
| `header` | `String, String` | none | Custom headers for all requests |

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Setup

```bash
# Clone the repository
git clone https://github.com/XposedOrNot/XposedOrNot-Java.git
cd XposedOrNot-Java

# Build
./gradlew build

# Run tests
./gradlew test
```

## License

MIT - see the [LICENSE](LICENSE) file for details.

## Links

- [XposedOrNot Website](https://xposedornot.com)
- [API Documentation](https://xposedornot.com/api_doc)
- [Maven Central](https://central.sonatype.com/artifact/com.xposedornot/xposedornot)
- [GitHub Repository](https://github.com/XposedOrNot/XposedOrNot-Java)
- [XposedOrNot API Repository](https://github.com/XposedOrNot/XposedOrNot-API)

---

<p align="center">
  Made with care by <a href="https://xposedornot.com">XposedOrNot</a>
</p>
