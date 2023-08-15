# Empowering Spring Boot: OpenAI-Driven Error Management

## Prerequisites

Before you begin, make sure you have the following prerequisites in place:

- Java Development Kit (JDK) 17
- Git (to clone the repository)
- OpenAI API Key

## Getting Started

Follow these steps to set up and run the tutorial application:

1. **Clone the Repository**: Open your terminal and run the following command to clone the tutorial repository:
   ```bash
   git clone git@github.com:emredmrcan/tutorials.git
   ```

2. **Configure API Key**: Open the `src/main/resources/application.properties` file and add your OpenAI API key:
   ```properties
   openai.api_key=YOUR_OPENAI_API_KEY
   ```

3. **Run the Application**: In the terminal, navigate to the project directory and run the Spring Boot application using the following command:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Trigger an Error**: Open your web browser and access `http://localhost:8080/tutorial/1` to intentionally trigger an error and observe the OpenAI-powered error management in action.

## Feedback and Contributions

I value your feedback! If you encounter any issues or have suggestions to improve the tutorial, please [create an issue](https://github.com/emredmrcan/tutorials/issues) in the GitHub repository. You
can also submit pull requests if you'd like to contribute enhancements.