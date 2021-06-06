def call(Map config = [:]){
    pipeline {
    agent any 
    stages {
        stage ('deploy terraform') {
            steps { 
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: "jenkins-terraform-user",
                    accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                    secretKeyVariable: "AWS_SECRET_ACCESS_KEY"
                ]]){
                    sh 'terraform init -force-copy'
                    sh 'terraform plan -var ${config.access_key}=${AWS_ACCESS_KEY_ID} -var ${config.secret_key}=${AWS_SECRET_ACCESS_KEY} -out Outputforplan'
                    sh 'terraform apply -input=false Outputforplan'
                }
                }
            }
        }
    }
}