/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.background.sync;


import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class ReminderUtilities {

    public static int REMINDER_INTERVAL_MINUTES = 15;
    public static int REMINDER_INTERVAL_SECONDS = (int) TimeUnit.MINUTES.toSeconds(
            REMINDER_INTERVAL_MINUTES);
    public static int SYNC_FLEXTIME_SECONDS;
    public static String REMINDER_JOB_TAG = "hydration_reminder_tag";
    private static boolean sInitialized;

    // synchronized to not have this method executed more than once at the time
    synchronized public static void scheduleChargingReminder(Context context) {
        if (sInitialized) {
            return;
        }
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job job = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(WaterReminderFirebaseJobService.class)
                // uniquely identifies the job
                .setTag(REMINDER_JOB_TAG)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        // only run when the device is charging
                        Constraint.DEVICE_CHARGING
                )
                // execute job forever even between reboots
                .setLifetime(Lifetime.FOREVER)
                // one-off job
                .setRecurring(true)
                // start between 0 and 15 minutes (900 seconds)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                // overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(job);
        sInitialized = true;
    }
}
