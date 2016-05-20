aFileDialog - Manual de usuario
===============================

## 1. Uso básico

### 1.1. Instalación

Para hacer uso de _aFileDialog_ en una aplicación se deben completar los siguientes pasos:

**1)** Agregar una referencia al proyecto de la librería (ubicado en la carpeta [library](../library/)). Dado que _aFileDialog_ es un _proyecto de librería de Android_, no puede ser compilado y distribuido como un archivo binario (como un archivo JAR), por lo que se tiene que referenciar al proyecto (con el código fuente) de _aFileDialog_, en lugar de referenciar a un único archivo JAR. 

**2)** Declarar la actividad _FileChooserActivity_ en el archivo manifiesto. Esto se puede hacer agregando las siguientes líneas entre las etiquetas `<application></application>`:

```xml
    <activity android:name="ar.com.daidalos.afiledialog.FileChooserActivity" />
```

**3)** Declarar el permiso READ_EXTERNAL_STORAGE en el archivo manifiesto, si es que se desea acceder a la tarjeta SD (y si se está utilizando Android 4.1 o superior).

```xml
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

Más información sobre como referenciar un proyecto de una librería y como declarar una actividad en el archivo manifiesto se puede encontrar en la [documentación oficial de Android](https://developer.android.com/sdk/installing/create-project.html#ReferencingLibraryModule).

### 1.2. Mostrar el selector de archivos

Si se desea utilizar un _Dialog_ para mostrar el selector de archivos, se pueden utilizar las siguientes líneas:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.show();
```

Si se desea utilizar una _Activity_ para mostrar el selector de archivos, entonces se pueden utilizar estas líneas de código:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    this.startActivityForResult(intent, 0);
```

Por defecto, el selector de archivos muestra la raíz de la tarjeta SD, sin embargo, se puede cambiar la carpeta que el selector de archivos muestra al iniciarse.

Si el selector de archivos se está mostrando con un _Dialog_, entonces se debe llamar al método _loadFolder()_ de la clase _FileChooserDialog_:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.loadFolder(Environment.getExternalStorageDirectory() + "/Download/");
    dialog.show();
```

Si el selector de archivos se está mostrando utilizando una _Activity_, entonces se debe agregar un extra en el _Intent_ cuyo nombre sea _FileChooserActivity.INPUT_START_FOLDER_ y cuyo valor sea la ruta de la carpeta:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_START_FOLDER, Environment.getExternalStorageDirectory() + "/Download/");
    this.startActivityForResult(intent, 0);
```
 
### 1.3. Obtener el archivo seleccionado

A fin de obtener el archivo _File_, representando al archivo seleccionado, si se está utilizando un _Dialog_ para mostrar el selector de archivos, entonces se debe llamar al método _addListener()_, de la clase _FileChooserDialog_, pasandole como parámetro una implementación de la interfaz _FileChooserDialog.OnFileSelectedListener_:

```java
    dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
        public void onFileSelected(Dialog source, File file) {
            source.hide();
            Toast toast = Toast.makeText(source.getContext(), "File selected: " + file.getName(), Toast.LENGTH_LONG);
            toast.show();
		}
		public void onFileSelected(Dialog source, File folder, String name) {
            source.hide();
            Toast toast = Toast.makeText(source.getContext(), "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
            toast.show();
		}
	});
