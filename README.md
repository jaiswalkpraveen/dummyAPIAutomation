# Privilee Dummy API Test Automation

This repository contains automated test scripts using RestAssured and Java to verify functionalities of the dummyAPI.

## Test Scenarios

For each endpoint (Movies, Blog Posts, Users, Pokemon, Products), we cover the following scenarios:

1. Get All: Retrieves all items and verifies the response.
2. Create and Get: Creates a new item, then retrieves it to verify creation.
3. Create with Invalid Data: Attempts to create an item with invalid data and verifies error handling.
4. Update: Creates an item, updates it, then verifies the update.
5. Delete: Creates an item, deletes it, then verifies it's no longer retrievable.

## Assertions Used

We use various assertions to ensure the correctness of our API responses:

- Status Code Verification: Ensures correct HTTP status codes for different operations.
- Response Time Checks: Verifies that responses are received within acceptable time limits.
- JSON Structure Validation: Checks for the presence of expected keys in JSON responses.
- Data Integrity Checks: Verifies that created/updated data matches the input data.
- Error Handling: Ensures appropriate error responses for invalid inputs.
- Content Type Verification: Checks that responses have the correct content type.

## Installation

1. Ensure you have Java JDK 8 or higher installed.
2. Install Maven if not already installed.
3. Clone this repository:
`git clone https://github.com/yourusername/api-automation.git`
4. Navigate to the project directory:
`cd dummyAPIAutomation`
5. Install dependencies:
`mvn clean install`


## Running Tests

To run all tests:
`mvn test`

## To run a specific test class:
`mvn test -Dtest=MoviesTest`

## GitHub Actions

This project is configured to run tests automatically on GitHub Actions. The workflow is defined in `.github/workflows/test.yml`. It runs on every push and pull request to the main branch.

## To view the GitHub Actions runs:

1. Go to your GitHub repository.
2. Click on the "Actions" tab.
3. You will see the list of workflow runs.

## Test Reports

After running the tests, TestNG generates an HTML report. To view the report:

1. Navigate to the `target/surefire-reports` directory.
2. Open `index.html` in a web browser.

For GitHub Actions runs, you can download the test reports as artifacts from the workflow run page.

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.