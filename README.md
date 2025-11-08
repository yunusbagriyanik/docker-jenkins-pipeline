### __Docker Deployment CI / CD Pipeline__

### Project Requirements

* JDK 17(openjdk-17)
* Spring Boot 2.6.6
* Maven 3.6.3 or newer
* Docker
* Jenkins

---

### Requirements Setup For GNU/Linux

### Install Package Manager
    $ apt-get update
    $ apt-get install sudo
    $ sudo apt-get update

### Install Docker

    $ sudo apt-get install apt-transport-https ca-certificates curl gnupg-agent software-properties-common
    $ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    $ sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
    $ sudo apt-get update
    $ sudo apt-get install docker-ce docker-ce-cli containerd.io

### Install Java 17
    $ wget https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_linux-x64_bin.tar.gz
    $ tar xvf openjdk-17.0.2_linux-x64_bin.tar.gz
    $ echo 'export JAVA_HOME=/opt/jdk-17' | tee -a ~/.bashrc
    $ echo 'export PATH=$PATH:$JAVA_HOME/bin '|tee -a ~/.bashrc
    $ source ~/.bashrc
    $ echo $JAVA_HOME
### Install Maven
    $ cd /opt
    $ wget https://downloads.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
    $ tar xzf apache-maven-3.6.3-bin.tar.gz
    $ mv apache-maven-3.6.3 apachemaven

    $ nano /etc/profile.d/apachemaven.sh

    Add the following lines:
        export M2_HOME=/opt/apachemaven
        export MAVEN_HOME=/opt/apachemaven
        export PATH=${M2_HOME}/bin:${PATH}
        
    $ chmod +x /etc/profile.d/apachemaven.sh
    $ source /etc/profile.d/apachemaven.sh

### Install Git
    $ sudo apt install git

### 1.Build Project

    $ git clone https://github.com/yunusbagriyanik/docker-deployment.git
    $ cd docker-deployment
    $ mvn clean install
    $ docker build -t deploy-app-image .

### 2.Run Docker

### TEST ENV

    $ docker network create deploy-jenkins-test-network
    $ docker create -v /var/lib/mysql --name deploy-jenkins-test-data mysql/mysql-server:latest
    $ docker container run --volumes-from deploy-jenkins-test-data --name deploy-jenkins-testdb --network deploy-jenkins-test-network -p 3307:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=appdb -d mysql:8.0.28
    $ docker run --name phpmyadmin-dashboard --network deploy-jenkins-test-network -p 8000:80 --link deploy-jenkins-testdb:db -d phpmyadmin/phpmyadmin

### Run Application Image 

    $ docker container run -e "SPRING_PROFILES_ACTIVE=test" --network deploy-jenkins-test-network --name app-test-container -p 8090:8090 -d  deploy-app-image

### DEV ENV

    $ docker network create deploy-jenkins-dev-network
    $ docker create -v /var/lib/mysql --name mysqldata mysql/mysql-server:latest
    $ docker container run --volumes-from mysqldata --name mysqldb --network deploy-jenkins-dev-network -p 3307:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=appdb -d mysql:8.0.28
    $ docker run --name phpmyadmin-dashboard --network deploy-jenkins-dev-network -p 8000:80 --link mysqldb:db -d phpmyadmin/phpmyadmin

### Run App Image

    $ mvn spring-boot:run

### Install Jenkins

#### With Docker

    $ docker run --network deploy-jenkins-test-network -p 8080:8080 -p 50000:50000 -d -v jenkins_home:/var/jenkins_home jenkins/jenkins:jdk17-preview

#### With sudo

    $ wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo apt-key add -
    $ sudo sh -c 'echo deb http://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'
    $ sudo apt update
    $ sudo apt install jenkins

###### Start Jenkins

    $ sudo systemctl start jenkins
    $ sudo systemctl status jenkins

#### With brew

    $ brew install jenkins-lts

###### Start Jenkins

    $ brew services start jenkins-lts


