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

package com.krissytosi.activities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * State class for the main activity.
 */
public class MainActivityState implements Parcelable {

    private String currentTabIdentifier;
    private String fragmentIdentifierInDetailView;

    public static final Parcelable.Creator<MainActivityState> CREATOR = new Parcelable.Creator<MainActivityState>() {
        @Override
        public MainActivityState createFromParcel(Parcel in) {
            return new MainActivityState(in);
        }

        @Override
        public MainActivityState[] newArray(int size) {
            return new MainActivityState[size];
        }
    };

    public MainActivityState() {

    }

    public MainActivityState(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currentTabIdentifier);
        dest.writeString(fragmentIdentifierInDetailView);
    }

    private void readFromParcel(Parcel in) {
        currentTabIdentifier = in.readString();
        fragmentIdentifierInDetailView = in.readString();
    }

    // Getters/Setters

    public String getCurrentTabIdentifier() {
        return currentTabIdentifier;
    }

    public void setCurrentTabIdentifier(String currentTabIdentifier) {
        this.currentTabIdentifier = currentTabIdentifier;
    }

    public String getFragmentIdentifierInDetailView() {
        return fragmentIdentifierInDetailView;
    }

    public void setFragmentIdentifierInDetailView(String fragmentIdentifierInDetailView) {
        this.fragmentIdentifierInDetailView = fragmentIdentifierInDetailView;
    }

}
