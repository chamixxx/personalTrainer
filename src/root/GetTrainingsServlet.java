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


public class GetTrainingsServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse response)
			throws IOException {
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		JSONArray trainings = new JSONArray();
		Query query = new Query("TrainingPlan");
		PreparedQuery pq = datastore.prepare(query);
		
		for (Entity result : pq.asIterable()) {
			System.out.println("test : " + result);
			JSONObject training = new JSONObject();
			training.put("title", result.getProperty("title"));
			training.put("description", result.getProperty("description"));
			training.put("domain", result.getProperty("domain"));
			trainings.add(training);
		}
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.write(trainings.toJSONString());
		out.flush();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
	}

}
