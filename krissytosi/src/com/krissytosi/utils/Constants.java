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
public final class Constants {

    public static final String PROD_API_URL = "http://www.krissytosi.com/api";
    public static final String LOCAL_API_URL = "http://localhost:8080/api";
    public static final String TEST_API_URL = "http://krissytosi.appspot.com/api";

    public static final int HTTP_RESPONSE_CODE_LENGTH = 3;

    public static final String NAME_ID = "name";
    public static final String NUMBER_OF_IMAGES_ID = "numberOfImages";
    public static final String START_INDEX_ID = "startIndex";
    public static final String ORDER_INDEX_ID = "orderIndex";

    public static final String ERROR_IDENTIFIER = "error";
    public static final String ERROR_DESCRIPTION = "description";
    public static final String ERROR_CODE = "code";
    public static final String NO_PORTFOLIOS_DESCRIPTION = "There are no portfolios";
    public static final int ERROR_CODE_INVALID_API_REQUEST = 1;
    public static final int NO_PORTFOLIOS = 2;
    public static final int API_ERROR = 3;

    public static final String RESPONSE_HEADER_NAME = "X-KT-API";
    public static final String RESPONSE_HEADER_VALUE = "X-KT-API";

    public static final String FRAGMENT_HOME_ID = "home";
    public static final String FRAGMENT_PORTFOLIO_ID = "portfolio";
    public static final String FRAGMENT_STORE_ID = "store";
    public static final String FRAGMENT_BLOG_ID = "blog";
    public static final String FRAGMENT_NEWS_ID = "news";
    public static final String FRAGMENT_CONTACT_ID = "contact";

    public static final String BLOG_URL = "http://cottagefarm.blogspot.com/";
}
