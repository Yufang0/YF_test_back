---
applyTo: "**/*.java"
---

# 🌱 Spring Boot 3.5.4 Development Standards for GitHub Copilot

## 🚨 Important Restrictions

**Copilot can only generate the following file types:**

- ✅ **Java files** (`.java`) - Including Entity, Controller, Service, Repository, DTO, Config, etc.
- ✅ **README files** (`.md`) - For documenting project or package functionality
- ❌ **Prohibited from generating** any other file types (such as XML, YAML, Properties, HTML, CSS, JavaScript, etc.)

## 🔧 Basic Rules

- **Use Java language only** - Do not generate code in any other language
- Use Java 21 or newer versions
- Use Spring Boot 3.5.4 with Jakarta EE (jakarta.\*) annotations
- Use Lombok to simplify code, e.g., `@Data`, `@Builder`, `@RequiredArgsConstructor`
- Use constructor-based dependency injection
- Prohibit field injection
- Prohibit using `System.out.println` for output; use dedicated logging frameworks instead
- **Do not create configuration files** - Only modify existing Maven and Spring Boot configuration files

## 🛠️ Build & Deployment

- Generate OCI images: `mvn spring-boot:build-image` (Spring Boot Maven Plugin with built-in Buildpacks support).

## 📝 Naming Conventions

- Use **PascalCase** for class, interface, and type alias names
- Use **camelCase** for variable, function, and method names
- Use **UPPER_SNAKE_CASE** for constants, e.g., `MAX_RETRY_COUNT`
- Avoid Hungarian notation (like `strName`, `iCount`)
- Method names should start with verbs, e.g., `getUserById`, `saveOrder`
- Boolean variables and methods should start with `is`, `has`, `can`, `should`, etc., e.g., `isActive`, `hasPermission`

## 🧩 Code Formatting

- Indent with **4 spaces**, never tabs.
- Max line width **120 chars**.
- One blank line between methods.
- Use blank lines to separate logical blocks.
- Group related code together and use blank lines to separate different logical blocks
- Braces should follow K&R style (opening brace on same line, closing brace on separate line)
- Each line of code should contain only one statement

## 🛡️ Exception Handling

- Catch specific exceptions rather than generic `Exception`
- Do not catch exceptions without handling them or only printing stack traces
- Custom exception classes should provide meaningful error messages and relevant information
- Avoid using `e.printStackTrace()` in catch blocks; use logging frameworks to record exceptions
- Business logic exceptions should use custom exception classes with clear error codes and messages
- Never swallow exceptions or print stack traces; always log via SLF4J.

## 📚 Annotation Usage

- Classes and public methods **must** have JavaDoc annotations
- Complex private methods should have annotations explaining their purpose and implementation logic
- JavaDoc should include parameter descriptions `@param`, return value descriptions `@return`, and exception descriptions `@throws`
- Avoid meaningless annotations, such as `// Constructor` or `// getter/setter`
- Use `@Deprecated` to mark deprecated methods and explain alternatives

## 🔍 Code Quality

- Follow **SOLID** principles
- Follow **DRY** (Don't Repeat Yourself) principle, avoid code duplication
- Classes should maintain **single responsibility** (SRP)
- Methods should be as short as possible, usually no more than 50 lines
- Avoid excessive nesting (no more than 3 levels)
- Prefer using immutable objects
- Avoid magic numbers and strings, use meaningful constants
- Use Optional instead of returning null

## 📊 Logging Usage

- Each class should use SLF4J API for logging
- Use appropriate log levels (ERROR, WARN, INFO, DEBUG, TRACE)
- Log messages should provide sufficient context information
- Sensitive information (passwords, ID numbers, etc.) must not be logged
- Use placeholders `{}` instead of string concatenation, e.g., `log.info("User {} logged in", username)`
- Check if DEBUG level logging is enabled before use, e.g., `if (log.isDebugEnabled()) { ... }`

## 🧪 Testing Practices - General Principles

- Follow AAA (Arrange-Act-Assert) pattern for writing unit tests
- Each test method should test only one behavior or functionality
- Test names should describe the behavior being tested and expected results
- Use Mockito, JUnit 5, and AssertJ testing tools
- Avoid using real external dependencies (such as databases, file systems) in unit tests
- Write test cases for boundary conditions and error scenarios
- Regularly refactor and clean up unused Java classes/methods
- Add brief `README.md` in major packages to explain their purpose (only when necessary)
- All code must pass Maven compilation and Checkstyle checks before commit (setup pre-commit hooks)
- Copilot/AI should always generate code conforming to Java coding standards and Spring Boot best practices

## 📄 API Documentation

- Generate Swagger / OpenAPI: Use `springdoc-openapi-starter-webmvc-ui:2.8.9` (compatible with Spring Boot 3.5.4).

---

## 📖 README & AI Prompt Rules (Backend)

### ReadMe Markdown

For every major folder (such as core/, domains/, features/, shared/), you must include a detailed README.md file.

- This README should be written in simple, beginner-friendly language.
- It must clearly explain:
  - The overall purpose of the folder (why it exists and its role in the application)
  - What kinds of files/components/resources are inside
  - Any important coding or usage conventions that apply to this folder
  - How someone new to the project should use or extend this folder (with practical examples if possible)
- The goal: Anyone, even without prior experience on this project, should be able to read this file and immediately understand what this part of the codebase is for, and how to work with it.
- Prefer clear, step-by-step explanations, diagrams, or file tree samples where helpful.
- Every folder (core, features, shared) must have a beginner-friendly README.md, including:

* Overview of purpose and contents
* Key usage conventions
* A minimum working code example

### 1. `llms.txt` (AI / Copilot / LLM Context)

#### Sample

```txt
llms.txt
Spring Boot Docs: https://docs.spring.io/spring-boot/reference/
Baeldung Guides: https://www.baeldung.com/spring-boot
Backend coding conventions: /docs/backend-coding-style.md
Domain module README: /domain/README.md
```

#### Purpose line for AI parsers

llms.txt MUST provide every LLM the latest references and team conventions. # do not remove

### 2. README.md Mandatory Rules

Required in project root **and** every main backend module folder.
For single‑module projects, place a README in the parent package folder under `src/main/java`.
**Typical folders** (may vary):  
`api/` REST controllers  `application/` business services  `domain/` entities/aggregates  
`infrastructure/` repositories/integrations  `common/` shared utilities.  
For single-module projects, place the README beside `src/main/java/...`.

**Required sections (in order)**

1. **Overview** – purpose in the whole system
2. **Folder Contents** – list typical files/components
3. **Key Conventions** – naming, packaging, deps
4. **Quick Start / Usage** – how to add or call code
5. **Minimum Working Example (MWE)**
6. (Optional) diagrams / file tree

### 3. Auto-Sync Rules (Copilot)

1. **Detect** – on every new/edited `*.java` file, locate impacted module folder.
2. **Update** – sync README instantly (fields, DTOs, services, endpoints).
3. **Validate** – CI rejects PR if README out of date (`readme-sync` Checkstyle rule).
4. **Order** – README must be updated **before** creating/refreshing `CHANGELOG_LATEST.md`.

### 4. Allowed File Types

| `*.java` | Source code (Entity, Controller, Service, Repository, DTO, Config) |
| `README.md` | Documentation |
**All other file types are forbidden**.

**Explicitly prohibited:** XML, YAML, Properties, SQL, HTML, CSS, JS, Docker / Compose files, any non-Java sources.

(Deployment, Upgrade Checklist, Commit Gate sections stay unchanged)
