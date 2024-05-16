
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class DataGenerator {
	
	private static final int TRAINING_SIZE = 4000;
	private static final int VAlIDATION_SIZE = 4000;
	
	private List<Point> trainingData;
	private List<Point> validationData;
	
	public static class Point{
		public float x1 ;
		public float x2 ; 
		public int category;
		
		public Point(float x1 , float x2, int category) {
			this.x1 = x1;
			this.x2 = x2;
			this.category = category;			
		}
	}
	
	
	//Constructor 
	public DataGenerator() {
		this.trainingData = generateData(TRAINING_SIZE);
		this.validationData = generateData(VAlIDATION_SIZE);
	}
	
	
	public List<Point> getTrainingData(){
		return this.trainingData;
	}
	
	public List<Point> getValidationData(){
		return this.validationData;
	}
	
	
	public  List<Point> generateData(int Size){
		List<Point> data = new ArrayList<>();
		Random random =new Random();
		
		//this is random values between the -1 and 1
		for (int i =0 ; i < Size ; i++) {
			float x1 = random.nextFloat()*2-1; 
			float x2 = random.nextFloat()*2-1;
			
			int category = categorizePoint(x1,x2);
			
			data.add(new Point(x1,x2,category));
		}
		return data;
	}

	
	private static int categorizePoint(float x1, float x2) {
		if((Math.pow(x1 -0.5, 2)+ Math.pow(x2 -0.5, 2)<0.2)){
			if(x1>0.5) 
			{
				return 1;
			}else if(x1<0.5) 
			{
				return 2;
			}
		}else if((Math.pow(x1+0.5, 2)+Math.pow(x2+0.5,2))<0.2){
			if(x1 > -0.5) {
				return 1;
			}else if(x1<-0.5) {
				return 2;
			}
		}else if ((Math.pow(x1 - 0.5, 2) + Math.pow(x2 + 0.5, 2)) < 0.2) {
            if (x1 > 0.5) {
                return 1;
            } else if (x1 < 0.5) {
                return 2;
            }
        }else if ((Math.pow(x1 + 0.5, 2) + Math.pow(x2 - 0.5, 2)) < 0.2) {
            if (x1 > -0.5) {
                return 1;
            } else if (x1 < -0.5) {
                return 2;
            }
        }else if (x1 > 0) {
            return 3;
        } else if (x1 < 0) {
            return 4;
        }
		
		
		return 0; // this will be returned if the code above fails. 
	}
	
	
	public void writeDataFile(String filePath, List<Point> data) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
			for ( Point point : data) { 
				writer.write(point.x1 + "," + point.x2 + "," + point.category);
				writer.newLine();
			}
			}catch(IOException e) {
				e.printStackTrace();
		}
	}

}
