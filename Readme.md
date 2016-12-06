aFileDialog
===========

**aFileDialog** is an Android library which implements a simple and easy to use _file chooser_.

His main features are:
 * The file chooser can be opened both as a _Dialog_ or as an _Activity_.
 * You can filter the files and folders using regular expressions.
 * Can select files or folders.
 * Can create files and folders.

How to use it
-------------

In order to use _aFileDialog_ in your application you must do three steps:

**1)** Add a reference to the library project (which is located in the [library](library/) folder). Note that, since _aFileDialog_ is a _Android Library project_, it can not be compiled into a binary file (such as a JAR file), so your project must reference to the _aFileDialog_ project, instead of reference to a single JAR file. 

**2)** Declare the activity _FileChooserActivity_ in the _manifest_ file. This can be done by adding the following lines inside the tags `<application></application>`:

```xml
    <activity android:name="ar.com.daidalos.afiledialog.FileChooserActivity" />
```

**3)** Add the permission _READ_EXTERNAL_STORAGE_ to the _manifest_ file, if you want to access the SD card (and if you are using Android 4.1 or superior).

```xml
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

**4)** Then you must only create an instance of _FileChooserDialog_ and call the _show()_ method:

```java
    FileChooserDialog dialog = new FileChooserDialog(context);
    dialog.show();
```

Or start the activity _FileChooserActivity_:

```java
    Intent intent = new Intent(this, FileChooserActivity.class);
    this.startActivityForResult(intent, 0);
```

> **Note:** the current version of the library is developed using _Android Studio_, if you are using _Eclipse_, then you should check the [eclipse](https://github.com/jfmdev/aFileDialog/tree/eclipse) branch.

Demo
----

In the [demo](demo/) folder who will find the project of a sample app that makes uses of _aFileDialog_.

You can also install this demo app from [Google Play](https://play.google.com/store/apps/details?id=ar.com.daidalos.afiledialog.test).

Documentation
-------------

_aFileDialog_ has been designed in order to be easy to use and easy to develop. 

In order to known how to use _aFileDialog_, check the [user guide](docs/UserGuide.md) (also available in [Spanish](docs/UserGuideEs.md) and [French](docs/UserGuideFr.md)).

If you want to modify or extend _aFileDialog_, you can read the [software design description](docs/SoftwareDesignDescription.md) (also available in [Spanish](docs/SoftwareDesignDescriptionEs.md)), although this is not mandatory, since the code is fully commented and you are going to figure out how it works in matters of minutes.

License
-------

This library is free software; you can redistribute it and/or
modify it under the terms of the Mozilla Public
License v2.0. You should have received a copy of the MPL 2.0 along with this library, otherwise you can obtain one at <http://mozilla.org/MPL/2.0/>.
