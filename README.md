Notre pipeline CI/CD suivra les étapes ci-dessous:

![Alt text](https://github.com/OumaymaCharrad1999/DevOps_Pipeline/blob/develop/images/Architecture.png)
## 1. Développement et publication du code
Le développeur apporte des modifications au code et le publie sur GitHub.
## 2. Déclenchement du pipeline
Grâce à la configuration d’un webhook, toute modification du code sur GitHub déclenche automatiquement le pipeline Jenkins, qui commence par récupérer le code depuis le dépôt distant.
## 3. Compilation et construction avec Maven
Maven compile et construit le code. Si cette étape échoue, le pipeline est interrompu et l’utilisateur est notifié par E-mail.
## 4. Tests unitaires avec JUnit
Les tests unitaires JUnit sont exécutés. Si l’application réussit les tests, le pipeline continue, sinon, le pipeline est interrompu et l’utilisateur est notifié par E-mail.

![Alt text](https://github.com/OumaymaCharrad1999/DevOps_Pipeline/blob/develop/images/Unit-Tests.png)
## 5. Vérification des dépendances avec OWASP Dependency-Check
Cette phase implique l’analyse des dépendances du projet pour identifier d’éventuelles vulnérabilités connues. Elle génère ensuite un rapport détaillé spécifiant l’état de chaque dépendance, tel que moyen, élevé ou critique, en fonction des failles détectées.

![Alt text](https://github.com/OumaymaCharrad1999/DevOps_Pipeline/blob/develop/images/Dependency-Check.png)
## 6. Analyse du code avec SonarQube
SonarQube analyse le code et transmet le rapport au serveur dédié. Ce rapport est évalué par une porte de qualité (quality gate), définissant des critères tels que le nombre de bugs, de vulnérabilités ou d’anomalies tolérables dans le code. Un webhook est mis en place pour transmettre l’état de cette porte à Jenkins. En cas d’échec de la porte de qualité, l’intégralité du pipeline est interrompue, et Jenkins notifie alors l’utilisateur de l’échec du pipeline par E-mail.

![Alt text](https://github.com/OumaymaCharrad1999/DevOps_Pipeline/blob/develop/images/SonarQube.png)
## 7. Publication de l’artefact dans Nexus Repository Manager
Jenkins publie l’artefact JAR dans Nexus Repository Manager pour des usages futurs, si nécessaire. Un échec de publication stoppe le pipeline.

![Alt text](https://github.com/OumaymaCharrad1999/DevOps_Pipeline/blob/develop/images/Nexus.png)
## 8. Construction de l’image Docker
L’étape de construction de l’image Docker est exécutée par Docker lui-même. Si cette construction échoue, l’intégralité du pipeline est interrompue, et Jenkins notifie l’utilisateur de l’échec. Une fois la construction réussie, l’image sera publiée dans Docker Hub, où elle sera récupérée ultérieurement pour le déploiement.

![Alt text](https://github.com/OumaymaCharrad1999/DevOps_Pipeline/blob/develop/images/Hub.png)
## 9. Déploiement dans l’environnement de développement
L’application est déployée dans l’environnement de développement. C’est là où nous pouvons tester initialement le code. Cet environnement est souvent utilisé pour les tests unitaires et les expérimentations.
## 10. Analyse de sécurité avec Trivy
Trivy procède à l’analyse approfondie de l’image Docker, identifiant et générant un rapport détaillé sur les vulnérabilités détectées.

![Alt text](https://github.com/OumaymaCharrad1999/DevOps_Pipeline/blob/develop/images/Trivy.png)
## 11. Tests de performance avec JMeter
JMeter réalise des tests de performance afin de simuler diverses charges sur le système, s’alignant ainsi sur les conditions de l’environnement de production.

![Alt text](https://github.com/OumaymaCharrad1999/DevOps_Pipeline/blob/develop/images/Performance.png)

![Alt text](https://github.com/OumaymaCharrad1999/DevOps_Pipeline/blob/develop/images/Performance%20-%202.png)
## 12. Déploiement dans l’environnement de production
Jenkins assure le déploiement de notre application dans un cluster Minikube. En cas d’échec lors de cette opération, tout le processus du pipeline sera interrompu et Jenkins informera l’utilisateur du dysfonctionnement du déploiement.

![Alt text](https://github.com/OumaymaCharrad1999/DevOps_Pipeline/blob/develop/images/Deploy.png)

![Alt text](https://github.com/OumaymaCharrad1999/DevOps_Pipeline/blob/develop/images/Minikube.png)

![Alt text](https://github.com/OumaymaCharrad1999/DevOps_Pipeline/blob/develop/images/Mail.png)
## 13. Surveillance
La surveillance du cluster Minikube ainsi que de l’intégralité de notre système sera effectuée à l’aide de Prometheurs et Grafana.

![Alt text](https://github.com/OumaymaCharrad1999/DevOps_Pipeline/blob/develop/images/Supervision%20Cluster.png)

![Alt text](https://github.com/OumaymaCharrad1999/DevOps_Pipeline/blob/develop/images/Supervision%20Système.png)
