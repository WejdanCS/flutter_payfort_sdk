package com.example.flutter_payfort_sdk;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class MySpinnerDialog extends DialogFragment {
    private  String message;
    public MySpinnerDialog(String message) {
        this.message=message;
        // use empty constructors. If something is needed use onCreate's
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        ProgressDialog _dialog = new ProgressDialog(getActivity());
        this.setStyle(STYLE_NO_TITLE, getTheme()); // You can use styles or inflate a view
        _dialog.setMessage(this.message); // set your messages if not inflated from XML

        _dialog.setCancelable(false);

        return _dialog;
    }
}
