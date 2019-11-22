<p align="center">
  <a href="https://www.inwx.com/en/" target="_blank">
    <img src="https://images.inwx.com/logos/inwx.png">
  </a>
</p>

INWX Domrobot Java Client
=========
You can access all functions of our frontend via our API, which is available via the JSON-RPC protocol and thus can be easily consumed with all programming languages.

There is also an OT&E test system, which you can access via [ote.inwx.com](https://ote.inwx.com/en/). Here you will find the known web interface which is using a test database. On the OT&E system no actions will be charged. So you can test as much as you like there.

Documentation
------
You can view a detailed description of the API functions in our documentation. You can find the online documentation [by clicking here](https://www.inwx.de/en/help/apidoc).

If you still experience any kind of problems don't hesitate to contact our [support via email](mailto:support@inwx.de).

Installation
-------

#### Add the domrobot as a dependency:

If you use gradle:
```gradle
compile 'com.inwx:domrobot:3.1.1'
```

Or if you use maven:
```xml
<dependency>
    <groupId>com.inwx</groupId>
    <artifactId>domrobot</artifactId>
    <version>3.1.1</version>
</dependency>
```

Example
-------

```java
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
```

You can also have a look at the [examples folder](examples) in the project for even more info.

License
----

MIT
