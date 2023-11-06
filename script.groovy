def buildJar() {
    echo "Building the JAR file..."
    sh "mvn clean package -DskipTests"
}

def test() {
    echo "Running JUnit Tests..."
    sh "mvn test"
}

def sonarScan() {
    echo "Running SonarQube Scanner..."
    sh "mvn clean verify sonar:sonar -Dsonar.projectKey=pet_store_pipeline -Dsonar.projectName='pet_store_pipeline' -DskipTests"
}

def publishToNexus(String serverIp) {
    echo "Pushing the JAR file to Nexus..."
    sh "mvn clean deploy -DskipTests"
}

def buildImage() {
    echo "Building the Docker Image..."
    withCredentials([usernamePassword(credentialsId: "Docker_Hub_Credentials", usernameVariable: "USER")]) {
        sh "docker build -t oumaymacharrad/my-repo:pet-store-app-${IMAGE_VERSION} ."
        echo "Pushing the Docker Image to Docker Hub..."
        sh "docker login -u $USER"
        sh "docker push oumaymacharrad/my-repo:pet-store-app-${IMAGE_VERSION}"
    }
}

return this