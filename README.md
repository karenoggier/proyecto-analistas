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
* **Comunicación en Tiempo Real:** Spring WebSocket + STOMP.
* **Build Tool:** Maven

### Frontend & Diseño
* **Framework:** Next.js (React) con CSS Modules.
* **Notificaciones UI:** `react-hot-toast`.
* **Diseño UI/UX:** Prototipado realizado en **Figma**.

### Infraestructura y Datos
* **Contenedores:** **Docker** & **Docker Compose** para orquestación local.
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
Encargado de orquestar el proceso de compra. Búsqueda de productos, carritos de compra, realizacion de pedidos y notificaciones.

* **Puerto:** `8082`
* **Context Path:** `/pedidoMs`
* **Documentación (Swagger):** [Ver API Docs](http://localhost:8082/pedidoMs/swagger-ui/index.html)

### 4. Microservicio de Pagos (`ms-pago`)
Encargado de la gestión de transacciones financieras. Integra el Checkout Pro de Mercado Pago para procesar pagos y gestionar Webhooks (notificaciones de estado).

* **Puerto:** `8083`
* **Context Path:** `/pagoMs`
* **Documentación (Swagger):** [Ver API Docs](http://localhost:8083/pagoMs/swagger-ui/index.html)

> **Nota:** Para ver la documentación, asegurate de tener el microservicio corriendo localmente.

---

## Sistema de Notificaciones en Tiempo Real

Se implementó un sistema de comunicación **Full-Duplex** para mejorar la experiencia de usuario (UX):

* **Protocolo:** STOMP sobre WebSockets con **SockJS** para asegurar compatibilidad.
* **Canales Privados:** Uso de `/user/queue/updates` para envío de mensajes segmentados por usuario autenticado.
* **Persistencia NoSQL:** Las notificaciones se almacenan en una colección de **MongoDB** integrada en el microservicio de pedidos, permitiendo consultar el historial sin penalizar el rendimiento de la DB relacional.

---

## Mantenimiento Automático (Scheduled Tasks)

El sistema cuenta con procesos de limpieza automáticos para garantizar la integridad de los datos:
* **Limpieza de Pedidos:** Borrado de pedidos en estado `PENDIENTE` con más de 2 horas de antigüedad.
* **Deduplicación de Pagos:** Tarea programada que elimina intentos de pago huérfanos y consolida un único registro final (Aprobado/Rechazado) por pedido.

---

## Diseño y UX/UI

El diseño de interfaz (estimativo/idea) fue realizado en figma.

* **Prototipo:** [Acceder al diseño en Figma](https://www.figma.com/design/Gm0sJx4la39Sqz5cDaDHXz/Seminario-Integrador?node-id=60-2&t=6rtdqOHcMa7WAk31-1)

---

## Instalación y Configuración

### 1. Requisitos Previos
* Java 21 & Maven.
* Docker & Docker Compose.
* Node.js & npm.
* Ngrok

### 2. Configuración de Pagos (Mercado Pago)
Para las pruebas de integración, utilizá las credenciales de prueba que se encuentran en el siguiente archivo del repositorio:
* 📄 **[Mocks - Mercado Pago.xlsx](https://frsfutneduar-my.sharepoint.com/:x:/g/personal/koggier_frsf_utn_edu_ar/IQCmphXqySjsRIoaW2xptUFoAcuGCSz3PnUffChnBQV4U90?e=MFLIoF)**
* 💳 **Tarjetas de Prueba:** Podés encontrar números de tarjeta para testear diferentes escenarios (pago aprobado, rechazado, etc.) aquí: [Tarjetas de prueba de Mercado Pago](https://www.mercadopago.com.ar/developers/es/docs/checkout-pro/integration-test/test-purchases).

### 3. Túnel de Webhooks con Ngrok
Como Mercado Pago necesita enviar notificacionesa al servidor local (ya que no está desplegado), se usa **ngrok** para exponer el microservicio de pagos:

1. **Instalar Ngrok:** Descargalo desde [ngrok.com](https://ngrok.com/download).
2. **Crea una cuenta/Inicia Sesión en Ngrok**
2. **Levantar el túnel:** Ejecutá el siguiente comando en la terminal de ngrok (ejecutándolo)

   ngrok config add-authtoken 'TU_AUTH_TOKEN'

   ngrok http 3000

### 4. Mercado Pago en el frontend
Para que el botón de pago funcione, se necesita instalar la SDK de Mercado Pago en el proyecto de Next.js:

cd frontend

npm install @mercadopago/sdk-react

---

## Ejecución del sistema

### 1. Infraestructura (Bases de datos):
* Para levantarla:

cd docker

docker-compose up -d

* Para bajarla:

cd docker

docker compose down

### 2. Ejecutar el frontend:
cd frontend

npm run dev

---

## Autores

* **Karen Juliana Oggier** 
* **Ana Carolina Ramos Bonvin**
* **Juan Marco Garcés**
