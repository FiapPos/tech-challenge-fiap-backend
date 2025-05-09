# Tech Challenge FIAP

# 🍽️ FoodSys

## 🚀 Como rodar o projeto

Certifique-se de ter o **Docker** e o **Docker Compose** instalados na sua máquina.

Caso você não tenha, siga esse tuturial: 
## Docker
**https://docs.docker.com/get-started/get-docker/**

## Docker Compose
**https://docs.docker.com/compose/install/**

### 🔧 Passos:

```bash
docker-compose down && docker-compose up --build -d
```

## 💾 Acesso ao banco de dados (H2 Console)

Após iniciar o projeto, acesse o console do banco de dados:

🔗 **URL:** [http://localhost:8080/h2-console/](http://localhost:8080/h2-console/)  
👤 **User Name:** `sa`  
🔐 **Password:** `senhasegura`  
🛢️ **JDBC URL:** `jdbc:h2:mem:foodsys`

![img.png](img.png)

## 📚 Documentação da API

A documentação da API está disponível em:

👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

# 🐞 Debugando containers Docker

Caso você precise debugar ou ver logs de um container, aqui estão alguns comandos úteis:

## 📜 Ver logs do container

```bash
docker logs <nome-ou-id-do-container>
```

---

### 🖥️ Acessar o terminal do container

Para containers com shell sh:

```bash
docker exec -it <nome-ou-id-do-container> sh
```

Para containers com bash:

```bash
docker exec -it <nome-ou-id-do-container> bash
```

Exemplo:

```bash
docker exec -it <nome-ou-id-do-container> sh
```

---

### 🔎 Listar, finalizar e executar containers

Para descobrir os nomes dos containers ativos:

```bash
docker ps -a 
```

Para finalizar todos os containers ativos: 

```bash
docker container prune -f 
```

Para parar e finalizar somente um container:

```bash
docker stop <nome_ou_id_do_container>
docker rm <nome_ou_id_do_container>
```

Para executar o container:

```bash
docker-compose up -d foodsys-api
```
