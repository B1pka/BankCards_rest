# 💳 Bank Cards REST API

REST API приложение для управления банковскими картами с поддержкой аутентификации, авторизации и переводов между картами.

Проект реализован на Spring Boot и демонстрирует работу с JWT, базой данных PostgreSQL, миграциями Liquibase и контейнеризацией через Docker.

---

## 🚀 Реализованный функционал
(7 РАБОЧИХ дней)

### 🔐 Аутентификация и безопасность

* Регистрация пользователей (`/api/users`)
* Аутентификация (логин) (`/api/auth/login`)
* JWT авторизация (Bearer token)
* Stateless security (без HTTP-сессий)
* Ролевая модель:

  * `ADMIN`
  * `USER`

---

### 👤 Пользователь (USER)

* Просмотр своих карт (`/api/cards/my`)
* Просмотр конкретной карты (`/api/cards/my/{id}`)
* Просмотр баланса карты (`/api/cards/my/{id}/balance`)
* Запрос на блокировку карты
* Переводы между своими картами (`/api/transfers`)

---

### 👨‍💼 Администратор (ADMIN)

* Создание карт (`POST /api/admin/cards`)
* Просмотр всех карт (с пагинацией)
* Блокировка карт
* Активация карт
* Удаление карт

---

### 💳 Карты

* Номер карты хранится в зашифрованном виде
* В API возвращается маскированный номер:

  ```
  **** **** **** 1234
  ```
* Карта привязана к пользователю
* Баланс хранится в БД
* Статусы карт:

  * `ACTIVE`
  * `BLOCKED`
  * `EXPIRED`
  * `BLOCK_REQUESTED`

---

### 💸 Переводы

* Переводы между картами одного пользователя
* Проверки:

  * карта принадлежит пользователю
  * карта активна
  * карта не просрочена
  * достаточно средств
  * нельзя переводить на ту же карту
* Используется транзакционность (`@Transactional`)

---

## 🗄 База данных

* PostgreSQL
* Управление схемой через Liquibase
* Миграции:

  * users
  * cards
  * transfers

---

## 🐳 Docker

Проект запускается через Docker Compose.

Поднимаются:

* backend приложение
* PostgreSQL база данных

---

## ⚙️ Используемые технологии

* Java 17
* Spring Boot
* Spring Security
* JWT (jjwt)
* Spring Data JPA (Hibernate)
* PostgreSQL
* Liquibase
* Docker & Docker Compose
* Maven
* Lombok
* Jakarta

---

## 🏃‍♂️ Запуск проекта

### 1. Клонирование репозитория

```bash
git clone https://github.com/B1pka/BankCards_rest.git
cd BankCards_rest
```

### 2. Запуск через Docker

```bash
docker compose up --build
```

### 3. Приложение доступно по адресу:

```
http://localhost:8080
```

---

## 🔑 Работа с API

### Регистрация

```
POST /api/users
```

### Логин

```
POST /api/auth/login
```

Ответ:

```json
{
  "token": "JWT_TOKEN"
}
```

---

### Использование токена

Во всех защищённых запросах:

```
Authorization: Bearer <TOKEN>
```

---

## 📌 Основные endpoints

### Пользователь

* `GET /api/cards/my`
* `GET /api/cards/my/{id}`
* `GET /api/cards/my/{id}/balance`
* `PATCH /api/cards/my/{id}/block-request`

---

### Администратор

* `POST /api/admin/cards`
* `GET /api/admin/cards`
* `PATCH /api/admin/cards/{id}/block`
* `PATCH /api/admin/cards/{id}/activate`
* `DELETE /api/admin/cards/{id}`

В текущей реализации удаление карты сделано через PATCH /api/admin/cards/{id} 
и вызывает deleteCard(...) в сервисе.

---

### Переводы

* `POST /api/transfers`

---

## 🔐 Безопасность

* JWT используется для аутентификации
* Все защищённые endpoints требуют токен
* `/api/admin/**` доступен только для `ROLE_ADMIN`
* `/api/cards/my/**` и `/api/transfers/**` доступны только авторизованным пользователям

---

## ⚠️ Ограничения проекта


* ❌ Нет глобального обработчика ошибок (@RestControllerAdvice);
* ❌ Нет unit / integration тестов;
* ❌ docs/openapi.yaml не сделан;
* ❌ AdminCardController удаление оформлено не как DELETE, а как PATCH /{id};
* ❌ Swagger dependency подключена, но полноценная OpenAPI-документация ещё не описана.
