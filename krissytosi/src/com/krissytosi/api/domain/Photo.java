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

/**
 * Represents a photo which is typically part of an {@link PhotoSet}.
 */
public class Photo extends ApiResponse {

    private String urlSquare;
    private String urlSmall;
    private String urlMedium;
    private String urlOriginal;
    private int heightSquare;
    private int widthSquare;
    private int heightSmall;
    private int widthSmall;
    private int heightMedium;
    private int widthMedium;

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
}
