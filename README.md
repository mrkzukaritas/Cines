# 🎬 Cines

Aplicación desarrollada en **Java** que permite consultar películas disponibles en diferentes cines y **comparar sus precios**, facilitando al usuario la elección de la mejor opción.

El sistema también permite gestionar la información de películas, cines y reservas mediante operaciones **CRUD (Crear, Leer, Actualizar y Eliminar)**.

## 📌 Funcionalidades

* 🎥 Consulta de películas disponibles.
* 🏢 Gestión de diferentes cines.
* 💰 Comparación de precios entre cines.
* 🎟️ Gestión de reservas.
* 👤 Inicio de sesión según el rol del usuario.
* 🔐 Acceso diferenciado para **Administrador** y **Cliente**.
* 📝 Operaciones CRUD para la administración de la información.
* 📋 Consulta de las reservas realizadas.

## 🏗️ Arquitectura

El proyecto está desarrollado siguiendo una arquitectura organizada por responsabilidades, separando la lógica de la aplicación en diferentes componentes.

La estructura principal se encuentra dentro de `src` y está orientada a separar:

* **Modelo:** contiene las clases y entidades que representan la información del sistema, como usuarios, películas, cines y reservas.
* **Vista:** se encarga de la interfaz con la que interactúa el usuario.
* **Controlador / lógica:** gestiona las acciones realizadas por el usuario y conecta la interfaz con los datos y las funcionalidades del sistema.
* **Persistencia:** permite almacenar y gestionar la información utilizada por la aplicación.

Esta organización facilita el mantenimiento, comprensión y ampliación del proyecto.

## 👥 Roles de usuario

El sistema cuenta con dos tipos de usuarios:

### 🔴 Administrador

Tiene acceso a las funcionalidades de gestión del sistema, incluyendo las operaciones CRUD sobre la información.

### 🔵 Cliente

Puede consultar las películas y cines disponibles, comparar precios y realizar o consultar sus reservas.

## 🚀 Instalación y ejecución

Clona el repositorio:

```bash
git clone https://github.com/mrkzukaritas/Cines.git
```

Después, abre el proyecto en un IDE compatible con **Java** y ejecuta la aplicación.

### 🔑 Usuarios precargados

El proyecto cuenta con usuarios de prueba **precargados**, por lo que después de clonar y ejecutar la aplicación puedes iniciar sesión directamente con las siguientes credenciales:

| Rol           | Email              | Contraseña   |
| ------------- | ------------------ | ------------ |
| Administrador | `admin@cine.com`   | `Admin123`   |
| Cliente       | `cliente@cine.com` | `Cliente123` |

No es necesario crear estos usuarios manualmente.

## 🛠️ Tecnologías

* **Java**
* Programación Orientada a Objetos (POO)
* Arquitectura por capas / separación de responsabilidades
* Operaciones CRUD
* Gestión de usuarios y roles

## 📄 Licencia

Este proyecto está disponible bajo la licencia **MIT**.
