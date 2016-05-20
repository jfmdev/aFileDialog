aFileDialog - User guide
========================

## 1. Basic usage

### 1.1. Installation

In order to use _aFileDialog_ in your application you must do three steps:

**1.** Add a reference to the library project (located in the [library](../library/) folder). Note that, since _aFileDialog_ is a _Android Library project_, it can not be compiled into a binary file (such as a JAR file), so your project must reference to the _aFileDialog_ project, instead of reference to a single JAR file.

**2.** Declare the activity _FileChooserActivity_ in the _manifest_ file. This can be done by adding the following lines inside the tags `<application></application>`:

```xml
    <activity android:name="ar.com.daidalos.afiledialog.FileChooserActivity" />
```

**3.** Add the permission _READ_EXTERNAL_STORAGE_ to the _manifest_ file, if you want to access the SD card (and if you are using Android 4.1 or superior).

```xml
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

You can check the [Android documentation](https://developer.android.com/sdk/installing/create-project.html#ReferencingLibraryModule) in order to get more information about how to reference a library project and how to declare an activity in the manifest file.

### 1.2. Show the dialog

If you want to use a _Dialog_ to show the file chooser, you can use the following lines:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.show();
```

If you want to use an _Activity_ to show the file chooser, then you can use these lines of code:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    this.startActivityForResult(intent, 0);
```

By default, the file chooser starts at the root of the SD card, however you can change the folder in which the file chooser starts.
If you are displaying the file chooser as a _Dialog_, then you must call the method _loadFolder()_ from the class _FileChooserDialog_:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.loadFolder(Environment.getExternalStorageDirectory() + "/Download/");
    dialog.show();
```

If you are displaying the file chooser using an _Activity_, then you must put an extra in the _Intent_ with name _FileChooserActivity.INPUT_START_FOLDER_ and the file's path as the value:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_START_FOLDER, Environment.getExternalStorageDirectory() + "/Download/");
    this.startActivityForResult(intent, 0);
```

### 1.3. Get the file selected

In order to get the _File_ object, representing the file selected, if you are using a _Dialog_ for display the file chooser, then you must call the method _addListener()_, of the class _FileChooserDialog_, passing as parameter an implementation of the interface _FileChooserDialog.OnFileSelectedListener_:

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

The interface _FileChooserDialog.OnFileSelectedListener_ defines two methods: _onFileSelected(Dialog source, File file)_, which is called when a file is selected; and _onFileSelected(Dialog source, File folder, String name)_, which is called when a file is created.

Note that it is reponsibility of the application to close the dialog once that a file has been selected (in this way, if the user selects an invalid file, the application can show an error message and leave open the dialog, given the possibility to the user of open another file).

If you are using an _Activity_ for show the file chooser, then your _Activity_ class must implement the method _onActivityResult()_:

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

Note that if a file has been created, then the value, inside the _Bundle_ object, represented by the key _FileChooserActivity.OUTPUT_NEW_FILE_NAME_ is going to contain the name of the file and the value represented by the key _FileChooserActivity.OUTPUT_FILE_OBJECT_ is going to contain the folder in which the file must be created. Otherwise, if a file has only been selected, _FileChooserActivity.OUTPUT_NEW_FILE_NAME_ is going to be null and _FileChooserActivity.OUTPUT_FILE_OBJECT_ is going to contain the file selected.

### 1.4 Demo

In the [demo](../demo/) folder who will find the project of a sample app that makes uses of _aFileDialog_.

You can install this demo app from [Google Play](https://play.google.com/store/apps/details?id=ar.com.daidalos.afiledialog.test).

## 2. Advanced features

### 2.1. Select folders

By default, the file chooser selects files, but you can configure it in order to select folders. In this case, an "Ok" button is going to appear in the bottom of the file chooser, this button is used for select the current folder (since the events of touching a folder is used to enter the folder and display all the files that are inside him). 

If you are using a _Dialog_ for display the file chooser, then you must call the method _setFolderMode()_ from _FileChooserDialog_ passing "true" as parameter:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.setFolderMode(true);
    dialog.show();
```

