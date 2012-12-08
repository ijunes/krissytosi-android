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

package com.krissytosi.utils;

import android.content.Context;

import com.krissytosi.tracking.Tracking;

public class LoggingTracking implements Tracking {

    @Override
    public void initialize(Context context, String key) {
        // TODO Auto-generated method stub

    }

    @Override
    public void trackTabChange(String tabIdentifier) {
        // TODO Auto-generated method stub

    }

    @Override
    public void trackPhotoSetChange(String photoSetIdentifier) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mediaShared(String photoSetIdentifier, long mode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void disableTracking(Context context) {
        // TODO Auto-generated method stub

    }

    @Override
    public void enableTracking(Context context) {
        // TODO Auto-generated method stub

    }

}
