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

package com.krissytosi.parse.json;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.krissytosi.domain.Portfolio;
import com.krissytosi.parse.ParserFactory;
import com.krissytosi.parse.PortfolioParser;

public class JsonPortfolioParser implements PortfolioParser {

	@Override
	public List<Portfolio> parsePortfolios(byte[] bytes) {
		List<Portfolio> portfolios = new ArrayList<Portfolio>();
		if (bytes != null) {
			try {
				JSONObject rootJson = new JSONObject(parseString(bytes));
				// TODO - iterate through the response.
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return portfolios;
	}

	/**
	 * Parses a string from the bytes parameter.
	 * 
	 * @param bytes
	 *            the raw response from the API server.
	 * @return a string representation of the bytes.
	 */
	private String parseString(byte[] bytes) {
		String str = null;
		try {
			if (Charset.isSupported(ParserFactory.DEFAULT_ENCODING)) {
				str = new String(bytes, ParserFactory.DEFAULT_ENCODING);
			} else {
				str = new String(bytes, Charset.defaultCharset().name());
			}
		} catch (UnsupportedEncodingException e) {
			// fallback
			str = new String(bytes);
		}
		return str;
	}
}
