package root;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.XML;
import org.json.JSONObject;



public class GetRSSServelet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse response)
			throws IOException {
		
		    StringBuffer sb = new StringBuffer();
			URL url = new URL("http://www.bodybuilding.com/rss/articles");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			
			String xml = sb.toString();
		    System.out.println(xml);
		    
		    JSONObject jsonObject = XML.toJSONObject(xml);
		    System.out.println(jsonObject);
		    
		    response.setContentType("application/json");
		    PrintWriter out = response.getWriter();
			out.write(jsonObject.toString());
			out.flush();
		    
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
	}
}
