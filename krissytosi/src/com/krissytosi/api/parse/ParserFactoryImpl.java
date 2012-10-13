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

package com.krissytosi.api.parse;

import com.krissytosi.api.parse.json.JsonParserFactory;

/**
 * Implementation instance of a parser factory.
 */
public final class ParserFactoryImpl {

    /**
     * Singleton instance variable
     */
    private static ParserFactory instance = new JsonParserFactory();

    /**
     * Private singleton constructor
     */
    private ParserFactoryImpl() {

    }

    /**
     * Singleton accessor.
     * 
     * @return the singleton instance for {@link ParserFactoryImpl}
     */
    public static ParserFactory getInstance() {
        return instance;
    }
}
