def compile() {
    echo "Compiling..."
    sh "mvn clean compile -DskipTests"
}

def buildJar() {
    echo "Building the JAR file..."
    sh "mvn package -DskipTests"
}

def test() {
    echo "Running JUnit Tests..."
    sh "mvn test"
}

def dependencyCheck() {
    echo "Checking third-party dependencies using Dependency-Check..."
    dependencyCheck additionalArguments: "--scan ./ --format XML", odcInstallation: "Dependency-Check-8.4.3"
    dependencyCheckPublisher pattern: "**/dependency-check-report.xml"
}

def sonarScan() {
    echo "Running SonarQube Scanner..."
    withSonarQubeEnv() {
        sh "mvn verify sonar:sonar -Dsonar.projectKey=pet_store_pipeline -Dsonar.projectName=pet_store_pipeline"
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

def trivyScan(){
    echo "Running Trivy Security Scan..."
    sh "trivy image oumaymacharrad/pet-store-app:${IMAGE_VERSION} --scanners vuln"
}

def jmeterTests(){
    echo "Running Performance Tests..."
    sh "jmeter -n -t src/main/resources/Test_Plan.jmx -l src/main/resources/Test_Plan_Results.jtl"
    perfReport filterRegex: "", showTrendGraphs: true, sourceDataFiles: "src/main/resources/Test_Plan_Results.jtl"
}

def deploy() {
    echo "Deploying the application using Kubernetes..."
    kubeconfig(credentialsId: "Kubernetes-Credentials", serverUrl: "192.168.49.2") {
        sh "kubectl create ns pet-store --dry-run=client"
        sh "kubectl apply -f deployment-pet-store.yaml"
        sh "kubectl apply -f service-pet-store.yaml"
        sh "kubectl create ns mysql --dry-run=client"
        sh "kubectl apply -f deployment-mysql.yaml"
        sh "kubectl apply -f service-mysql.yaml"
        sh "kubectl apply -f pvc.yaml"
        sh "kubectl get nodes"
        sh "kubectl get deployments"
        sh "kubectl get pods"
        sh "kubectl get services"
    }
}

return this