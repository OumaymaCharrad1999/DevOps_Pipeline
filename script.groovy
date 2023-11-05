def pushToNexus() {
    echo "Pushing the JAR file to Nexus..."
    sh 'mvn clean deploy -DskipTests'
}

return this