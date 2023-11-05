def pushToNexus() {
    echo "Pushing the JAR file to Nexus..."
    sh "mvn clean deploy -DskipTests"
}

def buildImage() {
    echo "Building the Docker Image..."
    withCredentials([usernamePassword(credentialsId: "Docker_Hub", passwordVariable: "PASS", usernameVariable: "USER")]) {
        sh "docker build -t oumaymacharrad/my-repo:pet-store-app-${IMAGE_VERSION} ."
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh "docker push oumaymacharrad/my-repo:pet-store-app-${IMAGE_VERSION}"
    }
}

return this