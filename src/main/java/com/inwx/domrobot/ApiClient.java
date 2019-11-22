/*
 * Copyright (c) 2014 - 2019 INWX GmbH & Co. KG
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.inwx.domrobot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jboss.aerogear.security.otp.Totp;

import com.google.gson.*;
import com.inwx.domrobot.model.BasicResponse;

/**
 * Used to make api calls to the INWX api.
 */
public class ApiClient {

    public static final String VERSION = "3.0";
    public static final String LIVE_URL = "https://api.domrobot.com";
    public static final String OTE_URL = "https://api.ote.domrobot.com";

    private String apiUrl;
    private ApiType apiType = ApiType.JSON_RPC;
    private boolean debugMode;

    private HttpClient httpClient;
    private CookieStore httpClientCookieStore;
    private HttpPost xmlRpcRequestTemplate;
    private Gson gson, prettyPrintingGson;

    /**
     * @param apiUrl url of the api.
     * @param debugMode whether requests and responses should be printed out.
     */
    public ApiClient(String apiUrl, boolean debugMode) {
        this.apiUrl = apiUrl;
        this.debugMode = debugMode;

        httpClientCookieStore = new BasicCookieStore();
        httpClient = HttpClients.custom().setDefaultCookieStore(httpClientCookieStore).build();

        xmlRpcRequestTemplate = buildXmlRpcRequestTemplate();

        gson = new Gson();
        prettyPrintingGson = new GsonBuilder().setPrettyPrinting().create();
    }

    public ApiClient(String apiUrl) {
        this(apiUrl, false);
    }

    /**
     * Performs a login at the api and saves the session cookie for following api calls.
     *
     * @param username your username.
     * @param password your password.
     * @param sharedSecret a secret used to generate a secret code to solve 2fa challenges when 2fa
     *        is enabled. This is the code/string encoded in the QR-Code you scanned with your
     *        google authenticator app when you enabled 2fa. If you don't have this secret anymore,
     *        disable and re-enable 2fa for your account but this time save the code/string encoded
     *        in the QR-Code.
     * @return BasicResponse
     * @throws IOException any exception occurred during the request.
     */
    public BasicResponse login(String username, String password, String sharedSecret)
                    throws IOException {
        JsonObject loginParams = new JsonObject();
        loginParams.addProperty("user", username);
        loginParams.addProperty("pass", password);

        BasicResponse loginResponse = callApi("account.login", loginParams);

        if (loginResponse.wasSuccessful() && loginResponse.getResData().has("tfa")
                        && !loginResponse.getResData().get("tfa").getAsString().equals("0")) {
            if (sharedSecret == null) {
                throw new IllegalArgumentException(
                                "Api requests two factor authentication but no shared secret is given.");
            }

            JsonObject unlockParams = new JsonObject();
            unlockParams.addProperty("tan", new Totp(sharedSecret).now());
            BasicResponse unlockResponse = callApi("account.unlock", unlockParams);

            if (!unlockResponse.wasSuccessful()) {
                return unlockResponse;
            }
        }

        return loginResponse;
    }

    /**
     * Performs a login at the api and saves the session cookie for following api calls.
     *
     * @param username your username.
     * @param password your password.
     * @return BasicResponse
     * @throws IOException
     */
    public BasicResponse login(String username, String password) throws IOException {
        return login(username, password, null);
    }

    /**
     * Logs out the user and destroys the session.
     *
     * @return BasicResponse
     * @throws IOException any exception occurred during the request.
     */
    public BasicResponse logout() throws IOException {
        BasicResponse logoutResponse = callApi("account.logout");
        httpClientCookieStore.clear();
        return logoutResponse;
    }


    /**
     * Makes an api call.
     *
     * @param method name of the method called in the api.
     * @param params model containing the request parameters. You could create your own models for
     *        requests and responses or just use an {@link JsonObject} or a
     *        {@link java.util.HashMap}. The given object will be mapped to a json object and sent
     *        via the api.
     * @param responseModelClass class of the model which the response should be mapped to.
     * @param <RequestModel> type of the request model.
     * @param <ResponseModel> type of the response model.
     * @return ResponseModel instance of the responseModelClass
     * @throws IOException any exception occurred during the request.
     */
    public <RequestModel, ResponseModel> ResponseModel callApi(String method, RequestModel params,
                    Class<ResponseModel> responseModelClass) throws IOException {
        JsonObject payload = new JsonObject();
        payload.addProperty("method", method);
        if (params != null) {
            payload.add("params", gson.toJsonTree(params));
        }

        HttpPost postRequest;
        try {
            postRequest = (HttpPost) xmlRpcRequestTemplate.clone();
            postRequest.setEntity(new StringEntity(gson.toJson(payload), StandardCharsets.UTF_8));
        } catch (CloneNotSupportedException exception) {
            throw new IOException("Could not set body for request. ", exception);
        }

        HttpResponse response = httpClient.execute(postRequest);
        ResponseModel parsedModel = gson.fromJson(
                        EntityUtils.toString(response.getEntity(), "UTF-8"), responseModelClass);

        if (debugMode) {
            System.out.println("Request (" + method + "): " + prettyPrintingGson.toJson(payload));
            System.out.println(
                            "Response (" + method + "): " + prettyPrintingGson.toJson(parsedModel));
        }

        return parsedModel;
    }

    /**
     * @param method name of the method called in the api.
     * @param params {@link JsonObject} used as a key value store with all request parameters.
     * @return BasicResponse
     * @throws IOException any exception occurred during the request.
     */
    public BasicResponse callApi(String method, JsonObject params) throws IOException {
        return callApi(method, params, BasicResponse.class);
    }

    /**
     * Makes an api call.
     *
     * @param method name of the method called in the api.
     * @return BasicResponse
     * @throws IOException any exception occurred during the request.
     */
    public BasicResponse callApi(String method) throws IOException {
        return callApi(method, null);
    }

    private HttpPost buildXmlRpcRequestTemplate() {
        HttpPost postRequest = new HttpPost(apiUrl + apiType.getPath());
        postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("User-Agent", "DomRobot/" + VERSION + " (Java "
                        + System.getProperty("java.version") + ")");

        return postRequest;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public ApiType getApiType() {
        return apiType;
    }

    public boolean isDebugMode() {
        return debugMode;
    }
}
