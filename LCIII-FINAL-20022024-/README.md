# LCIII-FINAL-20022024
# API REST de Gestión de Materias

## Contexto del Negocio:
Es el encargado de diseñar un sistema para la carga de notas de alumnos en sus respectivas materias y determinar su condición de regularidad según la calificación obtenida.

## Enpoint a diseñar

### Registro de materias **(10 pts)**

- Endpoint: ```/gestion/materias```
- Método: ```POST```
- Acción: Registrar materias en el sistema (por ejemplo: Literatura, Matemática, Historia, Ingles)
- Request:
```
  {
    "materia": "Literatura"
  }
```
- Respuesta: listar las materias cargadas
```
  {
    "materias": ["Literatura",
                 "Matematica",
                 "Historia",
                 "Ingles"
                ]
  }
```

### Registrar materias en alumnos **(10 pts)**

- Endpoint: ```/gestion```
- Método: ```POST```
- Acción: obtener las materias cargadas en el sistema previamente y registrar los alumnos en la base de datos con un estado inicial de calificación en "Pendiente"
- Request:
```
  {
    "legajo": "A001",
    "nombre": "Fernando",
    "materias": ["Literatura",
                 "Matematica",
                 "Historia",
                 "Ingles"
                ]
  }
```
- Respuesta:
```
  {
    "legajo": "A001",
    "nombre": "Fernando",
    "materias": [
                  {
                    "materia": "Literatura",
                    "calificacion": 0,
                    "estado": "Pendiente"
                  },
                  {
                    "materia": "Matematica",
                    "calificacion": 0,
                    "estado": "Pendiente"
                  },
                  {
                    "materia": "Historia",
                    "calificacion": 0,
                    "estado": "Pendiente"
                  },
                  {
                    "materia": "Ingles",
                    "calificacion": 0,
                    "estado": "Pendiente"
                  }
                ]
  }
```

### Registrar nota del alumno **(10 pts)**

- Endpoint: ```/gestion```
- Método: ```PUT```
- Acción: registrar la calificación por materia y actualizar el estado siguiendo la escala descripta en las reglas de negocio
 Request:
```
  {
    "legajo": "A001",
    "materia": "Literatura",
    "calificacion": 10,
  }
```
- Respuesta:
```
  {
    "legajo": "A001",
    "materia": "Ingles",
    "estado": "Promocionado"
  }
```

### Listar informe académico **(10 pts)**

- Endpoint: ```/gestion```
- Método: ```GET```
- Acción: listar las materias con el porcentaje de alumnos en cada estado disponible junto al resultado general del curso (ver reglas de negocio), debe permitir que de forma opcional se pueda filtrar por materia
- Respuesta: 
```
  {
    "materia": "Literatra",
    "estado":  {
                 "Libre": "25%",
                 "Regular": "25%",
                 "Promocionado": "50%"
                }
    "resultado": "Fracaso"
  }
```

## Reglas del Negocio:

### Calculo de estado académico

### Definición de estado **(10 pts)**
- El alumno tendra un estado académico por cada materia según la calificación registrada

* LIBRE: Nota menor a 4
* REGULAR: Nota mayor o igual a 4 y menor a 9
* PROMOCIONADO: Nota mayor o igual a 9

### Definición de resultado general de la materia **(10 pts)**
- La materia se considera EXITOSA cuando el porcentaje de alumnos regulares y procionados supera el 60%, caso contrario el resultado de la materia se considerará como FRACASO


---
### Testing **(40 pts)**
- Es obligatorio la creación de testing unitario sobre la lógica de negocio de todo el proyecto
