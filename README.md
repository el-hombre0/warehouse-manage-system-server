Для запуска Docker-контейнера с СУБД PostgreSQL использовать команду:
```
docker run --name toyshopdb -p 5430:5432 -e POSTGRES_USER=dbadmin -e POSTGRES_PASSWORD=qwerty -e POSTGRES_DB=toyshopdb -d postgres:latest
```

Для просмотра документации Swagger перейти по адресу:
http://localhost:8080/swagger-ui/index.html