If you are using an _Activity_ for display the file chooser, then you must put an extra in the _Intent_ with name _FileChooserActivity.INPUT_FOLDER_MODE_ and value "true":

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_FOLDER_MODE, true);
    this.startActivityForResult(intent, 0);
```

### 2.2. Create files

You can configure the file chooser to show a "New" button which is going to allow to the user to create new files or folders, by showing a prompt dialog in which the user can write the name of the file or folder that he wants to create.

Note that the file chooser do not creates the file, it only returns the folder in which the file must be created and the name of the file. It is responsibility of the application that opened the file chooser to create the file and to add the corresponding file extension to the file's name. 

If you are using a _Dialog_, then you must call the method _setCanCreateFiles()_ from the class _aFileDialog_, passing "true" as parameter:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.setCanCreateFiles(true);
    dialog.show();
```

If you are using an _Activity_, then you must put an extra in the _Intent_ with name _FileChooserActivity.INPUT_CAN_CREATE_FILES_ and value "true":

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_CAN_CREATE_FILES, true);
    this.startActivityForResult(intent, 0);
```

### 2.3. Filter files and folders

By default, the file chooser allows to the user to select any file, however we can user regular expressions in order to define which files can be selected and which not, depending of his name or extension.

If you are using a _Dialog_, then you must call the method _setFilter()_ from the class _aFileDialog_, passing the regular expression as parameter (in this example, the regular expression defines that only image files can be selected):

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
    dialog.show();
```

If you are using an _Activity_, then you must put an extra in the _Intent_ with name _FileChooserActivity.INPUT_REGEX_FILTER_ and the regular expression as value:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_REGEX_FILTER, ".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
    this.startActivityForResult(intent, 0);
```

In an analogous way, you can also filter folders by calling the method _setFolderFilter()_, if using a _Dialog_, or using the extra _FileChooserActivity.INPUT_REGEX_FOLDER_FILTER_, if using an _Activity_.

> Note that when filtering files, the regular expression is applied to the file's name, but when filtering folders, the regular expression is applied to the file's absolute path.

When filtering, you can also define if the files/folders that do not pass the filter must be hide or not (in which case they are going to be displayed in gray, in order to indicate that they can not be selected).

In order to do it, if you are using a _Dialog_ then you must call the method _setShowOnlySelectable()_ of the class _FileChooserDialog_; if you are using an _Activity_, then you must put an extra in the _Intent_ with name _FileChooserActivity.INPUT_SHOW_ONLY_SELECTABLE_.

### 2.4. Show confirmation dialogs

You can configure the file chooser for show a yes/no confirmation dialog when selecting or creating a file, in order to display messages of the type "Are you sure that you want to open this file?".

If you are using a _Dialog_, then you must call the method _setShowConfirmation()_ from the class _aFileDialog_, the first parameter defines if the confirmation dialog must be show when opening a file and the second parameter if the confirmation dialog must be show when creating a file:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.setShowConfirmation(true, false);
    dialog.show();
```

If you are using an _Activity_, then you must put two extras in the _Intent_ with names _FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_CREATE_ and _FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_SELECT_:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_SELECT, true);
    intent.putExtra(FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_CREATE, false);
    this.startActivityForResult(intent, 0);
```

Note that the messages displayed can be customized by using the class _FileChooserLabels_.

### 2.5. Define custom labels

You can re-define all the messages and labels that are displayed by the library, in order to add customized messages or in order to add support to more languages.
In order to do it, you must only create an instance of _FileChooserLabels_ and define the value of the texts that you want to change (note that, in the confirmations messages, the string "$file_name" is going to be replaced by the name of the selected file):

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
    labels.labelConfirmNoButton = "Note";
```

Then you must pass this instance to the file chooser. If you are using a _Dialog_, you must call the method _setLabels()_ from the class _aFileDialog_:

```java
    FileChooserDialog dialog = new FileChooserDialog(this);
    dialog.setLabels(labels);
    dialog.show();
```

If you are using an _Activity_, then you must put an extra in the _Intent_ with name _FileChooserActivity.INPUT_LABELS_:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    intent.putExtra(FileChooserActivity.INPUT_LABELS, (Serializable) labels); 
    this.startActivityForResult(intent, 0);
```
    
