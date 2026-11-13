# GastoSmart – Control de gastos

**GastoSmart** es una aplicación de consola desarrollada en Java, con programación orientada a objetos, que ayuda a estudiantes universitarios a registrar sus ingresos y gastos, organizarlos por categorías y controlar su presupuesto mensual.

## Requisitos

| Requisito | Detalle |
| --- | --- |
| Java | JDK 8 o superior (el programa usa `LocalDate`) |
| Editor (opcional) | Visual Studio Code con el *Extension Pack for Java* |
| Sistema operativo | Cualquiera con Java instalado |

## Instalación y ejecución

### Desde Visual Studio Code

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Mbenja19/GastoSmart.git
   ```
2. Abre la carpeta `GastoSmart` en Visual Studio Code.
3. Abre el archivo `src/App.java`.
4. Pulsa **Run** (▶) sobre el método `main`.
5. El programa se ejecuta en la terminal integrada.

## Manual de usuario

### 1. Inicio del programa y configuración inicial

Al ejecutar GastoSmart, el sistema solicita cuatro datos, uno por uno:

| Dato | Descripción |
| --- | --- |
| **Tu nombre** | Nombre con el que te saludará el sistema. |
| **Tu correo** | Correo electrónico del usuario. |
| **Dinero con el que empiezas** | Saldo inicial disponible, en soles (S/). |
| **Presupuesto máximo para este mes** | Límite de gasto para el mes, en soles (S/). |


> Ningún dato puede quedar vacío, y los montos deben ser números válidos.

### Menú principal

Después de la configuración, aparece el menú principal. El **saldo actual** se muestra siempre en el encabezado y se actualiza después de cada operación.

```
========== MENÚ (saldo: S/ 1000.00) ==========
1. Registrar ingreso
2. Registrar gasto
3. Ver movimientos
4. Ver saldo y presupuesto del mes
5. Agregar categoría
6. Ver categorías
7. Filtrar movimientos por categoría
8. Activar o desactivar una categoría
9. Crear una meta de ahorro
10. Abonar a una meta de ahorro
11. Ver mis metas de ahorro
12. Asistente de compra: ¿Puedo comprar esto?
0. Salir
0. Salir
Elige una opción:
```

Para usar una función, escribe el **número** de la opción y pulsa **Enter**.

### Opción 1: Registrar ingreso

Sirve para anotar dinero que recibes (mesada, sueldo, venta, propina, etc.). El sistema pide:

1. **Monto del ingreso**: debe ser mayor que cero.
2. **Descripción**: texto breve que identifique el ingreso.

El saldo aumenta automáticamente.

### Opción 2: Registrar gasto

Sirve para anotar cada compra o pago que realizas. El sistema pide:

1. **Número de la categoría**: se muestra la lista de categorías y eliges una escribiendo su número.
2. **Monto del gasto**: debe ser mayor que cero y no puede superar tu saldo disponible.
3. **Descripción**: texto breve que identifique el gasto.

El saldo disminuye automáticamente.

### Opción 3: Ver movimientos

Muestra el historial completo de ingresos y gastos. Cada línea indica el número de movimiento, el tipo, la categoría, la fecha, el monto y la descripción.

```
--- MOVIMIENTOS ---
#1 | INGRESO | Ingreso | 2026-11-01 | S/ 100.00 | Venta de una artesanía
#2 | GASTO   | General | 2026-11-01 | S/ 5.00 | gaseosa
```

> La fecha se asigna automáticamente: no necesitas escribirla. Si aún no hay movimientos, el sistema te lo informa.

### Opción 4: Ver saldo y presupuesto del mes

Muestra un resumen de tu situación financiera:

```
--- RESUMEN ---
Saldo actual            : S/ 1095.00
Presupuesto del mes     : S/ 800.00
Gastado este mes        : S/ 5.00
Te queda del presupuesto: S/ 795.00
```
### Opción 5: Agregar categoría

Permite crear categorías propias (por ejemplo, «Alquiler» o «Copias»). El sistema pide:

1. **Nombre de la categoría**: no puede repetirse con otra existente.
2. **¿Es una necesidad (esencial)? (s/n)**: escribe `s` si es un gasto básico o `n` si no lo es.
3. **Monto que quieres reservar**: dinero que apartas para esa categoría.

El programa incluye por defecto la categoría **General**.

### Opción 6: Ver categorías

Lista todas las categorías con su clasificación (esencial / no esencial), su estado (activa / inactiva) y el monto reservado.

```
--- CATEGORÍAS ---
1. General | no esencial | activa | reservado S/ 0.00
2. entretenimiento | no esencial | activa | reservado S/ 30.00
```
### Opción 7: Filtrar movimientos por categoría

Muestra solo los movimientos de la categoría que elijas. Es útil para saber cuánto gastas en un rubro específico.

```
¿De qué categoría quieres ver los movimientos?
1. General | no esencial | activa | reservado S/ 0.00
2. entretenimiento | no esencial | activa | reservado S/ 30.00
Número de la categoría: 1

