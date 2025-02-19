Notifications Service
======================================

Сборка
----------------
Для сборки приложения необходимо в корне проекта выполнить следующую команду:

`mvn clean package`  

Локальный запуск
----------------
По умолчанию приложение использует порт 8080. Если этот порт занят, можно установить любой свободный, задав параметр `server.port` в `application.yml`  
Для локального запуска приложения установите соответствующие значения в `application-local.yml`.  
При этом необходимо запускать приложение с профилем `local`.  
Установить профиль можно с помощью параметра JVM `-Dspring.profiles.active=local`  

Проверка качества написанного кода
----------------
Качество кода можно проверить используя maven-checkstyle плагин, встроенный в проект.  
Для этого запустите терминал и выполните следущую команду:

`mvn checkstyle:check`

Дополнительные функциональные возможности
----------------
Помимо решения прикладных задач, приложение по умолчанию поддерживает:
 * Мониторинг с возможностью отображения метрик в формате Prometheus.  
 * UI для задокументированного REST API, доступный на `/swagger-ui` endpoint.