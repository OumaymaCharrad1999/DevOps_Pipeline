#!/usr/bin/env groovy

def gv

pipeline {
    agent any

    environment {
        IMAGE_VERSION = "latest"
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

        stage("Build") {
            steps {
                script {
                    gv.buildJar()
                }
            }
        }

        stage("Test") {
            parallel {

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

                stage("SonarQube Analysis") {
                    steps {
                        script {
                            gv.sonarScan()
                        }
                    }
                }

            }
        }

        stage("Publish Docker Image to Docker Hub") {
            steps {
                script {
                    gv.buildImage()
                }
            }
        }

        stage("Trivy Scan") {
            steps {
                script {
                    gv.trivyScan()
                }
            }
        }

        stage("JMeter Tests") {
            steps {
                script {
                    gv.jmeterTests()
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

        stage("Send Email") {
            steps {
                script {
                    gv.sendEmail()
                }
            }
        }
    }

    /*post {
        always {
            junit "target/surefire-reports/*.xml"
        }
    }*/

}