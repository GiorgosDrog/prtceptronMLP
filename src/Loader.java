

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;



public class Loader {
	private String filePath;
	
	public Loader(String filePath) {
		this.filePath = filePath;
	}
	
	public void loadData(ArrayList<Integer> dataOut , ArrayList<float[]> dataInput){
		
		
		
		try{
			System.out.println(this.filePath);
			File file = new File(this.filePath);
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String[] values = scanner.nextLine().split(",");
				
				float x1 = Float.parseFloat(values[0].trim());
				float x2 = Float.parseFloat(values[1].trim());
				float[] inputPairs = {x1,x2};
				
				int category = Integer.parseInt(values[2].trim());
				
				dataInput.add(inputPairs);
				dataOut.add(category);
			}
			scanner.close();

		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
