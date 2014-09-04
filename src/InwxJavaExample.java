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

            if (connector.login(username,password)){
                ArrayElement myDomains = connector.checkDomain("inwx23werew4rewr.de");

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

               // connector.getNS("inwx.de");


                connector.logout();
                System.out.println("logout");
            }//if login
        
    }


}


