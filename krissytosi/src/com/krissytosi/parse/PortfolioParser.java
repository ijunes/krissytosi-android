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
package com.krissytosi.parse;

import java.util.List;

import com.krissytosi.domain.Portfolio;

public interface PortfolioParser {

	/**
	 * Parses a list of {@link Portfolio}s from a RAW API response.
	 * 
	 * @param bytes
	 *            the bytes retrieved from the API server.
	 * @return a list of {@link Portfolio} objects.
	 */
	public List<Portfolio> parsePortfolios(byte[] bytes);
}
