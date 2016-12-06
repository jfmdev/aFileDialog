aFileDialog - Software design description
=========================================

## 1. Introduction

### 1.1. Purpose of this document

This document has as objective to specify the architecture and the design considerations of the **aFileDialog** library, in order to serve as a reference for the developer's team involved in the maintenance and development of the library. 

This specification has been developed under the standard _IEEE 1016-2009_, also known as the "Standard for Information Technology - Systems Design - Software Design Descriptions", an IEEE standard that specifies an organizational structure for a system design description.

### 1.2. Definitions, acronyms and abbreviations==

* GUI - Graphical User Interface
* IEEE - Institute of Electrical and Electronic Engineers
* MPL - Mozilla Public License
* SDD - Software Design Description

### 1.3. References

IEEE Computer Society. "IEEE Standard for Information Technology - System Design - Software Design Descriptions". 2009

### 1.4. Overview of document

The remainder of this document outlines the system's architecture, describes the relationship of the various modules, and goes over the design decisions.

It is assumed that the reader have basic knowledge about object-oriented programming in Java and about application's development on Android.

### 2. System overview

_aFileDialog_ is a library that implements a file chooser for the Android operating system, which lacks of a native graphic component for provide this functionality. The library was designed to be easy to use and easy to develop, it is licensed as open source, under MPL v2.0, in order to encourage his use and development.

His main features are:
* The file chooser can be opened as a _Dialog_ and as an _Activity_.
* It has two selection modes, one for select files and another one for select folders.
* It allows to create files or folders.
* It can filter the files that can be selected by using regular expressions.
* It has multi-language support (by default, it only provides translations for English, Spanish and French, but the developers can re-define the value of the labels and texts, in order to add more languages or modify the current ones).
* It is entirely implemented in Java and it is compatible with Android 2.2 or superior.

## 3. Design considerations

### 3.1. Assumptions and dependencies

None.

### 3.2. General constraints

The library must be entirely implemented in Java and it must be compatible with Android 2.3 (at least) and later versions.

### 3.3. Goals and guidelines

The objective was to develop a library which provides a file chooser, for the Android operating system, easy to use and easy to modify, according to the KISS principle (_Keep it short and simple_).

### 3.4. Development methods
 
The development method used was incremental and iterative development, according to which the software is developed in several cycles, in each of which new features are added and the features added in the previous cycle are checked and corrected.

## 4. Architectural strategies

The software was structured using the design patterns embedded in the Android API.

## 5. System architecture description

### 5.1. Overview

The library is composed mainly by six Java files (_FileChooser_, _FileChooserCore_, _FileChooserDialog_, _FileChooserActivity_, _FileChooserLabels_ and _FileItem_) and by two XML layout files (_daidalos_file_chooser_ and _daidalos_file_item_).

* The library's core is defined by the class _FileChooserCore_, which implements the main features, y by the layout _file_chooder_, which defines the user interface of the file chooser.
* The classes _FileChooserDialog_ and _FileChooserActivity_ are charged, mainly, of embed the features provided by _FileChooserCore_ in, respectively, a _Dialog_ and in an _Activity_. Both classes implement the _FileChooser_ interface, which defines all the methods that the class _FileChooserCore_ requires in order to respond to user interface's events.
* The class _FileChooserLabels_ allows to re-define the values of the labels and the texts that are show in the file chooser, in order to allow change the default values or add new languages (besides English, Spanish and French).
* The class _FileItem_ and the layout _daidalos_file_item_ define a graphic component that represents the files and folders that are listed in the file chooser.

### 5.2. File chooser

All the functionalities of the file chooser are implemented in the class _FileChooserCore_, whom in his constructor requires an instance of the interface _FileChooser_. _FileChooser_ defines the methods which a subclass of _View_, that uses the layout _daidalos_file_chooser_, must implement in order to be used as a file chooser.

In this way, we can 'simulate' multiple inheritance (which is not supported by Java): the classes _FileChooserDialog_ y _FileChooserActivity_ inherit, respectively, from the classes _Dialog_ and _Activity_, and in his constructors they create an instance of _FileChooserCore_ in order to 'inherit' also all the features implemented by this class.

### 5.3. User interface

The graphical user interface is defined by the XML files _daidalos_file_chooser_ and _daidalos_file_item_.

_daidalos_file_chooser_ defines the interface of the file chooser, which is composed by a vertical layout, which occupies all the width and height of the screen and in which the files and folder (that can be selected) are listed, and by two buttons located in the bottom, one for create files or folders and another one for select the current folder (in the case that the file chooser is being used for select folders instead of files).

_daidalos_file_item_ defines the interface of the graphical component used for represent the files and folders that are listed in the vertical layout of the file chooser. This component is composed by a icon, which allows to differentiate files from folders, and a label, which shows the file's name.

_daidalos_file_chooser_ is used by the classes _FileChooserDialog_ and _FileChooserActivity_, which acts as intermediates between the layout and the class _FileChooserCore_, while _daidalos_file_item_ is used by the class _FileItem_, which is a subclass of _LineaLayout_ that defines the graphic component used for represent files and folder.

### 5.4. Internationalization

All the labels and messages show by the different classes and graphical interfaces are defined in files _string.xml_, making use of the internationalization mechanisms provided by the Android SDK.

By default, only the languages English, French and Spanish are supported, however the developers can add new languages (or modify the current ones) by using the class _FileChooserLabels_.

In each instance of the class _FileChooserLabels_ we can re-define the values of the labels and messages. This instance then must be passed to the class _FileChooserCore_, in order to use the values defined in this instance, instead of using the values defined in _string.xml_.

