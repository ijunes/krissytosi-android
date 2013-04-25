/*
   Copyright 2012 Sean O' Shea

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.krissytosi;

import android.os.Parcel;
import android.test.AndroidTestCase;

import com.krissytosi.activities.MainActivityState;

public class MainActivityStateTest extends AndroidTestCase {

    private final int currentTabPosition = 1;
    private final int fragmentIdentifierInDetailView = 2;

    public void testMainActivityStateRead() throws Exception {
        Parcel parcel = Parcel.obtain();
        parcel.writeInt(currentTabPosition);
        parcel.writeInt(fragmentIdentifierInDetailView);
        parcel.setDataPosition(0);
        MainActivityState state = new MainActivityState(parcel);
        assertTrue(state.getCurrentTabPosition() == currentTabPosition);
        assertTrue(state.getFragmentIdentifierInDetailView() == fragmentIdentifierInDetailView);
    }

    public void testMainActivityStateWrite() throws Exception {
        Parcel parcel = Parcel.obtain();
        MainActivityState state = new MainActivityState(parcel);
        state.setCurrentTabPosition(currentTabPosition);
        state.setFragmentIdentifierInDetailView(fragmentIdentifierInDetailView);
        state.writeToParcel(parcel, 0);
    }
}
