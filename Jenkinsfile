pipeline{
    agent any

    stages{
        stage('Build Backend'){
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }
        stage('Test'){
            steps{
                sh './mvnw test'
            }
        }
    }
}