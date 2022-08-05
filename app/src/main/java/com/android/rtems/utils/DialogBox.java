package com.android.rtems.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * Generalized Dialog Box
 *
 * How to Use Example:
 *      -> 1st parameter is thread object which you want to be executed when 'yes' is pressed
 *      -> 2nd parameter is thread object which you want to be executed when 'no' is pressed
 *      -> 3rd parameter is the message you to be displayed when dialog box is pressed
 *
 *      DialogBox dialogBox = new DialogBox(new TruncateTable(ActivitySettings.this,handler),null,"Clear parameters data permanently?");
 *      dialogBox.show(getSupportFragmentManager(),"tag");
 */
public class DialogBox extends AppCompatDialogFragment {

    private Thread successThread; //on pressing 'Yes', This thread will be executed
    private Thread failureThread; //on pressing 'No', This thread will be executed
    private String message;

    public DialogBox(Thread successThread,Thread failureThread,String message){
        this.successThread = successThread;
        this.failureThread = failureThread;
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBoxBuilder = new AlertDialog.Builder(requireActivity());
        dialogBoxBuilder.setTitle("Alert")
                .setMessage(message)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(failureThread != null) failureThread.start();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(successThread != null) successThread.start();
                    }
                });
        return dialogBoxBuilder.create();
    }
}

