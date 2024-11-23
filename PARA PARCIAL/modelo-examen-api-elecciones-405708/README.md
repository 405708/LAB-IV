# Examen - Elecciones 2023

El siguiente parcial esta orientado a que los alumnos puedan demostrar los conocimientos adquiridos en la materia. 
Para esto se eligio una tematica vigente en este año electoral, las elecciones 2023.

---

## Contexto

### ¿Que votamos?
En el año 2023 es un año de elecciones en Argentina.

En las elecciones nacionales, desarrolladas los días 13 de agosto (P.A.S.O) y 22 de octubre (Generales) 
y eventual segunda vuelta 19 de noviembre, las argentinas y los argentinos elegiremos:

- Presidente/a y vicepresidente/a
- 19 parlamentarios y parlamentarias del Mercosur por distrito nacional
- 24 parlamentarios y parlamentarias del Mercosur por distrito regional
- 130 diputados y diputadas nacionales en todo el país
- 24 senadores y senadoras nacionales en 8 provincias.

### Sistema de publicación de resultados electorales

La Dirección Nacional Electoral y el Observatorio Político Electoral del Ministerio del Interior ponen a disposición 
el Sistema de Publicación de Resultados Electorales como un estándar de preservación y difusión de datos electorales.

El Sistema de Publicación de Resultados Electorales es una estructura que permite el archivo, tratamiento, intercambio 
y publicación de datos históricos de manera ágil y la aplicación de la misma sobre los resultados provisionales de los 
últimos 10 años.

Los resultados que se presentan son los correspondientes a los escrutinios provisorios de elecciones nacionales. 
También se presentan los resultados provisionales de las elecciones provinciales y/o locales realizadas bajo la 
adhesión a ley de simultaneidad.

Al contar con un buscador avanzado permite consultar, a través de una interfaz amigable, los resultados provisorios 
obtenidos por cada una de las agrupaciones políticas, organizados por categorías electorales y ámbitos geográficos.

También dispone de una herramienta de generación de informes personalizados, la opción de descarga de resultados en 
formato CSV (archivo de texto separado por comas) y una sección destinada a desarrolladores que brinda acceso a la API 
de publicación de resultados electorales históricos. Para desarrolladores se proveen tres documentos. 
La documentación técnica de la API para que pueda utilizarse en un editor de API (Postman, Insomnia, Swagger, etc), 
una colección de Insomnia con la especificación de la API que incluye algunos ejemplos de consultas y el Estándar de 
Preservación de Datos Electorales, que provee información sobre los distintos campos y las convenciones de uso que 
aplican a los datos exportados en formato CSV.

### ¿Como se organiza el padrón electoral?

El padrón electoral es el listado de personas habilitadas para votar en una elección.

En Argentina, el padrón electoral se organiza por distrito, es decir, por provincia y por la Ciudad Autónoma de Buenos Aires.
Cada distrito está dividido en secciones electorales, que a su vez se dividen en circuitos electorales.

### Información dicional

