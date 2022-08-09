# Тестовое задание

## Команды

создание пользователя

    api/add {name:"name", password:"password"} 

получение access token действует 5 минут

    api/login {name:"name", password:"password"} 

создание сообщения от имени пользователя

где token ваш полученный токен при логине

    api/message {name:"name", message:"text"

    Headers:
    Content-Type: application/json;
    Authorization: Bearer_token;

получение последних X сообщение у пользователя

где token ваш полученный токен при логине
    
    api/message {name:"name", message:"history X"}

    Headers:
    Content-Type: application/json;
    Authorization: Bearer_token;