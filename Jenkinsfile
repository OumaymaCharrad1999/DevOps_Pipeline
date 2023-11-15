#!/usr/bin/env groovy

def gv

pipeline {
    agent any

    environment {
        IMAGE_VERSION="latest"
    }

    tools {
        maven "Maven-3.9.5"
    }

    stages {
        stage("Initialize") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }

        stage("Compile") {
            steps {
                script {
                    gv.compile()
                }
            }
        }

        stage("Build JAR") {
            steps {
                script {
                    gv.buildJar()
                }
            }
        }

        stage("Unit Tests") {
            steps {
                script {
                    gv.test()
                }
            }
        }

        stage("Dependency-Check") {
            steps {
                script {
                    gv.dependencyCheck()
                }
            }
        }

        stage("Checkmarx") {
            steps {
                script {
                    gv.checkmarx()
                }
            }
        }

        stage("SonarQube Analysis") {
            steps {
                script {
                    gv.sonarScan()
                }
            }
        }

        stage("Build & Push Docker Image to Docker Hub") {
            steps {
                script {
                    gv.buildImage()
                }
            }
        }

        stage("Deploy to Minikube Cluster") {
            steps {
                script {
                    gv.deploy()
                }
            }
        }
    }

}