- [Clases de votos](https://www.argentina.gob.ar/dine/clases-de-votos)
- [Electores](https://www.argentina.gob.ar/dine/electores)

---

## Consigna

### Precondiciones y Aclaraciones

- Para este parcial usaremos los datos oficiales provistos por el Ministerio del Interior de la Nación Argentina 
en formato CSV que han sido expuestos en una api restful en la siguiente imagen de docker: **tupfrcutn/elecciones-2023:2.0.0**
- La especificación de la api restful esta disponible en la misma imagen, en el link **http://localhost:8080/swagger-ui.html**
- En la api /resultados, expondrá los datos sumarizados por distrito, sección, tipo de voto y agrupación para el cargo 1 (PRESIDENTE Y VICE)

### Requerimientos Funcionales

1. Crear una api que exponga todos los distritos y que permita buscar por id. <span style="color:red">**(5 puntos)**</span>

**Request**
```http
curl --location 'http://localhost:8080/elecciones/distritos'
```

**Response**
```json
[
  {
    "id": 1,
    "nombre": "Ciudad Autónoma de Buenos Aires"
  },
  {
    "id": 2,
    "nombre": "Buenos Aires"
  },
  {
    "id": 3,
    "nombre": "Catamarca"
  },
  {
    "id": 4,
    "nombre": "Córdoba"
  }
]
```

**Request**
```http
curl --location 'http://localhost:8080/elecciones/distritos/1'
```
**Response**
```json
{
  "id": 1,
  "nombre": "Ciudad Autónoma de Buenos Aires"
}
```


2. Crear una api que exponga los datos de todos los cargos disponibles para 
votar por distrito o un cargo puntual de un distrito. <span style="color:red">**(5 puntos)**</span>

**Request**
```http
curl --location 'http://localhost:8080/elecciones/distritos/4/cargos'
```

**Response**
```json
[
  {
    "id": 1,
    "nombre": "PRESIDENTE Y VICE"
  },
  {
    "id": 3,
    "nombre": "DIPUTADO NACIONAL"
  },
  {
    "id": 8,
    "nombre": "PARLAMENTO MERCOSUR NACIONAL"
  },
  {
    "id": 9,
    "nombre": "PARLAMENTO MERCOSUR REGIONAL"
  }
]
```

**Request**
```http
curl --location 'http://localhost:8080/elecciones/distritos/4/cargos/1'
```
**Response**
```json
{
  "id": 1,
  "nombre": "PRESIDENTE Y VICE"
}
```

3. Crear una api que exponga todas las secciones de un distrito o buscar una sección de 
un distrito. <span style="color:red">**(5 puntos)**</span>

**Request**
```http
curl --location 'http://localhost:8080/elecciones/distritos/4/secciones'
```

**Response**
```json
[
  {
    "id": 1,
    "nombre": "Capital"
  },
  {
    "id": 2,
    "nombre": "Calamuchita"
  },
  {
    "id": 3,
    "nombre": "Colón"
  }
]
```

**Request**
```http
curl --location 'http://localhost:8080/elecciones/distritos/4/secciones/26'
```
**Response**
```json
{
   "id": 26,
   "nombre": "Unión"
}
```

4. Crear una api que exponga un resumen de los resultados (sumatoria total de votos) de un distrito
ordenados de mayor a menor segun la cantidad de votos obtenidos por agrupación politica, votos en blanco, nulos, 
impugnados y recurridos. Adicionalmente, se debe mostrar la cantidad de votos de la sección, 
el porcentaje de estos respecto al total del distrito, la lista de secciones y el nombre de la agrupación ganadora.
<span style="color:red">**(20 puntos)**</span>

**Request**
```http
curl --location 'http://localhost:8080/elecciones/distritos/4/resultados'
```
**Response**
```json
{
  "id": 4,
  "nombre": "Córdoba",
  "votantes": 2332112,
  "secciones": [
    "Calamuchita","Capital","Colón","Cruz del Eje","General Roca","General San Martín",
    "Ischilín","Juárez Celman","Marcos Juárez","Minas","Pocho","Presidente Roque Sáenz Peña",
    "Punilla","Río Cuarto","Río Primero","Río Seco","Río Segundo","San Alberto","San Javier",
    "San Justo","Santa María","Sobremonte","Tercero Arriba","Totoral","Tulumba","Unión"
  ],
  "porcentaje_padron_nacional": 0.0861,
  "agrupacion_ganadora": "LA LIBERTAD AVANZA",
  "resultados_agrupaciones": [
    {
      "nombre": "LA LIBERTAD AVANZA",
      "posicion": 1,
      "votos": 769847,
      "porcentaje": "33.01 %"
    },
    {
      "nombre": "HACEMOS POR NUESTRO PAIS",
      "posicion": 2,
      "votos": 665717,
      "porcentaje": "28.55 %"
    },
    {
      "nombre": "JUNTOS POR EL CAMBIO",
      "posicion": 3,
      "votos": 519252,
      "porcentaje": "22.27 %"
    },
    {
      "nombre": "UNION POR LA PATRIA",
      "posicion": 4,
      "votos": 308016,
      "porcentaje": "13.21 %"
    },
    {
      "nombre": "FRENTE DE IZQUIERDA Y DE TRABAJADORES - UNIDAD",
      "posicion": 5,
      "votos": 31895,
      "porcentaje": "1.37 %"
    },
    {
      "nombre": "NULO",
      "posicion": 6,
      "votos": 18869,
      "porcentaje": "0.81 %"
    },
    {
      "nombre": "EN BLANCO",
      "posicion": 7,
      "votos": 17852,
      "porcentaje": "0.77 %"
    },
    {
      "nombre": "RECURRIDO",
      "posicion": 8,
      "votos": 484,
      "porcentaje": "0.02 %"
    },
    {
      "nombre": "IMPUGNADO",
      "posicion": 9,
      "votos": 180,
      "porcentaje": "0.01 %"
    }
  ]
}
```

5. Crear una api que exponga un resumen de los resultados (sumatoria total de votos) a nivel nacional
   ordenados de mayor a menor segun la cantidad de votos obtenidos por agrupación politica, votos en blanco, nulos,
   impugnados y recurridos. Adicionalmente, se debe mostrar la lista de distritos, cantidad de votos escrutados y 
   la agrupación ganadora. Debe incluirse tambien el detalle por cada distrito. <span style="color:red">**(20 puntos)**</span>

**Request**
```http
curl --location 'http://localhost:8080/elecciones/resultados'
```
**Response**
```json
{
  "distritos": [
    "string"
  ],
  "votos_escrutados": 0,
  "agrupacion_ganadora": "string",
  "resultados_nacionales": [
    {
      "nombre": "string",
      "posicion": 0,
      "votos": 0,
      "porcentaje": "string"
    }
  ],
  "resultados_distritos": [
    {
      "id": 0,
      "nombre": "string",
      "secciones": [
        "string"
      ],
      "votos_escrutados": 0,
      "porcentaje_padron_nacional": 0,
      "agrupacion_ganadora": "string",
      "resultados_agrupaciones": [
        {
          "nombre": "string",
          "posicion": 0,
          "votos": 0,
          "porcentaje": "string"
        }
      ]
    }
  ]
}
```


5. Crear los Test para que cada clase nueva agregada tenga al menos 80% de cobertura. <span style="color:red">**(25 puntos)**</span>

6. Entregar el proyecto con el archivo Dockerfile que permita ejecutar las aplicación en un contenedor. <span style="color:red">**(5 puntos)**</span>

7. Entregar el proyecto con un archivo docker-compose para poder ehjecutar en simultaneo el contenedor 
del server (la imagen **tupfrcutn/elecciones-2023:2.0.0**) en el puerto 8080 y el cliente (nuestra app) en el puerto 8081
<span style="color:red">**(15 puntos)**</span>