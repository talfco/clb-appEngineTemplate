package com.cloudburo.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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
public class CustomerServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(CustomerServlet.class.getCanonicalName());
	private static final int HTTP_STATUSCODE_SUCCESS = 200;
	private static final int HTTP_STATUSCODE_NOTFOUND = 404;
	private static final int HTTP_STATUSCODE_BADREQUEST = 400;
	
	

	private static int sResponseLimit = 20;

	private class MetaRecord {

		@SuppressWarnings("unused")
		String _cursor;

		MetaRecord(String cursor) {
			_cursor = cursor;
		}
	}
	
	public static void setResponseResultSize(int limit) {
		sResponseLimit = limit;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		logger.log(Level.INFO, "Going to fetch {0} objects", Customer.class.getName());
		logger.log(Level.INFO, "Pathinfo {0}", req.getPathInfo());
		// Retrieving the records from the persistent store
		// TODO: If pathInfo == null then query all otherwise fetch one entry
		if (req.getPathInfo() == null) {
			Query<Customer> query = OfyService.ofy().load().type(Customer.class).limit(sResponseLimit);
			String cursorStr = req.getParameter("cursor");
			if (cursorStr != null)
				query = query.startAt(Cursor.fromWebSafeString(cursorStr));
			int nrRec = 0;
			QueryResultIterator<Customer> iterator = query.iterator();

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
			if (tok.countTokens() == 1) {
				Key<Customer> key = Key.create(Customer.class, Long.parseLong(tok.nextToken()));
				logger.log(Level.INFO, "Going to get customer {0}", key);
				Customer customer = OfyService.ofy().load().type(Customer.class).filterKey(key).first().now();
				if (customer != null) {
					resp.getWriter().print((new GsonWrapper()).getGson().toJson(customer));
				}
				else {
					resp.getWriter().print("{}");
				    resp.setStatus(HTTP_STATUSCODE_NOTFOUND);
				}
			} else {
				// FIXME: We have to throw an error that we don't understand this
				resp.sendError(HTTP_STATUSCODE_BADREQUEST);
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
		logger.log(Level.INFO, "Persisted Customer with id {0}",customer.get_id());
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
		logger.log(Level.INFO, "Persisted Customer with id {0}",customer.get_id());
		resp.getWriter().print((new GsonWrapper()).getGson().toJson(customer));
	}
}
