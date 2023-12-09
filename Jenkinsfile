#!/usr/bin/env groovy

def gv

pipeline {
    agent any

    environment {
        IMAGE_VERSION = "latest"
        BRANCH_NAME = "production"
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

        stage("Publish JAR file to Nexus") {
            steps {
                script {
                    gv.pushToNexus()
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

        stage("Deploy to Develop Environment") {
            steps {
                script {
                    gv.deployToDevEnv()
                }
            }
        }

        stage("Trivy Scan") {
            when {
                expression { env.BRANCH_NAME == "production" }
            }
            steps {
                script {
                    gv.trivyScan()
                }
            }
        }

        stage("JMeter Tests") {
            when {
                expression { env.BRANCH_NAME == "production" }
            }
            steps {
                script {
                    gv.jmeterTests()
                }
            }
        }

        stage("Deploy to Production Environment") {
            when {
                expression { env.BRANCH_NAME == "production" }
            }
            steps {
                script {
                    gv.deployToProdEnv()
                }
            }
        }

    }

    post {
        always {
            junit "target/surefire-reports/*.xml"
            emailext body: '''$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS: Check console output at $BUILD_URL to view the results.''',
                    subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!', to: 'charradoumayma1@gmail.com',
                    attachmentsPattern: "TrivyReport.html"
        }
    }

}
