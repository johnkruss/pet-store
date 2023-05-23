#!/bin/env groovy
def serverSocket = new ServerSocket(0)
def gradleArgs(branch) {
  if (branch == 'main') {
    return 'clean final build dockerBuild dockerPush artifactoryPublish'
  } else {
    return 'clean build'
  }
}

def getPackageVersion() {
  return sh(script: 'git describe --exact-match --abbrev=0', returnStdout: true).trim()
}

def getPreviousParameters() {
  copyArtifacts(projectName: "deploy-preference-service-dev")
  def params = readProperties  file: "versions.tfvars"
  def terraform_params = readProperties  file: "terraform_docker_tag.ini"
  params.put("terraform_docker_tag", terraform_params.terraform_docker_tag.toString())
  return params
}

pipeline {
  agent {
    label 'amzlnx2'
  }
  environment {
    GRADLE_ARGS = gradleArgs(env.BRANCH_NAME)
    LOCALSTACK_PORT = "${serverSocket.localPort}"
  }
  tools {
    jdk 'openjdk-17.0.1'
  }
  options {
    disableConcurrentBuilds()
  }
  stages {
    stage('docker hub login') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'chewbot-dockerhub-userpass', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
          sh 'echo $PASSWORD | docker login --username $USERNAME --password-stdin &> /dev/null'
        }
      }
    }
    stage('ECR login') {
      steps {
        sh "${ecrLogin()}"
      }
    }
    stage('Docker compose up') {
      steps {
        sh "docker-compose --file docker-compose-jenkins.yml up -d"
      }
    }
    stage('build') {
      steps {
        sh "git submodule init"
        sh "git submodule update"
        sh "./gradlew copyConfigurations"
        withCredentials([usernamePassword(credentialsId: 'jenkins-github-userpass',
                                          passwordVariable: 'GRGIT_PASS',
                                          usernameVariable: 'GRGIT_USER')]) {
          withSonarQubeEnv('sonarqube-nonprod') {
            sh "./gradlew ${GRADLE_ARGS} jacocoTestCoverageVerification sonarqube -DLOCALSTACK_PORT=${serverSocket.localPort} --no-daemon"
          }
        }
      }
    }
    stage('Deploy to dev') {
      when { branch 'main' }
      steps {
        script {
          def version = readFile "version.txt"
          def previousBuildParameters = getPreviousParameters()
          echo """
            TERRAFORM_DOCKER_TAG': ${previousBuildParameters.terraform_docker_tag.replace('"', '')}
            PREFERENCE_APPLICATION_CONTAINER_LABEL_BLUE: ${version}
            PREFERENCE_APPLICATION_CONTAINER_LABEL_GREEN: ${version}
        """
          build job: "deploy-preference-service-dev",
                parameters: [
                  [$class: 'StringParameterValue', name: 'TERRAFORM_DOCKER_TAG', value: "${previousBuildParameters.terraform_docker_tag.replace('"', '')}" ],
                  [$class: 'StringParameterValue', name: 'PREFERENCE_APPLICATION_CONTAINER_LABEL_BLUE', value: "${version}"],
                  [$class: 'StringParameterValue', name: 'PREFERENCE_APPLICATION_CONTAINER_LABEL_GREEN', value: "${version}"]
                ],
                wait: false
        }
      }
    }
  }
  post {
    always {
      sh 'docker-compose --file docker-compose-jenkins.yml down'
      script {
          sh "cp ${WORKSPACE}/*/build/test-results/test/*.xml ${WORKSPACE}"
      }
      junit testResults: "*.xml", allowEmptyResults: false
    }
    success {
      script {
        if (env.BRANCH_name == 'main') {
          env.PACKAGE_VERSION = getPackageVersion()
          currentBuild.description = env.PACKAGE_VERSION
        }
      }
    }
  }
}