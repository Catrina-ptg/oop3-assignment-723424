
#for Linux OS

#!/bin/bash
echo "⚙️  Building Spring Boot app with Maven (Java 21)..."

mvn clean package -DskipTests

# Show success or failure
if [ $? -eq 0 ]; then
  echo "✅ Build successful! JAR is in the 'target' directory."
else
  echo "❌ Build failed. Please check the output for errors."
  exit 1
fi
