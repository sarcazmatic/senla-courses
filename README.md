# Проект senla-courses

**Описание проекта**
Данный проект представляет собой систему управления образовательной платформой. Она включает в себя модули для
администрирования, взаимодействия с пользователями, управления курсами, модулями, задачами и сообщениями.

Благодаря имплементации *Spring Security* cистема предполагает разные уровни пользователей и предоставляет разные уровни
доступа: ADMIN, TEACHER, STUDENT и ALL.

**Функционал предполагает возможность:**
1. Регистрации и обновление информации о студентах и преподавателях
2. Аутентификации для всех пользователей
3. Создание курсов
4. Подключения к курсам преподавателей
5. Подключения к курсам студентов и модерацию их заявок администраторомка
6. Подключения к курсам образавательных модулей
7. Наполнение модулей файлами, заданиями и литературой
8. Обмен сообщениями между студентами и преподавателям

Миграция баз данных реализована с помощью **liquibase** и зафиксирована в скриптах.

Проект собран на **maven** и предполагает возможность развертывания в **docker**.

Реализовано логирование с помощью **logback** с выносом логов уровня warn и выше в отдельный файл.

Взаимодействие с БД реализовано благодаря **hibernate**. Настройки хронятся в конфигурационном файле.

Тестирование осуществлялось с помощью **mockito** и **JUnit**, а также с помощью **Postman** (скрипт в ресурсах).
