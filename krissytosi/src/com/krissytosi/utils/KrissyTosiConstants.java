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

    public static final String FRAGMENT_PORTFOLIO_ID = "portfolio";
    public static final String FRAGMENT_STORE_ID = "store";
    public static final String FRAGMENT_BLOG_ID = "blog";
    public static final String FRAGMENT_NEWS_ID = "news";
    public static final String FRAGMENT_CONTACT_ID = "contact";

    public static final String BLOG_URL = "http://cottagefarm.blogspot.com/";

    public static final String TRACKING_KEY = "";

    // broadcast receiver constants

    public static final String KT_TAB_SELECTED = "com.krissytosi.utils.Constants.KT_TAB_SELECTED";
    public static final String KT_TAB_SELECTED_KEY = "com.krissytosi.utils.Constants.KT_TAB_SELECTED_KEY";

    // settings constants

    public static final boolean TRACKING_PREFERENCE_DEFAULT = true;
    public static final String TRACKING_PREFERENCE = "tracking_preference";

    public static final int ECLAIR_OS_VERSION = 7;
    public static final int FROYO_OS_VERSION = 8;
    public static final int GINGERBREAD_OS_VERSION = 9;
    public static final int HONEYCOMB_OS_VERSION = 11;
    public static final int HONEYCOMB_3_2_OS_VERSION = 13;
    public static final int ICS_OS_VERSION = 14;
}
