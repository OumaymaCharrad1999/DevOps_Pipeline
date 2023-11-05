#!/usr/bin/env groovy

def gv

pipeline {
    agent any
    environment {
        SONARQUBE_SERVER_IP ="192.168.1.19:9000"
        SONARQUBE_SERVER_USER="admin"
        NEXUS_SERVER_IP ="192.168.1.19:8081"
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

        stage("Publish JAR file to Nexus") {
            steps {
                script {
                    gv.publishToNexus("${NEXUS_SERVER_IP}")
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