# Dummy API Test Automation
This project is an API automation framework for testing RESTful dummy APIs. It uses RestAssured, TestNG, and follows the Page Object Model design pattern.

## Tools and Technologies

- Java 17
- Maven
- TestNG
- RestAssured
- ExtentReports
- Lombok

## Design Pattern

This framework follows the Page Object Model (POM) design pattern, adapted for API testing. In this context:

- Each API endpoint or group of related endpoints is represented by a separate class (similar to pages in UI testing).
- These classes contain methods for different API operations and any necessary data manipulation.
- Test classes use these "API objects" to perform tests, promoting code reusability and easier maintenance.

## Project Structure

- `src/main/java/com/dummyapi/`
  - `config/`: Configuration files and classes
  - `utils/`: Utility classes including TestReporter
  - `endpoints/`: Classes representing API endpoints (following POM)
- `src/test/java/com/dummyapi/tests/`: Test classes
- `src/test/resources/`: Test data and configuration files
- `test-output/`: Generated test reports

## Setup and Execution

1. Clone the repository
2. Install dependencies: `mvn clean install`
3. Run tests: `mvn test`


## API Endpoints Covered

- Movies API
- Blog Posts API
- Users API
- Pokemon API
- Products API

## Test Scenarios

For each endpoint (Movies, Blog Posts, Users, Pokemon, Products), we cover the following scenarios:

#### User Endpoint scenario 
1. Get All user: Retrieves all users and verifies the response.
2. Get existing single user: Retrieve single user and verifies the response.
3. Get non existing single user: Retrieve mon existing user and verifies the response.
4. Create and get user: Creates a new user, then retrieves it to verify creation.
4. Create and get user with existing userID: Creates a new user with existing ID, then retrieves it to verify error response.

#### Movie, Blog Post, Pokemon, Products Endpoint scenario 
1. Fetch All records: Retrieves all details and verifies the response.
2. Fetch single record: Retrieve single detail and verifies the response.
3. Fetch non existing single record: Retrieve mon existing detail and verifies the error response.


## Assertions Used

We use various assertions to ensure the correctness of our API responses:

- Status Code Verification: Ensures correct HTTP status codes for different operations.
- Response Time Checks: Verifies that responses are received within acceptable time limits.
- JSON Structure Validation: Checks for the presence of expected keys in JSON responses.
- Data Integrity Checks: Verifies that created/existing data matches the input data.
- Error Handling: Ensures appropriate error responses for invalid inputs.
- Content Type Verification: Checks that responses have the correct content type.

## CI/CD (GitHub Actions)

This project is configured to run on GitHub Actions. The workflow is defined in `.github/workflows/test.yml`. It runs on every push and pull request to the main branch.


## To view the GitHub Actions runs:

1. Go to your GitHub repository.
2. Click on the "Actions" tab.
3. You will see the list of workflow runs.

## Test Reports

After running the tests, TestNG generates an HTML report. To view the report:

1. Navigate to the `test-output/` directory.
2. Open `ExtentReport.html` in a web browser.

For GitHub Actions runs, you can download the test reports as artifacts from the workflow run page.


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
