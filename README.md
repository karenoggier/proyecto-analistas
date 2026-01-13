# Proyecto Final - Analista de Sistemas

> **Soon Analysts!!** 🎓
> Proyecto integrador para la obtención del título intermedio de Analista de Sistemas.

Este repositorio contiene la implementación de una arquitectura de **Microservicios** 

---

## Tecnologías Utilizadas

* **Lenguaje:** Java 17+
* **Framework:** Spring Boot 3
* **Seguridad:** Spring Security + JWT (JSON Web Tokens)
* **Documentación:** OpenAPI (Swagger UI)
* **Base de Datos:** PostgreSQL / MongoDB
* **Build Tool:** Maven

---

## Arquitectura y Microservicios

El sistema está dividido en servicios independientes. A continuación se detallan los servicios disponibles y sus enlaces a la documentación:

### 1. Microservicio de Usuarios (`ms-usuarios`)
Encargado de la autenticación, registro de cuentas (Clientes/Vendedores) y gestión de roles.

* **Puerto:** `8080`
* **Context Path:** `/usuariosMs`
* **Documentación (Swagger):** [Ver API Docs](http://localhost:8080/usuariosMs/swagger-ui/index.html)

### 2. Microservicio de Catálogo (`ms-catalogo`)
Encargado de la gestión de productos, categorías y el perfil público de las tiendas (Vendedores). Utiliza base de datos documental (NoSQL) para alta performance de lectura.

* **Puerto:** `8081`
* **Context Path:** `/catalogoMs`
* **Documentación (Swagger):** [Ver API Docs](http://localhost:8081/catalogoMs/swagger-ui/index.html)

> **Nota:** Para ver la documentación, asegurate de tener el microservicio corriendo localmente.

---

## Autores

* **Karen Juliana Oggier** 
* **Ana Carolina Ramos Bonvin**
* **Juan Marco Garcés**
