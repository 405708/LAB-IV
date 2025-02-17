[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/U6HDBWBq)
[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/_RotgeOK)

# Final 2025 04/02 - Telemetria

## Contexto

### Introducción

En el contexto de gestión y monitoreo de dispositivos en una red,
es fundamental contar con información en tiempo real sobre su estado y rendimiento.
La telemetría permite recopilar datos clave de los dispositivos, como el
uso del disco, estado del micrófono y permisos de captura de pantalla y audio.

El objetivo de esta evaluación es desarrollar una API que permita registrar, consultar y
gestionar estos datos de telemetría, y sus dispositivos asociados garantizando la integridad de la información y la
correcta vinculación con los dispositivos correspondientes. Esto permitirá a los administradores
tomar decisiones informadas sobre la gestión y mantenimiento de los dispositivos en la red.

Para poder gestionar esta informacion, tiene que existir preeviamente un dispositivo al cual registrar la telemetria
asociada.

En este contexto propuesto van a existir 4 tipos de dipositivos:

- Laptop
- Desktop
- Tablet
- Smartphone

## Consigna

### Precondiciones y Aclaraciones

En la db debe persistirse los siguientes datos:

| Entidad   | Campo                | Tipo dato Java 
|-----------|----------------------|----------------|
| Device    | hostName             | String         | 
|           | createdDate          | LocalDateTime  |
|           | os                   | String         |
|           | macAddress           | String         | 
|           | type                 | Enum/String    | 
|           |                      |                | 
| Telemetry |                      |                | 
|           | ip                   | String         | 
|           | dataDate             | LocalDateTime  | 
|           | hostDiskFree         | Double         | 
|           | cpuUsage             | Double         | 
|           | microphoneState      | String         | 
|           | screenCaptureAllowed | Boolean        | 
|           | audioCaptureAllowed  | Boolean        | 
|           | hostname             | String         | 

- La macAddress debe ser unica.
- El hostName debe ser unico.
- El createdDate debe corresponder con el momento que se crea el dipositivo.
- La dataDate debe corresponder con el momento que se registra la telemetria.

- Pueden usar una db en memoria (h2) para resolver esta consigna.

### Desarrollo de Endpoints

#### 1. Registro de Telemetría

#### POST /telemetry

- Recibe un JSON con los datos de telemetría.
- Valida que el hostname exista en la tabla Device, de lo contrario, rechazar la solicitud.
- Guarda los datos en la base de datos.
- Retorna un 201 Created si la operación fue exitosa.

Request

```http
curl --location --request POST 'http://localhost:8080/api/telemetry'
```

```json

{
  "hostname": "PC-001",
  "cpuUsage": 75.5,
  "hostDiskFree": 60.2,
  "microphoneState": "active",
  "screenCaptureAllowed": false,
  "audioCaptureAllowed": true,
  "dataDate": "2024-08-30T14:30:00Z"
}
```

#### 2. Registro de Dispositivo

#### POST /device

- Recibe un JSON con los datos del dispositivo.
- Valida que el hostname no exista preeviamente en la tabla Device, de lo contrario, rechazar la solicitud.
- Guarda los datos en la base de datos.
- Retorna un 201 Created si la operación fue exitosa.

Request

```http
curl --location --request POST 'http://localhost:8080/api/device'
```

```json

{
  "hostname": "PC-001",
  "type": "Laptop",
  "os": "Windows 11",
  "macAddress": "00:1A:2B:3C:4D:5E"
}
```

### 3. Devuelve una lista con todas las entradas de telemetría registradas.

### GET /telemetry

Request

```http
curl --location 'http://localhost:8080/api/telemetry'
```

**Response**

```json
[
  {
    "ip": "192.168.1.1",
    "dataDate": "2025-02-02T15:30:00",
    "hostDiskFree": 128.5,
    "microphoneState": "active",
    "screenCaptureAllowed": true,
    "audioCaptureAllowed": true,
    "hostname": "device123"
  },
  {
    "ip": "10.0.0.2",
    "dataDate": "2025-02-02T16:00:00",
    "hostDiskFree": 256.3,
    "microphoneState": "inactive",
    "screenCaptureAllowed": false,
    "audioCaptureAllowed": true,
    "hostname": "device456"
  },
  {
    "ip": "192.168.1.3",
    "dataDate": "2025-02-02T17:45:00",
    "hostDiskFree": 75.2,
    "microphoneState": "active",
    "screenCaptureAllowed": true,
    "audioCaptureAllowed": false,
    "hostname": "device789"
  }
]
```

Deberia poder filtrarse por hostname:

```http
curl --location 'http://localhost:8080/api/telemetry?hostname=device123'
```

### 4. Obtener todos los dispositivos de un tipo específico

### GET /device?type=Laptop

**Request**

```http
curl --location 'http://localhost:8080/api/device?type=Laptop'
```

**Response**

```json
[
  {
    "id": 1,
    "hostname": "PC-001",
    "type": "Laptop",
    "os": "Windows 10",
    "macAddress": "00:1A:2B:3C:4D:5E"
  },
  {
    "id": 2,
    "hostname": "PC-002",
    "type": "Laptop",
    "os": "Ubuntu 22.04",
    "macAddress": "00:1A:2B:3C:4D:6F"
  }
]
```

### 5. Obtener todos los dispositivos que tengan asociada la ultima telemetria registrada con un consumo de cpu entre

### dos valores pasados por parametros. (No permitir que el lowThreshold sea mayor al upThreshold, en dicho caso devolver 400 bad request )

### GET /device?lowThreshold=70&upThreshold=80

**Request**

```http
curl --location --request POST 'http://localhost:8080/api/device?lowThreshold=70&upThreshold=80'
```

### 6. Testing

- Es obligatorio la creación de testing unitario sobre la lógica de negocio de todo el proyecto debe tener como minimo 75%.


### SOLO PARA LABORATORIO IV 
#### 7. Crear otro endpoint de tipo post en donde vaya a buscar dispositivos a la siguiente url:

```http
'https://67a106a15bcfff4fabe171b0.mockapi.io/api/v1/device/device'
```
El endpoint POST /api/save-consumed-devices debe cumplir con los siguientes criterios:

- Se debe insertar en la base de datos la mitad de los dispositivos devueltos por la API.
- La selección de los dispositivos a insertar debe ser aleatoria, evitando siempre insertar los mismos.
- El ID de los dispositivos no debe ser considerado al momento de la inserción.
- El campo createdDate en la base de datos debe reflejar el momento exacto de la inserción, en lugar de la fecha proporcionada por la API.

Request

```http
curl --location 'http://localhost:8080/api/save-consumed-devices'
```

### 8. Entregar el proyecto con el archivo Dockerfile que permita ejecutar la aplicación en un contenedor.

### 9. Entregar el proyecto con un archivo docker-compose para poder ejecutar el servicio creado. Debe existir una db 
### puede ser mysql/postgres real en donde persistir la data necesaria.


