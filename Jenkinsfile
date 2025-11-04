pipeline {

    agent {
        docker {
            image 'mcr.microsoft.com/playwright/java:v1.48.0-noble'
            args '-v /root/.m2:/root/.m2 --shm-size=2g'
        }
    }

    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Fetching latest code from Git...'
                checkout scm
            }
        }

        stage('Run Tests') {
            steps {
                echo 'Running Playwright automated tests...'
                sh '''
                    mvn clean test \
                    -Dsurefire.useFile=false \
                    -DforkCount=1 \
                    -DreuseForks=false
                '''
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
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