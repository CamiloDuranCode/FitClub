# 🏋️ Fit Club — Sistema de Gestión de Gimnasio

Sistema de escritorio desarrollado en Java con arquitectura en tres capas
(Modelo – DAO – Vista) para la gestión integral del gimnasio Fit Club.

## 👥 Equipo de Desarrollo

| Integrante | Rol | Rama |
|---|---|---|
| Camilo Andrés Durán Baquero | Líder de Desarrollo | `dev-camilo` |
| Wilberto Ariza Zapata | Frontend / Persistencia (DAO) | `dev-wilberto` |
| Juan Camilo Rangel Osias | Backend / Lógica de Negocio | `dev-juancamilo` |

## 🛠️ Tecnologías

- Java (JDK 17+)
- NetBeans IDE
- JDBC
- MySQL / PostgreSQL
- Git + GitHub

## 📁 Estructura del Proyecto

```
FitClub/
├── src/
│   ├── model/      # Clases del dominio (Cliente, Membresía, etc.)
│   ├── dao/        # Acceso a datos (interfaces + implementaciones)
│   ├── service/    # Lógica de negocio
│   ├── view/       # Formularios gráficos (NetBeans)
│   └── util/       # Conexión BD, validadores, constantes
├── sql/            # Scripts de creación de tablas y datos de prueba
├── docs/           # Diagramas UML, MER y documentación de fases
└── README.md
```

## ⚙️ Configuración

1. Clonar el repositorio: `git clone https://github.com/CamiloDuranCode/FitClub.git`
2. Importar el proyecto en NetBeans.
3. Ejecutar el script `sql/schema.sql` en tu gestor de base de datos.
4. Configurar las credenciales de BD en `src/util/Conexion.java`.
5. Ejecutar la clase principal `PrincipalForm.java`.

## 📅 Período de desarrollo

23 al 29 de mayo de 2025 — Programación de Computadores III (SS462)  
Docente: Ing. Esp. Alfredo Bautista — Universidad Popular del Cesar
