def pushToNexus() {
    echo "Pushing the jar file to Nexus..."
    sh 'mvn clean deploy -DskipTests'
}