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
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class GetResultsByKeyWord extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse response)
			throws IOException {
		
		String keyWords = req.getParameter("keyWord");
		String[] keyWordsArray = keyWords.split(" ");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		JSONArray trainings = new JSONArray();
		JSONArray exercices = new JSONArray();
		JSONObject results = new JSONObject();
		
		//Get trainings
		Query queryTraining = new Query("TrainingPlan");
		PreparedQuery pQueryTraining = datastore.prepare(queryTraining);
		
		for (Entity resultTraining : pQueryTraining.asIterable()) {
			String TitleTrainingPlan = (String)resultTraining.getProperty("title");
			if(TitreContientKeyWords(keyWordsArray,TitleTrainingPlan))
			{
				JSONObject training = new JSONObject();
				training.put("title", resultTraining.getProperty("title"));
				training.put("minute", resultTraining.getProperty("duree"));
				trainings.add(training);
			}	
		}
		
		//Get exercices
				Query queryExercice = new Query("Exercices");
				PreparedQuery pQueryExercice = datastore.prepare(queryExercice);
				
				for (Entity resultExo : pQueryExercice.asIterable()) {
					String TitleExo = (String)resultExo.getProperty("title");
					if(TitreContientKeyWords(keyWordsArray,TitleExo))
					{
						JSONObject exercice = new JSONObject();
						exercice.put("keyParent", KeyFactory.keyToString(resultExo.getParent()));
						exercice.put("title", resultExo.getProperty("title"));
						exercice.put("minute", resultExo.getProperty("duree"));
						exercices.add(exercice);
					}	
				}
		results.put("trainings", trainings);
		results.put("exercices", exercices);
				
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.write(results.toJSONString());
		out.flush();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
	}
	
	public boolean TitreContientKeyWords(String[] keyWordsArray,String Titre){
		String Titre_Lower = Titre.toLowerCase();
		for(String KeyWord:keyWordsArray) {
		   if(Titre_Lower.contains(KeyWord.toLowerCase()))
			   return true;
		}
		return false;
	}
}
