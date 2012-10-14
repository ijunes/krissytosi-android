
package com.krissytosi;

import android.test.ActivityInstrumentationTestCase2;

import com.krissytosi.activities.HomeActivity;

/**
 * This is a simple framework for a test of an Application. See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more
 * information on how to write and extend Application tests.
 * <p/>
 * To run this test, you can type: adb shell am instrument -w \ -e class
 * com.krissytosi.HomeActivityTest \
 * com.krissytosi.tests/android.test.InstrumentationTestRunner
 */
public class HomeActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {

    public HomeActivityTest() {
        super("com.krissytosi", HomeActivity.class);
    }
}
