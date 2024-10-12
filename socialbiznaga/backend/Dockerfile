# Usamos una imagen de Java como base
FROM openjdk:17-jdk-slim

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el archivo de construcción de Maven
COPY target/social-biznaga-0.0.1-SNAPSHOT.jar ./
COPY elastic-apm-agent.jar ./

# Exponemos el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación

CMD java -javaagent:elastic-apm-agent.jar \
-Delastic.apm.service_name=social-biznaga \
-Delastic.apm.server_urls=http://host.docker.internal:8200 \
-Delastic.apm.secret_token= \
-Delastic.apm.environment=production \
-Delastic.apm.application_packages=com.ramonlence.social_biznaga \
-jar social-biznaga-0.0.1-SNAPSHOT.jar

