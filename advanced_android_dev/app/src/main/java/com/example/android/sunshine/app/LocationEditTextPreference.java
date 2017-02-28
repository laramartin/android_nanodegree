package com.example.android.sunshine.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by lara on 27/2/17.
 */

public class LocationEditTextPreference extends EditTextPreference {

    public LocationEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LocationEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LocationEditText,
                0, 0);
        try {
            int minLenght = array.getInteger(R.styleable.LocationEditText_minimumLenght, 0);
            Log.v("locationEditTextPref", "min lenght: " + minLenght);
        } finally {
            array.recycle();
        }
    }

    public LocationEditTextPreference(Context context) {
        super(context);
    }

    @Override
    protected void showDialog(Bundle state) {
        final int DEFAULT_MINIMUM_LOCATION_INPUT_LENGTH = 2;
        super.showDialog(state);
        EditText editText = getEditText();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Dialog dialog = getDialog();
                if (dialog instanceof AlertDialog) {
                    {
                        AlertDialog alertDialog = (AlertDialog) dialog;
                        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        if (s.length() > DEFAULT_MINIMUM_LOCATION_INPUT_LENGTH) {
                            positiveButton.setEnabled(true);
                        } else {
                            positiveButton.setEnabled(false);
                        }
                    }
                }
            }
        });
    }
}
