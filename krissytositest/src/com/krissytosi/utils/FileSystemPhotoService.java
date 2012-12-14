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

import com.krissytosi.api.domain.Photo;
import com.krissytosi.api.domain.PhotoSet;
import com.krissytosi.api.services.PhotoService;

import java.util.ArrayList;
import java.util.List;

public class FileSystemPhotoService implements PhotoService {

    private String baseUrl;

    @Override
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public List<PhotoSet> getPhotoSets() {
        List<PhotoSet> photoSets = new ArrayList<PhotoSet>();
        PhotoSet photoSet = new PhotoSet();
        photoSet.setId("123");
        photoSet.setTitle("Title");
        photoSets.add(photoSet);
        return photoSets;
    }

    @Override
    public List<Photo> getPhotos(String photoSetId) {
        List<Photo> photos = new ArrayList<Photo>();
        Photo photo = new Photo();
        photo.setUrlMedium("http://www.google.com");
        photo.setPhotoSetId("123");
        photo.setIsPrimary(1);
        photos.add(photo);
        return photos;
    }

}
