### Author Details
Antonio Jenkins
Application version: 1.0
Date: 04/15/2023

### Purpose of the Application

The primary objective of this application is to offer a user-friendly, GUI-based scheduling desktop application.

### IDE and Java Module Details
IntelliJ IDEA 2021.2.3 (Community Edition)
Build #IC-212.5457.46, assembled on October 12, 2021
Runtime version: 11.0.12+7-b1504.40 aarch64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o. Registry: documentation.show.toolbar=true
Non-Bundled Plugins: com.intellij.javafx (1.0.3)

#### JavaFX: openjfx-11.0.2
#### MySQL Connector: mysql-connector-java-8.0.26

### Supplementary Report
For the customized report, I opted to showcase the number of appointments per country. SQL was employed to perform all tasks, resulting in a query that merges three tables and outputs two columns (Country and count). Although the query is intricate, it simplified the Java side, allowing the data to be easily displayed in a table view format.

### Executing the Program
Upon launching the program, users will encounter a login screen, requiring a valid username and password that corresponds to information stored in a MySQL database. This program necessitates Java 11 and has not been assessed with other JVMs.
