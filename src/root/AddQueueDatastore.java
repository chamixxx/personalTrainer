package root;


import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;



public class AddQueueDatastore extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse resp)
			throws IOException {
	
		
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		

	 
	    JSONParser parser = new JSONParser();
	    JSONArray exercices = null;
	    try
	    {
	    	exercices = (JSONArray) parser.parse(request.getParameter("exercices"));
	    } catch (ParseException e) { e.printStackTrace(); }
		
		
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String domain = request.getParameter("domain");
		String duree = request.getParameter("minute").toString();
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
		
		Entity trainingPlan = new Entity("TrainingPlan");
		trainingPlan.setProperty("title", title);
		trainingPlan.setProperty("description", description);
		trainingPlan.setProperty("domain", domain);
		trainingPlan.setProperty("duree", duree);
		datastore.put(trainingPlan);
		
		for (Object exo : exercices) {
			JSONObject exercice = (JSONObject) exo;
			String exoTitle = (String) exercice.get("title");
			String exoDescription = (String) exercice.get("description");
			String exoDuree = exercice.get("minute").toString();
			
			Entity exerciceEntity = new Entity("Exercices",trainingPlan.getKey());
			exerciceEntity.setProperty("title", exoTitle);
			exerciceEntity.setProperty("description", exoDescription);
			exerciceEntity.setProperty("duree", exoDuree);
			datastore.put(exerciceEntity);
		}
	}
}
