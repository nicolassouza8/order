# Etapa 1: Construir o projeto
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar os arquivos do projeto
COPY pom.xml .
COPY src ./src

# Baixar dependências e construir o projeto
RUN mvn clean package -DskipTests

# Etapa 2: Configurar a imagem final
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Expor a porta da aplicação
EXPOSE 8080

# Copiar o artefato gerado na etapa anterior
COPY --from=build /app/target/order-management-0.0.1-SNAPSHOT.jar app.jar

# Comando para executar o aplicativo
ENTRYPOINT ["java", "-jar", "app.jar"]
