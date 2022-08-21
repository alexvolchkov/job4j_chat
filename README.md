# job4j_chat

## О проекте

В этом проекте будет разработан чат с комнатами.

Будет использован  REST Api через Spring Boot.

Технологии проекта: Spring boot, Spring data, JWT.
Сборка проекта производится командой "mvn package".
Запуск проекта производится командой "java -jar job4j_chat-0.0.1-SNAPSHOT.jar"

API:

GET /person/ - получить список пользователей.
GET /person/{id} - получить пользователя по id.
POST /person/ - создать пользователя.
PUT /person/ - обновить пользователя.
DELETE /person/{id} - удалить пользователя.

GET /room/ - получить список комнат.
GET /room/{id} - получить комнату по id.
GET /room/{id}/messsages - получить сообщения комнаты по id.
POST /room/ - создать комнату.
PUT /room/ - обновить комнату.
DELETE /room/{id} - удалить комнату.

GET /message/ - получить список сообщений.
GET /message/{id} - получить сообщение по id.
POST /message/{roomId} - создать сообщение в комнате.
PUT /message/ - обновить сообщение.
DELETE /message/{id} - удалить сообщение.


