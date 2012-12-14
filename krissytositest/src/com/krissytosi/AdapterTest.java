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

package com.krissytosi;

import android.test.AndroidTestCase;
import android.view.View;

import com.etsy.etsyModels.Listing;
import com.krissytosi.api.domain.Photo;
import com.krissytosi.api.domain.PhotoSet;
import com.krissytosi.fragments.adapters.PhotoSetsAdapter;
import com.krissytosi.fragments.adapters.StoreAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterTest extends AndroidTestCase {

    public void testStoreAdapter() throws Exception {
        List<Listing> listings = new ArrayList<Listing>();
        Listing listing = new Listing();
        listings.add(listing);
        StoreAdapter storeAdapter = new StoreAdapter(getContext(), R.layout.store_listing,
                (ArrayList<Listing>) listings);
        storeAdapter.notifyDataSetChanged();
        assertTrue(storeAdapter.getCount() == 1);
        View v = storeAdapter.getView(0, null, null);
        assertNotNull(v);
    }

    public void testPhotoSetsAdapter() throws Exception {
        ArrayList<PhotoSet> photoSets = new ArrayList<PhotoSet>();
        PhotoSet photoSet = new PhotoSet();
        List<Photo> photos = new ArrayList<Photo>();
        Photo photo = new Photo();
        photo.setIsPrimary(1);
        photo.setPhotoSetId("1");
        photo.setUrlMedium("http://www.google.com");
        photos.add(photo);
        photoSet.setPhotos(photos);
        photoSet.setId("1");
        photoSet.setTitle("Title");
        photoSet.setIndexOfPrimaryPhoto(0);
        photoSets.add(photoSet);
        PhotoSetsAdapter photoSetsAdapter = new PhotoSetsAdapter(getContext(),
                R.layout.photoset_detail_view,
                photoSets);
        photoSetsAdapter.notifyDataSetChanged();
        assertTrue(photoSetsAdapter.getCount() == 1);
        View v = photoSetsAdapter.getView(0, null, null);
        assertNotNull(v);
    }
}
