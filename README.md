# TallerEvaluativo_DOSW-1
Taller evaluativo de sistema de reportes financieros

**Integrantes:**
- Juan Pablo Caballero Castellanos.
- Oscar Sanchez Porras.
- Robinson Steven Nuñez.
- David Santiago Palacios.
- Diego Fernando Chavarro.

**Nombre De la Rama:**

`develop`

---

## Diagramas UML

### *Diagrama de Clases:*

![alt text](docs/uml/diagramaClases.png)

### *Diagrama de Componentes Especifico:*

![alt text](docs/uml/DiagramaComponentesEspecificos.drawio.png)
En la arquitectura planteada, los controladores actúan como punto de entrada del sistema, recibiendo las solicitudes del usuario en forma de DTOs ReportDTO, TransactionDTO y devolviendo las respuestas adecuadas ReportResponseDTO.
Estos controladores delegan la lógica de negocio a los servicios, de manera que ReportController se comunica con ReportService y TransactionController con TransactionService. 
A su vez, los servicios utilizan los mappers ReportMapper, TransactionMapper para transformar los datos entre DTOs y entidades del dominio antes de interactuar con la capa de persistencia. 
Los servicios invocan a los repositorios ReportRepository, TransactionRepository para guardar o consultar información en el Backend, asegurando la persistencia de los reportes y transacciones. 
Además, en el caso de los reportes, ReportService se apoya en la ReportDecoratorFactory para extender dinámicamente las funcionalidades del reporte antes de su almacenamiento o presentación. 
De esta forma, cada capa cumple un rol específico y las dependencias fluyen de manera clara desde la entrada del sistema hasta la persistencia de datos.
---

#### **Principios SOLID**


###### *Single Responsibility Principle:*

Cada clase se encarga de un unica cosa y esto se ve reflejado en:

ReportBuillder ya que se encarga de construir el reporte y con BaseReportRecourse generamos la representación de ese reporte.

La Transaction se encarga de los movimientos de los datos del reporte.

Las clases que extienden de ReportDecorator se encargan de diferentes de reportes pero todas hacen algo diferente.



###### *Open/Closed Principle:*


Con la interfaz IReportRecourse y la clase abstracta ReportDecorator nos permiten crear nuevas subclases y que a su vez estas puedan
definir su propio comportamiento, gracias a esta abstracción, cada decorador puede sobreescribir únicamente lo que necesita y 
asi podemos añadir funcionalidades de los gráficos, marcas de agua y resumenes. 



###### *Interface Segregation Principle:*

Se diseñaron interfaces concretas y con pocos metodos para establecer contratos para los recursos
como IReportRecourse y DecoratorFactory.


###### *Dependency Inversion Principle*

ReportDecoratorBuilder es un modulo de alto nivel por lo que no estamos dependiendo de los modulos de bajo nivel ya que
define el flujo usando IReportRecourse y implementando DecoratorFactory asi el builder trabaja con abstracciones no con clases concretas.

El builder orquesta la composicion usando unicamente estas abstracciones y que decoradores aplicar y en que orden pero no como estan implementados