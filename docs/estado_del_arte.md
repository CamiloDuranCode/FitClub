## Estado del Arte / Vigilancia Tecnológica

En la actualidad, existen diversas soluciones de software orientadas a la gestión de gimnasios y centros
de acondicionamiento físico. Con el fin de comprender el panorama tecnológico existente y orientar las
decisiones de diseño del presente proyecto, se analizaron las plataformas más representativas del mercado,
tanto a nivel internacional como local.

### Soluciones comerciales identificadas

| Solución    | Tipo               | Stack / Plataforma              | Módulos principales                                                                 | Limitación identificada                                                                                      |
|-------------|--------------------|---------------------------------|-------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| Mindbody    | SaaS (nube)        | Web + app móvil (iOS/Android)   | Membresías, reservas de clases, pagos en línea, análisis de datos                  | Costo elevado; orientado a mercados anglosajones; requiere conectividad permanente                           |
| Gym Master  | Escritorio + nube  | Windows / Web                   | Registro de miembros, control de acceso biométrico, generación de reportes         | Licenciamiento de pago; interfaz poco intuitiva para usuarios sin experiencia técnica                        |
| Perfect Gym | SaaS (nube)        | Web + app móvil                 | Automatización de marketing, gestión de membresías, análisis de comportamiento     | Enfocado en grandes cadenas de gimnasios; excesivo para establecimientos pequeños o medianos                 |
| GymSoft     | Escritorio         | Windows (Java / .NET)           | Facturación electrónica, registro de clientes, control de membresías               | Adaptado al contexto colombiano (DIAN), pero con documentación técnica limitada y sin comunidad activa       |

**Mindbody** (Mindbody Inc., 2024) es la plataforma SaaS más ampliamente utilizada en Estados Unidos y Europa.
Integra gestión de membresías, reservas de clases, pagos en línea y seguimiento de clientes a través de
aplicación móvil y tablero de análisis de datos. No obstante, su modelo de suscripción y su orientación al
mercado anglosajón lo hacen inasequible para pequeños gimnasios de la región.

**Gym Master** es un software disponible en modalidad de escritorio y en la nube, orientado a pequeños y
medianos gimnasios. Incluye módulos de registro de miembros, control de acceso biométrico y generación de
reportes, aunque su interfaz presenta una curva de aprendizaje muy alta para personal administrativo sin
formación técnica.

**Perfect Gym** es una solución europea con enfoque en la automatización de marketing, la gestión de
membresías y el análisis del comportamiento del cliente. Su enfoque apunta a cadenas grandes de gimnasios,
lo que la hace desproporcionada para el contexto de Fit Club.

**GymSoft** es una solución local desarrollada para el contexto latinoamericano, con módulos de facturación
electrónica adaptados a la normativa colombiana (DIAN). Aunque es la alternativa más cercana geográfica y
normativamente, su documentación técnica es limitada y no cuenta con una comunidad activa de soporte o
desarrollo.

### Tendencias tecnológicas identificadas

Desde el punto de vista tecnológico, los sistemas de gestión para gimnasios modernos presentan las
siguientes tendencias:

- **Arquitectura en capas y patrones MVC/MVP:** la separación entre modelo, vista y controlador es el
  estándar sobresaliente en sistemas de escritorio y web, facilitando el mantenimiento y la escalabilidad
  del código.
- **Persistencia en bases de datos relacionales:** MySQL y PostgreSQL siguen siendo las opciones más
  utilizadas en sistemas de mediana escala, tanto por su madurez como por su compatibilidad con lenguajes
  como Java a través de JDBC.
- **Acceso a datos mediante el patrón DAO:** el uso de interfaces DAO (Data Access Object) para desacoplar
  la lógica de negocio de la persistencia es una práctica consolidada en aplicaciones empresariales Java
  (Fowler, 2002).
- **Tendencia hacia SaaS y arquitecturas en la nube:** las soluciones comerciales actuales migran
  progresivamente hacia modelos basados en la nube, lo que, si bien ofrece mayor disponibilidad, requiere
  una conectividad irrompible y genera dependencia de terceros, aspectos que no son prioritarios para un
  gimnasio de tamaño pequeño como Fit Club.
- **Java como lenguaje de referencia académica:** en el ámbito académico, Java sigue siendo uno de los
  lenguajes más usados para enseñar programación orientada a objetos y desarrollo de aplicaciones de
  escritorio, especialmente con entornos como NetBeans y el acceso a datos mediante JDBC
  (Oracle Corporation, 2024).

### Posicionamiento del proyecto

El presente proyecto se diferencia de las soluciones comerciales identificadas en varios aspectos clave.
En primer lugar, no busca competir con ellas ni adoptar o imitar su escala, sino servir como ejercicio
integral de ingeniería de software en el que se apliquen los conceptos del curso: diseño orientado a
objetos, herencia, polimorfismo, patrones GRASP y principios SOLID. En segundo lugar, la solución está
contextualizada para un gimnasio pequeño de la región Caribe colombiana, donde las plataformas SaaS
resultan costosas o técnicamente inaccesibles. Finalmente, la decisión de desarrollar en Java con
arquitectura en tres capas, patrón DAO y persistencia en base de datos relacional responde tanto a los
estándares tecnológicos identificados en la vigilancia como a los objetivos pedagógicos de la asignatura.
