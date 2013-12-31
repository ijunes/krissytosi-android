/*
   Copyright 2012 - 2014 Sean O' Shea

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

/**
 * Represents a photo which is typically part of an {@link PhotoSet}.
 */
public class Photo extends ApiResponse implements Parcelable {

    /**
     * Associates the photo with a unique photo set.
     */
    private String photoSetId;

    /**
     * Url for the square resource.
     */
    private String urlSquare;

    /**
     * Url for the small resource.
     */
    private String urlSmall;

    /**
     * Url for the medium resource.
     */
    private String urlMedium;

    /**
     * Url for the original resource.
     */
    private String urlOriginal;

    /**
     * Height for the square resource.
     */
    private int heightSquare;

    /**
     * Width for the square resource.
     */
    private int widthSquare;

    /**
     * Height for the small resource.
     */
    private int heightSmall;

    /**
     * Width for the small resource.
     */
    private int widthSmall;

    /**
     * Height for the medium resource.
     */
    private int heightMedium;

    /**
     * Width for the medium resource.
     */
    private int widthMedium;

    /**
     * Indicates whether or not the photo is the primary photo for a photo set.
     */
    private int isPrimary;

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public Photo() {

    }

    public Photo(Parcel in) {
        this();
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photoSetId);
        dest.writeString(urlSquare);
        dest.writeString(urlSmall);
        dest.writeString(urlMedium);
        dest.writeString(urlOriginal);
        dest.writeInt(heightSquare);
        dest.writeInt(widthSquare);
        dest.writeInt(heightSmall);
        dest.writeInt(widthSmall);
        dest.writeInt(heightMedium);
        dest.writeInt(widthMedium);
        dest.writeInt(isPrimary);
    }

    final private void readFromParcel(Parcel in) {
        photoSetId = in.readString();
        urlSquare = in.readString();
        urlSmall = in.readString();
        urlMedium = in.readString();
        urlOriginal = in.readString();
        heightSquare = in.readInt();
        widthSquare = in.readInt();
        heightSmall = in.readInt();
        widthSmall = in.readInt();
        heightMedium = in.readInt();
        widthMedium = in.readInt();
        isPrimary = in.readInt();
    }

    // Getters/Setters

    public String getUrlSquare() {
        return urlSquare;
    }

    public void setUrlSquare(String urlSquare) {
        this.urlSquare = urlSquare;
    }

    public String getUrlSmall() {
        return urlSmall;
    }

    public void setUrlSmall(String urlSmall) {
        this.urlSmall = urlSmall;
    }

    public String getUrlMedium() {
        return urlMedium;
    }

    public void setUrlMedium(String urlMedium) {
        this.urlMedium = urlMedium;
    }

    public String getUrlOriginal() {
        return urlOriginal;
    }

    public void setUrlOriginal(String urlOriginal) {
        this.urlOriginal = urlOriginal;
    }

    public int getHeightSquare() {
        return heightSquare;
    }

    public void setHeightSquare(int heightSquare) {
        this.heightSquare = heightSquare;
    }

    public int getWidthSquare() {
        return widthSquare;
    }

    public void setWidthSquare(int widthSquare) {
        this.widthSquare = widthSquare;
    }

    public int getHeightSmall() {
        return heightSmall;
    }

    public void setHeightSmall(int heightSmall) {
        this.heightSmall = heightSmall;
    }

    public int getWidthSmall() {
        return widthSmall;
    }

    public void setWidthSmall(int widthSmall) {
        this.widthSmall = widthSmall;
    }

    public int getHeightMedium() {
        return heightMedium;
    }

    public void setHeightMedium(int heightMedium) {
        this.heightMedium = heightMedium;
    }

    public int getWidthMedium() {
        return widthMedium;
    }

    public void setWidthMedium(int widthMedium) {
        this.widthMedium = widthMedium;
    }

    public String getPhotoSetId() {
        return photoSetId;
    }

    public void setPhotoSetId(String photoSetId) {
        this.photoSetId = photoSetId;
    }

    public int getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(int isPrimary) {
        this.isPrimary = isPrimary;
    }
}
