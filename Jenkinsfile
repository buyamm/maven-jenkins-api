pipeline{
    agent any

    tools{
        maven 'my-maven'
    }

    environment{
        MYSQL_ROOT_LOGIN = credentials('mysql-root-login')
    }

    stages{
        stage('Build with Maven'){
            steps{
                sh 'java --version'
                sh 'mvn --version'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Packaging and Pushing image'){
            steps{
                withDockerRegistry(credentialsId: 'dockerhub', url: 'https://registry.hub.docker.com'){
                    sh 'docker build -t truongcongly/maven-jenkins-api .'
                    sh 'docker push truongcongly/maven-jenkins-api'
                }
            }
        }

        stage('Deploy MYSQL'){
            steps{
                script {
                    def imageExists = sh(script: "docker images -q mysql:8.0", returnStdout: true).trim()
                        if (!imageExists) {
                            echo "mysql:8.0 image not found, pulling from Docker Hub..."
                            sh 'docker pull mysql:8.0'
                        } else {
                            echo "mysql:8.0 image already exists, skipping pull."
                        }
                }
                sh 'docker network create dev || echo "this network existed"'

                sh 'docker run --name truongcongly-mysql --network dev -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_LOGIN_PSW} -e MYSQL_DATABASE=maven_jenkins_api -d mysql:8.0'
            }
        }

        stage('Deploy Application Spring Boot'){
            steps{
                sh 'docker pull truongcongly/maven-jenkins-api'
                sh 'docker stop truongcongly/maven-jenkins-api || echo "this container does not exist" '
                sh 'echo y | docker container prune '

                sh 'docker run -d --name truongcongly/maven-jenkins-api -p 8081:8081 --network dev truongcongly/maven-jenkins-api'
            }
        }
    }

    post{
        always{
            cleanWs()
        }

        success{
            echo 'Deploy successful - by TruongCongLy'
        }

        failure{
            echo 'Deploy failed! - by TruongCongLy'
        }
    }
}
