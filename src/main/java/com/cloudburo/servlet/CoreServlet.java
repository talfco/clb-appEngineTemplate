package com.cloudburo.servlet;


import java.lang.reflect.Field;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;

import com.cloudburo.entity.GsonWrapper;

@SuppressWarnings("serial")
public class CoreServlet extends HttpServlet {
	
	protected static int sResponseLimit = 20;
	
	private static final Logger logger = Logger.getLogger(CoreServlet.class.getCanonicalName());
	
	protected class MetaRecord {

		@SuppressWarnings("unused")
		String _cursor;

		MetaRecord(String cursor) {
			_cursor = cursor;
		}
	}
	
	public static void setResponseResultSize(int limit) {
		sResponseLimit = limit;
	}
	
	protected String errorMsg (String usrMsg, String code, String moreInfo) {
		StringBuffer buf = new StringBuffer("{");
		buf.append("message: \"").append(usrMsg).append("\"\n");
		buf.append("errorCode: \"").append(code).append("\"\n");
		buf.append("moreInfo: \"").append(moreInfo).append("\"\n");
		buf.append("}"); 
		return buf.toString();
	}
	
	protected String partialResponse(String filterList, Object elem) throws Exception {
		Vector<String> fieldVec = new Vector<String>();
		StringTokenizer tok = new StringTokenizer(filterList,",");
		while (tok.hasMoreTokens()) fieldVec.add(tok.nextToken());
		Field[] fields = elem.getClass().getDeclaredFields();
		StringBuffer buf = new StringBuffer("{");
		for (int i=0; i< fields.length; i++) {
			if (fieldVec.indexOf(fields[i].getName()) >= 0) {
				logger.log(Level.INFO, "Adding Field {0}",fields[i].getName()); 
				Object value = fields[i].get(elem);
				buf.append("\""+fields[i].getName()+"\""+":"+(new GsonWrapper()).getGson().toJson(value));
				buf.append(",\n");
			}
		}
		buf.replace(buf.length()-2, buf.length()-1, "");
		buf.append("}");
		logger.log(Level.INFO, "Returning JSON {0}",buf.toString()); 
		 
		return buf.toString();
	}
}
