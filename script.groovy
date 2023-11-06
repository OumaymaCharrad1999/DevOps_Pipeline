def test() {
    echo "Run JUnit Tests..."
    sh "mvn test"
}

def sonarScan(String serverIp, String serverUser) {
    echo "Running SonarQube Scanner..."
    def runSonar = '"export MYSQLDB_ROOT_PASSWORD=oumayma MYSQLDB_DATABASE=pet_store MYSQLDB_LOCAL_PORT=3306 MYSQLDB_DOCKER_PORT=3306 && bash runSonarQube.sh"'
    sshagent (credentials: ['SonarQube-Credentials']) {
        sh "ssh -o StrictHostKeyChecking=no ${serverUser}@${serverIp} ${runSonar}"
    }}

def buildJar() {
    echo "Building the JAR file..."
    sh "mvn clean package -DskipTests"
}

def publishToNexus(String serverIp) {
    echo "Pushing the JAR file to Nexus..."
    sh "mvn clean deploy -DskipTests"
}

def buildImage() {
    echo "Building the Docker Image..."
    withCredentials([usernamePassword(credentialsId: "Docker_Hub_Credentials", passwordVariable: "PASS", usernameVariable: "USER")]) {
        sh "docker build -t oumaymacharrad/my-repo:pet-store-app-${IMAGE_VERSION} ."
        echo "Pushing the Docker Image to Docker Hub..."
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh "docker push oumaymacharrad/my-repo:pet-store-app-${IMAGE_VERSION}"
    }
}

return this