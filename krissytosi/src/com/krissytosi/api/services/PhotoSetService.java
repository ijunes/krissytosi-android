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

package com.krissytosi.api.services;

import com.krissytosi.api.domain.Photo;
import com.krissytosi.api.domain.PhotoSet;

import java.util.List;

/**
 * Defines methods which should be implemented by any object proposing to be a
 * photo set service.
 */
public interface PhotoSetService {

    /**
     * Sets the base url which this photo set service should target.
     * 
     * @param baseUrl the base url.
     */
    void setBaseUrl(String baseUrl);

    /**
     * Retrieves a list of {@link PhotoSet} objects.
     * 
     * @return a list of {@link PhotoSet} objects
     */
    List<PhotoSet> getPhotoSets();

    /**
     * Retrieves a list of {@link Photo}s for a given photo set.
     * 
     * @param photoSetId the id which identifies the photo set.
     * @return a list of {@link Photo} objects.
     */
    List<Photo> getPhotos(String photoSetId);
}
