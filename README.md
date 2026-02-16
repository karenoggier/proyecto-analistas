# Proyecto Final - Analista de Sistemas

> **Soon Analysts!!** 🎓
> Proyecto integrador para la obtención del título intermedio de Analista de Sistemas.

Este repositorio contiene la implementación de una arquitectura de **Microservicios** 

---

## Tecnologías Utilizadas

### Backend & Core
* **Lenguaje:** Java 21 
* **Framework:** Spring Boot 4
* **Seguridad:** Spring Security + JWT (JSON Web Tokens)
* **Build Tool:** Maven

### Frontend & Diseño
* **Framework:** Next.js (React) con CSS Modules.
* **Diseño UI/UX:** Prototipado realizado en **Figma**.

### Infraestructura y Datos
**Contenedores:** **Docker** & **Docker Compose** para orquestación local.
* **Bases de Datos:** PostgreSQL (SQL) y MongoDB (NoSQL).
* **Comunicación:** Comunicación sincrónica **HTTP** entre microservicios (RestClient / WebClient).
* **Documentación:** OpenAPI (Swagger UI).

### Integraciones de Terceros
* **Pagos:** Checkout Pro de **Mercado Pago**.
* **Geolocalización:** Integración con **Photon** para búsqueda de direcciones y mapas.

---

## Arquitectura y Microservicios

El sistema está dividido en servicios independientes. A continuación se detallan los servicios disponibles y sus enlaces a la documentación:

### 1. Microservicio de Usuarios (`ms-usuarios`)
Encargado de la autenticación, registro de cuentas (Clientes/Vendedores) y gestión de roles.

* **Puerto:** `8080`
* **Context Path:** `/usuariosMs`
* **Documentación (Swagger):** [Ver API Docs](http://localhost:8080/usuariosMs/swagger-ui/index.html)

### 2. Microservicio de Catálogo (`ms-catalogo`)
Encargado de la gestión de productos, perfiles de tiendas (vendedores) y pedidos recibidos.

* **Puerto:** `8081`
* **Context Path:** `/catalogoMs`
* **Documentación (Swagger):** [Ver API Docs](http://localhost:8081/catalogoMs/swagger-ui/index.html)

### 3. Microservicio de Pedidos (`ms-pedido`)
Encargado de orquestar el proceso de compra. Búsqueda de productos, carritos de compra y realizacion de pedidos.

* **Puerto:** `8082`
* **Context Path:** `/pedidoMs`
* **Docs:** [Swagger UI](http://localhost:8082/pedidoMs/swagger-ui/index.html)

> **Nota:** Para ver la documentación, asegurate de tener el microservicio corriendo localmente.

---

## Autores

* **Karen Juliana Oggier** 
* **Ana Carolina Ramos Bonvin**
* **Juan Marco Garcés**
