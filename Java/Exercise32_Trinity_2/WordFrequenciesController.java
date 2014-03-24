package Exercise32_Trinity_2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class WordFrequenciesController {
	
	private WordFrequenciesModel model;
	
	WordFrequenciesController(WordFrequenciesModel model){
		this.model = model;
	}
	
	public void run(){
		Pattern pattern = Pattern.compile("\\s*");
		while(true){
			System.out.println("\nNext file:");
		    BufferedReader reader= new BufferedReader(new InputStreamReader(System.in));
			try {
				String filename = reader.readLine();
				if(pattern.matcher(filename).matches()) continue;
				this.model.update(filename);
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		WordFrequenciesModel model = new WordFrequenciesModel();
		WordFrequenciesView view = new WordFrequenciesView(model);
		// controller doesn't need to know the existence of any view
		WordFrequenciesController controller = new WordFrequenciesController(model);
		controller.run();
	}

}
