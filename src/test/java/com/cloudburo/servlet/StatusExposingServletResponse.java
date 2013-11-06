package com.cloudburo.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

// http://stackoverflow.com/questions/1302072/how-can-i-get-the-http-status-code-out-of-a-servletresponse-in-a-servletfilter
public class StatusExposingServletResponse extends HttpServletResponseWrapper {
	
	 public StatusExposingServletResponse(HttpServletResponse response) {
	        super(response);
	    }

	    @Override
	    public void sendError(int sc) throws IOException {
	        super.sendError(sc);
	    }

	    @Override
	    public void sendError(int sc, String msg) throws IOException {
	        super.sendError(sc, msg);
	    }

	    @Override
	    public void setStatus(int sc) {
	    	System.out.println(">>> Set Status");
	        super.setStatus(sc);
	    }

	    public int getStatus() {
	        try {
	            ServletResponse object = super.getResponse();

	            // call the private method 'getResponse'
	            Method method1 = object.getClass().getMethod("getResponse");
	            Object servletResponse = method1.invoke(object, new Object[] {});

	            // call the parents private method 'getResponse'
	            Method method2 = servletResponse.getClass().getMethod("getResponse");
	            Object parentResponse = method2.invoke(servletResponse, new Object[] {});

	            // call the parents private method 'getResponse'
	            Method method3 = parentResponse.getClass().getMethod("getStatus");
	            int httpStatus = (Integer) method3.invoke(parentResponse, new Object[] {});

	            return httpStatus;
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            return HttpServletResponse.SC_ACCEPTED;
	        }
	    }

	    public String getMessage() {
	        try {
	            ServletResponse object = super.getResponse();

	            // call the private method 'getResponse'
	            Method method1 = object.getClass().getMethod("getResponse");
	            Object servletResponse = method1.invoke(object, new Object[] {});

	            // call the parents private method 'getResponse'
	            Method method2 = servletResponse.getClass().getMethod("getResponse");
	            Object parentResponse = method2.invoke(servletResponse, new Object[] {});

	            // call the parents private method 'getResponse'
	            Method method3 = parentResponse.getClass().getMethod("getReason");
	            String httpStatusMessage = (String) method3.invoke(parentResponse, new Object[] {});

	            if (httpStatusMessage == null) {
	                int status = getStatus();
	                java.lang.reflect.Field[] fields = HttpServletResponse.class.getFields();

	                for (java.lang.reflect.Field field : fields) {
	                    if (status == field.getInt(servletResponse)) {
	                        httpStatusMessage = field.getName();
	                        httpStatusMessage = httpStatusMessage.replace("SC_", "");
	                        if (!"OK".equals(httpStatusMessage)) {
	                            httpStatusMessage = httpStatusMessage.toLowerCase();
	                            httpStatusMessage = httpStatusMessage.replace("_", " ");
	                            httpStatusMessage = capitalizeFirstLetters(httpStatusMessage);
	                        }

	                        break;
	                    }
	                }
	            }

	            return httpStatusMessage;
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            return "";
	        }
	    }

	    private static String capitalizeFirstLetters(String s) {

	        for (int i = 0; i < s.length(); i++) {
	            if (i == 0) {
	                // Capitalize the first letter of the string.
	                s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
	            }

	            if (!Character.isLetterOrDigit(s.charAt(i))) {
	                if (i + 1 < s.length()) {
	                    s = String.format("%s%s%s", s.subSequence(0, i + 1), 
	                            Character.toUpperCase(s.charAt(i + 1)), 
	                            s.substring(i + 2));
	                }
	            }
	        }

	        return s;

	    }

	    @Override
	    public String toString() {
	        return this.getMessage() + " " + this.getStatus();
	    }
}
