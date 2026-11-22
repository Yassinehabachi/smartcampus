FROM jenkins/jenkins:lts-jdk17
USER root
RUN apt-get update \
 && apt-get install -y --no-install-recommends maven curl ca-certificates \
 && rm -rf /var/lib/apt/lists/* \
 && curl -fsSL https://download.docker.com/linux/static/stable/x86_64/docker-26.1.4.tgz \
    | tar xz --strip-components=1 -C /usr/local/bin docker/docker
USER jenkins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt
COPY casc.yaml /usr/share/jenkins/ref/casc.yaml
ENV CASC_JENKINS_CONFIG=/usr/share/jenkins/ref/casc.yaml
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
