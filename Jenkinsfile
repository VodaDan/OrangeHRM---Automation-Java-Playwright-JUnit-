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
                    # Get host machine IP from container's perspective
                    HOST_IP=$(ip route | grep default | awk '{print $3}')
                    echo "Detected host IP: $HOST_IP"

                    # Replace localhost with host IP in all Java test files
                    # This will match: http://localhost/orangehrm-5.7/
                    find src/test -type f -name "*.java" -exec sed -i "s|http://localhost/orangehrm-5.7|http://$HOST_IP/orangehrm-5.7|g" {} +

                    # Verify replacement worked (optional, for debugging)
                    echo "Sample after replacement:"
                    grep -r "http://$HOST_IP/orangehrm-5.7" src/test | head -3 || echo "No matches found"

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