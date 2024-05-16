
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Main {
	
	final static int INPUT_SIZE = 2; 
	final static int OUTPUT_SIZE = 4; 
	final static int[] HIDDEN_LAYER_SIZES = {10,10,10};
 	final static String ACTIVATION_FUNC = "tanh" ; 
	final static String OUTPUT_ACTIVATION_FUNC = "sigmoid"; 
	final static boolean USE_MINIBATCHES = true; 
	final static int BATCH_SIZE = 60; 
	final static float EPOCH_BREAK_POINT = 0.000001f; 
	final static float LEARNING_STEP = 0.1f; 
	final static String LOSSFUNCTION = "MSE";
	final static int MAX_EPOCHS = 1000;
	
	
	static ArrayList<Integer> dataOutputs = new ArrayList<Integer>(); // target
	static ArrayList<float[]> dataInputs = new ArrayList<float[]>(); //input points
	
	static ArrayList<Integer> dataOutputsValidation = new ArrayList<Integer>(); // target
	static ArrayList<float[]> dataInputsValidation = new ArrayList<float[]>(); //input points
	
	public static float[] encodeToOneHot(int category) {
	    if (category < 1 || category > 4) {
	        throw new IllegalArgumentException("Invalid category: " + category);
	    }

	    float[] oneHot = new float[4];
	    oneHot[category - 1] = 1.0f;

	    return oneHot;
	}
	
	
	public static void main(String[] args)
	{
		boolean newData= false; // chance this to produce new data 
		if(newData == true) 
		{
			DataGenerator datagenerator  = new DataGenerator();
	    	List<DataGenerator.Point> training; 
	    	List<DataGenerator.Point> validation;
	    	training = datagenerator.getTrainingData();
	    	validation = datagenerator.getValidationData();
	    	
	        datagenerator.writeDataFile("training_data.txt", training);
	        datagenerator.writeDataFile("validation_data.txt", validation);
		}

        
		Loader loaderTrain = new Loader("training_data.txt");	
		loaderTrain.loadData(dataOutputs, dataInputs);
		
		Loader validationSet = new Loader("validation_data.txt");
		validationSet.loadData(dataOutputsValidation, dataInputsValidation);

		int[] layerLengths = new int[HIDDEN_LAYER_SIZES.length+2];
		for (int i = 0; i < HIDDEN_LAYER_SIZES.length; i++) 
		{
			layerLengths[i+1] = HIDDEN_LAYER_SIZES[i]; 
		}
		
		layerLengths[0] = INPUT_SIZE;
		layerLengths[layerLengths.length-1] = OUTPUT_SIZE;
		
		ModelMLP model = new ModelMLP( 3, layerLengths, ACTIVATION_FUNC, OUTPUT_ACTIVATION_FUNC, USE_MINIBATCHES,LOSSFUNCTION);
		for (Neuron n : model.getNeurons()) 
		{
			n.setLearningStep(LEARNING_STEP); 
		}
		
		float Error = 0;
		float Validation = 0;
		int epoch = 1;
		float previousError = Float.MAX_VALUE;
		
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("loss_values.txt"))) 
        {
        // Training loop
	        while (true) {
	        	Error = 0;
	
	        	int currentSample = 0; //The current sample of this epoch.
	        	
				while (currentSample < dataOutputs.size())
				{
	
					for (int j = 0; j < BATCH_SIZE; j++)
					{	
						if (currentSample == dataOutputs.size()) 
						{
							break; 
						}
						float[] input = dataInputs.get(currentSample);
						int target = dataOutputs.get(currentSample);
						float[] target_onehot = encodeToOneHot(target);
	 					
						model.forward(input);
						Error += model.backPropagation(target_onehot, false);
	
						currentSample++;
					}
	
					model.applyBatchMean();
				}
	
				float meanError = Error/(dataOutputs.size());
				System.out.println("epoch [ " + epoch +" ]" + "Loss : " + meanError);
                writer.write("\t" + meanError);
                writer.newLine();
				epoch++;
				//
				if (epoch > MAX_EPOCHS && Math.abs(meanError-previousError) < EPOCH_BREAK_POINT || epoch > 1500 ) { break; }
				previousError = meanError;
				Error = 0;
				
				if(epoch> 700) {
					for (Neuron n : model.getNeurons()) { n.setLearningStep(LEARNING_STEP/2);}
				}
	        }
        }catch (IOException e) {
            e.printStackTrace();
        }
        // testing process 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("visualize.txt"))) 
        {
        for(int i = 0 ; i <dataInputsValidation.size(); i++ ) {
        	float[] input = {0,0};
        	float[] target = {0,0,0,0};
        	int Value = -1; 
        	
        	input = dataInputsValidation.get(i);
        	target = encodeToOneHot(dataOutputsValidation.get(i));
        	for (int sample =0; sample<target.length; sample++ ) {
        		if(target[sample] == 1) {
        			Value = sample;
        			break;
        		}
        	}
        	model.forward(input);
        	int predicted_values = model.getPrediction();
            writer.write(Arrays.toString(input)+ ", Expected: " + Value + ", Guessed: " + predicted_values + "\n");
        	
        	if(predicted_values != Value) {
        		Validation++;
        	}        	
        }
        System.out.println(" end of final test with accuracy percentange " + (1.0f-(Validation/4000))*100 );
        }catch (IOException e) {
            e.printStackTrace();
        }
	}
}
