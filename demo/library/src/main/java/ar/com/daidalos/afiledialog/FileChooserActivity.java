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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;

/**
 * A file chooser implemented in an Activity.
 */
public class FileChooserActivity extends Activity implements FileChooser {

    // ----- Fields ----- //

    /**
     * Constant used for represent the key of the bundle object (inside the start's intent) which contains the
     * path of the folder which files are going to be listed.
     */
    public static final String INPUT_START_FOLDER = "input_start_folder";
    /**
     * Constant used for represent the key of the bundle object (inside the start's intent) which contains
     * a boolean that indicates if the user is going to select folders instead of select files.
     */
    public static final String INPUT_FOLDER_MODE = "input_folder_mode";
    /**
     * Constant used for represent the key of the bundle object (inside the start's intent) which contains
     * a boolean that indicates if the user can create files.
     */
    public static final String INPUT_CAN_CREATE_FILES = "input_can_create_files";

    // ----- Constants ----- //
    /**
     * Constant used for represent the key of the bundle object (inside the start's intent) which contains
     * a boolean that indicates if the cancel button must be show.
     */
    public static final String INPUT_SHOW_CANCEL_BUTTON = "input_show_cancel_button";
    /**
     * Constant used for represent the key of the bundle object (inside the start's intent) which contains
     * a regular expression which is going to be used as a filter to determine which files can be selected.
     */
    public static final String INPUT_REGEX_FILTER = "input_regex_filter";
    /**
     * Constant used for represent the key of the bundle object (inside the start's intent) which contains
     * a regular expression which is going to be used as a filter to determine which folders can be explored.
     */
    public static final String INPUT_REGEX_FOLDER_FILTER = "input_regex_folder_filter";
    /**
     * Constant used for represent the key of the bundle object (inside the start's intent) which contains
     * a boolean that indicates if only the files that can be selected must be displayed.
     */
    public static final String INPUT_SHOW_ONLY_SELECTABLE = "input_show_only_selectable";
    /**
     * Constant used for represent the key of the bundle object (inside the start's intent) which contains
     * an instance of the class FileChooserLabels that allows to override the default value of the labels.
     */
    public static final String INPUT_LABELS = "input_labels";
    /**
     * Constant used for represent the key of the bundle object (inside the start's intent) which contains
     * a boolean that indicates if a confirmation dialog must be displayed when creating a file.
     */
    public static final String INPUT_SHOW_CONFIRMATION_ON_CREATE = "input_show_confirmation_on_create";
    /**
     * Constant used for represent the key of the bundle object (inside the start's intent) which contains
     * a boolean that indicates if a confirmation dialog must be displayed when selecting a file.
     */
    public static final String INPUT_SHOW_CONFIRMATION_ON_SELECT = "input_show_confirmation_on_select";
    /**
     * Constant used for represent the key of the bundle object (inside the start's intent) which contains
     * a boolean that indicates if the title must show the full path of the current's folder (true) or only
     * the folder's name (false).
     */
    public static final String INPUT_SHOW_FULL_PATH_IN_TITLE = "input_show_full_path_in_title";
    /**
     * Constant used for represent the key of the bundle object (inside the start's intent) which contains
     * a boolean that indicates if the 'Back' button must be used to navigate to the parents folder (true) or
     * if must follow the default behavior (and close the activity when the button is pressed).
     */
    public static final String INPUT_USE_BACK_BUTTON_TO_NAVIGATE = "input_use_back_button_to_navigate";
    /**
     * Constant used for represent the key of the bundle object (inside the result's intent) which contains the
     * File object, that represents the file selected by the user or the folder in which the user wants to create
     * a file.
     */
    public static final String OUTPUT_FILE_OBJECT = "output_file_object";
    /**
     * Constant used for represent the key of the bundle object (inside the result's intent) which contains the
     * name of the file that the user wants to create.
     */
    public static final String OUTPUT_NEW_FILE_NAME = "output_new_file_name";
    /**
     * The folder that the class opened by default.
     */
    private File startFolder;
    /**
     * The core of the file chooser.
     */
    private FileChooserCore core;
    /**
     * A boolean indicating if the 'back' button must be used to navigate to parent folders.
     */
    private boolean useBackButton;

    // ---- Activity methods ----- //

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Call superclass creator.
        super.onCreate(savedInstanceState);

        // Set layout.
        this.setContentView(R.layout.daidalos_file_chooser);

