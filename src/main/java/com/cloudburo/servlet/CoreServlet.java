package com.cloudburo.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class CoreServlet extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(CoreServlet.class.getCanonicalName());

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		logger.log(Level.INFO, "Going to fetch  static files at path {0}",req.getContextPath());
	}
}
