aFileDialog - Guide de l'utilisateur
====================================

## 1. Utilisation basique

### Installation

Pour utiliser _aFileDialog_, dans votre logiciel, il faut suivre les suivants pas:

**1)** Ajouter une référence au projet de la librairie (situé dans le dossier [library](../library/)). Étant donné que _aFileDialog_ est un _projet de librairie d'Android_, il ne peut pas être compilé et distribué comme un fichier binaire (comme, par exemple, un fichier JAR), à cause de ça on doit faire la référence au dossier du projet (avec le code source), en lieu de faire la référence à un fichier JAR. 

**2)** Déclarer la activité _FileChooserActivity_ dans le fichier de manifeste. On peut faire ça en ajoutant les lignes suivantes entre les étiquettes `<application></application>`:
	
```xml
    <activity android:name="ar.com.daidalos.afiledialog.FileChooserActivity" />
```

*3)* Déclarer le permis _READ_EXTERNAL_STORAGE_ dans le fichier de manifeste, si on a besoin d’accéder à la carte SD (et si on utilise Android 4.1 ou supérieur).

```xml
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

Pour plus d'information sur comment faire une référence à un projet de librairie et comment déclarer une activité dans le fichier de manifeste, vous pouvez lire la [documentation officielle d'Android](https://developer.android.com/sdk/installing/create-project.html#ReferencingLibraryModule).

###  1.2. Afficher le sélecteur de fichiers 

Si on veut utiliser un _Dialog_ pour afficher le sélecteur de fichiers, on peut utiliser les lignes suivantes:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.show();
```
	
Si on veut utiliser une _Activity_ pour afficher le sélecteur de fichiers, alors on peut utiliser ces lignes de code:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    this.startActivityForResult(intent, 0);
```
	
Par défaut, le sélecteur de fichiers montre le racine de la carte SD, néanmoins, on peut changer le dossier que le sélecteur montre la première fois qu'il est ouvert.

Si on utilise un _Dialog_ pour afficher le sélecteur, alors on doit appeler le méthode _loadFolder()_ de la classe _FileChooserDialog_:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.loadFolder(Environment.getExternalStorageDirectory() + "/Download/");
    dialog.show();
```
	
Si on utilise une _Activity_ pour afficher le sélecteur, alors on doit ajouter un extra au _Intent_ avec le nom _FileChooserActivity.INPUT_START_FOLDER_ et le chemin du dossier comme le valeur:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_START_FOLDER, Environment.getExternalStorageDirectory() + "/Download/");
    this.startActivityForResult(intent, 0);
``` 

###  1.3. Retrouver le fichier sélectionné 

À fin de retrouver le fichier, représente comme un objet _File_, choisi par l'utilisateur, si on utilise un _Dialog_ pour afficher le sélecteur, il faut appeler le méthode  _addListener()_, de la classe _FileChooserDialog_, en utilisant comme paramètre une implémentation de la interface _FileChooserDialog.OnFileSelectedListener_:

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
	
L'interface _FileChooserDialog.OnFileSelectedListener_ défini deux méthodes: _onFileSelected(Dialog source, File file)_, qui est appelé quand un fichier est sélectionné; et _onFileSelected(Dialog source, File folder, String name)_, qui est appelé quand un fichier est créé.

Il faut tenir en compte, qu'est responsabilité de l'application de fermer le sélecteur de fichiers, une fois que un fichier a été sélectionné. De cette manière, si l'utilisateur sélectionne un fichier incorrecte, l’application peut montrer un message d'erreur et ne pas fermer le sélecteur de fichiers, à fin de permettre à l'utilisateur de sélectionner autre fichier.

Si on utilise une _Activity_ pour afficher le sélecteur de fichiers, alors la _Activity_ que appelle le sélecteur doit implémenter le méthode _onActivityResult()_:

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
	
Il faut tenir en compte que si un fichier a été créé, alors le valeur (à l’intérieur de l'objet _Bundle_) représenté par la mot _FileChooserActivity.OUTPUT_NEW_FILE_NAME_ contiendra le nom de fichier et le valeur représenté par la mot _FileChooserActivity.OUTPUT_FILE_OBJECT_ contiendra le dossier dans lequel le fichier doit être créé. Autrement, si le fichier a été seulement sélectionné, _FileChooserActivity.OUTPUT_NEW_FILE_NAME_ contiendra un valeur nulle et _FileChooserActivity.OUTPUT_FILE_OBJECT_ contiendra le fichier sélectionné.

### 1.4 Demo

Dans le dossier [demo](../demo/) on peut trouver le projet d'une application référence et utilise la librairie _aFileDialog_.

On peut aussi installer cette application dans [Google Play](https://play.google.com/store/apps/details?id=ar.com.daidalos.afiledialog.test).

##  2. Options avancés 

###  2.1. Sélectionner dossiers 

Par défaut, le sélecteur sélectionne fichiers, néanmoins, on le peut paramétrer pour sélectionner dossiers. Dans ce cas, un bouton "Ok" sera affiché à la section inférieur du sélecteur de fichiers; en appuyant ce bouton on sélectionnera le dossier actuelle (étant donné que l’évènement de toucher un dossier s'utilise pour ouvrir le dossier et afficher tous les fichiers que sont à l’intérieur). 

Si on utilise un _Dialog_ pour afficher le sélecteur de fichiers, alors il faut appeler le méthode _setFolderMode(), de la classe _FileChooserDialog_, en utilisant "true" comme paramètre:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.setFolderMode(true);
    dialog.show();
```
	
