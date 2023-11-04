#!/usr/bin/env groovy

def gv

pipeline {
    agent any
    environment {
        IMAGE_VERSION="1.0.0"
    }
    tools {
        maven 'maven'
    }

    stages {
        stage("Initialize") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }

        stage('Increment version') {
            steps {
                script {
                    echo 'Incrementing app version...'
                    sh 'mvn build-helper:parse-version versions:set \
                        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                        versions:commit'
                    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
                    def version = matcher[0][1]
                    env.IMAGE_VERSION = "$version-$BUILD_NUMBER"
                }
            }
        }

        stage("Push JAR to Nexus") {
            steps {
                script {
                    gv.pushToNexus()
                }
            }
        }
    }
}