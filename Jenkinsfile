pipeline{
    agent any

    stages{
        stage('Build Backend'){
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
    }
}