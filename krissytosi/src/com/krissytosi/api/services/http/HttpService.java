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

import android.util.Log;

import com.krissytosi.utils.Constants;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Deals with putting API requests on the wire. Currently only supports GET
 * operations.
 */
public class HttpService {

    private static final String LOG_TAG = "HttpService";

    // http constant timeouts
    private static final int SOCKET_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 30000;
    private static final int MAX_TOTAL_CONNECTIONS = 10;

    // port constants
    private static final int HTTP_PORT = 80;
    private static final int HTTPS_PORT = 443;

    // header constants
    private static final String httpProtocolKey = "http.protocol.version";
    private static final String httpUserAgentKey = "http.useragent";
    private static final String USER_AGENT_HEADER = "Java API Client";
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String GZIP_ENCODING = "gzip";
    private static final String CONTENT_ENCODING_HEADER = "Content-Encoding";

    /**
     * Used to manage a pool of connections.
     */
    private static ThreadSafeClientConnManager connectionManager;

    static {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
        params.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SOCKET_TIMEOUT);
        params.setParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024);
        params.setParameter(ConnManagerParams.TIMEOUT, CONNECTION_TIMEOUT * 2);
        params.setParameter(ConnManagerParams.MAX_TOTAL_CONNECTIONS, MAX_TOTAL_CONNECTIONS);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", SSLSocketFactory
                .getSocketFactory(), HTTPS_PORT));
        schemeRegistry.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), HTTP_PORT));
        HttpService.connectionManager = new ThreadSafeClientConnManager(params,
                schemeRegistry);
    }

    /**
     * Creates a url which can be handed off to a GET or a POST operation based
     * on the input parameters.
     * 
     * @param baseUrl the start of the URL. This can change depending on whether
     *            we are in a test or prod environment.
     * @param options key value pairs appended to the URL in query string
     *            format.
     * @return a fully qualified URL with all parameters accounted for.
     */
    public String createUrl(String baseUrl, Map<String, String> options) {
        // the url is appended to for each key/value pair in the options
        // parameter.
        StringBuffer url = new StringBuffer();
        // first append the base url to hit.
        url.append(baseUrl);
        for (Map.Entry<String, String> entry : options.entrySet()) {
            String key = entry.getKey();
            final String value = entry.getValue();
            if (value != null) {
                String safeValue = null;
                String safeKey = null;
                try {
                    // ensure that the keys and values are safely encoded.
                    safeValue = java.net.URLEncoder.encode(value,
                            DEFAULT_ENCODING);
                    safeKey = java.net.URLEncoder.encode(key, DEFAULT_ENCODING);
                } catch (UnsupportedEncodingException e) {
                    Log.e(LOG_TAG, "createUrl", e);
                }
                String separator = "&";
                // make sure that the first separator in the query string is a
                // question mark.
                if (url.indexOf("?") == -1) {
                    separator = "?";
                }
                // add the key/value pair to the query string.
                url.append(separator);
                url.append(safeKey);
                url.append("=");
                url.append(safeValue);
            }
        }
        return url.toString();
    }

    /**
     * Executes a simple HTTP get.
     * 
     * @param url the URL to target.
     * @return a String representation of the response from the GET.
     */
    public String doGet(String url) {
        String result = "500";
        try {
            HttpClient httpClient = generateHttpClient();
            if (Log.isLoggable(LOG_TAG, Log.DEBUG)) {
                Log.d(LOG_TAG, url);
            }
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpget);
            result = parseApiResponse(response);
            if (Log.isLoggable(LOG_TAG, Log.DEBUG)) {
                Log.d(LOG_TAG, result);
            }
            return result;
        } catch (ClientProtocolException e) {
            Log.e(LOG_TAG, "doGet", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "doGet", e);
        } catch (IllegalArgumentException iae) {
            Log.e(LOG_TAG, "doGet", iae);
        }
        return result;
    }

    /**
     * Creates a {@link HttpClient} object using some of the constants defined
     * at the top of this class.
     * 
     * @return a {@link HttpClient} object which can be used to execute HTTP
     *         requests.
     */
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

    /**
     * Tries to get a String response back from the response parameter.
     * 
     * @param response the response retrieved from the HTTP GET.
     * @return a String representation of the response.
     */
    private String parseApiResponse(HttpResponse response) {
        String result = "500";
        // check that we're getting back a valid header - this helps when the
        // user runs into splash screens
        if (checkResponseForCustomHeader(response)) {
            // see if we got a viable HTTP response back from the API server.
            final int httpResponseCode = response.getStatusLine().getStatusCode();
            if (httpResponseCode == HttpStatus.SC_OK) {
                // get the entity and parse the response
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream;
                    try {
                        instream = entity.getContent();
                        // check to see if gzip is enabled ...
                        Header contentEncoding = response
                                .getFirstHeader(CONTENT_ENCODING_HEADER);
                        if (contentEncoding != null
                                && contentEncoding.getValue().equalsIgnoreCase(
                                        GZIP_ENCODING)) {
                            instream = new GZIPInputStream(instream);
                        }
                        // get the bytes and create a string
                        byte[] bytes = IOUtils.toByteArray(instream);
                        result = parseString(bytes);
                    } catch (IllegalStateException e) {
                        Log.e(LOG_TAG, "parseApiResponse", e);
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "parseApiResponse", e);
                    }
                }
            } else {
                result = String.valueOf(httpResponseCode);
            }
        }
        return result;
    }

    /**
     * Checks the response to ensure that we've definitely hit our API server.
     * 
     * @param response the HTTP response from the wire.
     * @return boolean indicating that a specific header is available in the
     *         response. See {@link Constants} for details of the header key
     *         value pair.
     */
    private boolean checkResponseForCustomHeader(HttpResponse response) {
        // TODO - custom headers and python responses?
        boolean hasValidHeader = true;
        Header headers[] = response.getHeaders(Constants.RESPONSE_HEADER_NAME);
        if (headers != null) {
            for (Header header : headers) {
                if (header.getValue().equalsIgnoreCase(
                        Constants.RESPONSE_HEADER_VALUE)) {
                    hasValidHeader = true;
                    break;
                }
            }
        }
        return hasValidHeader;
    }

    /**
     * Parses a string from the bytes parameter. Tries to use UTF-8 decoding
     * when converting the bytes to a string.
     * 
     * @param bytes the raw response from the API server.
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
            Log.e(LOG_TAG, "parseString", e);
        }
        return str;
    }
}
