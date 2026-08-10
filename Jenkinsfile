pipeline{
    agent any

    stages{
        stage('Build Backend'){
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests -Dfile.encoding=UTF-8'
            }
        }
        stage('Test'){
            steps{
                sh './mvnw test'
            }
        }
        post{
            success{
                archiveArtifacts archiveArtifacts: 'target/*.jar',fingerprint: true
            }
        }
    }
}