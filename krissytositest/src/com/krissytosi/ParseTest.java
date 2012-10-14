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

import com.krissytosi.api.domain.Portfolio;
import com.krissytosi.api.parse.ParserFactoryImpl;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.List;

public class ParseTest extends AndroidTestCase {

    private static final String FILE_NAME = "portfolios.json";
    private static final String ERROR_FILE_NAME = "portfolios_error.json";

    public void testSuccessfulParserImplementation() {
        String fileContents = readFile("/assets/responses/json/" + FILE_NAME);
        List<Portfolio> portfolios = ParserFactoryImpl.getInstance().getPortfolioParser()
                .parsePortfolios(fileContents);
        assertTrue(portfolios.size() == 3);
        Portfolio firstPortfolio = portfolios.get(0);
        assertTrue(firstPortfolio.getName().equalsIgnoreCase("portfolioOne"));
        assertTrue(firstPortfolio.getErrorCode() == -1);
        assertTrue(firstPortfolio.getErrorDescription() == null);
        assertTrue(firstPortfolio.getNumberOfImages() == 1);
        assertTrue(firstPortfolio.getOrderIndex() == 0);
        assertTrue(firstPortfolio.getStartIndex() == 0);
    }

    public void testErrorParserImplementation() {
        String fileContents = readFile("/assets/responses/json/" + ERROR_FILE_NAME);
        List<Portfolio> portfolios = ParserFactoryImpl.getInstance().getPortfolioParser()
                .parsePortfolios(fileContents);
        assertTrue(portfolios.size() == 1);
        Portfolio firstPortfolio = portfolios.get(0);
        assertTrue(firstPortfolio.getErrorCode() == 1);
        assertTrue(firstPortfolio.getErrorDescription().equalsIgnoreCase("There was an error"));
    }

    public void testNullParserImplementation() {
        List<Portfolio> portfolios = ParserFactoryImpl.getInstance().getPortfolioParser()
                .parsePortfolios(null);
        assertTrue(portfolios.size() == 1);
        Portfolio firstPortfolio = portfolios.get(0);
        assertTrue(firstPortfolio.getErrorCode() == 3);
        assertTrue(firstPortfolio.getErrorDescription().equalsIgnoreCase("There are no portfolios"));
    }

    private String readFile(String filePath) {
        byte[] bytes = null;
        try {
            bytes = IOUtils.toByteArray(getClass().getResourceAsStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }
}
