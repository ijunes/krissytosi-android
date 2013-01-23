/*
   Copyright 2012 - 2013 Sean O' Shea

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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Used to understand whether or not the device has a network connection or not.
 */
public class NetworkReceiver extends BroadcastReceiver {

    /**
     * Indicating that the device has connectivity to the internet.
     */
    public boolean hasConnectivity;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInformation = connMgr.getActiveNetworkInfo();
        boolean hadConnectivity = hasConnectivity;
        hasConnectivity = networkInformation != null
                && networkInformation.isConnectedOrConnecting();
        // check to see whether there is any different between the previous
        // state and the current state
        if (hadConnectivity != hasConnectivity) {
            // TODO
        }
    }
}
