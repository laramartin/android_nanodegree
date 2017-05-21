package com.udacity.gradle.testing;

import android.util.Log;

public class Echo {
    static <T> T echo(T o) {
        return echo(o, false);
    }

    static <T> T echo(T o, boolean log) {
        if (log) Log.i(Echo.class.getName(), o.toString());

        return o;
    }
}
