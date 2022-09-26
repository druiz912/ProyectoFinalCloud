# VirtualTravel

<br />
<div align="center">
  <h3 align="center">VIRTUALTRAVEL</h3>
  <p align="center">
Ejercicio final realizado con las siguientes tecnologías:<br/>
-Java
<br/>
-Spring Boot
<br/>
-Spring Cloud
<br/>
-Kafka
<br/>
- Load Balancer
<br/>
- Microservices
 </p>
</div>

## Empecemos
To run the application follow the next steps

### 1. Running the containers
Execute the following *docker-compose* command to run the app in containers
  ```sh
  docker-compose up -d
  ```
> **Once the project is started and gateway has detected all the instances...**

| Service   | Port                           | Description                             |
| -------- | ------------------------ | --------------------------------|
| `Eureka`    | `8167`|  Server discovery|
| `Gateway` | `8080`| Load Balancer.|
| `Back-Empresa` | `8081`| Enterprise app for bookings|
| `Back-Web` | `8082`| This app simulate a front-end sends requests|

> After it you will have  access to the app throught the *gateway* in the port 8080, and you will have two instances of *backweb* and *backempresa*

# Let`s try the app
> Follow the points step by step in order, they cover all the possibilities or so I think...
> Im sorry now its time to write in spanish.

## ¿Cómo realizo una reserva?

### 1. Crear una reserva
> Endpoint de backweb

| Método   | URL                                      | Descripción                              |
| -------- | ---------------------------------------- | ---------------------------------------- |
| `POST`    | `/api/v0/reservas`                      | Crea una nueva reserva                    |

### 2. Revisar que se haya hecho correctamente
#### 2.1. Obtener todas las reservas
> Endpoint de backweb

| Method   | URL                                      | Description                              |
| -------- | ---------------------------------------- | ---------------------------------------- |
| `GET`    | `/api/v0/reservas`         | Todo el historial de reservas        |
#### 2.2. Obtener reservas según parámetros
> Parámetros --> Ciudad, fecha (inferior ,superior), hora(inferior y superior)

| Método   | URL                                      | Descripción                              |
| -------- | ---------------------------------------- | ---------------------------------------- |
| `GET`    | `api/v0/reservas/available/{city}?`| Lista de reservas según parámetros|

| Params   | Descripción                               |
| -------- | ---------------------------------------- |
| `lowerDate`    | La fecha por debajo de la reserva ( obligatorio ) |
| `upperDate`    | La fecha tope de la reserva (opcional) |
| `lowerHour`    | La fecha minima de la reserva (opcional) |
| `upperHour`    | La hora tope de la reserva (opcional) |

#### 2.3. Obtener lista de buses
| Method   | URL                                      | Description                              |
| -------- | ---------------------------------------- | ---------------------------------------- |
| `GET`    | `api/v0/bus/available/{city}?lowerDate=2022-09-20`         | Lista de buses según parámetros         |
> Parámetros --> Ciudad, fecha (inferior ,superior), hora(inferior y superior)

| Params   | Descripción                               |
| -------- | ---------------------------------------- |
| `lowerDate`    | La fecha por debajo de la reserva ( obligatorio ) |
| `upperDate`    | La fecha tope de la reserva (opcional) |
| `lowerHour`    | La fecha minima de la reserva (opcional) |
| `upperHour`    | La hora tope de la reserva (opcional) |

## 3. Obtener token de auth 
| Method   | URL                                      | Description                              |
| -------- | ---------------------------------------- | ---------------------------------------- |
| `GET`    | `api/v0/login`         | Registro de usuario con *mail* y *phone*
#### Responses
```
[
  {
    Bearer Token: **token**
}
]
```
> ¿Pero qué hago con ese token? 
> *Crear una reserva desde empresa y reenviar mails*

## 4. Obtener lista de usuarios 
| Method   | URL                                      | Description                              |
| -------- | ---------------------------------------- | ---------------------------------------- |
| `GET`    | `api/v0/users`         | Lista de todos los usuarios que han realizado una reserva con **VirtualTravel.**
```
