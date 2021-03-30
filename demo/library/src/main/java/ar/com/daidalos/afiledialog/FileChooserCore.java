/*
 * <Copyright 2013 Jose F. Maldonado>
 *
 *  This file is part of aFileDialog.
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ar.com.daidalos.afiledialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import ar.com.daidalos.afiledialog.view.FileItem;

/**
 * This class implements the common features of a file chooser.
 */
class FileChooserCore {

    // ----- Attributes ----- //

    /**
     * Static attribute for save the folder displayed by default.
     */
    private static File defaultFolder;

    /**
     * Static constructor.
     */
    static {
        defaultFolder = null;
    }

    /**
     * The file chooser in which all the operations are performed.
     */
    private FileChooser chooser;
    /**
     * The listeners for the event of select a file.
     */
    private List<OnFileSelectedListener> fileSelectedListeners;
    /**
     * The listeners for the event of select a file.
     */
    private List<OnCancelListener> cancelListeners;
    /**
     * A regular expression for filter the files.
     */
    private String filter;
    /**
     * A regular expression for filter the folders.
     */
    private String folderFilter;
    /**
     * A boolean indicating if only the files that can be selected (they pass the filter) must be show.
     */
    private boolean showOnlySelectable;
    /**
     * A boolean indicating if the user can create files.
     */
    private boolean canCreateFiles;
    /**
     * A boolean indicating if the chooser is going to be used to select folders.
     */
    private boolean folderMode;
    /**
     * A boolean indicating if the chooser is going to be used to select folders.
     */
    private boolean showCancelButton;
    /**
     * A file that indicates the folder that is currently being displayed.
     */
    private File currentFolder;
    /**
     * This attribut allows to override the default value of the labels.
     */
    private FileChooserLabels labels;
    /**
     * A boolean that indicates if a confirmation dialog must be displaying when selecting a file.
     */
    private boolean showConfirmationOnSelect;

    // ---- Static attributes ----- //
    /**
     * A boolean that indicates if a confirmation dialog must be displaying when creating a file.
     */
    private boolean showConfirmationOnCreate;
    /**
     * A boolean indicating if the folder's full path must be show in the title.
     */
    private boolean showFullPathInTitle;

    // ----- Constructor ----- //
    /**
     * Implementation of the click listener for when the add button is clicked.
     */
    private View.OnClickListener addButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            // Get the current context.
            Context context = v.getContext();

            // Create an alert dialog.
            AlertDialog.Builder alert = new AlertDialog.Builder(context);

            // Define the dialog's labels.
            String title = context.getString(FileChooserCore.this.folderMode ? R.string.daidalos_create_folder : R.string.daidalos_create_file);
            if (FileChooserCore.this.labels != null && FileChooserCore.this.labels.createFileDialogTitle != null)
                title = FileChooserCore.this.labels.createFileDialogTitle;
            String message = context.getString(FileChooserCore.this.folderMode ? R.string.daidalos_enter_folder_name : R.string.daidalos_enter_file_name);
            if (FileChooserCore.this.labels != null && FileChooserCore.this.labels.createFileDialogMessage != null)
                message = FileChooserCore.this.labels.createFileDialogMessage;
            String posButton = (FileChooserCore.this.labels != null && FileChooserCore.this.labels.createFileDialogAcceptButton != null) ? FileChooserCore.this.labels.createFileDialogAcceptButton : context.getString(R.string.daidalos_accept);
            String negButton = (FileChooserCore.this.labels != null && FileChooserCore.this.labels.createFileDialogCancelButton != null) ? FileChooserCore.this.labels.createFileDialogCancelButton : context.getString(R.string.daidalos_cancel);

            // Set the title and the message.
            alert.setTitle(title);
            alert.setMessage(message);

            // Set an EditText view to get the file's name.
            final EditText input = new EditText(context);
            input.setSingleLine();
            alert.setView(input);

