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

    public static final String DIGEST_ALGORITHM = "MD5";
    public static final String FLICKR_API_KEY = "3426649638b25fe317be122d3fbbc1b1";
    public static final String FLICKR_API_SECRET = "2e375fd517079607";
    // right now these two URLs are exactly the same, but we could make them
    // different if new Flickr services come online for developer betas.
    public static final String PROD_API_URL = "http://api.flickr.com/services/rest/?";
    public static final String TEST_API_URL = "http://api.flickr.com/services/rest/?";

    public static final String FLICKR_GET_LIST_METHOD = "flickr.photosets.getList";
    public static final String FLICKR_GET_PHOTOS_METHOD = "flickr.photosets.getPhotos";

    public static final String FLICKR_USER_ID = "48071389@N07";
    public static final String FLICKR_USER_ID_PARAM = "user_id";
    public static final String FLICKR_API_KEY_PARAM = "api_key";
    public static final String FLICKR_PHOTOSET_ID_PARAM = "photoset_id";
    public static final String FLICKR_NO_JSON_CALLBACK_PARAM = "nojsoncallback";
    public static final String FLICKR_FORMAT_PARAM = "format";
    public static final String FLICKR_METHOD_PARAM = "method";
    public static final String FLICKR_EXTRAS_ID_PARAM = "extras";
    public static final String FLICKR_API_SIGNATURE_PARAM = "api_sig";

    public static final String FLICKR_FORMAT_VALUE = "json";
    public static final String FLICKR_NO_JSON_CALLBACK_VALUE = "1";
    public static final String FLICKR_PHOTOSETS_LIST_VALUE = "flickr.photosets.getList";
    public static final String FLICKR_PHOTOSETS_PHOTOS_VALUE = "flickr.photosets.getPhotos";
    public static final String FLICKR_PHOTOSETS_EXTRAS_VALUE = "url_sq,url_s,url_m,url_o";

    public static final int HTTP_RESPONSE_CODE_LENGTH = 3;
    public static final String PHOTOSETS_ID = "photosets";
    public static final String PHOTOSET_ID = "photoset";
    public static final String ID_ID = "id";
    public static final String PHOTOS_ID = "photos";
    public static final String VIDEOS_ID = "videos";
    public static final String TITLE_ID = "title";
    public static final String CONTENT_ID = "_content";
    public static final String DESCRIPTION_ID = "description";
    public static final String PHOTO_ID = "photo";
    public static final String URL_SQUARE_ID = "url_sq";
    public static final String URL_SMALL_ID = "url_s";
    public static final String URL_MEDIUM_ID = "url_m";
    public static final String HEIGHT_SQUARE_ID = "height_sq";
    public static final String WIDTH_SQUARE_ID = "width_sq";
    public static final String HEIGHT_SMALL_ID = "height_s";
    public static final String WIDTH_SMALL_ID = "width_s";
    public static final String HEIGHT_MEDIUM_ID = "height_m";
    public static final String WIDTH_MEDIUM_ID = "width_m";
    public static final String PHOTO_SET_ID = "photo_set_id";
    public static final String IS_PRIMARY_ID = "isprimary";
    public static final String ERROR_IDENTIFIER = "error";
    public static final String ERROR_DESCRIPTION = "description";
    public static final String ERROR_CODE = "code";
    public static final String NO_PHOTOSETS_DESCRIPTION = "There are no photo sets.";
    public static final String NO_PHOTOS_DESCRIPTION = "There are no photos associated with this photo set.";
    public static final int ERROR_CODE_INVALID_API_REQUEST = 1;
    public static final int NO_PHOTOSETS = 2;
    public static final int API_ERROR = 3;
    public static final int NO_PHOTOS_IN_PHOTOSET = 4;

    public static final String ETSY_API_KEY = "l5k8bfu3uyvjy80n0o547zlq";
    public static final String ETSY_API_SECRET = "lxep7iqhx9";
    public static final String ETSY_STORE_ID = "5547124";
    public static final String ETSY_CALLBACK = "oob";
    private static final String[] ETSY_SCOPE_ARRAY = {
            PermissionScope.READ_LISTINGS, PermissionScope.READ_FEEDBACK
    };
    public static final PermissionScope ETSY_SCOPE = new PermissionScope(ETSY_SCOPE_ARRAY);

}
