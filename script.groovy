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
    withSonarQubeEnv() {
        sh "mvn clean verify sonar:sonar -Dsonar.projectKey=pet_store_pipeline -Dsonar.projectName='pet_store_pipeline'"
    }
}

def buildImage() {
    echo "Building the Docker Image..."
    withCredentials([usernamePassword(credentialsId: "Docker-Hub-Credentials", passwordVariable: "PASS", usernameVariable: "USER")]) {
        sh "docker build -t oumaymacharrad/pet-store-app:${IMAGE_VERSION} ."
        echo "Pushing the Docker Image to Docker Hub..."
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh "docker push oumaymacharrad/pet-store-app:${IMAGE_VERSION}"
    }
}

def deploy() {
    echo "Deploying the application using Kubernetes..."
    sh "kubectl delete ns mysql"
    sh "kubectl delete ns pet-store"
    sh "kubectl create ns pet-store"
    sh "kubectl apply -f deployment_pet_store.yaml"
    sh "kubectl apply -f service_pet_store.yaml"
    sh "kubectl create ns mysql"
    sh "kubectl apply -f deployment_mysql.yaml"
    sh "kubectl apply -f service_mysql.yaml"
    sh "kubectl apply -f pvc.yaml"
    sh "kubectl get nodes"
    sh "kubectl get deployments"
    sh "kubectl get pods"
    sh "kubectl get services"
}

return this