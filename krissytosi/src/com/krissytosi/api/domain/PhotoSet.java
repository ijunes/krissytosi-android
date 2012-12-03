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

package com.krissytosi.api.domain;

import java.util.List;

/**
 * Defines what makes up a photo set.
 */
public class PhotoSet extends ApiResponse {

    /**
     * Unique identifier for the photo set.
     */
    private String id;

    /**
     * The photo set's title.
     */
    private String title;

    /**
     * The photo set's description.
     */
    private String description;

    /**
     * List of photos associated with the photo set.
     */
    private List<Photo> photos;

    /**
     * Index in the photos member variable of the photo which represents the
     * primary photo in this set.
     */
    private int indexOfPrimaryPhoto;

    // Getters/Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public int getIndexOfPrimaryPhoto() {
        return indexOfPrimaryPhoto;
    }

    public void setIndexOfPrimaryPhoto(int indexOfPrimaryPhoto) {
        this.indexOfPrimaryPhoto = indexOfPrimaryPhoto;
    }
}
