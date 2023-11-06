#!/usr/bin/env groovy

def gv

pipeline {
    agent any
    environment {
        SONARQUBE_IP ="192.168.1.19"
        SONARQUBE_USER="admin"
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

        stage("Build JAR") {
            steps {
                script {
                    gv.buildJar()
                }
            }
        }

        stage("SonarQube Testing and Scan") {
            steps {
                script {
                    gv.sonarScan("${SONARQUBE_IP}","${SONARQUBE_USER}")
                }
            }
        }

        stage("Test") {
            steps {
                script {
                    gv.test()
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
    }
}