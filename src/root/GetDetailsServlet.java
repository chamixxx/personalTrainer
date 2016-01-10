package root;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

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
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;

import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class GetDetailsServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse response)
			throws IOException {
		
		String name = req.getParameter("name");
		String type = req.getParameter("type");
		String parentKey = req.getParameter("parentKey");
		
		System.out.println(name);
		System.out.println(type);
		System.out.println(parentKey);
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		JSONObject training = new JSONObject();
		Query query = new Query(type);
		Filter filterName = new FilterPredicate("title",
                FilterOperator.EQUAL,
                name);
		query.setFilter(filterName);

		PreparedQuery pq = datastore.prepare(query);
		
		if (type.equals("TrainingPlan")) {
			for (Entity result : pq.asIterable()) {
				System.out.println("test : " + result.getKey());
				training.put("title", result.getProperty("title"));
				training.put("description", result.getProperty("description"));
				training.put("domain", result.getProperty("domain"));
				training.put("duree", result.getProperty("duree"));
				
				JSONArray exercices = new JSONArray();
				Query queryExo = new Query("Exercices");
				queryExo.setAncestor(result.getKey());
				PreparedQuery pqexo = datastore.prepare(queryExo);
				
				for (Entity exo : pqexo.asIterable()) {
					System.out.println("exo : " + exo.getParent());
					JSONObject exercice = new JSONObject();
					exercice.put("title", exo.getProperty("title"));
					exercice.put("duree", exo.getProperty("duree"));
					exercice.put("description", exo.getProperty("description"));
					exercices.add(exercice);
				}
				training.put("exercices", exercices);
			}
		}
		else {
			Entity trainingPlan;
			try {
				trainingPlan = datastore.get(KeyFactory.stringToKey(parentKey));
				training.put("title", trainingPlan.getProperty("title"));
				training.put("description", trainingPlan.getProperty("description"));
				training.put("domain", trainingPlan.getProperty("domain"));
				training.put("duree", trainingPlan.getProperty("duree"));
				
				JSONArray exercices = new JSONArray();
				Query queryExo = new Query("Exercices");
				queryExo.setAncestor(trainingPlan.getKey());
				queryExo.setFilter(filterName);
				PreparedQuery pqexo = datastore.prepare(queryExo);
				
				for (Entity exo : pqexo.asIterable()) {
					System.out.println("exo : " + exo.getParent());
					JSONObject exercice = new JSONObject();
					exercice.put("title", exo.getProperty("title"));
					exercice.put("duree", exo.getProperty("duree"));
					exercice.put("description", exo.getProperty("description"));
					exercices.add(exercice);
				}
				training.put("exercices", exercices);
				

			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.write(training.toJSONString());
		out.flush();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		StringBuffer sb = new StringBuffer();
		 
	    try 
	    {
	      BufferedReader reader = request.getReader();
	      String line = null;
	      while ((line = reader.readLine()) != null)
	      {
	        sb.append(line);
	      }
	    } catch (Exception e) { e.printStackTrace(); }
	 
	    JSONParser parser = new JSONParser();
	    JSONObject req = null;
	    try
	    {
	      req = (JSONObject) parser.parse(sb.toString());
	    } catch (ParseException e) { e.printStackTrace(); }
	    
	    System.out.println(req);	
	    
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		JSONObject training = new JSONObject();
		Query queryExisting = new Query("StatisticsExo");
		
		Filter filterTrainingName = new FilterPredicate("traingPlanName",
                FilterOperator.EQUAL,
                req.get("traingPlanName"));
		
		Filter filterExoName = new FilterPredicate("exoName",
                FilterOperator.EQUAL,
                req.get("exoName"));
		
		Filter filterUser = new FilterPredicate("userId",
                FilterOperator.EQUAL,
                req.get("userId")); 
		
		Filter combinedFilters = CompositeFilterOperator.and(filterTrainingName, filterExoName, filterUser);
		
		queryExisting.setFilter(combinedFilters);

		PreparedQuery pqExist = datastore.prepare(queryExisting);
		Entity existingEntity = null;
		
		for (Entity result : pqExist.asIterable()) {
			existingEntity = result;
		}
		
		System.out.println(existingEntity);
		
		if (existingEntity == null) {
			existingEntity = new Entity("StatisticsExo"); 
		}
		
		String traingPlanName = (String) req.get("traingPlanName");
		String exoName = (String) req.get("exoName");
		String currentDate = (String) req.get("currentDate");
		String userId = (String) req.get("userId");
		Boolean completed = (Boolean) req.get("completed");
		
		existingEntity.setProperty("traingPlanName", traingPlanName);
		existingEntity.setProperty("exoName", exoName);
		existingEntity.setProperty("currentDate", currentDate);
		existingEntity.setProperty("userId", userId);
		existingEntity.setProperty("completed", completed);
		
		datastore.put(existingEntity);
	}

}

