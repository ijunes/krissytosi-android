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

    public static final String ETSY_API_KEY = "l5k8bfu3uyvjy80n0o547zlq";
    public static final String ETSY_API_SECRET = "lxep7iqhx9";
    public static final String ETSY_STORE_ID = "7386975";
    public static final String ETSY_CALLBACK = "oob";
    private static final String[] ETSY_SCOPE_ARRAY = {
            PermissionScope.READ_LISTINGS, PermissionScope.READ_FEEDBACK
    };
    public static final PermissionScope ETSY_SCOPE = new PermissionScope(ETSY_SCOPE_ARRAY);

}
