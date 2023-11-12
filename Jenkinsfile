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

        stage("Publish JAR file to Nexus") {
            steps {
                script {
                    gv.publishToNexus()
                }
            }
        }
    }
}