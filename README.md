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
0. Salir
Elige una opción:
```

Para usar una función, escribe el **número** de la opción y pulsa **Enter**.

### Opción 1: Registrar ingreso

Sirve para anotar dinero que recibes (mesada, sueldo, venta, propina, etc.). El sistema pide:

1. **Monto del ingreso**: debe ser mayor que cero.
2. **Descripción**: texto breve que identifique el ingreso.

El saldo aumenta automáticamente.

