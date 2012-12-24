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

package com.krissytosi.api.store;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.etsy.etsyModels.Listing;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ParcelableListing implements Parcelable {

    private final static String LOG_TAG = "ParcelableListing";

    /**
     * The {@link Listing} object which backs this object.
     */
    private Listing listing;

    public static final Parcelable.Creator<ParcelableListing> CREATOR = new Parcelable.Creator<ParcelableListing>() {
        @Override
        public ParcelableListing createFromParcel(Parcel in) {
            return new ParcelableListing(in);
        }

        @Override
        public ParcelableListing[] newArray(int size) {
            return new ParcelableListing[size];
        }
    };

    public ParcelableListing() {

    }

    public ParcelableListing(Parcel in) {
        this();
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(listing.getUrl());
        dest.writeString(listing.getTitle());
        dest.writeString(listing.getDescription());
        dest.writeString(listing.getCurrencyCode());
        dest.writeString(listing.getPrice());
        dest.writeInt(listing.getQuantity());
        dest.writeString(listing.getWhenMade());
    }

    final private void readFromParcel(Parcel in) {
        // create a new listing & build up a map of properties
        listing = new Listing();
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("url", in.readString());
        properties.put("title", in.readString());
        properties.put("description", in.readString());
        properties.put("currency_code", in.readString());
        properties.put("price", in.readString());
        properties.put("quantity", String.valueOf(in.readInt()));
        properties.put("when_made", in.readString());
        try {
            listing.parseData(new JSONObject(properties));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "readFromParcel", e);
        }
    }

    // Getters/Setters

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }
}
