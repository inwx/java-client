package de.inwx;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import model.ArrayElement;
import model.Element;
import model.MapElement;

import org.apache.commons.codec.*;
import org.apache.commons.logging.*;
import org.apache.commons.httpclient.*;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

public class Connector {
	final XmlRpcClient client = new XmlRpcClient();
	
	private String server = "https://api.ote.domrobot.com/xmlrpc/";
	private XmlRpcClientConfigImpl serverConfig = new XmlRpcClientConfigImpl();
	
	public Connector(){
		super();
	}
	
	private void init() throws MalformedURLException {
		String version = "1.1";
		try {
			File f = new File("./cert/cacerts");
			String path= f.getAbsolutePath();
			System.setProperty("javax.net.ssl.trustStore",path);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		XmlRpcCommonsTransportFactory transport = new XmlRpcCommonsTransportFactory(client);
		transport.setHttpClient(new HttpClient());
		client.setTransportFactory(transport);
		
		serverConfig.setServerURL(new URL(server));
		serverConfig.setEncoding(XmlRpcClientConfigImpl.UTF8_ENCODING);
		serverConfig.setConnectionTimeout(2000);
		serverConfig.setUserAgent("DomRobot/"+version+" (Java " + System.getProperty("java.version") + ")");
		serverConfig.setEnabledForExtensions(true);
	}
	
	public boolean logout() throws XmlRpcException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Vector<HashMap<String, Object>> attr = new Vector<HashMap<String, Object>>();
		map.put("lang", "eng");
		attr.add(map);
		Map<String, Object> response = (Map<String, Object>) client.execute(serverConfig,"account.logout",attr);
		long code = new Long(response.get("code").toString()).longValue();
		if(code == 1000){
			return true;
		}
		return false;
	}
	
	public boolean login(String username,String password) throws MalformedURLException, XmlRpcException {
		init();
		Vector<HashMap<String, Object>> attr = new Vector<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("user", username);
		map.put("pass", password);
		map.put("lang", "eng");
		attr.add(map);
		
		Map<String, Object> response = (Map<String, Object>) client.execute(serverConfig,"account.login",attr);
		long code = new Long(response.get("code").toString()).longValue();
		if(code == 1000){
			return true;
		}
		return false;
	}
	
	public HashMap<String, Element> getUserInfo() throws MalformedURLException, XmlRpcException {
		Vector attr = new Vector();
		Map<String, Object> res = (Map<String, Object>) client.execute(serverConfig,"account.info",attr);
		HashMap<String, Element> result = new HashMap<String, Element>();
		result.putAll((new MapElement(res)).getMap());
		return result;
	}
	
	public ArrayElement checkDomain(String domainName) throws XmlRpcException {
		return checkDomain(domainName,"2");
	}
	
	public ArrayElement checkDomain(String domainName,String wide) throws XmlRpcException {
		Vector<HashMap<String, Object>> attr = new Vector<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("domain", domainName);
		map.put("lang","eng");
		if (wide != null) map.put("wide",wide);
		attr.add(map);
		Map<String, Object> res = (Map<String, Object>) client.execute(serverConfig,"domain.check",attr);
		HashMap<String, Element> result = new HashMap<String, Element>();
		result.putAll((new MapElement(res)).getMap());
		int code = new Integer(result.get("code").toString()).intValue();
		
		ArrayElement domains  = new ArrayElement();
		if(code != 1000) {
			System.out.println(result.get("reason"));
			System.out.println(result.get("reasonCode"));
			System.out.println(result.get("msg"));
			return null;
		} else{
			MapElement resData = (MapElement) result.get("resData");
			domains = (ArrayElement) resData.get("domain");
		}
		return domains;
	}
	
	public ArrayElement getAllNS() throws XmlRpcException {
		return getNS(null);
	}
	
	public ArrayElement getNS(String domainName) throws XmlRpcException {
		Vector<HashMap<String, Object>> attr = new Vector<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(domainName != null) map.put("domain", domainName);
		map.put("lang","eng");
		//map.put("pagelimit","100");       //max. entries per page (default: 20)
		//map.put("page","2");                    //- page of the list (default: 1)
		attr.add(map);
		Map<String, Object> res = (Map<String, Object>) client.execute(serverConfig,"nameserver.list",attr);
		HashMap<String, Element> result = new HashMap<String, Element>();
		result.putAll((new MapElement(res)).getMap());
		MapElement resData = (MapElement) result.get("resData");
		ArrayElement domains = (ArrayElement) resData.get("domains");
		return domains;
	}
}