Si on utilise une _Activity_ pour afficher le sélecteur de fichiers, alors on doit ajouter un extra au _Intent_ avec le nom _FileChooserActivity.INPUT_FOLDER_MODE_ et avec le valeur "true":

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_FOLDER_MODE, true);
    this.startActivityForResult(intent, 0);
```

###  2.2. Créer fichiers 

On peut paramétrer le sélecteur de fichiers pour afficher un bouton "New", pour donner la possibilité aux utilisateurs de créer fichiers ou dossiers. En appuyant ce bouton, une fenêtre émergent que permettra à l'utilisateur de saisir le nom du fichier ou dossier qu'il veut créer.

Il faut tenir en compte que le sélecteur de fichiers ne crée pas les fichiers ou dossiers, il se limite à obtenir le dossiers, dans lequel le fichier doit être créé, et le nom du fichier. Il est responsabilité de l'application, que a ouvert le sélecteur de fichiers, de créer le fichier et de l'ajouter l'extension correcte.

Si on utilise un _Dialog_, alors on doit appeler le méthode _setCanCreateFiles()_ de la classe _FileChooserDialog_, en utilisant "true" comme paramètre:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.setCanCreateFiles(true);
    dialog.show();
```
	
Si on utilise une _Activity_, alors on doit ajouter un extra dans le _Intent_ avec le nom _FileChooserActivity.INPUT_CAN_CREATE_FILES_ et le valeur "true":

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_CAN_CREATE_FILES, true);
    this.startActivityForResult(intent, 0);
```
	
###  2.3. Filtrer fichiers et dossiers

Par défaut, le sélecteur de fichiers permet à l'utilisateur de sélectionner n'importe quel fichier, néanmoins, on peut utiliser expressions régulières pour définir quels fichiers peuvent être sélectionné, selon son nom ou extension.

Si on utilise un _Dialog_, alors on doit appeler le méthode _setFilter()_ de la classe _FileChooserDialog_, en utilisant une expression régulière comme paramètre (dans cet exemple, l'expression régulière définit que seulement fichiers d'images peuvent être sélectionné):

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
    dialog.show();
```
	
Si on utilise une _Activity_, alors on doit ajouter un extra au _Intent_ avec nom  _FileChooserActivity.INPUT_REGEX_FILTER_ et avec l'expression régulière comme valeur:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_REGEX_FILTER, ".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
    this.startActivityForResult(intent, 0);
```

De manière analogue, on peut également filtrer les dossiers en appelant la méthode _setFolderFilter () _, si vous utilisez un _Dialog_, ou en utilisant l'extra _FileChooserActivity.INPUT_REGEX_FOLDER_FILTER_, si vous utilisez une _Activity_.

> Il faut Noter que lors du filtrage de fichiers, l'expression régulière est appliquée au nom du fichier, mais lors du filtrage des dossiers, l'expression régulière est appliquée sur le chemin absolu du fichier.

Quand on filtre des fichiers ou dossiers, on peut définir aussi si les fichiers/dossiers que ne sont pas sélectionnables doivent être affichés ou cachés (quand il sont affichés, il sont montrés avec un couleur grise, à fin d'indiquer à l'utilisateur que il ne le peut pas sélectionner).

Pour faire ça, si on utilise un _Dialog_, alors on doit appeler le méthode _setShowOnlySelectable()_ de la classe _FileChooserDialog_; si on utilise une _Activity_, alors on doit ajouter un extra au _Intent_ avec le nom _FileChooserActivity.INPUT_SHOW_ONLY_SELECTABLE_.

###  2.4. Afficher messages de confirmation 

Le sélecteur de fichiers peut être paramétré  pour afficher de fenêtres avec messages de confirmation (avec les options "Oui" et "Non") au sélectionner ou créer un fichier, avec messages comme "Vous êtes sure que vous voulez ouvrir ce fichier?".

Si on utilise un _Dialog_, on doit appeler le méthode _setShowConfirmation()_ de la classe _FileChooserDialog_, le premier paramètre définit si un fenêtre de confirmation doit être affiché au ouvrir un fichier et le deuxième paramètre si la fenêtre doit être affiché quand on créé un fichier:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.setShowConfirmation(true, false);
    dialog.show();
```
	
Si on utilise une _Activity_, on doit ajouter deux extras au _Intent_ avec les noms _FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_CREATE_ et _FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_SELECT_:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_SELECT, true);
    intent.putExtra(FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_CREATE, false);
    this.startActivityForResult(intent, 0);
```
	
Il faut remarquer que les messages des fenêtres peuvent être changés avec la classe _FileChooserLabels_.

###  2.5. Paramétrer étiquettes 

On peut changer tous les messages et étiquettes utilisés par la librairie, pour ajouter de nouveaux messages ou pour ajouter des autre langages.

Pour faire ça, on doit créer une instance de la classe _FileChooserLabels_ et définir le valeur des textes qu'on veut modifier (dans le cas des messages de confirmation, le texte "$file_name" sera remplacé par le nom du fichier concerné):

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
	
Après, il faut passer cette instance au sélecteur de fichiers. Si on utilise un _Dialog_, on doit appeler le méthode _setLabels()_ de la classe _FileChooserDialog_:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.setLabels(labels);
    dialog.show();
```
	
Si on utilise une _Activity_, on doit ajouter un extra au _Intent_ avec le nom _FileChooserActivity.INPUT_LABELS_:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_LABELS, (Serializable) labels); 
    this.startActivityForResult(intent, 0);
```