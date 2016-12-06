aFileDialog - Descripción de diseño de software
===============================================

## 1. Introducción

### 1.1. Propósito de este documento

Este documento tiene como objetivo especificar la arquitectura y las consideraciones de diseño de la librería **aFileDialog**, a fin de servidor como referencia para los desarrolladores encargados de extender, actualizar o corregir la librería.

La especificación a sido desarrollada bajo el estándar _IEEE 1016-2009_, también conocido como "Standard for Information Technology - Systems Design - Software Design Descriptions", un estándar del IEEE que especifica la organización estructura de una descripción de diseño de software.

### 1.2. Definiciones, acrónimos y abreviaciones

* GUI - Interfaz gráfica de usuario
* IEEE - Instituto de Ingenieros Eléctricos y Electrónicos
* MPL - Licencia pública de Mozilla
* SDD - Descripción de diseño de software

### 1.3. Referencias

IEEE Computer Society. "IEEE Standard for Information Technology - System Design - Software Design Descriptions". 2009

### 1.4. Descripción del documento

El resto de este documento describe la arquitectura del sistema, la relación de los distintos módulos y las decisiones de diseño.

Se asume que el lector posee conocimientos básicos sobre programación orientada a objectos en Java y sobre el desarrollo de aplicaciones para Android.

## 2. Descripción general del software

_aFileDialog_ es una librería que implementa un selector de archivos para el sistema operativo Android, el cual carece de un componente gráfico nativo que provea esta funcionalidad. La librería fue diseñada para ser fácil de utilizar y fácil de modificar y está licenciada como código abierto, bajo MPL v2.0, a fin de alentar su uso y mantenimiento.

Sus principales características son:

* El selector de archivos puede abrirse como una _Dialog_ o como una _Activity_.
* Posee dos modos de selección, uno para seleccionar archivos y otro para seleccionar directorios.
* Permite crear el archivo o la carpeta que se desea seleccionar.
* Posibilita filtrar los archivos que pueden ser seleccionados mediante el uso de expresiones regulares.
* Provee soporte para múltiples idiomas (por defecto provee traducciones al español, al inglés y al francés, pero los desarrolladores puede definir el valor de los diferentes textos y etiquetas, a fin de internacionalizar el selector de archivos a otros idiomas).
* Está implementado íntegramente en Java y es compatible con Android 2.2 o superior.

## 3. Consideraciones de diseño

### 3.1. Supuestos y dependencias

Ninguna.

### 3.2. Restricciones generales

La librería deber ser implementada íntegramente en Java y debe ser compatible con Android 2.3 (como mínimo) y versiones posteriores.

### 3.3. Objetivos y lineamientos

El objetivo es desarrollar una librería que implemente un selector de archivos, para el sistema operativo Android, que sea fácil de utilizar y fácil de modificar, de acuerdo al principio de KISS (_Keep it short and simple_).

### 3.4. Métodos de desarrollo
 
El método de desarrollo utilizado es el desarrollo incremental e iterativo, según el cual el software se desarrolla en varios ciclos, en cada uno de los cuales se agregan nuevas funcionalidades y se verifican y corrigen las funcionalidades agregadas en el ciclo anterior.

### 4. Estrategias de arquitectura

El software se estructuró haciendo uso de los patrones de diseños propios de la API de Android.

## 5. Descripción de la arquitectura del software

### 5.1. Introducción

La librería está compuesta principalmente por seis archivos Java (_FileChooser_, _FileChooserCore_, _FileChooserDialog_, _FileChooserActivity_, _FileChooserLabels_ y _FileItem_) y por dos archivos XML de layout (_daidalos_file_chooser_ y _daidalos_file_item_):

* El núcleo de la librería está definido por la clase _FileChooserCore_, que implementa las principales funcionalidades, y el layout _daidalos_file_chooser_, que define la interfaz gráfica de los selectores de archivos.
* Las clases _FileChooserDialog_ y _FileChooserActivity_ se encargan principalmente de embeber las funcionalidades, implementadas en _FileChooserCore_, en, respectivamente, una _Dialog_ y en una _Activity_. Esto lo realizan al implementar la interfaz _FileChooser_, la cual define los métodos callback que la clase _FileChooserCore_ requiere para poder responder a los diferentes eventos de interfaz.
* La clase _FileChooserLabels_ permite re-definir los valores de los textos y las etiquetas que son mostrados en los selectores de archivos, a fin de permitir internacionalizar la librería.
* La clase _FileItem_ y la layout _daidalos_file_item_ permiten definir un componente gráfico para representar a los archivos y los directorios, que serán listados en el selector de archivos.

### 5.2. Selector de archivos

Todas las funcionalidades del selector de archivos se encuentran implementadas en la clase _FileChooserCore_, la cual, en su constructor, requiere una instancia de la interfaz _FileChooser_.
 
_FileChooser_ define los métodos que una subclase de _View_, que utilice el layout _daidalos_file_chooser_, debe implementar a fin de poder ser utilizada como un selector de archivos.

De esta forma se puede 'simular' la herencia múltiple (la cual no es soportada por Java): las clases _FileChooserDialog_ y _FileChooserActivity_ heredan, respectivamente, de las clases _Dialog_ y _Activity_, y en sus constructores crean una instancia de _FileChooserCore_ a fin 'heredar' también las funcionalidades implementadas por esta clase.

### 5.3. Interfaz gráfica

La interfaz gráfica está definida por los archivos XML de layout _daidalos_file_chooser_ y _daidalos_file_item_.

_daidalos_file_chooser_ define la interfaz de los selectores de archivos, la cual se compone de un layout vertical, que ocupa todo el alto y ancho de la pantalla y en el que se ubican los archivos y directorios que se pueden seleccionar, y de dos botones en la parte inferior, uno para crear archivos o directorios y otros para seleccionar el directorio actual (en el caso de que se esté en el modo de selección de directorios).

_daidalos_file_item_ define la interfaz del componete gráfico utilizado para representar a los archivos y carpetas que se listan en el layout vertical de los selectores de archivos. Este compnente se compone de un ícono (que permite diferenciar los archivos de las carpetas) y de un label (con el nombre del archivo).

El layout _daidalos_file_chooser_ es utilizado por las clases _FileChooserDialog_ y _FileChooserActivity_, que actúan de intermediarios entre el layout y la clase _FileChooserCore_, mientras que el layout _daidalos_file_item_ es utilizado por la clase _FileItem_, la cual es una subclase _LinearLayout_ que define un componente gráfico para representar archivos y directorios.

### 5.4. Internacionalización

Todas las etiquetas y mensajes mostrados por las diferentes clases e interfaces se encuentran definidos en archivos _string.xml_, haciendo uso de los mecanismos de internacionalización provistos por la SDK de Android.

Por defecto, solo los idiomas inglés, francés y español están soportados, sin embargo, los desarrolladores pueden agregar nuevos idiomas (o modificar los existentes) haciendo uso de la clase _FileChooserLabels_.

En cada intancia de la clase _FileChooserLabels_ se puede re-definir el valor de los textos y etiquetas. Esta instancia luego debe ser pasada a la clase _FileChooserCore_, a fin de que esta utilice los valores de dicha instancia, en vez de los valores definidos en _string.xml_.

