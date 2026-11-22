pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        timeout(time: 30, unit: 'MINUTES')
    }

    triggers {
        pollSCM('H/5 * * * *')
    }

    environment {
        REGISTRY = 'localhost:5000'
        IMAGE    = "${REGISTRY}/smartcampus"
        TAG      = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package --no-transfer-progress'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test --no-transfer-progress'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker build') {
            steps {
                sh 'docker build -t $IMAGE:$TAG -t $IMAGE:latest .'
            }
        }

        stage('Security scan (Trivy)') {
            steps {
                sh '''
                    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
                      aquasec/trivy:0.51.1 image --severity HIGH,CRITICAL --ignore-unfixed \
                      --exit-code 0 $IMAGE:$TAG
                '''
            }
        }

        stage('Push') {
            steps {
                sh 'docker push $IMAGE:$TAG && docker push $IMAGE:latest'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    docker rm -f smartcampus-jenkins || true
                    docker run -d --name smartcampus-jenkins -p 8090:8080 \
                      -e SPRING_PROFILES_ACTIVE=demo $IMAGE:$TAG
                '''
            }
        }

        stage('Smoke test') {
            steps {
                sh '''
                    for i in $(seq 1 30); do
                      if docker run --rm --network container:smartcampus-jenkins curlimages/curl:8.7.1 \
                           -fs http://localhost:8080/actuator/health | grep -q UP; then
                        echo "deployment healthy"; exit 0
                      fi
                      sleep 5
                    done
                    docker logs smartcampus-jenkins | tail -50
                    exit 1
                '''
            }
        }
    }

    post {
        failure {
            echo 'Pipeline failed'
        }
    }
}
