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

/**
 * Some constants for the app.
 */
public final class KrissyTosiConstants {

    public static final String PACKAGE_IDENTIFIER = "com.krissytosi";

    public static final String FRAGMENT_PHOTOSETS_ID = "photosets";
    public static final String FRAGMENT_STORE_ID = "store";
    public static final String FRAGMENT_BLOG_ID = "blog";
    public static final String FRAGMENT_CONTACT_ID = "contact";

    public static final int FRAGMENT_PHOTOSETS_POSITION = 0;
    public static final int FRAGMENT_STORE_POSITION = 1;
    public static final int FRAGMENT_BLOG_POSITION = 2;
    public static final int FRAGMENT_CONTACT_POSITION = 3;

    public static final String BLOG_URL = "http://cottagefarm.blogspot.com/";

    public static final String TRACKING_KEY = "";

    // broadcast receiver constants

    public static final String KT_TAB_SELECTED = "com.krissytosi.utils.Constants.KT_TAB_SELECTED";
    public static final String KT_CURRENT_TAB_SELECTED = "com.krissytosi.utils.Constants.KT_CURRENT_TAB_SELECTED";
    public static final String KT_FRAGMENT_IDENTIFIER_KEY = "com.krissytosi.utils.Constants.KT_FRAGMENT_IDENTIFIER_KEY";
    public static final String KT_FRAGMENT_IN_DETAIL_VIEW_KEY = "com.krissytosi.utils.Constants.KT_FRAGMENT_IN_DETAIL_VIEW_KEY";
    public static final String KT_FRAGMENT_IN_DETAIL_VIEW = "com.krissytosi.utils.Constants.KT_FRAGMENT_IN_DETAIL_VIEW";
    public static final String KT_NOTIFY_DETAIL_VIEW_KEY = "com.krissytosi.utils.Constants.KT_NOTIFY_DETAIL_VIEW_KEY";
    public static final String KT_PHOTO_LOADED = "com.krissytosi.utils.Constants.KT_PHOTO_LOADED";
    public static final String KT_PHOTO_LOADED_HEIGHT = "com.krissytosi.utils.Constants.KT_PHOTO_LOADED_HEIGHT";
    public static final String KT_PHOTO_LOADED_WIDTH = "com.krissytosi.utils.Constants.KT_PHOTO_LOADED_WIDTH";
    public static final String KT_PHOTOSET_LONG_PRESS = "com.krissytosi.utils.Constants.KT_PHOTOSET_LONG_PRESS";

    // settings constants

    public static final boolean TRACKING_PREFERENCE_DEFAULT = true;
    public static final String TRACKING_PREFERENCE = "tracking_preference";
}
