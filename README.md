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

<h3 align="left">Tech Stack</h3>
<p align="left"> 
<a href="https://www.spring.io" target="_blank"> <img src="https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg" alt="spring" width="100" height="50"/>
<a href="https://www.java.com/en/" target="_blank"> <img src="https://upload.wikimedia.org/wikipedia/tr/2/2e/Java_Logo.svg" alt="java" width="50" height="50"/>
<a href="https://hibernate.org/orm/" target="_blank"> <img src="https://in.relation.to/images/hibernate_icon_whitebkg.svg" alt="hibernate" width="50" height="50"/>
<a href="https://www.docker.com/" target="_blank"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/docker/docker-original-wordmark.svg" alt="docker" width="50" height="50"/>
<a href="https://git-scm.com/" target="_blank"> <img src="https://www.vectorlogo.zone/logos/git-scm/git-scm-icon.svg" alt="git" width="50" height="50"/>
<a href="https://www.mysql.com/" target="_blank"> <img src="https://www.onurbabur.com/wp-content/uploads/2020/09/MySQL-Logo.wine_.png" alt="mysql" width="50" height="50"/>
<a href="https://www.jenkins.io" target="_blank"> <img src="https://www.vectorlogo.zone/logos/jenkins/jenkins-icon.svg" alt="jenkins" width="50" height="50"/>
<a href="https://dbeaver.io/" target="_blank"> <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/b/b5/DBeaver_logo.svg/1200px-DBeaver_logo.svg.png" alt="dbeaver" width="50" height="50"/>
<a href="https://www.phpmyadmin.net/" target="_blank"> <img src="https://wikiimg.tojsiabtv.com/wikipedia/commons/thumb/4/4f/PhpMyAdmin_logo.svg/1200px-PhpMyAdmin_logo.svg.png" alt="dbeaver" width="60" height="50"/>
</p>

