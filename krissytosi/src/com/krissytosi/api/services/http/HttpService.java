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

package com.krissytosi.api.services.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpService {

	private final static int SOCKET_TIMEOUT = 30;
	private final static int CONNECTION_TIMEOUT = 30;

	private static final String httpProtocolKey = "http.protocol.version";
	private static final String httpUserAgentKey = "http.useragent";
	private static final String USER_AGENT_HEADER = "Java API Client";
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final String GZIP_ENCODING = "gzip";
	private static final String CONTENT_ENCODING_HEADER = "Content-Encoding";

	// TODO - this should be passed down to the HttpService
	private static final String API_URL = "http://localhost:8080";

	static {
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
		params.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 15000);
		params.setParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024);
		params.setParameter(ConnManagerParams.TIMEOUT, 60000);
		params.setParameter(ConnManagerParams.MAX_TOTAL_CONNECTIONS, 10);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		HttpService.connectionManager = new ThreadSafeClientConnManager(params,
				schemeRegistry);
	}

	private static ThreadSafeClientConnManager connectionManager = null;

	public String createUrl(Map<String, String> options) {
		StringBuffer url = new StringBuffer();
		final String apiUrl = API_URL;
		url.append(apiUrl);
		for (Map.Entry<String, String> entry : options.entrySet()) {
			String key = entry.getKey();
			final String value = entry.getValue();
			if (value != null) {
				String safeValue = null;
				String safeKey = null;
				try {
					safeValue = java.net.URLEncoder.encode(value,
							DEFAULT_ENCODING);
					safeKey = java.net.URLEncoder.encode(key, DEFAULT_ENCODING);
				} catch (UnsupportedEncodingException e) {
					// TODO - never drop exceptions without logging
				}
				url.append("&");
				url.append(safeKey);
				url.append("=");
				url.append(safeValue);
			}
		}
		return url.toString();
	}

	public String doGet(String url) {
		String result = "500";
		try {
			HttpClient httpClient = generateHttpClient();
			HttpGet httpget = new HttpGet(url);
			// TODO - log the URL?
			HttpResponse response = httpClient.execute(httpget);
			result = parseApiResponse(response);
			// TODO - log the response?
			return result;
		} catch (ClientProtocolException e) {
			// TODO - never drop exceptions without logging
		} catch (IOException e) {
			// TODO - never drop exceptions without logging
		} catch (IllegalArgumentException iae) {
			// TODO - never drop exceptions without logging
		}
		return result;
	}

	private HttpClient generateHttpClient() {
		HttpClient httpClient = new DefaultHttpClient(
				HttpService.connectionManager, null);
		httpClient.getParams().setParameter(HttpService.httpProtocolKey,
				HttpVersion.HTTP_1_1);
		httpClient.getParams()
				.setParameter(httpUserAgentKey, USER_AGENT_HEADER);
		HttpConnectionParams.setSocketBufferSize(httpClient.getParams(), 8192);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(),
				SOCKET_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
				CONNECTION_TIMEOUT);
		return httpClient;
	}

	private String parseApiResponse(HttpResponse response) {
		String result = "500";
		final int httpResponseCode = response.getStatusLine().getStatusCode();
		if (httpResponseCode == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream;
				try {
					instream = entity.getContent();
					Header contentEncoding = response
							.getFirstHeader(CONTENT_ENCODING_HEADER);
					if (contentEncoding != null
							&& contentEncoding.getValue().equalsIgnoreCase(
									GZIP_ENCODING)) {
						instream = new GZIPInputStream(instream);
					}
					byte[] bytes = IOUtils.toByteArray(instream);
					result = parseString(bytes);
				} catch (IllegalStateException e) {
					// TODO - never drop exceptions without logging
				} catch (IOException e) {
					// TODO - never drop exceptions without logging
				}
			}
		} else {
			result = String.valueOf(httpResponseCode);
		}
		return result;
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
			if (Charset.isSupported(DEFAULT_ENCODING)) {
				str = new String(bytes, DEFAULT_ENCODING);
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
