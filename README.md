# GastoSmart – Control de gastos

**GastoSmart** es una aplicación de escritorio, hecha en Java con Swing, que ayuda a estudiantes universitarios a registrar sus ingresos y gastos, organizarlos por categorías, controlar su presupuesto mensual, ahorrar para metas y consultar a un asistente si una compra es conveniente.

## Requisitos

| Requisito | Detalle |
| --- | --- |
| Java | Versión 17 o superior |
| IDE | Cualquiera compatible con Java y Maven (Apache NetBeans, IntelliJ IDEA, Eclipse, Visual Studio Code con el Extension Pack for Java, etc.) |
| Sistema operativo | Cualquiera con Java instalado |

## Instalación y ejecución

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Mbenja19/GastoSmart.git
   ```
2. Abre la carpeta `GastoSmart` como proyecto Maven en tu IDE.
3. Abre el archivo `src/App.java`.
4. Ejecuta la clase principal AppGUI (la que contiene el método main) con la opción de ejecución de tu IDE.

## Manual de usuario

### 1. Pantalla de inicio

Al abrir el programa aparece un formulario con cuatro datos:

| Campo | Regla |
|---|---|
| Tu nombre | Obligatorio; solo letras y espacios |
| Tu correo | Obligatorio; formato válido (ejemplo: `nombre@correo.com`) |
| Dinero con el que empiezas | Número mayor o igual a 0 |
| Presupuesto del mes | Número mayor o igual a 0 (`0` = sin presupuesto) |

Presiona **Comenzar** (o Enter) para entrar a la ventana principal. Si algún dato es incorrecto, se muestra un mensaje de error y no se avanza.

### 2. Ventana principal

En la parte superior siempre se muestra tu **saldo actual**. Debajo hay cinco pestañas:

#### Resumen
Muestra el saldo actual, el **dinero libre**, el presupuesto del mes, lo gastado, lo que te queda y una barra de progreso del presupuesto.

- **Cambiar presupuesto del mes:** define un nuevo monto (`0` desactiva el presupuesto). Si ya gastaste este mes, el programa pregunta si el nuevo presupuesto debe **empezar de cero** (sin contar lo ya gastado) o seguir contando.

#### Movimientos
Tabla con todas tus transacciones (ingresos, gastos y ahorros). Puedes ordenarla haciendo clic en el encabezado de cada columna.

- **Registrar ingreso:** monto mayor a 0 y descripción.
- **Registrar gasto:** elige una categoría activa, escribe el monto y la descripción. Si el monto es mayor a tu saldo, el gasto se rechaza.
- **Filtrar por categoría:** el selector superior muestra solo los movimientos de la categoría elegida.

Después de registrar un gasto, el programa puede mostrar avisos:
- cuánto te queda de la **reserva** de esa categoría, o que la reserva se completó y la categoría fue eliminada;
- que te quedan pocos recursos del presupuesto (20 % o menos);
- que **superaste** el presupuesto del mes.

#### Categorías
Las categorías agrupan los gastos (Comida, Transporte, etc.). Siempre existe la categoría **General**.

- **Agregar categoría:** nombre (no se permiten repetidos), si es **esencial** (necesidad) y el monto que quieres **reservar** para ella.
- **Activar / desactivar:** selecciona una fila y presiona el botón. Una categoría desactivada no permite registrar gastos y, si es esencial, deja de reservar dinero.

#### Metas de ahorro
Objetivos de ahorro con una barra de progreso por cada meta.

- **Crear meta:** nombre y monto que necesitas juntar.
- **Abonar a la meta seleccionada:** el monto no puede superar tu **dinero libre** ni lo que falta para completar la meta. El abono se descuenta de tu saldo y se registra como un movimiento de tipo *Ahorro*.
- Al completar la meta, el programa te felicita.

#### Asistente de compra
Escribe qué quieres comprar y su precio, y presiona **Evaluar compra**. El resultado puede ser:

| Resultado | Cuándo ocurre |
|---|---|
| **VIABLE** (verde) | El precio es menor o igual al 30 % de tu dinero libre |
| **VIABLE CON RIESGO** (naranja) | El precio usa más del 30 % de tu dinero libre, pero no lo supera |
| **NO VIABLE** (rojo) | El precio supera tu dinero libre |

Además, muestra cuánto te quedaría libre (o cuánto te falta) y un **consejo financiero** al azar.
¡Hasta pronto, ...!
```

> Los datos se guardan solo mientras el programa está abierto. Al salir, la información registrada se pierde.
> Los gastos de una categoría eliminada (al completar su reserva) siguen apareciendo en la tabla de movimientos, pero ya no se pueden filtrar por esa categoría.
