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

package com.krissytosi.api.parse.json;

import android.util.Log;

import com.krissytosi.api.domain.Portfolio;
import com.krissytosi.api.parse.PortfolioParser;
import com.krissytosi.utils.KrissyTosiConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON-specific implementation of a {@link PortfolioParser}
 */
public class JsonPortfolioParser implements PortfolioParser {

    private static final String LOG_TAG = "JsonPortfolioParser";

    @Override
    public List<Portfolio> parsePortfolios(String response) {
        List<Portfolio> portfolios = new ArrayList<Portfolio>();
        // make sure we got at least *something* back from the API server
        if (response != null && !"".equalsIgnoreCase(response)) {
            // make sure it wasn't just a HTTP status code
            if (response.length() != KrissyTosiConstants.HTTP_RESPONSE_CODE_LENGTH) {
                try {
                    // parse the response into an array and iterate
                    JSONArray rootJson = new JSONArray(response);
                    for (int i = 0, l = rootJson.length(); i < l; i++) {
                        // digest one portfolio
                        JSONObject portfolioJson = rootJson.getJSONObject(i);
                        Portfolio portfolio = digestPortfolio(portfolioJson);
                        portfolios.add(portfolio);
                        // check to see if its an error
                        if (portfolio.getErrorCode() != -1
                                || portfolio.getErrorDescription() != null) {
                            break;
                        }
                    }
                } catch (JSONException e) {
                    portfolios.add(createErrorPortfolio(KrissyTosiConstants.NO_PORTFOLIOS));
                    Log.e(LOG_TAG, "parsePortfolios", e);
                }
            } else {
                portfolios.add(createErrorPortfolio(KrissyTosiConstants.API_ERROR));
                Log.d(LOG_TAG,
                        "Failed to retrieve portfolios - got a HTTP response code back from the API server instead "
                                + response);
            }
        } else {
            portfolios.add(createErrorPortfolio(KrissyTosiConstants.API_ERROR));
            Log.d(LOG_TAG,
                    "Failed to retrieve portfolios - got nothing back from the API server");
        }
        return portfolios;
    }

    /**
     * Parses a portfolio from the API response.
     * 
     * @param portfolioJson a portfolio object in json format.
     * @return a {@link Portfolio} object which corresponds to the portfolioJson
     *         parameter.
     * @throws JSONException
     */
    private Portfolio digestPortfolio(JSONObject portfolioJson) throws JSONException {
        Portfolio portfolio = new Portfolio();
        if (!portfolioJson.has(KrissyTosiConstants.ERROR_IDENTIFIER)) {
            portfolio.setName(portfolioJson.getString(KrissyTosiConstants.NAME_ID));
            portfolio.setNumberOfImages(portfolioJson.getInt(KrissyTosiConstants.NUMBER_OF_IMAGES_ID));
            portfolio.setStartIndex(portfolioJson.getInt(KrissyTosiConstants.START_INDEX_ID));
            portfolio.setOrderIndex(portfolioJson.getInt(KrissyTosiConstants.ORDER_INDEX_ID));
        } else {
            portfolio = digestErrorResponse(portfolioJson.getJSONObject(KrissyTosiConstants.ERROR_IDENTIFIER));
        }
        return portfolio;
    }

    /**
     * Checks the JSON returned from the API server for errors.
     * 
     * @param errorJson the JSON returned from the API server.
     * @return a {@link Portfolio} object which has details on what caused the
     *         error.
     * @throws JSONException
     */
    private Portfolio digestErrorResponse(JSONObject errorJson) throws JSONException {
        Portfolio errorPortfolio = new Portfolio();
        errorPortfolio.setErrorCode(errorJson.getInt(KrissyTosiConstants.ERROR_CODE));
        errorPortfolio.setErrorDescription(errorJson.getString(KrissyTosiConstants.ERROR_DESCRIPTION));
        return errorPortfolio;
    }

    /**
     * Creates a dummy Portfolio error object due to parsing errors or the fact
     * that the HTTP request was dropped or interrupted.
     * 
     * @param errorCode the error code to associate with the portfolio.
     * @return a {@link Portfolio} object describing the error.
     */
    private Portfolio createErrorPortfolio(int errorCode) {
        Portfolio errorPortfolio = new Portfolio();
        errorPortfolio.setErrorCode(errorCode);
        errorPortfolio.setErrorDescription(KrissyTosiConstants.NO_PORTFOLIOS_DESCRIPTION);
        return errorPortfolio;
    }
}
