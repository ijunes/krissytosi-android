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

package com.krissytosi.api.modules;

import com.krissytosi.api.parse.PortfolioParser;
import com.krissytosi.api.parse.json.JsonPortfolioParser;
import com.krissytosi.api.services.PortfolioService;
import com.krissytosi.api.services.http.PortfolioServiceImpl;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(entryPoints = {
        PortfolioService.class, PortfolioParser.class
})
public class ApiModule {

    @Provides
    @Singleton
    PortfolioService providePortfolioService() {
        return new PortfolioServiceImpl();
    }

    @Provides
    @Singleton
    PortfolioParser providePortfolioParser() {
        return new JsonPortfolioParser();
    }

}
