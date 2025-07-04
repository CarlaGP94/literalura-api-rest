# Literalura API REST

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC3333?style=for-the-badge&logo=flyway&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)

Una API REST robusta desarrollada con Spring Boot para gestionar y consultar una vasta biblioteca de libros y autores, obtenida de la API externa de Gutendex. Esta API permite buscar, listar y filtrar libros, así como gestionar la autenticación de usuarios mediante JWT.

---

## 📚 Descripción del Proyecto

`Literalura API REST` es un servicio backend diseñado para catalogar y ofrecer una interfaz programática para una base de datos de libros y autores. La aplicación integra datos de la **API de Gutendex**, cargando una cantidad significativa de información al inicio para proporcionar una experiencia de búsqueda rica.

El proyecto sigue una arquitectura de capas clara, promoviendo la modularidad, la mantenibilidad y la escalabilidad. Incorpora principios de Clean Code y buenas prácticas de diseño de APIs REST, incluyendo seguridad basada en JWT para la autenticación de usuarios.

---

## ✨ Características Principales

* **Carga de Datos Inicial:** Importa automáticamente una gran cantidad de libros y autores desde la API de Gutendex al iniciar la aplicación (configurable en `DataInitializer`).
* **Búsqueda de Libros:**
    * Buscar libros por título (con validación de entrada).
    * Listar todos los libros disponibles.
    * Filtrar libros por idioma.
* **Gestión de Autores:**
    * Listar todos los autores registrados.
    * Listar autores vivos en un determinado año.
* **Autenticación de Usuarios (JWT):**
    * Endpoint de inicio de sesión (`/login`) que devuelve un JSON Web Token (JWT) al autenticar correctamente al usuario.
    * Protección de endpoints para requerir un token JWT válido.
* **Estructura Limpia:** Organización clara por capas (`controller`, `service`, `repository`, `model`, `record`, `external`, `config/security`).
* **Persistencia:** Utiliza Spring Data JPA con PostgreSQL como base de datos.
* **Migraciones de Base de Datos:** Gestionadas con Flyway para un control de versiones de esquema robusto.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 17+
* **Framework:** Spring Boot 3
* **Base de Datos:** PostgreSQL
* **ORM:** Spring Data JPA / Hibernate
* **Migraciones DB:** Flyway
* **Seguridad:** Spring Security, JWT (JSON Web Tokens)
* **Validación:** Jakarta Validation (Bean Validation)
* **Cliente HTTP:** Spring WebClient para la API de Gutendex
* **Herramienta de Construcción:** Maven

---

## 🚀 Cómo Ejecutar el Proyecto

### Prerrequisitos

* JDK 17+ instalado.
* Maven instalado.
* PostgreSQL instalado y funcionando.
* (Opcional) Una herramienta para probar APIs REST como [Insomnia](https://insomnia.rest/download) o [Postman](https://www.postman.com/downloads/).

### Configuración de la Base de Datos

1.  Asegúrate de tener un servidor PostgreSQL en ejecución.
2.  Crea una nueva base de datos para el proyecto (ej. `literalura_db`).
    ```sql
    CREATE DATABASE literalura_db;
    ```
3.  Actualiza el archivo `src/main/resources/application.properties` con tus credenciales de PostgreSQL:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/literalura_db
    spring.datasource.username=tu_usuario_postgres
    spring.datasource.password=tu_contraseña_postgres
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.jpa.hibernate.ddl-auto=none # Flyway se encarga de las migraciones
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true

    # Configuración de Flyway
    spring.flyway.enabled=true
    spring.flyway.locations=classpath:/db/migration # Directorio de tus scripts de migración

    # Configuración del JWT
    api.security.token.secret=TU_SECRETO_SUPER_SEGURO_PARA_JWT # ¡CAMBIA ESTO POR UNA CADENA LARGA Y SEGURA!
    ```

### Clonar y Ejecutar

1.  Clona el repositorio:
    ```bash
    git clone [https://github.com/CarlaGP94/literalura-api-rest.git](https://github.com/CarlaGP94/literalura-api-rest.git)
    cd literalura-api-rest
    ```
2.  Construye el proyecto con Maven:
    ```bash
    mvn clean install
    ```
3.  Ejecuta la aplicación Spring Boot:
    ```bash
    mvn spring-boot:run
    ```
    La aplicación se iniciará en `http://localhost:8080`. Flyway ejecutará las migraciones de la base de datos automáticamente y `DataInitializer` cargará los datos iniciales de Gutendex (esto puede tardar unos minutos la primera vez).

---

## 🔍 Endpoints de la API (Ejemplos)

Una vez que la aplicación esté ejecutándose y los datos cargados, puedes interactuar con ella:

### Autenticación

* **`POST /login`**
    * **Descripción:** Autentica a un usuario y devuelve un token JWT.
    * **Body (JSON):**
        ```json
        {
            "login": "usuario",
            "password": "contraseña"
        }
        ```
    * **Respuesta (200 OK):**
        ```json
        {
            "token": "eyJhbGciOiJIUzI1Ni..."
        }
        ```
    * *Nota:* Actualmente, los usuarios deben ser insertados manualmente en la base de datos (con la contraseña encriptada) para poder iniciar sesión.

### Libros

* **`GET /book`**
    * **Descripción:** Lista todos los libros disponibles en la base de datos.
    * **Requiere Token JWT:** SÍ (agregar `Authorization: Bearer <tu_token_jwt>` en los headers)
* **`GET /book/search?title=tu_titulo`**
    * **Descripción:** Busca libros por título (insensible a mayúsculas/minúsculas, contiene).
    * **Parámetros:** `title` (obligatorio, no puede estar en blanco).
    * **Requiere Token JWT:** SÍ
* **`GET /book/language?language=ESPANOL`**
    * **Descripción:** Lista libros por un idioma específico.
    * **Parámetros:** `language` (ej. `ESPANOL`, `INGLES`, `FRANCES`).
    * **Requiere Token JWT:** SÍ

### Autores

* **`GET /author`**
    * **Descripción:** Lista todos los autores registrados en la base de datos.
    * **Requiere Token JWT:** SÍ
* **`GET /author/active_in?startYear=1900&endYear=1999`**
    * **Descripción:** Lista autores vivos en un año específico.
    * **Parámetros:** `year` (año numérico).
    * **Requiere Token JWT:** SÍ

---

## 🤝 Contribuciones

¡Las contribuciones son bienvenidas! Si encuentras un error o tienes una sugerencia de mejora, por favor, abre un "Issue" o envía un "Pull Request".

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.

---

## 📧 Contacto

Carla Pasandi - [https://www.linkedin.com/in/carla-pasandi]

Proyecto desarrollado como parte del programa One Oracle Next Education - Alura Latam.
