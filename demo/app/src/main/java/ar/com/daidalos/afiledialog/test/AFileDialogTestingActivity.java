/*
 * <Copyright 2013 Jose F. Maldonado>
 *
 *  This file is part of aFileDialog.
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ar.com.daidalos.afiledialog.test;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.io.Serializable;

import ar.com.daidalos.afiledialog.FileChooserActivity;
import ar.com.daidalos.afiledialog.FileChooserDialog;
import ar.com.daidalos.afiledialog.FileChooserLabels;

public class AFileDialogTestingActivity extends Activity {
    private OnClickListener btnActivitySimpleOpen = new OnClickListener() {
        public void onClick(View v) {
            // Create the intent for call the activity.
            Intent intent = new Intent(AFileDialogTestingActivity.this, FileChooserActivity.class);

            // Call the activity
            AFileDialogTestingActivity.this.startActivityForResult(intent, 0);
        }
    };

    // ----- Buttons for open a dialog ----- //
    private OnClickListener btnActivityOpenDownloads = new OnClickListener() {
        public void onClick(View v) {
            // Create the intent for call the activity.
            Intent intent = new Intent(AFileDialogTestingActivity.this, FileChooserActivity.class);

            // Define start folder.
            intent.putExtra(FileChooserActivity.INPUT_START_FOLDER, Environment.getExternalStorageDirectory() + "/Download/");

            // Call the activity
            AFileDialogTestingActivity.this.startActivityForResult(intent, 0);
        }
    };
    private OnClickListener btnActivitySelectFolders = new OnClickListener() {
        public void onClick(View v) {
            // Create the intent for call the activity.
            Intent intent = new Intent(AFileDialogTestingActivity.this, FileChooserActivity.class);

            // Activate the folder mode.
            intent.putExtra(FileChooserActivity.INPUT_FOLDER_MODE, true);

            // Call the activity
            AFileDialogTestingActivity.this.startActivityForResult(intent, 0);
        }
    };
    private OnClickListener btnActivityCreateFiles = new OnClickListener() {
        public void onClick(View v) {
            // Create the intent for call the activity.
            Intent intent = new Intent(AFileDialogTestingActivity.this, FileChooserActivity.class);

            // Activate the button for create files.
            intent.putExtra(FileChooserActivity.INPUT_CAN_CREATE_FILES, true);

            // Call the activity
            AFileDialogTestingActivity.this.startActivityForResult(intent, 0);
        }
    };
    private OnClickListener btnActivitySelectImages = new OnClickListener() {
        public void onClick(View v) {
            // Create the intent for call the activity.
            Intent intent = new Intent(AFileDialogTestingActivity.this, FileChooserActivity.class);

            // Define the filter for select images.
            intent.putExtra(FileChooserActivity.INPUT_REGEX_FILTER, ".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");

            // Call the activity
            AFileDialogTestingActivity.this.startActivityForResult(intent, 0);
        }
    };
    private OnClickListener btnActivityAskConfirmation = new OnClickListener() {
        public void onClick(View v) {
            // Create the intent for call the activity.
            Intent intent = new Intent(AFileDialogTestingActivity.this, FileChooserActivity.class);

            // Activate the button for create files.
            intent.putExtra(FileChooserActivity.INPUT_CAN_CREATE_FILES, true);

            // Activate the confirmation dialogs.
            intent.putExtra(FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_CREATE, true);
            intent.putExtra(FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_SELECT, true);

            // Call the activity
            AFileDialogTestingActivity.this.startActivityForResult(intent, 0);
        }
    };
    private OnClickListener btnActivityCustomLabels = new OnClickListener() {
        public void onClick(View v) {
            // Create the intent for call the activity.
            Intent intent = new Intent(AFileDialogTestingActivity.this, FileChooserActivity.class);

            // Activate the folder mode.
            intent.putExtra(FileChooserActivity.INPUT_FOLDER_MODE, true);

            // Activate the button for create files.
            intent.putExtra(FileChooserActivity.INPUT_CAN_CREATE_FILES, true);

            // Activate the cancel button.
            intent.putExtra(FileChooserActivity.INPUT_SHOW_CANCEL_BUTTON, true);

            // Activate the confirmation dialogs.
            intent.putExtra(FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_CREATE, true);
            intent.putExtra(FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_SELECT, true);

            // Define the labels.
            FileChooserLabels labels = new FileChooserLabels();
            labels.createFileDialogAcceptButton = "AcceptButton";
            labels.createFileDialogCancelButton = "CancelButton";
            labels.createFileDialogMessage = "DialogMessage";
            labels.createFileDialogTitle = "DialogTitle";
            labels.labelAddButton = "AddButton";
            labels.labelSelectButton = "SelectButton";
            labels.messageConfirmCreation = "messageConfirmCreation";
            labels.messageConfirmSelection = "messageConfirmSelection";
            labels.labelConfirmYesButton = "yesButton";
            labels.labelConfirmNoButton = "noButton";
            labels.labelCancelButton = "cancelButton";
            intent.putExtra(FileChooserActivity.INPUT_LABELS, (Serializable) labels);

            // Call the activity
            AFileDialogTestingActivity.this.startActivityForResult(intent, 0);
        }
    };
    private OnClickListener btnActivityCancelButton = new OnClickListener() {
        public void onClick(View v) {
            // Create the intent for call the activity.
            Intent intent = new Intent(AFileDialogTestingActivity.this, FileChooserActivity.class);

            // Activate the folder mode.
            intent.putExtra(FileChooserActivity.INPUT_SHOW_CANCEL_BUTTON, true);

            // Call the activity
            AFileDialogTestingActivity.this.startActivityForResult(intent, 0);
        }
    };
    private OnClickListener btnActivityFilterFolders = new OnClickListener() {
        public void onClick(View v) {
            // Create the intent for call the activity.
            Intent intent = new Intent(AFileDialogTestingActivity.this, FileChooserActivity.class);

            // Define start folder and don't allow to go up that folder.
            intent.putExtra(FileChooserActivity.INPUT_START_FOLDER, Environment.getExternalStorageDirectory() + "");
            intent.putExtra(FileChooserActivity.INPUT_REGEX_FOLDER_FILTER, "(" + Environment.getExternalStorageDirectory() + ")|((.*)Download(.*))");

            // Call the activity
            AFileDialogTestingActivity.this.startActivityForResult(intent, 0);
        }
    };
    private FileChooserDialog.OnFileSelectedListener onFileSelectedListener = new FileChooserDialog.OnFileSelectedListener() {
        public void onFileSelected(Dialog source, File file) {
            source.hide();
            Toast toast = Toast.makeText(AFileDialogTestingActivity.this, "File selected: " + file.getName(), Toast.LENGTH_LONG);
            toast.show();
        }

        public void onFileSelected(Dialog source, File folder, String name) {
            source.hide();
            Toast toast = Toast.makeText(AFileDialogTestingActivity.this, "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
            toast.show();
        }
    };

    // ---- Buttons for open an activity ----- //
    private OnClickListener btnDialogSimpleOpen = new OnClickListener() {
        public void onClick(View v) {
            // Create the dialog.
            FileChooserDialog dialog = new FileChooserDialog(AFileDialogTestingActivity.this);

            // Assign listener for the select event.
            dialog.addListener(AFileDialogTestingActivity.this.onFileSelectedListener);

            // Show the dialog.
            dialog.show();
        }
    };
    private OnClickListener btnDialogOpenDownloads = new OnClickListener() {
        public void onClick(View v) {
            // Create the dialog.
            FileChooserDialog dialog = new FileChooserDialog(AFileDialogTestingActivity.this);

            // Assign listener for the select event.
            dialog.addListener(AFileDialogTestingActivity.this.onFileSelectedListener);

            // Define start folder.
            dialog.loadFolder(Environment.getExternalStorageDirectory() + "/Download/");

            // Show the dialog.
            dialog.show();
        }
    };
    private OnClickListener btnDialogSelectFolders = new OnClickListener() {
        public void onClick(View v) {
            // Create the dialog.
            FileChooserDialog dialog = new FileChooserDialog(AFileDialogTestingActivity.this);

            // Assign listener for the select event.
            dialog.addListener(AFileDialogTestingActivity.this.onFileSelectedListener);

            // Activate the folder mode.
            dialog.setFolderMode(true);

            // Show the dialog.
            dialog.show();
        }
    };
    private OnClickListener btnDialogCreateFiles = new OnClickListener() {
        public void onClick(View v) {
            // Create the dialog.
            FileChooserDialog dialog = new FileChooserDialog(AFileDialogTestingActivity.this);

            // Assign listener for the select event.
            dialog.addListener(AFileDialogTestingActivity.this.onFileSelectedListener);

            // Activate the button for create files.
            dialog.setCanCreateFiles(true);

            // Show the dialog.
            dialog.show();
        }
    };
    private OnClickListener btnDialogSelectImages = new OnClickListener() {
        public void onClick(View v) {
            // Create the dialog.
            FileChooserDialog dialog = new FileChooserDialog(AFileDialogTestingActivity.this);

            // Assign listener for the select event.
            dialog.addListener(AFileDialogTestingActivity.this.onFileSelectedListener);

            // Define the filter for select images.
            dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
            dialog.setShowOnlySelectable(false);

            // Show the dialog.
            dialog.show();
        }
    };
    private OnClickListener btnDialogAskConfirmation = new OnClickListener() {
        public void onClick(View v) {
            // Create the dialog.
            FileChooserDialog dialog = new FileChooserDialog(AFileDialogTestingActivity.this);

            // Assign listener for the select event.
            dialog.addListener(AFileDialogTestingActivity.this.onFileSelectedListener);

            // Activate the button for create files.
            dialog.setCanCreateFiles(true);

            // Activate the confirmation dialogs.
            dialog.setShowConfirmation(true, true);

            // Show the dialog.
            dialog.show();
        }
    };
    private OnClickListener btnDialogCustomLabels = new OnClickListener() {
        public void onClick(View v) {
            // Create the dialog.
            FileChooserDialog dialog = new FileChooserDialog(AFileDialogTestingActivity.this);

            // Assign listener for the select event.
            dialog.addListener(AFileDialogTestingActivity.this.onFileSelectedListener);

            // Activate the folder mode.
            dialog.setFolderMode(true);

            // Activate the button for create files.
            dialog.setCanCreateFiles(true);

            // Activate the button for cancel.
            dialog.setShowCancelButton(true);

            // Activate the confirmation dialogs.
            dialog.setShowConfirmation(true, true);

            // Define the labels.
            FileChooserLabels labels = new FileChooserLabels();
            labels.createFileDialogAcceptButton = "AcceptButton";
            labels.createFileDialogCancelButton = "CancelButton";
            labels.createFileDialogMessage = "DialogMessage";
            labels.createFileDialogTitle = "DialogTitle";
            labels.labelAddButton = "AddButton";
            labels.labelSelectButton = "SelectButton";
            labels.messageConfirmCreation = "messageConfirmCreation";
            labels.messageConfirmSelection = "messageConfirmSelection";
            labels.labelConfirmYesButton = "yesButton";
            labels.labelConfirmNoButton = "noButton";
            labels.labelCancelButton = "cancelButton";
            dialog.setLabels(labels);

            // Show the dialog.
            dialog.show();
        }
    };
    private OnClickListener btnDialogCancelButton = new OnClickListener() {
        public void onClick(View v) {
            // Create the dialog.
            FileChooserDialog dialog = new FileChooserDialog(AFileDialogTestingActivity.this);

            // Assign listener for the select event.
            dialog.addListener(AFileDialogTestingActivity.this.onFileSelectedListener);

            // Activate the button for create files.
            dialog.setShowCancelButton(true);

            // Show the dialog.
            dialog.show();
        }
    };
    private OnClickListener btnDialogFilterFolders = new OnClickListener() {
        public void onClick(View v) {
            // Create the dialog.
            FileChooserDialog dialog = new FileChooserDialog(AFileDialogTestingActivity.this);

            // Assign listener for the select event.
            dialog.addListener(AFileDialogTestingActivity.this.onFileSelectedListener);

            // Define start folder and don't allow to go up that folder.
            dialog.loadFolder(Environment.getExternalStorageDirectory() + "");
            dialog.setFolderFilter("(" + Environment.getExternalStorageDirectory() + ")|((.*)Download(.*))");

            // Show the dialog.
            dialog.show();
        }
    };

	/*
    private OnClickListener clickButtonOpenActivity = new OnClickListener() {
    	public void onClick(View v) {
            Intent intent = new Intent(AFileDialogTestingActivity.this, FileChooserActivity.class);
            intent.putExtra(FileChooserActivity.INPUT_REGEX_FILTER, ".*pdf|.*jpg|.*png|.*mp3|.*mp4|.*avi");           
            intent.putExtra(FileChooserActivity.INPUT_SHOW_ONLY_SELECTABLE, true);           
            intent.putExtra(FileChooserActivity.INPUT_CAN_CREATE_FILES, true);
            intent.putExtra(FileChooserActivity.INPUT_FOLDER_MODE, true);
            intent.putExtra(FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_CREATE, true);
            intent.putExtra(FileChooserActivity.INPUT_SHOW_CONFIRMATION_ON_SELECT, true);
            
    		// Define labels.
    		FileChooserLabels labels = new FileChooserLabels();
    		labels.createFileDialogAcceptButton = "AcceptButton";
    		labels.createFileDialogCancelButton = "CancelButton";
    		labels.createFileDialogMessage = "DialogMessage";
    		labels.createFileDialogTitle = "DialogTitle";
    		labels.labelAddButton = "AddButton";
    		labels.labelSelectButton = "SelectButton";
    		labels.messageConfirmCreation = "messageConfirmCreation";
    		labels.messageConfirmSelection = "messageConfirmSelection";
    		labels.labelConfirmYesButton = "yesButton";
    		labels.labelConfirmNoButton = "noButton";
    		intent.putExtra(FileChooserActivity.INPUT_LABELS, (Serializable) labels);   
            
            AFileDialogTestingActivity.this.startActivityForResult(intent, 0);
    	}
	};
	*/

	private static int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101;

    // ---- Methods for display the results ----- //

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Assign behaviors to the buttons.
        Button buttonActivity1 = (Button) this.findViewById(R.id.activity_simple_open);
        buttonActivity1.setOnClickListener(btnActivitySimpleOpen);
        Button buttonActivity2 = (Button) this.findViewById(R.id.activity_open_downloads);
        buttonActivity2.setOnClickListener(btnActivityOpenDownloads);
        Button buttonActivity3 = (Button) this.findViewById(R.id.activity_select_folders);
        buttonActivity3.setOnClickListener(btnActivitySelectFolders);
        Button buttonActivity4 = (Button) this.findViewById(R.id.activity_create_files);
        buttonActivity4.setOnClickListener(btnActivityCreateFiles);
        Button buttonActivity5 = (Button) this.findViewById(R.id.activity_select_images);
        buttonActivity5.setOnClickListener(btnActivitySelectImages);
        Button buttonActivity6 = (Button) this.findViewById(R.id.activity_ask_confirmation);
        buttonActivity6.setOnClickListener(btnActivityAskConfirmation);
        Button buttonActivity7 = (Button) this.findViewById(R.id.activity_custom_labels);
        buttonActivity7.setOnClickListener(btnActivityCustomLabels);
        Button buttonActivity8 = (Button) this.findViewById(R.id.activity_cancel_button);
        buttonActivity8.setOnClickListener(btnActivityCancelButton);
        Button buttonActivity9 = (Button) this.findViewById(R.id.activity_filter_folders);
        buttonActivity9.setOnClickListener(btnActivityFilterFolders);

        Button buttonDialog1 = (Button) this.findViewById(R.id.dialog_simple_open);
        buttonDialog1.setOnClickListener(btnDialogSimpleOpen);
        Button buttonDialog2 = (Button) this.findViewById(R.id.dialog_open_downloads);
        buttonDialog2.setOnClickListener(btnDialogOpenDownloads);
        Button buttonDialog3 = (Button) this.findViewById(R.id.dialog_select_folders);
        buttonDialog3.setOnClickListener(btnDialogSelectFolders);
        Button buttonDialog4 = (Button) this.findViewById(R.id.dialog_create_files);
        buttonDialog4.setOnClickListener(btnDialogCreateFiles);
        Button buttonDialog5 = (Button) this.findViewById(R.id.dialog_select_images);
        buttonDialog5.setOnClickListener(btnDialogSelectImages);
        Button buttonDialog6 = (Button) this.findViewById(R.id.dialog_ask_confirmation);
        buttonDialog6.setOnClickListener(btnDialogAskConfirmation);
        Button buttonDialog7 = (Button) this.findViewById(R.id.dialog_custom_labels);
        buttonDialog7.setOnClickListener(btnDialogCustomLabels);
        Button buttonDialog8 = (Button) this.findViewById(R.id.dialog_cancel_button);
        buttonDialog8.setOnClickListener(btnDialogCancelButton);
        Button buttonDialog9 = (Button) this.findViewById(R.id.dialog_filter_folders);
        buttonDialog9.setOnClickListener(btnDialogFilterFolders);

        // Check permissions (required starting Android 6).
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, ask it.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            boolean fileCreated = false;
            String filePath = "";

            Bundle bundle = data.getExtras();
            if (bundle != null) {
                if (bundle.containsKey(FileChooserActivity.OUTPUT_NEW_FILE_NAME)) {
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

            String message = fileCreated ? "File created" : "File opened";
            message += ": " + filePath;
            Toast toast = Toast.makeText(AFileDialogTestingActivity.this, message, Toast.LENGTH_LONG);
            toast.show();
        }
    }
}