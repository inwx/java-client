package com.inwx.domrobot;

import com.google.gson.JsonObject;
import com.inwx.domrobot.model.BasicResponse;

public class DomaincheckExample {

    public static void main(String[] args) throws Exception {

        String username = "";
        String password = "";
        String sharedSecret = ""; // Only necessary if 2FA is enabled.
        String domain = "my-test-domain-which-is-definitely-not-registered6737.com";

        // By default the ApiClient uses the test api (OT&E). If you want to use the production/live api
        // we have a constant named LIVE_URL in the ApiClient class you can use.
        ApiClient client = new ApiClient(ApiClient.OTE_URL, true);

        BasicResponse loginResponse = client.login(username, password, sharedSecret);
        if (!loginResponse.wasSuccessful()) {
            throw new Exception("Api login error. Code: " + loginResponse.getCode()
                    + ", Message: " + loginResponse.getMsg());
        }

        // Create an object with all parameters for the request.
        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("domain", domain);

        BasicResponse response = client.callApi("domain.check", requestParameters);

        if (!response.wasSuccessful()) {
            client.logout();
            throw new Exception("Api error while checking the domain status. Code: " + response.getCode()
                    + ", Message: " + response.getMsg());
        }

        // get the first domain in the result array 'domain'
        JsonObject domainObject = response.getResData()
                .get("domain")
                .getAsJsonArray().get(0)
                .getAsJsonObject();

        // when avail is 1 the domain is available, when it's 0 its not available
        if (domainObject.get("avail").getAsInt() != 0) {
            System.out.println(domain + " is still available!");
        } else {
            System.out.println("Unfortunately, " + domain + " is already registered.");
        }

        client.logout();
    }
}
