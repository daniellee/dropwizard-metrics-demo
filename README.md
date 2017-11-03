
# Instructions for DropWizard Demo

1. Install Java. On Ubuntu - `sudo apt-get install openjdk-8-jdk`
2. Install Maven (might be able to skip this step).
    - `cd /opt`
    - `wget http://apache.mirrors.spacedump.net/maven/maven-3/3.5.2/binaries/apache-maven-3.5.2-bin.tar.gz`
    - `sudo tar -xvzf apache-maven-3.5.2-bin.tar.gz`
    - `sudo mv apache-maven-3.5.2 maven`
    - Create an environment file: `sudo vim /etc/profile.d/mavenenv.sh`
    -
        ```bash
        export M2_HOME=/opt/maven
        export PATH=${M2_HOME}/bin:${PATH}
        ```
    - `chmod +x /etc/profile.d/mavenenv.sh`
    - `sudo -s`
    - `source /etc/profile.d/mavenenv.sh`
3. Download DropWizard example or use this jar. If building from source (might have to change http port in example.yml):
    - mvn package
    - `java -jar target/dropwizard-example-1.2.1-SNAPSHOT.jar db migrate example.yml`
    - `java -jar target/dropwizard-example-1.2.1-SNAPSHOT.jar server example.yml `
