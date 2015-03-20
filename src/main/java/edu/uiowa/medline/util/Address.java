package edu.uiowa.medline.util;

import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class Address {
    static Logger logger = Logger.getLogger(Address.class);

    static Pattern trailingEmail = Pattern.compile("(.* )?([-.A-Za-z0-9]+\\@[-.A-Za-z0-9]+)$");
    
    String address = null;
    String email = null;
    Vector<AddressComponent> componentVector = new Vector<AddressComponent>();
    int threadID = 0;
    
    public Address(String address) {
	this.address = address;
    }
    
    public void setThreadID(int threadID) {
	this.threadID = threadID;
    }
    
    public void decompose() {
	    Matcher matcher = trailingEmail.matcher(address);
	    if (matcher.matches()) {
		email = matcher.group(2);
		address = matcher.group(1);
	    }
	    
	    logger.info("[" + threadID + "] address: " + address);
	    if (email != null)
		logger.info("[" + threadID + "]\temail: " + email);
	    
	    // in case we only had an email address
	    if (address == null)
		return;
	    	    
	    // do some end-of-line cleanup
	    address = address.trim();
	    if (address.endsWith(".") || address.endsWith(";"))
		address = address.substring(0,address.length()-1);
	    else if (address.endsWith("; and"))
		address = address.substring(0,address.lastIndexOf(';'));
	    else if (address.endsWith("; and"))
		address = address.substring(0,address.lastIndexOf(';'));
	    else if (address.endsWith(" and"))
		address = address.substring(0,address.lastIndexOf(' '));
	    else if (address.endsWith(". Electronic address:"))
		address = address.substring(0,address.lastIndexOf('.'));
	    else if (address.endsWith(". E-mail:"))
		address = address.substring(0,address.lastIndexOf('.'));
	    else if (address.endsWith(" E-mail:"))
		address = address.substring(0,address.lastIndexOf(' '));
	    
	    StringTokenizer tokenizer = new StringTokenizer(address,",");
	    while (tokenizer.hasMoreTokens()) {
		String token = tokenizer.nextToken().trim();
		
		if (token.equals("and") || token.equals("Email:") || token.equals("E-mail:"))
		    continue;
		
		componentVector.add(new AddressComponent(token));
		logger.debug("[" + threadID + "]\ttoken: " + token);
	    }
	
    }

}
