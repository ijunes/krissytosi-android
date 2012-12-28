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

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.etsy.etsyModels.ListingImage;
import com.krissytosi.api.domain.Photo;

/**
 * Just some utilities used throughout the application.
 */
public class KrissyTosiUtils {

    public enum ImageSize {
        SMALL, MEDIUM, LARGE, SQUARE
    }

    /**
     * @return boolean indicating that the current OS is >= honeycomb (3.0).
     */
    public static boolean atLeastHoneyComb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * Checks to see whether the application has a viable internet connection.
     * Does NOT check to see whether any possible Wi-Fi connection is blocked by
     * a splash screen.
     * 
     * @param context
     * @return boolean indicating that there is at least some internet
     *         connection (either 3g, 4g or Wi-Fi).
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInformation = connectivityManager.getActiveNetworkInfo();
        return networkInformation != null && networkInformation.isConnectedOrConnecting();
    }

    /**
     * Layer of indirection
     * (http://en.wikipedia.org/wiki/David_Wheeler_(computer_scientist)) which
     * helps when figuring out the most appropriate image to load.
     * 
     * @param listingImage the image returned from the store API.
     * @param imageSize
     * @return String where the URL can be accessed.
     */
    public static String determineImageUrl(ListingImage listingImage, ImageSize imageSize) {
        String imageUrl = "";
        // TODO - take device density into account when determining the most
        // appropriate image to load.
        if (imageSize == ImageSize.MEDIUM) {
            imageUrl = listingImage.getUrl170x135();
        } else {
            imageUrl = listingImage.getUrlFullxfull();
        }
        Log.d("KTU", "imageURL " + imageUrl);
        return imageUrl;
    }

    /**
     * Gets the most appropriate url from a {@link Photo} object.
     * 
     * @param photo the photo object which contains the image url.
     * @param imageSize the image size requested by the client.
     * @return a usable url which points to a resource on a server.
     */
    public static String determineImageUrl(Photo photo, ImageSize imageSize) {
        String imageUrl = "";
        if (imageSize == ImageSize.MEDIUM) {
            imageUrl = photo.getUrlSmall();
        } else if (imageSize == ImageSize.SQUARE) {
            imageUrl = photo.getUrlSquare();
        } else {
            imageUrl = photo.getUrlMedium();
        }
        return imageUrl;
    }

    /**
     * Gets the max allowable height for a photo given the original width and
     * height.
     * 
     * @param height the original height of the photo.
     * @param width the original width of the photo.
     * @param activity used to determine whether or not the user is in portrait
     *            or landscape.
     * @return double indicating the maximum allowable height for the photo.
     */
    public static double getAllowedHeight(int height, int width, Activity activity) {
        double allowedHeight = height;
        boolean isPortrait = activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (isPortrait) {
            // check to see whether the requested image width will be
            // automatically squashed
            if (metrics.widthPixels < width) {
                // this will have an impact on the height of the image.
                double percentage = (double) metrics.widthPixels / width;
                allowedHeight = percentage * height;
            }
        } else {
            // TODO
        }
        return allowedHeight;
    }
}
