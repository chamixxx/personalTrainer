package root;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class AddWelcomeServlet extends HttpServlet {
	public static final String WELCOME_MSG_ENTITY_KEY="WelcomeMsg";
	public static final String WELCOME_MSG_MSG_ENTITY_PROPERTY="msg";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity msgW = new Entity(WELCOME_MSG_ENTITY_KEY);
		String message = "Bienvenue sur votre application d'entrainement personalise";
		msgW.setProperty(WELCOME_MSG_MSG_ENTITY_PROPERTY,message);
		datastore.put(msgW); 
		
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
	}

}
