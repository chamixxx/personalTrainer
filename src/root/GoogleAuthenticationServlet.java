package root;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;
import com.google.appengine.repackaged.com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.appengine.repackaged.com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.appengine.repackaged.com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.appengine.repackaged.com.google.api.client.http.HttpTransport;
import com.google.appengine.repackaged.com.google.api.client.http.javanet.NetHttpTransport;
import com.google.appengine.repackaged.com.google.api.client.json.jackson.JacksonFactory;


@SuppressWarnings("serial")
public class GoogleAuthenticationServlet extends HttpServlet{
	private String NAME_KEY = "name";
	private String MAIL_KEY = "mail";
	Cache cache=null;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
	    String name = (String) cache.get(NAME_KEY);
	    String email = (String) cache.get(MAIL_KEY);
	    
	    JSONObject jsonResponse = new JSONObject();
	    jsonResponse.put(NAME_KEY, name);
	    jsonResponse.put(MAIL_KEY, email);
	    resp.setContentType("application/json");
		resp.getWriter().println(jsonResponse);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp)
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
	 
	    String idTokenString = (String) req.get("idtoken");
	  

		
		System.out.println("token : " + idTokenString);

		
		String CLIENT_ID = "787433281420-16ol1h0areuh13j2egd0339ejrltoaq3.apps.googleusercontent.com";
		HttpTransport transport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
	    .setAudience(Arrays.asList(CLIENT_ID))
	    .build();
		
		GoogleIdToken idToken = null;
		try {
			idToken = verifier.verify(idTokenString);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (idToken != null) {
			  Payload payload = idToken.getPayload();
			  System.out.println("Good Token");
			  writeInfoInCache(payload.getEmail(), (String)payload.get("given_name"));
			
		}
		else {
			System.out.println("Invalid ID token.");
		}
	}
	
	public void writeInfoInCache(String email, String name) {
		


	    Map props = new HashMap();
	    props.put(GCacheFactory.EXPIRATION_DELTA, 3600);
	    props.put(MemcacheService.SetPolicy.ADD_ONLY_IF_NOT_PRESENT, true);
	    try {
	      // Recuperation du Cache
	        CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
	        // creation/recuperation du cache suivant des proprietes specifiques
	        cache = cacheFactory.createCache(props);
	        // Si aucune propriete n'est specifiee, 
	        //creer/recuperer un cache comme ci-dessous
	        //cache = cacheFactory.createCache(Collections.emptyMap());
	     } catch (CacheException e) {
	         // Traitement en cas d'erreur sur la recuperation/configuration du cache
	     }
	    cache.put(NAME_KEY, name);
	    cache.put(MAIL_KEY, email);
	}
}
