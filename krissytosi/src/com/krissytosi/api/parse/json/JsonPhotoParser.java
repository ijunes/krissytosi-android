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

package com.krissytosi.api.parse.json;

import android.util.Log;

import com.krissytosi.api.domain.Photo;
import com.krissytosi.api.domain.PhotoSet;
import com.krissytosi.api.parse.PhotoParser;
import com.krissytosi.utils.ApiConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON-specific implementation of a {@link PhotoParser}
 */
public class JsonPhotoParser implements PhotoParser {

    private static final String LOG_TAG = "JsonPhotoSetParser";

    @Override
    public List<PhotoSet> parsePhotoSets(String response) {
        List<PhotoSet> photoSets = new ArrayList<PhotoSet>();
        // make sure we got at least *something* back from the API server
        if (response != null && !"".equalsIgnoreCase(response)) {
            // make sure it wasn't just a HTTP status code
            if (response.length() != ApiConstants.HTTP_RESPONSE_CODE_LENGTH) {
                try {
                    if (response.charAt(0) == '{') {
                        // parse the response into an object and see if we can
                        // find the photo sets.
                        JSONObject rootJson = new JSONObject(response);
                        JSONObject photoSetsJson = rootJson
                                .optJSONObject(ApiConstants.PHOTOSETS_ID);
                        if (photoSetsJson != null) {
                            JSONArray photoSetJson = photoSetsJson
                                    .optJSONArray(ApiConstants.PHOTOSET_ID);
                            if (photoSetJson != null) {
                                for (int i = 0, l = photoSetJson.length(); i < l; i++) {
                                    // digest one photo set
                                    JSONObject json = photoSetJson.optJSONObject(i);
                                    if (json != null) {
                                        PhotoSet photoSet = digestPhotoSet(json);
                                        photoSets.add(photoSet);
                                        // check to see if its an error
                                        if (photoSet.getErrorCode() != -1
                                                || photoSet.getErrorDescription() != null) {
                                            break;
                                        }
                                    } else {
                                        // make sure that the user is aware of
                                        // this error.
                                        Log.d(LOG_TAG,
                                                "Api server returned a response with photosets, but they were not deemed valid. Api server returned "
                                                        + response);
                                        photoSets
                                                .add(createErrorPhotoSet(ApiConstants.NO_PHOTOSETS));
                                    }
                                }
                            } else {
                                // if there's no photoset element, we should
                                // build an error.
                                Log.d(LOG_TAG,
                                        "Api server did not response with a photoset element in the json object response. Instead it returned "
                                                + response);
                                photoSets.add(createErrorPhotoSet(ApiConstants.NO_PHOTOSETS));
                            }
                        } else {
                            // if there's no photo sets in the response, we
                            // should build an error
                            Log.d(LOG_TAG,
                                    "Api server did not response with a photosets element in the json object response. Instead it returned "
                                            + response);
                            photoSets.add(createErrorPhotoSet(ApiConstants.NO_PHOTOSETS));
                        }
                    } else {
                        // if we can't parse the initial json object, it should
                        // be construed as an error.
                        Log.d(LOG_TAG,
                                "Api server did not response with a viable json object response. Instead it returned "
                                        + response);
                        photoSets.add(createErrorPhotoSet(ApiConstants.API_ERROR));
                    }
                } catch (JSONException e) {
                    photoSets.add(createErrorPhotoSet(ApiConstants.API_ERROR));
                    Log.e(LOG_TAG, "parsePhotoSets", e);
                }
            } else {
                photoSets.add(createErrorPhotoSet(ApiConstants.API_ERROR));
                Log.d(LOG_TAG,
                        "Failed to retrieve photo sets - got a HTTP response code back from the API server instead "
                                + response);
            }
        } else {
            photoSets.add(createErrorPhotoSet(ApiConstants.API_ERROR));
            Log.d(LOG_TAG,
                    "Failed to retrieve photo sets - got nothing back from the API server");
        }
        return photoSets;
    }

    @Override
    public List<Photo> parsePhotos(String response) {
        List<Photo> photos = new ArrayList<Photo>();
        // make sure we got at least *something* back from the API server
        if (response != null && !"".equalsIgnoreCase(response)) {
            // make sure it wasn't just a HTTP status code
            if (response.length() != ApiConstants.HTTP_RESPONSE_CODE_LENGTH) {
                try {
                    if (response.charAt(0) == '{') {
                        // parse the response into an object and see if we can
                        // find the photo sets.
                        JSONObject rootJson = new JSONObject(response);
                        JSONObject photoSetJson = rootJson.optJSONObject(ApiConstants.PHOTOSET_ID);
                        if (photoSetJson != null) {
                            JSONArray photoSetArray = photoSetJson
                                    .optJSONArray(ApiConstants.PHOTO_ID);
                            if (photoSetArray != null) {
                                for (int i = 0, l = photoSetArray.length(); i < l; i++) {
                                    // digest one photo set
                                    JSONObject json = photoSetArray.optJSONObject(i);
                                    if (json != null) {
                                        photos.add(digestPhoto(json));
                                    } else {
                                        // TODO
                                    }
                                }
                            } else {
                                // TODO
                            }
                        } else {
                            // TODO
                        }
                    } else {
                        // if we can't parse the initial json object, it should
                        // be construed as an error.
                        Log.d(LOG_TAG,
                                "Api server did not response with a viable json object response. Instead it returned "
                                        + response);
                        photos.add(createErrorPhotoResponse(ApiConstants.NO_PHOTOS_IN_PHOTOSET));
                    }
                } catch (JSONException e) {
                    photos.add(createErrorPhotoResponse(ApiConstants.NO_PHOTOS_IN_PHOTOSET));
                    Log.e(LOG_TAG, "parsePhotos", e);
                }
            } else {
                photos.add(createErrorPhotoResponse(ApiConstants.API_ERROR));
                Log.d(LOG_TAG,
                        "Failed to retrieve photos - got a HTTP response code back from the API server instead "
                                + response);
            }
        } else {
            photos.add(createErrorPhotoResponse(ApiConstants.API_ERROR));
            Log.d(LOG_TAG,
                    "Failed to retrieve photos - got nothing back from the API server");
        }
        return photos;
    }

