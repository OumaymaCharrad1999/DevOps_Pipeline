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
        sh "mvn verify sonar:sonar -Dsonar.projectKey=pet_store_pipeline_prod -Dsonar.projectName=pet_store_pipeline -Dsonar.token=sqp_8ea72241756816953d65e260ba4430a159c5f9d7"
    }
}

def pushToNexus() {
    echo "Pushing the JAR file to Nexus Repository Manager..."
    nexusArtifactUploader artifacts: [[artifactId: "devops-pipeline", classifier: "", file: "/var/jenkins_home/workspace/Pet Store Pipeline/target/devops-pipeline-0.0.1-SNAPSHOT.jar", type: "jar"]], 
        credentialsId: "Nexus-Credentials", groupId: "com.example", nexusUrl: "192.168.8.100:8081", nexusVersion: "nexus3", protocol: "http",
        repository: "maven-snapshots", version: "0.0.1-SNAPSHOT"
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

def deployToDevEnv() {
    echo "Deploying the application to Develop Environment using Kubernetes..."
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

def trivyScan(){
    echo "Running Trivy Security Scan..."
    sh "trivy image --format template --template '@/usr/local/share/trivy/templates/html.tpl' -o TrivyReport.html oumaymacharrad/pet-store-app:${IMAGE_VERSION} --scanners vuln"
}

def jmeterTests(){
    echo "Running Performance Tests..."
    sh "jmeter -n -t src/test/resources/jmeter/Test_Plan.jmx -l src/test/resources/jmeter/Test_Plan.jtl"
    perfReport filterRegex: "", showTrendGraphs: true, sourceDataFiles: "src/test/resources/jmeter/Test_Plan.jtl; target/surefire-reports/TEST-*.xml"
}

def deployToProdEnv() {
    input "Do you approve deployment to production environment ?"
    echo "Deploying the application to Production Environment using Kubernetes..."
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