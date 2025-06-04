# Postgres App

A Java application for interacting with PostgreSQL database.

## Prerequisites

- Java 11 or higher (jdk 17)
- Maven (apache-maven-3.8.8)
- PostgreSQL database (pg4)
- Vs Code (with java extension)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/postgres-app.git
   ```
2. Navigate to the project directory:
   ```bash
   cd postgres-app
   ```
3. Install dependencies:
   ```bash
   mvn install
   ```

## Development

### Build and Clean

Clean and compile the project:
```bash
mvn clean compile
```

### Run the Application

Execute the main class:
```bash
mvn exec:java -Dexec.mainClass="com.galih.tugas_pbo_2.App"
```

## Usage

1. Ensure your PostgreSQL database is running.
2. Update the database connection settings in the `App.java` file.
3. Run the application using the instructions above.

## Project Structure

```
postgres-app/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── galih/
│                   └── tugas_pbo_2/
│                       └── App.java
└── pom.xml
```

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request
