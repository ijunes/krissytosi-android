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

package com.krissytosi.api.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.krissytosi.api.domain.Portfolio;
import com.krissytosi.api.parse.ParserFactoryImpl;
import com.krissytosi.api.services.http.HttpService;

public class PortfolioServiceImpl extends HttpService implements
		PortfolioService {

	private String baseUrl;

	@Override
	public List<Portfolio> getPortfolios() {
		Map<String, String> options = new HashMap<String, String>();
		options.put("q", "portfolios");
		String portfolioUrl = createUrl(baseUrl, options);
		String response = doGet(portfolioUrl);
		return ParserFactoryImpl.getInstance().getPortfolioParser()
				.parsePortfolios(response);
	}

	// Getters/Setters

	public String getBaseUrl() {
		return baseUrl;
	}

	@Override
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}