        // Set the background color.
        LinearLayout layout = (LinearLayout) this.findViewById(R.id.rootLayout);
        layout.setBackgroundColor(getResources().getColor(R.color.daidalos_backgroud));

        // Initialize fields.
        this.useBackButton = false;

        // Create the core of the file chooser.
        this.core = new FileChooserCore(this);

        // Verify if the optional parameters has been defined.
        String folderPath = null;
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(INPUT_START_FOLDER))
                folderPath = extras.getString(INPUT_START_FOLDER);
            if (extras.containsKey(INPUT_REGEX_FILTER))
                core.setFilter(extras.getString(INPUT_REGEX_FILTER));
            if (extras.containsKey(INPUT_REGEX_FOLDER_FILTER))
                core.setFolderFilter(extras.getString(INPUT_REGEX_FOLDER_FILTER));
            if (extras.containsKey(INPUT_SHOW_ONLY_SELECTABLE))
                core.setShowOnlySelectable(extras.getBoolean(INPUT_SHOW_ONLY_SELECTABLE));
            if (extras.containsKey(INPUT_FOLDER_MODE))
                core.setFolderMode(extras.getBoolean(INPUT_FOLDER_MODE));
            if (extras.containsKey(INPUT_CAN_CREATE_FILES))
                core.setCanCreateFiles(extras.getBoolean(INPUT_CAN_CREATE_FILES));
            if (extras.containsKey(INPUT_LABELS))
                core.setLabels((FileChooserLabels) extras.get(INPUT_LABELS));
            if (extras.containsKey(INPUT_SHOW_CONFIRMATION_ON_CREATE))
                core.setShowConfirmationOnCreate(extras.getBoolean(INPUT_SHOW_CONFIRMATION_ON_CREATE));
            if (extras.containsKey(INPUT_SHOW_CANCEL_BUTTON))
                core.setShowCancelButton(extras.getBoolean(INPUT_SHOW_CANCEL_BUTTON));
            if (extras.containsKey(INPUT_SHOW_CONFIRMATION_ON_SELECT))
                core.setShowConfirmationOnSelect(extras.getBoolean(INPUT_SHOW_CONFIRMATION_ON_SELECT));
            if (extras.containsKey(INPUT_SHOW_FULL_PATH_IN_TITLE))
                core.setShowFullPathInTitle(extras.getBoolean(INPUT_SHOW_FULL_PATH_IN_TITLE));
            if (extras.containsKey(INPUT_USE_BACK_BUTTON_TO_NAVIGATE))
                this.useBackButton = extras.getBoolean(INPUT_USE_BACK_BUTTON_TO_NAVIGATE);
        }

        // Load the files of a folder.
        core.loadFolder(folderPath);
        this.startFolder = this.core.getCurrentFolder();

        // Add a listener for when a file is selected.
        core.addListener(new FileChooserCore.OnFileSelectedListener() {
            public void onFileSelected(File folder, String name) {
                // Pass the data through an intent.
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(OUTPUT_FILE_OBJECT, folder);
                bundle.putString(OUTPUT_NEW_FILE_NAME, name);
                intent.putExtras(bundle);

                setResult(RESULT_OK, intent);
                finish();
            }

            public void onFileSelected(File file) {
                // Pass the data through an intent.
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(OUTPUT_FILE_OBJECT, file);
                intent.putExtras(bundle);

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // Add a listener for when the cancel button is pressed.
        core.addListener(new FileChooserCore.OnCancelListener() {
            public void onCancel() {
                // Close activity.
                FileChooserActivity.super.onBackPressed();
            }
        });
    }

    /**
     * Called when the user push the 'back' button.
     */
    @Override
    public void onBackPressed() {
        // Verify if the activity must be finished or if the parent folder must be opened.
        File current = this.core.getCurrentFolder();
        if (!this.useBackButton || current == null || current.getParent() == null || current.getPath().compareTo(this.startFolder.getPath()) == 0) {
            // Close activity.
            super.onBackPressed();
        } else {
            // Open parent.
            this.core.loadFolder(current.getParent());
        }
    }

    // ----- FileChooser methods ----- //

    public LinearLayout getRootLayout() {
        View root = this.findViewById(R.id.rootLayout);
        return (root instanceof LinearLayout) ? (LinearLayout) root : null;
    }

    public Context getContext() {
        //return this.getBaseContext();
        return this;
    }

    public void setCurrentFolderName(String name) {
        this.setTitle(name);
    }
}
