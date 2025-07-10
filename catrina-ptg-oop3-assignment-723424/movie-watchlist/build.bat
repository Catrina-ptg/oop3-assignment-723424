@echo off
echo Building Spring Boot app with Maven...
mvn clean package -DskipTests

IF %ERRORLEVEL% EQU 0 (
  echo Build successful. Find your JAR in the 'target' folder.
) ELSE (
  echo Build failed. Please check the Maven output for details.
)
