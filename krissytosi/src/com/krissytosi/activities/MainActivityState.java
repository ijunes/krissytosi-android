/*
   Copyright 2012 - 2014 Sean O' Shea

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

    /**
     * Stores what tab the user is currently on.
     */
    private int currentTabPosition = -1;

    /**
     * Indicates which fragment is in a detail view (Store or PhotoSet)
     */
    private int fragmentIdentifierInDetailView;

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
        dest.writeInt(currentTabPosition);
        dest.writeInt(fragmentIdentifierInDetailView);
    }

    private void readFromParcel(Parcel in) {
        currentTabPosition = in.readInt();
        fragmentIdentifierInDetailView = in.readInt();
    }

    // Getters/Setters

    public int getCurrentTabPosition() {
        return currentTabPosition;
    }

    public void setCurrentTabPosition(int currentTabPosition) {
        this.currentTabPosition = currentTabPosition;
    }

    public int getFragmentIdentifierInDetailView() {
        return fragmentIdentifierInDetailView;
    }

    public void setFragmentIdentifierInDetailView(int fragmentIdentifierInDetailView) {
        this.fragmentIdentifierInDetailView = fragmentIdentifierInDetailView;
    }
}
