### Java Application Development

### Purpose of the Application

The main goal of this application is to provide an easy-to-use, efficient, and visually appealing scheduling desktop app with a graphical user interface.

### Author Details

Antonio Jenkins

ajen474@wgu.edu

Application version: 1.0

Date: 04/15/2023

### IDE and Java Module Details
IntelliJ IDEA 2021.1.3 (Community Edition)
Build #IC-211.7628.21, built on June 30, 2021
Runtime version: 11.0.11+9-b1341.60 amd64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
Windows 10 10.0
GC: G1 Young Generation, G1 Old Generation
Memory: 768M
Cores: 4

Kotlin: 211-1.4.32-release-IJ7628.19

### Executing the Program
When starting the program, users will be greeted with a login screen, where they need to enter a valid username and password that matches the information saved in a MySQL database. This software requires Java 17 and hasn't been tested with other Java Virtual Machines.

### Supplementary Report
In the tailored report, I decided to highlight the total number of appointments for every country. I used SQL to handle all the tasks, which led to a query that brings together three tables and displays two columns: "Country" and "Count". Even though the query is a bit complicated, it made things easier on the Java side, allowing the data to be neatly displayed in a table format.


#### JavaFX: javafx-sdk-17.0.1
#### MySQL Connector: mysql-connector-java-8.0.25
