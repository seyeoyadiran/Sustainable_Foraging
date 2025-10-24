# 🌿 Foraging Tracker

A Spring-based Java console application for tracking, managing, and reporting foraging activities. Users can record which foragers collected which items, how much was collected, and generate reports on foraging data.

## 📖 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Features](#-features)
- [Technologies Used](#-technologies-used)
- [Example Workflow](#-example-workflow)
- [Educational Value](#-educational-value)
- [Possible Extensions](#-possible-extensions)
- [Directory Structure](#-directory-structure)
- [Getting Started](#-getting-started)

## 🧭 Overview

The Foraging Tracker is a file-based data management system built with Spring Core. It allows users to:

- Add and manage Foragers (people)
- Add and manage Items (things collected)
- Record Forages (when a forager collects an item)
- View Reports on kilograms collected and total value by category

The application stores data in local .csv and .txt files under a structured directory, without using a database.

## 🧩 Architecture

The application follows a clean, layered architecture inspired by MVC and Domain-Driven Design.


### 1. UI Layer (`learn.foraging.ui`)
- Handles all console interactions
- Uses ConsoleIO for input/output
- View formats menus and displays data
- The Controller coordinates between the user interface and service layer

### 2. Service Layer (`learn.foraging.domain`)
- Contains core business logic and validation
- Ensures valid data before persistence
- Performs calculations and enforces rules (e.g., no duplicate forages, valid kilogram ranges)
- Example services:
  - ForageService
  - ForagerService
  - ItemService

### 3. Data Layer (`learn.foraging.data`)
- Manages file-based persistence
- Each forage date has its own CSV file



- Repositories:
  - ForagerFileRepository
  - ItemFileRepository
  - ForageFileRepository

### 4. Model Layer (`learn.foraging.models`)
- Simple POJOs representing the domain:
  - **Forager** — id, first name, last name, state
  - **Item** — id, name, category, price per kilogram
  - **Forage** — connects a forager, item, date, and kilograms
  - **Category** — enum of item categories (e.g., EDIBLE, MEDICINAL)

### 5. Application Initialization
- Spring manages dependency injection with annotations:
  - `@Component`
  - `@Service`
  - `@Repository`
  - `@Autowired`
- The main entry point (`App.java`) initializes Spring context and starts the interactive console menu via:
  - `AnnotationConfigApplicationContext(AppConfig.class)`

## ✅ Features

- Add, view, and manage foragers and items
- Record forages and store them by date
- Generate reports:
  - Kilograms collected per item
  - Total value collected by category
- Input validation with clear console feedback
- File-based storage for persistent data
- Modular design for easy testing and maintenance

## ⚙️ Technologies Used

| Technology | Purpose |
|------------|---------|
| Java 17+ | Core language |
| Spring Framework (Core) | Dependency Injection & Bean Management |
| File I/O (BufferedReader, PrintWriter) | Persistent storage in CSV files |
| Streams & Collectors | Data aggregation and filtering |
| BigDecimal | Accurate financial and weight calculations |
| UUID | Unique IDs for records |
| LocalDate & DateTimeFormatter | Date handling and validation |
| Result<T> Pattern | Validation and error encapsulation |

## 📊 Example Workflow

### Adding a Forage
1. User selects "Add a Forage"
2. App prompts for date, forager, item, and kilograms
3. ForageService validates the input:
   - Forager exists
   - Item exists
   - Kilograms are within 0–250
4. If valid, the record is saved to `data/forage_data/{date}.csv`

### Generating a Report
1. User selects "Report → Kilograms per Item"
2. User enters a date
3. The system aggregates all forages for that date by item and displays:



## 🎓 Educational Value

This project demonstrates:

- Structuring a real-world console app using clean architecture
- Applying Spring dependency injection without a web framework
- Implementing file-based persistence with validation and error handling
- Building testable and maintainable service layers

## 🚀 Possible Extensions

To enhance or modernize the application:

- Replace CSV files with a database (JDBC or Spring Data JPA)
- Add unit and integration tests (JUnit + Mockito)
- Create a REST API layer using Spring Boot
- Develop a React or JavaFX frontend
- Add CSV or PDF export for reports
- Introduce user authentication and roles

## 📂 Directory Structure
src/
├─ main/
│ ├─ java/learn/foraging/
│ │ ├─ App.java
│ │ ├─ AppConfig.java
│ │ ├─ data/
│ │ ├─ domain/
│ │ ├─ models/
│ │ ├─ ui/
│ └─ resources/
└─ test/
└─ java/learn/foraging/


## 🏁 Getting Started

**To Run the App:**

1. Clone the repository
2. Open in your IDE (e.g., IntelliJ, Eclipse, VS Code with Java extension)
3. Run `App.java`
4. Follow the console menu instructions

## 💡 Example Architecture Diagram
