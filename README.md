# Project Name
Nortvis Java Assignment

- Java 11
- Maven
- Spring Boot 2.7.5



# API Endpoints

-Register a User:
POST
http://localhost:8080/api/users/register
{
    "name": "Alphy",
    "age": 25,
    "username": "alphy@gmail.com",
    "password": "alphy" 
}



-Login:
POST 
http://localhost:8080/api/users/login
{
  "username": "alphy@gmail.com",
    "password": "alphy" 
}


-Upload an Image:
POST 
http://localhost:8080/api/images/upload
Add token
Add file in body(form-data -> key:file, value:select

-View an Image:
GET
http://localhost:8080/api/images/view/{imageId}

-Delete an Image:
DELETE
http://localhost:8080/api/images/delete/{imageId}
