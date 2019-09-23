package com.inwx.domrobot;

import com.google.gson.annotations.SerializedName;
import com.inwx.domrobot.model.BasicResponse;
import com.inwx.domrobot.model.GenericResponse;

import java.util.List;

public class DomaincheckWithCustomModelsExample {

    public static void main(String[] args) throws Exception {

        String username = "";
        String password = "";
        String sharedSecret = ""; // Only necessary if 2FA is enabled.
        String domain = "my-test-domain-which-is-definitely-not-registered6737.com";

        ApiClient client = new ApiClient(ApiClient.OTE_URL, true);

        // By default the ApiClient uses the test api (OT&E). If you want to use the production/live api
        // we have a constant named LIVE_URL in the ApiClient class you can use.
        BasicResponse loginResponse = client.login(username, password, sharedSecret);
        if (!loginResponse.wasSuccessful()) {
            throw new Exception("Api login error. Code: " + loginResponse.getCode()
                    + ", Message: " + loginResponse.getMsg());
        }

        RequestModel request = new RequestModel(domain);
        ResponseModel response = client.callApi("domain.check", request, ResponseModel.class);

        if (!response.wasSuccessful()) {
            client.logout();
            throw new Exception("Api error while checking the domain status. Code: " + response.getCode()
                    + ", Message: " + response.getMsg());
        }

        if (response.getDomains().get(0).isAvailable()) {
            System.out.println(domain + " is still available!");
        } else {
            System.out.println("Unfortunately, " + domain + " is already registered.");
        }

        client.logout();
    }

    public static class RequestModel {

        @SerializedName("domain")
        private String domain;

        public RequestModel(String domain) {
            this.domain = domain;
        }

        public String getDomain() {
            return domain;
        }
    }

    public static class ResponseModel extends GenericResponse<ResponseModel.ResData> {

        public static class ResData {
            @SerializedName("domain")
            private List<Domain> domains;

            public static class Domain {
                @SerializedName("domain")
                private String name;
                @SerializedName("avail")
                private String avail;
                @SerializedName("status")
                private String status;
                @SerializedName("checktime")
                private int checkTime;

                public boolean isAvailable() {
                    return Integer.parseInt(avail) == 1;
                }

                public String getName() {
                    return name;
                }

                public String getStatus() {
                    return status;
                }

                public int getCheckTime() {
                    return checkTime;
                }
            }
        }

        public List<ResData.Domain> getDomains() {
            return getResData().domains;
        }
    }
}

