# 🏨 Sistema de gestion para un hotel

## 📋 Descripción general
**HotelGUI-BD** es una aplicación de escritorio desarrollada en **Java (JavaFX)** con conexión a una base de datos **MySQL**, diseñada para la gestión integral de un hotel.  
Permite administrar **clientes**, **habitaciones** y **reservaciones** mediante una interfaz gráfica moderna e intuitiva.  

El sistema implementa una arquitectura **MVC (Modelo–Vista–Controlador)** para mantener una separación clara entre las capas de datos, lógica y presentación.

---

## ⚙️ Tecnologías utilizadas

| Componente | Tecnología |
|-------------|-------------|
| Lenguaje principal | Java |
| Interfaz gráfica | JavaFX |
| Base de datos | MySQL |
| Conexión BD | JDBC |
| IDE recomendado | IntelliJ IDEA |
| Control de versiones | Git + GitHub |

---

## 🧩 Estructura del proyecto

```
src/
 ├─ com.sistema.hotelguibd/
 │   ├─ modelo/         → Clases DTO (Cliente, Habitacion, Reservacion)
 │   ├─ datos/          → Acceso a datos (consultas SQL, conexión JDBC)
 │   ├─ logica/         → Reglas de negocio y validaciones
 │   └─ controller/     → Controladores JavaFX
resources/
 ├─ com/sistema/hotelguibd/vista/ → Archivos .fxml
```

---

## 🗄️ Estructura de la base de datos

### Nombre: `hotel`

```sql
CREATE DATABASE hotel;
USE hotel;
```

### Tablas principales

#### 🧍 cliente
| Campo | Tipo | Descripción |
|--------|------|-------------|
| id | INT (PK, AI) | Identificador único |
| nombre | VARCHAR(80) | Nombre del cliente |
| primerApellido | VARCHAR(80) | Primer apellido |
| segundoApellido | VARCHAR(80) | Segundo apellido |
| identificacion | VARCHAR(20) | Cédula o pasaporte |
| fechaNacimiento | DATE | Fecha de nacimiento |

#### 🏠 habitacion
| Campo | Tipo | Descripción |
|--------|------|-------------|
| id | INT (PK, AI) | Identificador de la habitación |
| numero | INT | Número asignado |
| tipo | VARCHAR(80) | Tipo (simple, doble, suite...) |
| descripcionTipo | VARCHAR(80) | Descripción del tipo |
| estado | VARCHAR(80) | Estado (disponible, ocupada, limpieza...) |
| descripcionEstado | VARCHAR(80) | Detalle del estado |
| precio | DOUBLE | Precio por noche |
| capacidad | INT | Capacidad máxima |

#### 📅 reservacion
| Campo | Tipo | Descripción |
|--------|------|-------------|
| id | INT (PK, AI) | Identificador de la reserva |
| idHabitacion | INT (FK) | Referencia a habitación |
| idCliente | INT (FK) | Referencia a cliente |
| fechaReservacion | DATE | Fecha en que se realiza la reserva |
| fechaLlegada | DATE | Fecha de llegada |
| cantidadNoches | INT | Número de noches |
| fechaSalida | DATE | Fecha de salida |
| preciototal | DOUBLE | Monto total de la reserva |
| descuento | DOUBLE | Descuento aplicado |

---

## 💾 Cómo clonar y ejecutar el proyecto

### 1️⃣ Clonar el repositorio
```bash
git clone https://github.com/SebastianGomezSolis/HotelGUI-BD.git
cd HotelGUI-BD
```

### 2️⃣ Importar el proyecto
Abre el proyecto en tu IDE (IntelliJ, Eclipse o NetBeans) y asegúrate de tener configurado **JavaFX**.

### 3️⃣ Crear la base de datos
1. Abre **MySQL Workbench** o **phpMyAdmin**.  
2. Ejecuta el scrip que esta en el proyecto llamado: hotel.sql

---

### 4️⃣ Ejecutar la aplicación
- Ejecuta la clase principal (por ejemplo `Main.java` o `HotelGUI.java`).
- Se abrirá la interfaz principal de gestión hotelera.

---

## 🧠 Funcionalidades principales

### 👤 Gestión de clientes
- Registrar nuevos clientes.  
- Consultar, editar o eliminar registros.  
- Buscar por nombre o identificación.  
- Mostrar edad y detalles del cliente.  

### 🏠 Gestión de habitaciones
- Registrar habitaciones nuevas.  
- Cambiar su estado (disponible, ocupada, mantenimiento...).  
- Consultar capacidad, tipo y precio.  
- Actualizar información y eliminar habitaciones.  

### 📅 Gestión de reservaciones
- Crear nuevas reservaciones asignando cliente y habitación.  
- Calcular el total según cantidad de noches.  
- Aplicar descuentos.  
- Editar y eliminar reservaciones existentes.  

---

## 🧮 Arquitectura del sistema

El sistema está basado en el patrón **MVC (Modelo–Vista–Controlador):**

| Capa | Descripción |
|------|--------------|
| **Modelo** | Representa las entidades (`Cliente`, `Habitacion`, `Reservacion`). |
| **Vista** | Archivos `.fxml` diseñados con **JavaFX Scene Builder**. |
| **Controlador** | Gestiona eventos y comunicación entre vista y modelo. |
| **Datos** | Clases que manejan la conexión y consultas SQL mediante JDBC. |

---

## 📚 Diagrama conceptual
El sistema se apoya en tres entidades principales:

```
Cliente ───< Reservacion >─── Habitacion
```

- Un **cliente** puede tener múltiples **reservaciones**.  
- Una **habitación** puede estar asociada a varias **reservaciones** en distintos periodos.  
- La **reservacion** actúa como entidad intermedia que conecta ambas tablas.  

---

## 🧑‍💻 Autor
**Sebastián Gómez Solís**  
---
