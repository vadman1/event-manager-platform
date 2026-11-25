# Event Manager Platform

Учебный стартовый репозиторий для выпускного проекта по Java Backend.

Внутри уже подготовлены базовая Maven-структура, стартовая конфигурация Spring Boot,
`Docker Compose` для инфраструктуры и OpenAPI-контракты. Это позволяет быстрее
перейти к бизнес-логике, API и архитектуре сервиса.

## Что есть в репозитории

- `event-common/` — общий контракт Kafka.
- `event-manager/` — сервис мероприятий и выдачи JWT (порт 8080).
- `event-notificator/` — сервис уведомлений (порт 8081).
- `infra/` — compose-файлы для локального подъема PostgreSQL, Kafka и Redis.
- `docs/openapi/event-manager-openapi.yaml` — контракт API сервиса `event-manager`.
- `docs/openapi/event-notificator-openapi.yaml` — контракт API сервиса `event-notificator`.

## Стек проекта

- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security + JWT
- Liquibase
- PostgreSQL
- Kafka
- Redis
- Maven
- Docker Compose
- OpenAPI / Swagger

## Сборка и запуск

Поднимите PostgreSQL для обоих сервисов и Kafka:

```bash
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.kafka.yml up -d --wait
```

В IntelliJ IDEA запустите `EventManagerApplication` и `EventNotificatorApplication`.
Для `GET /notifications` и `POST /notifications` получите новый JWT через
`POST /users/auth` в `event-manager`. Если задаёте `JWT_SECRET_KEY`, используйте
одно значение в обоих приложениях.

Сборка всего проекта:

```bash
mvn clean verify
```

Для запуска из консоли соберите модули, затем запустите приложения в отдельных терминалах:

```bash
mvn -DskipTests package
java -jar event-manager/target/event-manager-0.0.1-SNAPSHOT.jar
java -jar event-notificator/target/event-notificator-0.0.1-SNAPSHOT.jar
```

Запуск тестов только модуля `event-manager`:

```bash
mvn -pl event-manager -am test
```
