# 📱 MINDFIT - Plataforma de Bienestar Universitario

[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 📌 Introducción

**MINDFIT** es una aplicación móvil diseñada para optimizar la atención en los servicios de Bienestar Universitario. Su objetivo es brindar acceso ágil y digital a espacios de apoyo psicológico, desarrollo cultural y entrenamiento físico para estudiantes y personal universitario.

La aplicación permite a los usuarios agendar citas, acceder a recursos educativos y gestionar su historial de atención directamente desde su dispositivo móvil.

---

## 🎯 Objetivos

### Objetivo General
- Mejorar la experiencia del usuario en servicios de Bienestar Universitario mediante una plataforma digital accesible.

### Objetivos Específicos
- Desarrollar una interfaz intuitiva que facilite la navegación.
- Permitir la programación en línea de citas para psicología, cultura y gimnasio.
- Gestionar historias clínicas (psicología) y membresías (gimnasio).
- Controlar horarios y disponibilidad del personal y recursos.

---

## ✨ Funcionalidades Principales

### 👥 Para Usuarios
- Registro y autenticación segura.
- Programación de citas en:
  - 🧠 Psicología (con verificación de disponibilidad).
  - 🎭 Cultura (teatro, música, danza, pintura).
  - 💪 Gimnasio (con gestión de pagos).
- Visualización del historial de citas.
- Recuperación de contraseña.
- Acceso a contenido educativo sobre salud integral.
- Notificaciones recordatorias de citas próximas (pendiente de implementación).

### 🛠 Para Administradores (Futuro)
- Gestión de usuarios y roles.
- Control de agendas y disponibilidad del personal.
- Generación y consulta de historias clínicas (Psicología).
- Administración de membresías y pagos del gimnasio.
- Configuración de horarios para eventos culturales.

---

## 🖼️ Pantallas de la Aplicación

> ✨ Aquí puedes insertar imágenes representativas de cada pantalla de tu app. Te dejamos el espacio listo:

### 🔐 Login y Registro
![Pantalla de Login](ruta/a/login.png)
![Pantalla de Registro](ruta/a/registro.png)

### 🧠 Citas de Psicología
![Pantalla de Psicología](ruta/a/psicologia.png)

### 🎭 Citas Culturales
![Pantalla de Cultura](ruta/a/cultura.png)

### 💪 Registro de Gimnasio
![Pantalla de Gimnasio](ruta/a/gimnasio.png)

### 📅 Historial de Citas
![Historial de citas](ruta/a/historial.png)

### 🔑 Recuperación de Contraseña
![Recuperación de contraseña](ruta/a/recuperacion.png)

---

## 🛠️ Tecnologías Utilizadas

| Categoría         | Tecnologías                                                                 |
|-------------------|-----------------------------------------------------------------------------|
| Lenguaje          | Kotlin                                                                      |
| Arquitectura      | MVVM (Model-View-ViewModel)                                                 |
| Base de datos     | SQLite                                                                      |
| Autenticación     | SQLite                                                                      |
| UI                | Jetpack Compose                                                             |
| Asincronía        | Kotlin Coroutines                                                           |
| Navegación        | Navigation Component                                                        |
| Inyección de dependencias | Hilt                                                                |

---

## 📁 Estructura del Proyecto

```plaintext
com.example.pasculizate
├── activities/
├── fragments/
├── viewmodels/
├── utils/
├── ui.theme/
