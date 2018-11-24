package com.owlbear.rowcounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MessageDialog extends DialogFragment {

    public static final String MESSAGE = "MessageDialog.Message";

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle args = getArguments();
                builder.setMessage(args.getString(MESSAGE))
                        .setPositiveButton("OK", null);
        return builder.create();
    }

}