--- MOVIMIENTOS DE GENERAL ---
#2 | GASTO   | General | 2026-11-01 | S/ 5.00 | gaseosa
```

> Si la categoría no tiene movimientos, el sistema muestra un mensaje informativo.

### Opción 8: Activar o desactivar una categoría

Cambia el estado de una categoría: si está activa la desactiva, y si está desactivada la activa. Una categoría desactivada **no puede usarse para registrar nuevos gastos** hasta que se vuelva a activar.

```
¿Qué categoría quieres activar o desactivar?
1. General | no esencial | activa | reservado S/ 0.00
2. entretenimiento | no esencial | activa | reservado S/ 30.00
Número de la categoría: 2
entretenimiento ahora está DESACTIVADA.
```
### Opción 9: Crear una meta de ahorro

Permite definir un objetivo de ahorro (por ejemplo, una laptop o un viaje). Puedes crear varias metas.

```
¿Para qué quieres ahorrar? (ej: Laptop, Viaje): Viaje
¿Cuánto necesitas juntar? S/ 400
Meta creada: Viaje por S/ 400.00. ¡Tú puedes!
```
### Opción 10: Abonar a una meta de ahorro

Sirve para destinar dinero de tu saldo a una de tus metas. El monto abonado se descuenta del saldo actual, y el sistema indica cuánto falta para completar la meta y el porcentaje de avance.

```
¿A qué meta quieres abonar?
1. Viaje | ahorrado S/ 0.00 de S/ 400.00 | falta S/ 400.00
Número de la meta: 1
Monto a abonar: S/ 50
Abono registrado. Te faltan S/ 350.00 para tu meta (13% completado).
Saldo actual: S/ 950.00
```

### Opción 11: Ver mis metas de ahorro

Muestra todas tus metas con el monto ahorrado, el monto objetivo, lo que falta y una barra de progreso con el porcentaje completado.

```
--- MIS METAS DE AHORRO ---
1. Viaje | ahorrado S/ 50.00 de S/ 400.00 | falta S/ 350.00
   [#---------] 13%
```

### Opción 12: Asistente de compra

Antes de comprar algo que no tenías previsto, esta función te ayuda a decidir si puedes permitírtelo sin afectar el dinero que necesitas para tus gastos esenciales.

El sistema muestra un análisis con el precio, tu saldo actual, tu **dinero libre** (lo que queda sin tocar tus necesidades), el **resultado** (viable o no viable) y cuánto dinero libre te quedaría después de la compra. Cierra con un consejo para evitar compras impulsivas.

```
¿Qué quieres comprar?: alfombra
¿Cuánto cuesta? S/ 200

--- ASISTENTE DE COMPRA ---
Quieres comprar         : alfombra
Precio                  : S/ 200.00
Tu saldo actual         : S/ 950.00
Dinero libre            : S/ 950.00  (lo que queda sin tocar tus necesidades)
Resultado               : VIABLE
Después de comprarlo te quedarían S/ 750.00 libres.
Consejo: Antes de comprar, pregúntate: ¿lo necesito o solo lo quiero?
```

> El asistente solo **evalúa** la compra: no la registra ni modifica tu saldo. Si decides comprar, registra el gasto con la opción 2.

### Opción 0: Salir
Finaliza el programa y muestra un mensaje de despedida.

```
Elige una opción: 0
¡Hasta pronto, ...!
```

> Los datos se guardan solo mientras el programa está abierto. Al salir, la información registrada se pierde.
