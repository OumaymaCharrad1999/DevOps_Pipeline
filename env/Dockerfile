FROM jenkins/jenkins:2.414.3-jdk11

USER root

RUN apt-get update && apt-get install -y lsb-release curl wget apt-transport-https gnupg
RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
    https://download.docker.com/linux/debian/gpg
RUN echo "deb [arch=$(dpkg --print-architecture) \
    signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
    https://download.docker.com/linux/debian \
    $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
RUN apt-get update && apt-get install -y docker-ce-cli
RUN groupadd -g 973 docker && usermod -aG docker jenkins

# Install Kubectl
RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl; \
    chmod +x ./kubectl; mv ./kubectl /usr/local/bin/kubectl

# Install Trivy
RUN wget -qO - https://aquasecurity.github.io/trivy-repo/deb/public.key | apt-key add -
RUN echo "deb https://aquasecurity.github.io/trivy-repo/deb $(lsb_release -sc) main" > /etc/apt/sources.list.d/trivy.list
RUN apt-get update && apt-get install -y trivy

# Install JMeter
RUN mkdir -p /opt/jmeter \
    && curl -fsSL https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-5.4.1.tgz | tar -xz -C /opt/jmeter --strip-components=1 \
    && chown -R jenkins:jenkins /opt/jmeter
ENV PATH="/opt/jmeter/bin:${PATH}"

USER jenkins

# Install Jenkins plugins
RUN jenkins-plugin-cli --plugins "blueocean docker-workflow"