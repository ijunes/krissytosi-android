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

import com.krissytosi.api.domain.Photo;
import com.krissytosi.api.domain.PhotoSet;
import com.krissytosi.api.modules.ApiModule;
import com.krissytosi.api.parse.PhotoSetParser;

import dagger.ObjectGraph;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ParseTest extends AndroidTestCase {

    private static final String PHOTOSETS_FILE_NAME = "photosets.json";
    private static final String PHOTOS_FILE_NAME = "photos.json";
    private static final String MALFORMED_FILE_NAME = "malformed.json";

    private PhotoSetParser parser;

    public void testSuccessfulPhotoSetParserImplementation() {
        String fileContents = readFile("/assets/responses/json/" + PHOTOSETS_FILE_NAME);
        List<PhotoSet> photoSets = getPortfolioParser().parsePhotoSets(fileContents);
        assertTrue(photoSets.size() == 5);
        PhotoSet firstPortfolio = photoSets.get(0);
        assertTrue(firstPortfolio.getErrorCode() == -1);
        assertTrue(firstPortfolio.getErrorDescription() == null);
    }

    public void testNullPhotoSetParserImplementation() {
        List<PhotoSet> portfolios = getPortfolioParser().parsePhotoSets(null);
        assertTrue(portfolios.size() == 1);
        PhotoSet firstPortfolio = portfolios.get(0);
        assertTrue(firstPortfolio.getErrorCode() == 3);
        assertTrue(firstPortfolio.getErrorDescription()
                .equalsIgnoreCase("There are no photo sets."));
    }

    public void testSuccessfulPhotoParserImplementation() {
        String fileContents = readFile("/assets/responses/json/" + PHOTOS_FILE_NAME);
        List<Photo> photos = getPortfolioParser().parsePhotos(fileContents);
        assertTrue(photos.size() == 6);
        Photo photo = photos.get(0);
        assertTrue(photo.getHeightMedium() == 500);
        assertTrue(photo.getWidthMedium() == 378);
    }

    public void testNullPhotosParserImplementation() {
        List<Photo> photos = getPortfolioParser().parsePhotos(null);
        assertTrue(photos.size() == 1);
        Photo firstPhoto = photos.get(0);
        assertTrue(firstPhoto.getErrorCode() == 3);
        assertTrue(firstPhoto.getErrorDescription()
                .equalsIgnoreCase("There are no photos associated with this photo set."));
    }

    public void testMalformedParserImplementation() {
        String fileContents = readFile("/assets/responses/json/" + MALFORMED_FILE_NAME);
        List<Photo> photos = getPortfolioParser().parsePhotos(fileContents);
        assertTrue(photos.size() == 1);
        Photo firstPhoto = photos.get(0);
        assertTrue(firstPhoto.getErrorCode() == 4);
        List<PhotoSet> portfolios = getPortfolioParser().parsePhotoSets(fileContents);
        assertTrue(portfolios.size() == 1);
        PhotoSet firstPortfolio = portfolios.get(0);
        assertTrue(firstPortfolio.getErrorCode() == 3);
    }

    public void testHttpErrorParser() {
        String fileContents = "500";
        List<Photo> photos = getPortfolioParser().parsePhotos(fileContents);
        assertTrue(photos.size() == 1);
        Photo firstPhoto = photos.get(0);
        assertTrue(firstPhoto.getErrorCode() == 3);
        List<PhotoSet> portfolios = getPortfolioParser().parsePhotoSets(fileContents);
        assertTrue(portfolios.size() == 1);
        PhotoSet firstPortfolio = portfolios.get(0);
        assertTrue(firstPortfolio.getErrorCode() == 3);
    }

    private String readFile(String filePath) {
        byte[] bytes = null;
        InputStream stream = null;
        try {
            stream = getClass().getResourceAsStream(filePath);
            bytes = IOUtils.toByteArray(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new String(bytes);
    }

    private PhotoSetParser getPortfolioParser() {
        if (parser == null) {
            ObjectGraph objectGraph = ObjectGraph.create(new ApiModule());
            parser = objectGraph.get(PhotoSetParser.class);
        }
        return parser;
    }
}