            // Set the 'ok' and 'cancel' buttons.
            alert.setPositiveButton(posButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String fileName = input.getText().toString();
                    // Verify if a value has been entered.
                    if (fileName != null && fileName.length() > 0) {
                        // Notify the listeners.
                        FileChooserCore.this.notifyFileListeners(FileChooserCore.this.currentFolder, fileName);
                    }
                }
            });
            alert.setNegativeButton(negButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Do nothing, automatically the dialog is going to be closed.
                }
            });

            // Show the dialog.
            alert.show();
        }
    };

    // ----- Events methods ----- //
    /**
     * Implementation of the click listener for when the ok button is clicked.
     */
    private View.OnClickListener okButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            // Notify the listeners.
            FileChooserCore.this.notifyFileListeners(FileChooserCore.this.currentFolder, null);
        }
    };
    /**
     * Implementation of the click listener for when the cancel button is clicked.
     */
    private View.OnClickListener cancelButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            // Notify the listeners.
            FileChooserCore.this.notifyCancelListeners();
        }
    };
    /**
     * Implementation of the click listener for when a file item is clicked.
     */
    private FileItem.OnFileClickListener fileItemClickListener = new FileItem.OnFileClickListener() {
        public void onClick(FileItem source) {
            // Verify if the item is a folder.
            File file = source.getFile();
            if (file.isDirectory()) {
                // Open the folder.
                FileChooserCore.this.loadFolder(file);
            } else {
                // Notify the listeners.
                FileChooserCore.this.notifyFileListeners(file, null);
            }
        }
    };

    /**
     * Creates an instance of this class.
     *
     * @param fileChooser The graphical file chooser.
     */
    public FileChooserCore(FileChooser fileChooser) {
        // Initialize attributes.
        this.chooser = fileChooser;
        this.fileSelectedListeners = new LinkedList<OnFileSelectedListener>();
        this.cancelListeners = new LinkedList<OnCancelListener>();
        this.filter = null;
        this.folderFilter = null;
        this.showOnlySelectable = false;
        this.setCanCreateFiles(false);
        this.setFolderMode(false);
        this.currentFolder = null;
        this.labels = null;
        this.showConfirmationOnCreate = false;
        this.showConfirmationOnSelect = false;
        this.showFullPathInTitle = false;
        this.showCancelButton = false;

        // Add listener for the buttons.
        LinearLayout root = this.chooser.getRootLayout();
        Button addButton = (Button) root.findViewById(R.id.buttonAdd);
        addButton.setOnClickListener(addButtonClickListener);
        Button okButton = (Button) root.findViewById(R.id.buttonOk);
        okButton.setOnClickListener(okButtonClickListener);
        Button cancelButton = (Button) root.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(cancelButtonClickListener);
    }

    /**
     * Add a listener for the event of a file selected.
     *
     * @param listener The listener to add.
     */
    public void addListener(OnFileSelectedListener listener) {
        this.fileSelectedListeners.add(listener);
    }

    /**
     * Removes a listener for the event of a file selected.
     *
     * @param listener The listener to remove.
     */
    public void removeListener(OnFileSelectedListener listener) {
        this.fileSelectedListeners.remove(listener);
    }

    /**
     * Add a listener for the event of a file selected.
     *
     * @param listener The listener to add.
     */
    public void addListener(OnCancelListener listener) {
        this.cancelListeners.add(listener);
    }

    /**
     * Removes a listener for the event of a file selected.
     *
     * @param listener The listener to remove.
     */
    public void removeListener(OnCancelListener listener) {
        this.cancelListeners.remove(listener);
    }

    /**
     * Removes all the listeners for the event of a file selected.
     */
    public void removeAllListeners() {
        this.fileSelectedListeners.clear();
        this.cancelListeners.clear();
    }

    /**
     * Notify to all listeners that the cancel button has been pressed.
     */
    private void notifyCancelListeners() {
        for (int i = 0; i < FileChooserCore.this.cancelListeners.size(); i++) {
            FileChooserCore.this.cancelListeners.get(i).onCancel();
        }
    }

    /**
     * Notify to all listeners that a file has been selected or created.
     *
     * @param file The file or folder selected or the folder in which the file must be created.
     * @param name The name of the file that must be created or 'null' if a file was selected (instead of being created).
     */
    private void notifyFileListeners(final File file, final String name) {
        // Determine if a file has been selected or created.
        final boolean creation = name != null && name.length() > 0;

        // Verify if a confirmation dialog must be show.
        if ((creation && this.showConfirmationOnCreate || !creation && this.showConfirmationOnSelect)) {
            // Create an alert dialog.
            Context context = this.chooser.getContext();
            AlertDialog.Builder alert = new AlertDialog.Builder(context);

            // Define the dialog's labels.
            String message = null;
            if (FileChooserCore.this.labels != null && ((creation && FileChooserCore.this.labels.messageConfirmCreation != null) || (!creation && FileChooserCore.this.labels.messageConfirmSelection != null))) {
                message = creation ? FileChooserCore.this.labels.messageConfirmCreation : FileChooserCore.this.labels.messageConfirmSelection;
            } else {
                if (FileChooserCore.this.folderMode) {
                    message = context.getString(creation ? R.string.daidalos_confirm_create_folder : R.string.daidalos_confirm_select_folder);
                } else {
                    message = context.getString(creation ? R.string.daidalos_confirm_create_file : R.string.daidalos_confirm_select_file);
                }
            }
            if (message != null)
                message = message.replace("$file_name", name != null ? name : file.getName());
            String posButton = (FileChooserCore.this.labels != null && FileChooserCore.this.labels.labelConfirmYesButton != null) ? FileChooserCore.this.labels.labelConfirmYesButton : context.getString(R.string.daidalos_yes);
            String negButton = (FileChooserCore.this.labels != null && FileChooserCore.this.labels.labelConfirmNoButton != null) ? FileChooserCore.this.labels.labelConfirmNoButton : context.getString(R.string.daidalos_no);

            // Set the message and the 'yes' and 'no' buttons.
            alert.setMessage(message);
            alert.setPositiveButton(posButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Notify to listeners.
                    for (int i = 0; i < FileChooserCore.this.fileSelectedListeners.size(); i++) {
                        if (creation) {
                            FileChooserCore.this.fileSelectedListeners.get(i).onFileSelected(file, name);
                        } else {
                            FileChooserCore.this.fileSelectedListeners.get(i).onFileSelected(file);
                        }
                    }
                }
            });
            alert.setNegativeButton(negButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Do nothing, automatically the dialog is going to be closed.
                }
            });

            // Show the dialog.
            alert.show();
        } else {
            // Notify to listeners.
            for (int i = 0; i < FileChooserCore.this.fileSelectedListeners.size(); i++) {
                if (creation) {
                    FileChooserCore.this.fileSelectedListeners.get(i).onFileSelected(file, name);
                } else {
                    FileChooserCore.this.fileSelectedListeners.get(i).onFileSelected(file);
                }
            }
        }
    }

    /**
     * Allows to define if a confirmation dialog must be show when selecting a file.
     *
     * @param show 'true' for show the confirmation dialog, 'false' for not show the dialog.
     */
    public void setShowConfirmationOnSelect(boolean show) {
        this.showConfirmationOnSelect = show;
    }

    /**
     * Allows to define if a confirmation dialog must be show when creating a file.
     *
     * @param show 'true' for show the confirmation dialog, 'false' for not show the dialog.
     */
    public void setShowConfirmationOnCreate(boolean show) {
        this.showConfirmationOnCreate = show;
    }

    // ----- Get and set methods ----- //

    /**
     * Allows to define if, in the title, must be show only the current folder's name or the full file's path..
     *
     * @param show 'true' for show the full path, 'false' for show only the name.
     */
    public void setShowFullPathInTitle(boolean show) {
        this.showFullPathInTitle = show;
    }

    /**
     * Defines the value of the labels.
     *
     * @param labels The labels.
     */
    public void setLabels(FileChooserLabels labels) {
        this.labels = labels;

        // Verify if the buttons for add a file or select a folder has been modified.
        if (labels != null) {
            LinearLayout root = this.chooser.getRootLayout();

            if (labels.labelAddButton != null) {
                Button addButton = (Button) root.findViewById(R.id.buttonAdd);
                addButton.setText(labels.labelAddButton);
            }

            if (labels.labelSelectButton != null) {
                Button okButton = (Button) root.findViewById(R.id.buttonOk);
                okButton.setText(labels.labelSelectButton);
            }

            if (labels.labelCancelButton != null) {
                Button cancelButton = (Button) root.findViewById(R.id.buttonCancel);
                cancelButton.setText(labels.labelCancelButton);
            }
        }
    }

    /**
     * Set a regular expression to filter the files that can be selected.
     *
     * @param filter A regular expression.
     */
    public void setFilter(String filter) {
        if (filter == null || filter.length() == 0) {
            this.filter = null;
        } else {
            this.filter = filter;
        }

        // Reload the list of files.
        this.loadFolder(this.currentFolder);
    }

    /**
     * Set a regular expression to filter the folders that can be explored.
     *
     * @param folderFilter A regular expression.
     */
    public void setFolderFilter(String folderFilter) {
        if (folderFilter == null || folderFilter.length() == 0) {
            this.folderFilter = null;
        } else {
            this.folderFilter = folderFilter;
        }

        // Reload the list of files.
        this.loadFolder(this.currentFolder);
    }

    /**
     * Defines if the chooser is going to be used to select folders, instead of files.
     *
     * @param folderMode 'true' for select folders or 'false' for select files.
     */
    public void setFolderMode(boolean folderMode) {
        this.folderMode = folderMode;

        // Show or hide the 'Ok' button.
        updateButtonsLayout();

        // Reload the list of files.
        this.loadFolder(this.currentFolder);
    }

    /**
     * Defines if the chooser is going to be used to select folders, instead of files.
     *
     * @param showCancelButton 'true' for show the cancel button or 'false' for not showing it.
     */
    public void setShowCancelButton(boolean showCancelButton) {
        this.showCancelButton = showCancelButton;

        // Show or hide the 'Cancel' button.
        updateButtonsLayout();
    }

    /**
     * Defines if the user can create files, instead of only select files.
     *
     * @param canCreate 'true' if the user can create files or 'false' if it can only select them.
     */
    public void setCanCreateFiles(boolean canCreate) {
        this.canCreateFiles = canCreate;

        // Show or hide the 'Add' button.
        updateButtonsLayout();
    }

    /**
     * Defines if only the files that can be selected (they pass the filter) must be show.
     *
     * @param show 'true' if only the files that can be selected must be show or 'false' if all the files must be show.
     */
    public void setShowOnlySelectable(boolean show) {
        this.showOnlySelectable = show;

        // Reload the list of files.
        this.loadFolder(this.currentFolder);
    }

    /**
     * Returns the current folder.
     *
     * @return The current folder.
     */
    public File getCurrentFolder() {
        return this.currentFolder;
    }

    /**
     * Changes the height of the layout for the buttons, according if the buttons are visible or not.
     */
    private void updateButtonsLayout() {
        // Get the buttons layout.
        LinearLayout root = this.chooser.getRootLayout();

        // Verify if the 'Add' button is visible or not.
        View addButton = root.findViewById(R.id.buttonAdd);
        addButton.setVisibility(this.canCreateFiles ? View.VISIBLE : View.GONE);

        // Verify if the 'Ok' button is visible or not.
        View okButton = root.findViewById(R.id.buttonOk);
        okButton.setVisibility(this.folderMode ? View.VISIBLE : View.GONE);

        // Verify if the 'Cancel' button is visible or not.
        View cancelButton = root.findViewById(R.id.buttonCancel);
        cancelButton.setVisibility(this.showCancelButton ? View.VISIBLE : View.GONE);
    }

    /**
     * Loads all the files of the SD card root.
     */
    public void loadFolder() {
        this.loadFolder(defaultFolder);
    }

    // ----- Miscellaneous methods ----- //

    /**
     * Loads all the files of a folder in the file chooser.
     * <p>
     * If no path is specified ('folderPath' is null) the root folder of the SD card is going to be used.
     *
     * @param folderPath The folder's path.
     */
    public void loadFolder(String folderPath) {
        // Get the file path.
        File path = null;
        if (folderPath != null && folderPath.length() > 0) {
            path = new File(folderPath);
        }

        this.loadFolder(path);
    }

    /**
     * Loads all the files of a folder in the file chooser.
     * <p>
     * If no path is specified ('folder' is null) the root folder of the SD card is going to be used.
     *
     * @param folder The folder.
     */
    public void loadFolder(File folder) {
        // Remove previous files.
        LinearLayout root = this.chooser.getRootLayout();
        LinearLayout layout = (LinearLayout) root.findViewById(R.id.linearLayoutFiles);
        layout.removeAllViews();

        // Get the file path.
        if (folder == null || !folder.exists()) {
            if (this.defaultFolder != null) {
                this.currentFolder = this.defaultFolder;
            } else {
                this.currentFolder = Environment.getExternalStorageDirectory();
            }
        } else {
            this.currentFolder = folder;
        }

        // Verify if the path exists.
        if (this.currentFolder.exists() && layout != null) {
            List<FileItem> fileItems = new LinkedList<FileItem>();

            // Add the parent folder.
            if (this.currentFolder.getParent() != null) {
                File parent = new File(this.currentFolder.getParent());
                if (parent.exists()) {
                    FileItem parentFolder = new FileItem(this.chooser.getContext(), parent, "..");
                    parentFolder.setSelectable(this.folderFilter == null || parent.getAbsolutePath().matches(this.folderFilter));
                    fileItems.add(parentFolder);
                }
            }

            // Verify if the file is a directory.
            if (this.currentFolder.isDirectory()) {
                // Get the folder's files.
                File[] fileList = this.currentFolder.listFiles();

                //Workarround if directories in /storage/emulated cannot be listed
                if (fileList == null && this.currentFolder.getAbsolutePath().equals("/storage/emulated")) {
                    fileList = new File[1];
                    fileList[0] = Environment.getExternalStorageDirectory();
                }

                if (fileList != null) {
                    // Order the files alphabetically and separating folders from files.
                    Arrays.sort(fileList, new Comparator<File>() {
                        public int compare(File file1, File file2) {
                            if (file1 != null && file2 != null) {
                                if (file1.isDirectory() && (!file2.isDirectory())) return -1;
                                if (file2.isDirectory() && (!file1.isDirectory())) return 1;
                                return file1.getName().compareTo(file2.getName());
                            }
                            return 0;
                        }
                    });

                    // Iterate all the files in the folder.
                    for (int i = 0; i < fileList.length; i++) {
                        // Verify if file can be selected.
                        boolean selectable = true;
                        if (!fileList[i].isDirectory()) {
                            // File is selectable as long the user is not selecting folders and if pass the filter (if defined).
                            selectable = !this.folderMode && (this.filter == null || fileList[i].getName().matches(this.filter));
                        } else {
                            // Folders can be selected iif pass the filter (if defined).
                            selectable = this.folderFilter == null || fileList[i].getAbsolutePath().matches(this.folderFilter);
                        }

                        // Verify if the file must be show.
                        if (selectable || !this.showOnlySelectable) {
                            // Create the file item and add it to the list.
                            FileItem fileItem = new FileItem(this.chooser.getContext(), fileList[i]);
                            fileItem.setSelectable(selectable);
                            fileItems.add(fileItem);
                        }
                    }
                }

                // Set the name of the current folder.
                String currentFolderName = this.showFullPathInTitle ? this.currentFolder.getPath() : this.currentFolder.getName();
                this.chooser.setCurrentFolderName(currentFolderName);
            } else {
                // The file is not a folder, add only this file.
                fileItems.add(new FileItem(this.chooser.getContext(), this.currentFolder));
            }


            // Add click listener and add the FileItem objects to the layout.
            for (int i = 0; i < fileItems.size(); i++) {
                fileItems.get(i).addListener(this.fileItemClickListener);
                layout.addView(fileItems.get(i));
            }

            // Refresh default folder.
            defaultFolder = this.currentFolder;
        }
    }

    /**
     * Interface definition for a callback to be invoked when a file is selected.
     */
    public interface OnFileSelectedListener {
        /**
         * Called when a file has been selected.
         *
         * @param file The file selected.
         */
        void onFileSelected(File file);

        /**
         * Called when an user wants to be create a file.
         *
         * @param folder The file's parent folder.
         * @param name   The file's name.
         */
        void onFileSelected(File folder, String name);
    }

    /**
     * Interface definition for a callback to be invoked when the cancel button is clicked.
     */
    public interface OnCancelListener {
        /**
         * Called when the cancel button is clicked.
         */
        void onCancel();
    }
}