```

La interfaz _FileChooserDialog.OnFileSelectedListener_ define dos métodos: _onFileSelected(Dialog source, File file)_, que es invocado cuando un archivo es seleccionado; y _onFileSelected(Dialog source, File folder, String name)_, que es invocado cuando un archivo es creado.

Es necesario tener en cuenta de que es responsabilidad de la aplicación cerrar el selector de archivos una vez que el archivo a sido seleccionado (de esta forma, si el usuario selecciona un archivo incorrecto, la aplicación puede mostrar un mensaje de error y continuar mostrando el selector de archivos, permitiendo al usuario seleccionar otro archivo).

Si se está usando una _Activity_ para mostrar el selector de archivos, entonces la _Activity_ que abre al selector de archivos debe implementar el método _onActivityResult()_:

```java
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == Activity.RESULT_OK) {
	    	boolean fileCreated = false;
	    	String filePath = "";
	    	
	    	Bundle bundle = data.getExtras();
	        if(bundle != null)
	        {
	        	if(bundle.containsKey(FileChooserActivity.OUTPUT_NEW_FILE_NAME)) {
	        		fileCreated = true;
	        		File folder = (File) bundle.get(FileChooserActivity.OUTPUT_FILE_OBJECT);
	        		String name = bundle.getString(FileChooserActivity.OUTPUT_NEW_FILE_NAME);
	        		filePath = folder.getAbsolutePath() + "/" + name;
	        	} else {
	        		fileCreated = false;
	        		File file = (File) bundle.get(FileChooserActivity.OUTPUT_FILE_OBJECT);
	        		filePath = file.getAbsolutePath();
	        	}
	        }
	    	
	        String message = fileCreated? "File created" : "File opened";
	        message += ": " + filePath;
	    	Toast toast = Toast.makeText(AFileDialogTestingActivity.this, message, Toast.LENGTH_LONG);
			toast.show();
	    }
	}
```

Cabe destacar que si un archivo a sido creado, entonces el valor (dentro del objeto _Bundle_) representado por la clave _FileChooserActivity.OUTPUT_NEW_FILE_NAME_ va a contener el nombre de larchivo y el valor representado por la clave _FileChooserActivity.OUTPUT_FILE_OBJECT_ va a contener la carpeta en la que el archivo debe ser creado. De lo contrario, si el archivo a sido solo seleccionado, _FileChooserActivity.OUTPUT_NEW_FILE_NAME_ va a contener un valor nulo y _FileChooserActivity.OUTPUT_FILE_OBJECT_ va a contener al archivo seleccionado.

### 1.4 Demo

En la carpeta [demo](../demo/) se puede encontrar el proyecto de una aplicación de demostración que hace uso de _aFileDialog_.

Esta demostración puede también ser instalada desde [Google Play](https://play.google.com/store/apps/details?id=ar.com.daidalos.afiledialog.test).

## 2. Opciones avanzadas

### 2.1. Seleccionar carpetas

Por defecto, el selector de archivos selecciona archivos, sin embargo se lo puede configurar para seleccionar carpetas. En este caso, un botón de "Ok" se va a mostrar en la parte inferior del selector de archivos y al presionarlo se va a seleccionar el directorio actual (dado que el evento de tocar a una carpeta se utiliza para abrir la carpeta y mostrar todos los archivos dentro de él). 

Si se está utilizando un _Dialog_ para mostrar el selector de archivos, entonces se debe llamar al método _setFolderMode(), de la clase _FileChooserDialog,_ pasando "true" como parámetro:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.setFolderMode(true);
    dialog.show();
```

Si se está utilizando una _Activity_ para mostrar el selector de archivos, entonces se debe poner un extra en el _Intent_ con nombre _FileChooserActivity.INPUT_FOLDER_MODE_ y con "true" como valor:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_FOLDER_MODE, true);
    this.startActivityForResult(intent, 0);
```

### 2.2. Crear archivos

Se puede configurar al selector de archivos para mostrar un botón "New" que permita al usuario crear archivos o carpetas, al mostrar una ventana emergente en la que el usuario podrá escribir el nombre del archivo o carpeta que desea crear.

Tenga en cuenta de que el selector de archivos no crea los archivos o carpetas, si no que se limita a obtener el directorio en el que el archivo debe crear y el nombre del archivo.  Es reponsabilidad de la aplicación, que abrió el selector de archivos, crear el archivo y de agregarle la extensión correcta.

Si se está utilizando un _Dialog_, entonces se debe llamar al método _setCanCreateFiles()_ de la clase _FileChooserDialog_, pasando "true" como parámetro:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.setCanCreateFiles(true);
    dialog.show();
```

Si se está utilizando una _Activity_, entonces se debe poner un extra en el _Intent_ de nombre _FileChooserActivity.INPUT_CAN_CREATE_FILES_ y con "true" como valor:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_CAN_CREATE_FILES, true);
    this.startActivityForResult(intent, 0);
