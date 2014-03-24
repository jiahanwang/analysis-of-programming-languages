package Exercise33_Restful_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Client {
	
	public List<Object> render_and_get_input(List<Object> args) throws IOException{
		String state_representation = (String)args.get(0);
		List<Object> links = args.subList(1, args.size());
		System.out.println(state_representation);
		System.out.flush();
	    BufferedReader reader= new BufferedReader(new InputStreamReader(System.in));
		if(links.size() > 1){
			try{
				int input = Integer.parseInt(reader.readLine());
				if(input <= links.size())
					return (List<Object>) links.get(input - 1);
				else throw new Exception();
			}catch(Exception e){
				List<Object> response = new ArrayList<Object>();
		        response.add("get");
		        response.add("default");
		        response.add(new ArrayList<Object>());
		        return response;
			}
		}else if(links.size() == 1){
			List<Object> link = (List<Object>) links.get(0);
			if(((String)link.get(1)).equals("post")){
				String input = reader.readLine();
		        List<Object> data = new ArrayList<Object>();
		        data.add(input);
		        link.remove(link.size()-1);
				link.add(data);
				return link;
			}else
				return link;
			
		}else{
			List<Object> response = new ArrayList<Object>();
	        response.add("get");
	        response.add("default");
	        response.add(null);
	        return response;
		}
	}

}
