node {
    def WORKSPACE = "/.jenkins/workspace/docker-jenkins-pipeline"
    def dockerImageTag = "docker-jenkins-pipeline${env.BUILD_NUMBER}"

    try{
         stage('Clone Repo') {
            git url: '',
                credentialsId: '###',
                branch: 'master'
         }
          stage('Build Docker') {
                 dockerImage = docker.build("docker-jenkins-pipeline:${env.BUILD_NUMBER}")
          }

          stage('Deploy Docker'){
                  echo "Docker Image Tag Name: ${dockerImageTag}"
                  sh "docker stop docker-jenkins-pipeline || true && docker rm docker-jenkins-pipeline || true"
                  sh "docker run --name docker-jenkins-pipeline -d -p 8081:8081 docker-jenkins-pipeline:${env.BUILD_NUMBER}"
          }

    } catch(e) {
        throw e
    } finally {

    }
}

