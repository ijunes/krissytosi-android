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

package com.krissytosi.api.modules;

import com.krissytosi.api.parse.PhotoParser;
import com.krissytosi.api.parse.json.JsonPhotoParser;
import com.krissytosi.api.services.PhotoService;
import com.krissytosi.api.services.http.PhotoServiceImpl;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Dagger injection module for the API classes. Includes entry points for the
 * {@link PhotoService} & {@link PhotoParser} implementations.
 */
@Module(entryPoints = {
        PhotoService.class, PhotoParser.class
})
public class ApiModule {

    @Provides
    @Singleton
    PhotoService providePhotoSetService() {
        return new PhotoServiceImpl();
    }

    @Provides
    @Singleton
    PhotoParser providePhotoSetParser() {
        return new JsonPhotoParser();
    }
}
