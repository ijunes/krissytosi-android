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

package com.krissytosi.api.parse;

import com.krissytosi.api.domain.Photo;
import com.krissytosi.api.domain.PhotoSet;

import java.util.List;

/**
 * Defines the API parsing methods associated with generating {@link PhotoSet}
 * objects.
 */
public interface PhotoParser {

    /**
     * Parses a list of {@link PhotoSet}s from a String representation of an API
     * response.
     * 
     * @param response an API response.
     * @return a list of {@link PhotoSet} objects.
     */
    List<PhotoSet> parsePhotoSets(String response);

    /**
     * Parses a list of {@link Photo}s from a String representation of an API
     * response.
     * 
     * @param response an API response.
     * @return a list of {@link Photo} objects.
     */
    List<Photo> parsePhotos(String response);
}
