mvn clean verify sonar:sonar \
  -Dsonar.projectKey=devops-pipeline\
  -Dsonar.host.url=http://192.168.1.19:9000 \
  -Dsonar.login=squ_57a7780ccb95e05273b96593a7511d48cdea76fc