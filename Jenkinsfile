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

        stage("Publish JAR File to Nexus") {
            steps {
                script {
                    gv.publishToNexus()
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