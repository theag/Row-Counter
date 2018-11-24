package com.owlbear.rowcounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ConfirmDialog extends DialogFragment {

    public static final String MESSAGE = "ConfirmDialog.Message";

    private ConfirmDialogListener listener;

    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  ConfirmDialogListener) {
            listener = (ConfirmDialogListener)context;
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle args = getArguments();
        builder.setMessage(args.getString(MESSAGE))
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    confirmOnClick(true);
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    confirmOnClick(false);
                }
            });
        return builder.create();
    }

    public void confirmOnClick(boolean ok) {
        if(listener != null) {
            listener.onclick(getTag(), ok);
        }
    }

    public interface ConfirmDialogListener {
        public void onclick(String tag, boolean ok);
    }

}
