pipeline {

    agent {
        docker {
            image 'maven:3.9-eclipse-temurin-17'
            args '-v /root/.m2:/root/.m2'
        }
    }

    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'
    }

    stages {

        stage('Initialize') {
                    steps {
                        echo 'Initializing Docker environment...'
                        // This only applies if you defined a Docker tool manually in Jenkins
                        script {
                            def dockerHome = tool 'myDocker'
                            env.PATH = "${dockerHome}/bin:${env.PATH}"
                        }
                    }
        }

        stage('Checkout') {
            steps {
                echo 'Fetching latest code from Git...'
                checkout scm
            }
        }

        stage('Install Playwright Browsers') {
            steps {
                echo 'Installing Playwright browsers...'
                sh 'npx playwright install --with-deps || true'
            }
        }

        stage('Run Tests') {
            steps {
                echo 'Running Playwright automated tests...'
                sh 'mvn clean test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo 'Generating Allure report...'
                allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }
    }

    post {
        success {
            echo 'All tests passed successfully!'
        }
        failure {
            echo 'Some tests failed. Check reports in Jenkins.'
        }
    }
}