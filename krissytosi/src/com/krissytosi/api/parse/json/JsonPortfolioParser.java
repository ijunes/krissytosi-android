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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.krissytosi.api.domain.Portfolio;
import com.krissytosi.api.parse.PortfolioParser;

public class JsonPortfolioParser implements PortfolioParser {

	private static final String LOG_TAG = "JsonPortfolioParser";

	@Override
	public List<Portfolio> parsePortfolios(String response) {
		List<Portfolio> portfolios = new ArrayList<Portfolio>();
		// make sure we got at least *something* back from the API server
		if (response != null && !"".equalsIgnoreCase(response)) {
			// make sure it wasn't just a HTTP status code
			if (response.length() != 3) {
				try {
					JSONObject rootJson = new JSONObject(response);
					// TODO - iterate through the response.
				} catch (JSONException e) {
					// TODO - should never just leave an exception block empty
				}
			} else {
				Log.d(LOG_TAG,
						"Failed to retrieve portfolios - got a HTTP response code back from the API server instead "
								+ response);
			}
		} else {
			Log.d(LOG_TAG,
					"Failed to retrieve portfolios - got nothing back from the API server");
		}
		return portfolios;
	}
}