    /**
     * Parses a photo set from the API response.
     * 
     * @param photoSetJson a photo set object in json format.
     * @return a {@link PhotoSet} object which corresponds to the photoSetJson
     *         parameter.
     * @throws JSONException if parsing of the photo set blows up.
     */
    private PhotoSet digestPhotoSet(JSONObject photoSetJson) throws JSONException {
        PhotoSet photoSet = new PhotoSet();
        if (!photoSetJson.has(ApiConstants.ERROR_IDENTIFIER)) {
            // id, photos & videos hang off the main photoSetJson object.
            photoSet.setId(photoSetJson.getString(ApiConstants.ID_ID));
            // getting the title is a little tricker
            String title = digestDeeplyNestedString(photoSetJson, ApiConstants.TITLE_ID);
            photoSet.setTitle(title);
            // same goes for the description
            String description = digestDeeplyNestedString(photoSetJson, ApiConstants.DESCRIPTION_ID);
            photoSet.setDescription(description);
        } else {
            photoSet = digestErrorResponse(photoSetJson
                    .getJSONObject(ApiConstants.ERROR_IDENTIFIER));
        }
        return photoSet;
    }

    /**
     * Creates a {@link Photo} object from a json representation.
     * 
     * @param photoJson the json representation of a photo.
     * @return a {@link Photo} object.
     * @throws JSONException is thrown if all goes wrong when parsing out the
     *             {@link Photo} object.
     */
    private Photo digestPhoto(JSONObject photoJson) throws JSONException {
        Photo photo = new Photo();
        photo.setUrlSquare(photoJson.getString(ApiConstants.URL_SQUARE_ID));
        photo.setUrlSmall(photoJson.getString(ApiConstants.URL_SMALL_ID));
        photo.setUrlMedium(photoJson.getString(ApiConstants.URL_MEDIUM_ID));
        photo.setHeightSquare(photoJson.getInt(ApiConstants.HEIGHT_SQUARE_ID));
        photo.setWidthSquare(photoJson.getInt(ApiConstants.WIDTH_SQUARE_ID));
        photo.setHeightSmall(photoJson.getInt(ApiConstants.HEIGHT_SMALL_ID));
        photo.setWidthSmall(photoJson.getInt(ApiConstants.WIDTH_SMALL_ID));
        photo.setHeightMedium(photoJson.getInt(ApiConstants.HEIGHT_MEDIUM_ID));
        photo.setWidthMedium(photoJson.getInt(ApiConstants.WIDTH_MEDIUM_ID));
        photo.setIsPrimary(photoJson.getInt(ApiConstants.IS_PRIMARY_ID));
        return photo;
    }

    private String digestDeeplyNestedString(JSONObject photoSetJson, String key)
            throws JSONException {
        String result = "";
        if (photoSetJson.has(key)) {
            JSONObject descriptionJson = photoSetJson.getJSONObject(key);
            if (descriptionJson.has(ApiConstants.CONTENT_ID)) {
                result = descriptionJson.getString(ApiConstants.CONTENT_ID);
            }
        }
        return result;
    }

    /**
     * Checks the JSON returned from the API server for errors.
     * 
     * @param errorJson the JSON returned from the API server.
     * @return a {@link PhotoSet} object which has details on what caused the
     *         error.
     * @throws JSONException
     */
    private PhotoSet digestErrorResponse(JSONObject errorJson) throws JSONException {
        PhotoSet errorPhotoSet = new PhotoSet();
        errorPhotoSet.setErrorCode(errorJson.getInt(ApiConstants.ERROR_CODE));
        errorPhotoSet.setErrorDescription(errorJson.getString(ApiConstants.ERROR_DESCRIPTION));
        return errorPhotoSet;
    }

    /**
     * Creates a dummy photo set error object due to parsing errors or the fact
     * that the HTTP request was dropped or interrupted.
     * 
     * @param errorCode the error code to associate with the photo set.
     * @return a {@link PhotoSet} object describing the error.
     */
    private PhotoSet createErrorPhotoSet(int errorCode) {
        PhotoSet errorPhotoSet = new PhotoSet();
        errorPhotoSet.setErrorCode(errorCode);
        errorPhotoSet.setErrorDescription(ApiConstants.NO_PHOTOSETS_DESCRIPTION);
        return errorPhotoSet;
    }

    private Photo createErrorPhotoResponse(int errorCode) {
        Photo photo = new Photo();
        photo.setErrorCode(errorCode);
        photo.setErrorDescription(ApiConstants.NO_PHOTOS_DESCRIPTION);
        return photo;
    }
}
