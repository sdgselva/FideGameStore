# FideGameStore

## Descripción

**FideGameStore** es una aplicación web desarrollada con **Spring Boot** para la venta de claves digitales de videojuegos, tarjetas de regalo y otros productos digitales.

El sistema permite administrar productos, plataformas, regiones, usuarios, anuncios y claves digitales, además de gestionar órdenes de compra y enviar correos electrónicos de confirmación.

## Tecnologías utilizadas

* Java 26
* Spring Boot
* Spring Security
* Thymeleaf
* MySQL
* Firebase Storage
* Brevo API
* Bootstrap 5
* jQuery

## Funcionalidades principales

### Gestión de productos

* Crear productos.
* Editar productos.
* Eliminar productos.
* Activar o desactivar productos.
* Subir imágenes de productos.
* Asociar productos con categorías.

### Gestión de variantes

Cada producto puede tener diferentes variantes según:

* Región.
* Plataforma.

Por ejemplo:

```text
Minecraft
├── Global / Steam
├── Norteamérica / Steam
├── Global / PlayStation
└── Norteamérica / PlayStation
```

### Gestión de claves

El sistema permite administrar las claves digitales disponibles para cada variante de producto.

Las claves pueden asociarse con:

* Producto.
* Región.
* Plataforma.
* Orden de compra.

### Gestión de anuncios

Los anuncios representan las publicaciones de productos disponibles para la venta.

Cada anuncio contiene información como:

* Variante del producto.
* Precio.
* Estado activo/inactivo.

### Gestión de usuarios

El sistema permite:

* Registrar usuarios.
* Iniciar sesión.
* Administrar usuarios.
* Asignar roles.
* Activar cuentas mediante correo electrónico.
* Gestionar imágenes de perfil.

### Gestión de órdenes

Los usuarios pueden realizar compras y consultar la información de sus órdenes.

Una orden contiene:

* Usuario.
* Fecha de creación.
* Precio total.
* Estado.
* Claves adquiridas.

### Envío de correos

La aplicación utiliza la API de **Brevo** para enviar correos electrónicos mediante HTTP.

Se utiliza para funcionalidades como:

* Activación de cuentas.
* Confirmación de órdenes.
* Envío de claves digitales.

La API Key se configura mediante variables de entorno y no se almacena directamente en el código fuente.

### Almacenamiento de imágenes

Las imágenes se almacenan utilizando **Firebase Storage**.

Las credenciales de Firebase se manejan mediante configuración segura para permitir el despliegue de la aplicación sin incluir las credenciales privadas en el repositorio.

## Arquitectura

El proyecto utiliza una arquitectura basada en capas. También se utilizan servicios externos para determinadas funcionalidades:

## Estructura del proyecto

```text
src/
└── main/
    ├── java/
    │   └── com/
    │       └── fidegamestore/
    │           ├── controller/
    │           ├── domain/
    │           ├── repository/
    │           ├── service/
    │           ├── config/
    │           └── FidegamestoreApplication.java
    │
    └── resources/
        ├── static/
        ├── templates/
        └── application.properties
```

## Base de datos

El proyecto utiliza **MySQL** como sistema gestor de base de datos.

Entre las principales entidades se encuentran:

* Usuario
* Rol
* Producto
* Categoría
* Plataforma
* Región
* VarianteProducto
* Anuncio
* Llave
* Orden
* OrdenLlave
* EstadoOrden
* Ruta
* Constante

Las relaciones entre estas entidades permiten gestionar el catálogo de productos, las variantes disponibles, las claves digitales y las compras realizadas.

## Configuración

Las credenciales y datos sensibles no deben almacenarse directamente en el repositorio.

Ejemplo de configuración:

```properties
spring.datasource.url=jdbc:mysql://HOST:PORT/DATABASE
spring.datasource.username=USERNAME
spring.datasource.password=PASSWORD

brevo.api-key=${BREVO_API_KEY}
brevo.sender-email=${BREVO_SENDER_EMAIL}
brevo.sender-name=FideGameStore
```

Las variables sensibles deben configurarse como variables de entorno en el entorno donde se ejecute la aplicación.

## Ejecución local

### Requisitos

Antes de ejecutar el proyecto se necesita tener instalado:

* Java 26
* Maven
* MySQL

### Clonar el repositorio

```bash
git clone REPOSITORIO
```

### Entrar al proyecto

```bash
cd FideGameStore
```

### Compilar el proyecto

```bash
mvn clean package
```

### Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en:

```text
http://localhost:80
```

El puerto puede modificarse desde `application.properties`.

## Variables de entorno

Para ejecutar correctamente la aplicación en producción se deben configurar las variables necesarias.

### Brevo

```text
BREVO_API_KEY
BREVO_SENDER_EMAIL
```

### Firebase

Las credenciales de Firebase deben configurarse de manera segura y no deben subirse al repositorio.

## Despliegue

La aplicación está preparada para ejecutarse mediante Docker y puede desplegarse en servicios de hosting compatibles con contenedores.

El despliegue requiere configurar correctamente:

* Java 26.
* Variables de entorno.
* Credenciales de Firebase.
* Credenciales de la base de datos.
* API Key de Brevo.
* Puerto proporcionado por el proveedor de hosting.

## Seguridad

Las siguientes credenciales nunca deben incluirse directamente en Git:

```text
API Keys
Contraseñas
Credenciales de Firebase
Credenciales de MySQL
Tokens
Claves privadas
```

Se recomienda utilizar variables de entorno para almacenar información sensible.

## Estado del proyecto

Proyecto académico desarrollado como parte del curso de **Desarrollo de Aplicaciones Web y Patrones**.

## Autor

**Sol Gutiérrez**

## Licencia

Este proyecto fue desarrollado con fines académicos.
