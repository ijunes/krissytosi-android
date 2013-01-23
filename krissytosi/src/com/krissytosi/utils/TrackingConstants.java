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

package com.krissytosi.utils;

/**
 * Used to hold constants which are used to track various user interactions in
 * the application.
 */
public class TrackingConstants {

    public static final String TRACKING_KEY = "UA-32840879-2";

    public static final int DEFAULT_SAMPLE_RATE_SECONDS = 60;
    public static final boolean DEFAULT_DEBUG_ENABLED = false;

    public static final String CATEGORY_IDENTIFIER_UI_ACTION = "ui_action";
    public static final String ACTION_IDENTIFIER_TAB_PRESS = "tab_press";
    public static final String ACTION_IDENTIFIER_PHOTOSET_CHANGE = "photoset_change";
    public static final String ACTION_IDENTIFIER_MEDIA_SHARE = "media_share";

}
