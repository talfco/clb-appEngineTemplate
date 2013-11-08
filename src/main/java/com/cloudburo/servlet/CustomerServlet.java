/**
 * The MIT License (MIT)
 *
 * Copyright (C) 2013 Felix Kuestahler <felix@cloudburo.com> http://cloudburo.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of 
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO 
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */
package com.cloudburo.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.cloudburo.entity.GsonWrapper;
import com.cloudburo.entity.OfyService;
import com.cloudburo.entity.Customer;

@SuppressWarnings("serial")
public class CustomerServlet extends CoreServlet {

	private static final Logger logger = Logger.getLogger(CustomerServlet.class.getCanonicalName());


	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		logger.log(Level.INFO, "Going to fetch {0} objects", Customer.class.getName());
		logger.log(Level.INFO, "Pathinfo {0}", req.getPathInfo());
		// Partial response
		String filter= req.getParameter("filter");
		logger.log(Level.INFO, "Filter parameter {0}", filter);
		// Retrieving the records from the persistent store
		// If pathInfo == null then query all otherwise fetch one entry
		if (req.getPathInfo() == null || req.getPathInfo().length()==1) {
			Query<?> query = OfyService.ofy().load().type(Customer.class).limit(sResponseLimit);
			String cursorStr = req.getParameter("cursor");
			if (cursorStr != null)
				query = query.startAt(Cursor.fromWebSafeString(cursorStr));
			int nrRec = 0;
			QueryResultIterator<?> iterator = query.iterator();

			Collection collection = new ArrayList();		
			while (iterator.hasNext()) {
				nrRec++;
				collection.add(iterator.next());
			}
			String cursor="";
			if (nrRec==sResponseLimit) {
				cursor = iterator.getCursor().toWebSafeString();
			}
			collection.add(new MetaRecord(cursor));
			resp.setContentType("application/json");
			resp.getWriter().print((new GsonWrapper()).getGson().toJson(collection));
		} else {
			StringTokenizer tok = new StringTokenizer(req.getPathInfo(),"/");
			// This must be the identifier
			if (tok.countTokens() == 1) {
				Key<Customer> key = Key.create(Customer.class, Long.parseLong(tok.nextToken()));
				logger.log(Level.INFO, "Going to get customer {0}", key);
				Customer customer = OfyService.ofy().load().type(Customer.class).filterKey(key).first().now();
				if (customer != null) {
					if (filter == null || filter.equals(""))
						resp.getWriter().print((new GsonWrapper()).getGson().toJson(customer));
					else
						try {
							resp.getWriter().print(partialResponse(filter,customer));
						} catch (Exception e) {
							resp.sendError(resp.SC_BAD_REQUEST);
							resp.getWriter().print(errorMsg(e.getMessage(),"0","0"));
							// This shouldn't happen at all IllegalAccessException
						}
				}
				else {
					resp.getWriter().print("{}");
				    resp.setStatus(resp.SC_NOT_FOUND);
				}
			} else {
				// FIXME: We have to throw an error that we don't understand this
				resp.sendError(resp.SC_BAD_REQUEST);
			}
			
		}
	}

	/**
	 * doPut will be called when a entity gets updated
	 * @param req: The json entity object (complete) with the changed attributes
	 * @param resp: The json entity object after it got persisted
	 */
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.log(Level.INFO, "Updating Customer");
		Customer customer = (new GsonWrapper()).getGson().fromJson(req.getReader(),Customer.class);
		OfyService.ofy().save().entity(customer).now();
		logger.log(Level.INFO, "Persisted Customer with id {0}",customer._id);
		resp.getWriter().print((new GsonWrapper()).getGson().toJson(customer));
	}

	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Key<Customer> customerKey = Key.create(Customer.class, Long.parseLong(req.getPathInfo().substring(1)));
		logger.log(Level.INFO, "Deleting object with identifier {0}",  customerKey);
		OfyService.ofy().delete().key(customerKey).now();
	}

	/**
	 * doPost will be called when a new entity will be created 
	 * @param req: the json entity object 
	 * @param resp: the persisted json entity object (i.e. the object complemented by the key value)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.log(Level.INFO, "Creating Customer");
		Customer customer = (new GsonWrapper()).getGson().fromJson(req.getReader(),Customer.class);
		OfyService.ofy().save().entity(customer).now();
		logger.log(Level.INFO, "Persisted Customer with id {0}",customer._id);
		resp.getWriter().print((new GsonWrapper()).getGson().toJson(customer));
	}
}
