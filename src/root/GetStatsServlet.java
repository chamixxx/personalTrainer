package root;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class GetStatsServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse response)
			throws IOException {
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		JSONArray stats = new JSONArray();
		Query query = new Query("StatisticsExo");
		
		Filter filterUser = new FilterPredicate("userId",
                FilterOperator.EQUAL,
                req.getParameter("userId"));
		
		query.setFilter(filterUser);
		PreparedQuery pq = datastore.prepare(query);
		
		for (Entity result : pq.asIterable()) {
			System.out.println("stat : " + result);
			JSONObject exo = new JSONObject();
			exo.put("exoName", result.getProperty("exoName"));
			exo.put("traingPlanName", result.getProperty("traingPlanName"));
			exo.put("currentDate", result.getProperty("currentDate"));
			exo.put("completed", result.getProperty("completed"));
			stats.add(exo);
		}
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.write(stats.toJSONString());
		out.flush();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
	}

}
