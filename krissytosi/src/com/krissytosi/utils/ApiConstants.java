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

import com.etsy.etsyCore.PermissionScope;

/**
 * Some constants for API access.
 */
public class ApiConstants {

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

    public static final String ETSY_API_KEY = "l5k8bfu3uyvjy80n0o547zlq";
    public static final String ETSY_API_SECRET = "lxep7iqhx9";
    public static final String ETSY_STORE_ID = "5547124";
    public static final String ETSY_CALLBACK = "oob";
    private static final String[] ETSY_SCOPE_ARRAY = {
            PermissionScope.READ_LISTINGS, PermissionScope.READ_FEEDBACK
    };
    public static final PermissionScope ETSY_SCOPE = new PermissionScope(ETSY_SCOPE_ARRAY);

}
