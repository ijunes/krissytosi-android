/*
   Copyright 2012 - 2013 Sean O' Shea

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

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Details everything about what constitutes a photo set.
 */
public class PhotoSet extends ApiResponse implements Parcelable {

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

    public static final Parcelable.Creator<PhotoSet> CREATOR = new Parcelable.Creator<PhotoSet>() {
        @Override
        public PhotoSet createFromParcel(Parcel in) {
            return new PhotoSet(in);
        }

        @Override
        public PhotoSet[] newArray(int size) {
            return new PhotoSet[size];
        }
    };

    public PhotoSet() {

    }

    public PhotoSet(Parcel in) {
        this();
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeTypedList(photos);
        dest.writeInt(indexOfPrimaryPhoto);
    }

    final private void readFromParcel(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        in.readTypedList(photos, Photo.CREATOR);
        indexOfPrimaryPhoto = in.readInt();
    }

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
