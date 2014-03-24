package Exercise33_Restful_2;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		try {
			Server server = new Server();
			Client client = new Client();
	        List<Object> request = new ArrayList<Object>();
	        request.add("get");
	        request.add("default");
	        request.add(new ArrayList<Object>());
	        while(true){
	        	List<Object> response = server.handle_request(request);
	        	request = client.render_and_get_input(response);
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
