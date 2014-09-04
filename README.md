inwx.com XML-RPC Java Client
=========
You can access all functions of our frontend via an application programming interface (API). Our API is based on the XML-RPC protocol and thus can be easily addressed by almost all programming languages. The documentation and programming examples in PHP, Java, Ruby and Python can be downloaded here.

There is also an OT&E test system, which you can access via ote.inwx.com. Here you will find the known web interface which is using a test database. On the OTE system no actions will be charged. So you can test how to register domains etc.

Documentation
------
You can view a detailed description of the API functions in our documentation. The documentation as PDF ist part of the Projekt. You also can read the documentation online http://www.inwx.de/en/help/apidoc

Example
-------

The implementation in Java requires the included .JAR files.

```java
import model.ArrayElement;
import model.Element;
import model.MapElement;
import org.apache.xmlrpc.XmlRpcException;
import java.net.MalformedURLException;

public class InwxJavaExample {
  public static void main(String args[]) throws MalformedURLException, XmlRpcException {
    System.out.println("Starting inwx Java example ...");
    String username = "your username";
    String password = "your password";

    Connector connector = new Connector();
    if (connector.login(username,password)) {
      ArrayElement myDomains = connector.checkDomain("inwx23 werew4rewr.de");
      for ( Element item : (Element[]) myDomains.getArray(  ) ) {
        MapElement _item = (MapElement) item;
        System.out.println("tld: " + _item.get("tld").toString());
        System.out.println("name: " + _item.get("name").toString());
        System.out.println("domain: " + _item.get("domain").toString());
        System.out.println("status: " + _item.get("status").toString());
        System.out.println("price: " + _item.get("price").toString());
      }

      ArrayElement domains = connector.getAllNS();
      for ( Element item : (Element[]) domains.getArray(  ) ) {
        MapElement _item = (MapElement) item;
        System.out.println(_item.get("domain")+":"+_item.get("type"));
      }
      
      connector.logout();
      System.out.println("logout");
    }//if login
  }
}
```

License
----

MIT
