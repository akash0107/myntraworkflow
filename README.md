Myntra Automation (Selenium Java TestNG)
📌 Project Overview
This project automates the "Add to Cart" functionality on Myntra using Selenium WebDriver with Java and TestNG.

📂 Project Structure
myntra_automation/
│── src/
│   ├── main/
│   │   ├── java/                # (For main application code, currently empty)
│   │   ├── resources/           # (For config/test data if needed)
│   ├── test/
│   │   ├── java/
│   │   │   ├── MyntraCartTest.java  # (Test case implementation)
│── target/                      # (Generated test artifacts)
│── pom.xml                       # (Maven dependencies)
│── .gitignore                     # (Ignored files)
│── External Libraries              # (Maven dependencies)

🛠 Prerequisites
Make sure you have the following installed:

Java 8+ (JDK)
Maven
TestNG Plugin (if using an IDE like IntelliJ or Eclipse)
Google Chrome and ChromeDriver (Ensure compatibility with the browser version)
🚀 Setup & Execution
1️⃣ Clone the Repository

git clone <repository-url>
cd myntra_automation

2️⃣ Install Dependencies

3️⃣ Run Tests
Since there's no testng.xml, you can run the test directly via:
mvn test

or from your IDE using "Run" on MyntraCartTest.java.

📝 Future Enhancements
POM implementation
Add a testng.xml file for better test execution control.
Implement logging with Log4j.
Parameterize test data using @DataProvider or external files.

