package root;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;


public class AddTrainingPlanServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse resp)
			throws IOException {
	
		
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("success");

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
	    
	    String exercices = (req.get("exercices")).toString();
	    
	    Queue queue = QueueFactory.getDefaultQueue();
	    TaskOptions task = TaskOptions.Builder.withUrl("/addQueueDatastore").param("title",(String) req.get("title"))
	    															.param("description",(String) req.get("description"))
	    															.param("domain", (String) req.get("domain"))
	    															.param("minute", req.get("minute").toString())
	    															.param("exercices", exercices);
	    queue.add(task);
	}
}
