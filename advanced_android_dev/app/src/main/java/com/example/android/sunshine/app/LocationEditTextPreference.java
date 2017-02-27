package com.example.android.sunshine.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.util.Log;

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
}