```

### 2.3. Filtrar archivos y directorios

Por defecto, el selector de archivos permite al usuario seleccionar cualquier archivo, sin embargo se pueden utilizar expresiones regulares a fin de definir que archivos pueden ser seleccionados o no, dependiendo de su nombre o de su extensión.

Si se está utilizando un _Dialog_, entonces se debe llamar al método _setFilter()_ de la clase _FileChooserDialog_, pasando la expresión regular como parámetro (en este ejemplo, la expresión regular define que solo archivos de imágenes puedan ser seleccionados):

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
    dialog.show();
```

Si se está utilizando una _Activity_, entonces se debe poner un extra en el _Intent_ con nombre _FileChooserActivity.INPUT_REGEX_FILTER_ y con la expresión regular como valor:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_REGEX_FILTER, ".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
    this.startActivityForResult(intent, 0);
```

De manera similar, también se pueden filtrar directorios llamando al método _setFolderFilter()_, si se usa un _Dialog_, o usando el extra _FileChooserActivity.INPUT_REGEX_FOLDER_FILTER_, si se usa una _Activity_.

> Tener en cuenta que al filtrar archivos, la expresión regular se aplica al nombre del archivo, mientras que al filtrar directorios, la expresión regular se aplica a la ruta absoluta del directorio.

Al filtrar archivos y directorios, se puede definir además si los archivos/directorios que no pasen el filtro deben ser ocultados o no (en el caso de que no sean ocultados, son mostrados en gris para indicar que no pueden ser seleccionados).

Con el fin de hacer esto, si se está utilizando un _Dialog_, entonces se debe llamar al método _setShowOnlySelectable()_ de la clase _FileChooserDialog_; si se está utilizando una _Activity_, entonces se debe poner un extra en el _Intent_ de nombre _FileChooserActivity.INPUT_SHOW_ONLY_SELECTABLE_.

### 2.4. Mostrar mensajes de confirmación

El selector de archivos puede ser configurado para mostrar ventanas de confirmación (con las opciones "Si" y "No") al seleccionar o crear un archivo, con mensajes del tipo "¿Está seguro de que desea abrir este archivo?".

Si se está utilizando un _Dialog_, entonces se debe llamar al método _setShowConfirmation()_ de la clase _FileChooserDialog_, el primer parámetro define si una ventana de confirmación se debe mostrar al abrir un archivo y el segundo parámetro si una ventana se debe mostrar al crear un archivo:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.setShowConfirmation(true, false);
    dialog.show();
```

Si se está utilizando una _Activity_, entonces se debe poner dos extras en el _Intent_ con los nombres _FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_CREATE_ y _FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_SELECT_:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_SELECT, true);
    intent.putExtra(FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_CREATE, false);
    this.startActivityForResult(intent, 0);
```

Cabe mencionar que los mensajes de las ventanas pueden ser personalizados utilizando la clase _FileChooserLabels_.

### 2.5. Personalización de las etiquetas

Se puede re-definir todos los mensajes y etiquetas utilizados por la librería, a fin de agregar mensajes personalizados o agregar soporte a otros idiomas.

Con el fin de hacer esto, se debe crear una instancia de _FileChooserLabels_ y definir el valor de los textos que se desean cambiar (en el caso de los mensajes de confirmación, el texto "$file_name" será reemplazado por el nombre del archivo en cuestión):

```java
    FileChooserLabels labels = new FileChooserLabels();
    labels.createFileDialogAcceptButton = "Accept";
    labels.createFileDialogCancelButton = "Cancel";
    labels.createFileDialogMessage = "Enter the name of the file";
    labels.createFileDialogTitle = "Create file";
    labels.labelAddButton = "Add";
    labels.labelSelectButton = "Select";
    labels.messageConfirmCreation = "Are you sure that you want to create the file $file_name?";
    labels.messageConfirmSelection = "Are you sure that you want to open the file $file_name?";
    labels.labelConfirmYesButton = "Yes";
    labels.labelConfirmNoButton = "Note;
```

Luego de debe pasar esta instancia al selector de archivos. Si se está utilizando un _Dialog_, se debe invocar el método _setLabels()_ de la clase _FileChooserDialog_:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.setLabels(labels);
    dialog.show();
```

Si se está utilizando una _Activity_, se debe agregar un extra en el _Intent_ usando el nombre _FileChooserActivity.INPUT_LABELS_:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_LABELS, (Serializable) labels); 
    this.startActivityForResult(intent, 0);</code>