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
        stage('Deploy'){
            steps {
                echo "Iniciando Simulacro de Despliegue (Continuos Delivery)"
                echo "Tomando el Artefacto inmutable (.jar) de la bóveda..."
                echo "Enviando al servidor de Produccion"
                sh 'sleep 5'
                echo "Despliegue Exitoso El Motor de Decisiones está en línea"
            }
        }
    }
        post{
            success{
                archiveArtifacts artifacts: 'target/*.jar',fingerprint: true

        }
    }
}