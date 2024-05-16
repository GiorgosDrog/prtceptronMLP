
import java.util.ArrayList;


public class ModelMLP {
	
	//layers of the model architecture 3 hidden 1 input 1 output
	private ArrayList<Neuron>[] model_layers; 
	//list of the neurons of the model
	private ArrayList<Neuron> neurons = new ArrayList<Neuron>();
	// desired output 
	private float[] targetData; 
	// loss
	private String lossFunction;
	
	private float loss = -1; // forward total error/cost function
	
	public ModelMLP(int hiddenLayers, int[] neuronsNum,String hiddenLayerActivationFunc,String outputLayerActivationFunc, boolean Mini_batch,String lossFunction)
	{
		int id = 0; //ID. An incremental int that gives a unique identifier to each neuron.
		this.setLossFunction(lossFunction);
		this.model_layers = new ArrayList[hiddenLayers+2];
		
		for (int i = 0; i < hiddenLayers+2; i++) { // +2 unsure that we will include the input and output_layer 
			model_layers[i] = new ArrayList<Neuron>();
			
			for (int j  = 0 ; j< neuronsNum[i]; j++ ) 
			{	
				int inputSize = i == 0 ? 1 : neuronsNum[i - 1];
			    int outputSize = i == hiddenLayers + 1 ? 1 : neuronsNum[i + 1];
			    String activationFunction = i == hiddenLayers + 1 ? outputLayerActivationFunc : hiddenLayerActivationFunc;
			    
			    String nueron_type = "undefine";
			    if( i == 0) {
			    	nueron_type = "input";	
			    }
			    else if( i == hiddenLayers +1 ) {
			    	nueron_type = "output";
			    }else {
			    	nueron_type = "hidden";
			    }
//			    System.out.println(id);
			    Neuron neuron = new Neuron(id,activationFunction,inputSize,outputSize,nueron_type,Mini_batch);
			    if(i != 0 ) {
			    	for( Neuron n : model_layers[i-1]) {
			    		n.addForwardConnection(neuron);
			    		neuron.addWeightConnection(n);
			    	}
			    }
			    
			    model_layers[i].add(neuron);
			    neurons.add(neuron);
			    id++;
			}
		}
	}
	
	public float[] forward(float[] input){
		insertInput(input);

	    // Forward pass through each layer and neuron
		for (int i = 0; i < this.model_layers.length; i++)
		{
			for (Neuron n : this.model_layers[i])
			{
				n.forward();
			}
		}
		
		float[] output = new float[this.model_layers[this.model_layers.length-1].size()];
		for (int i = 0; i < this.model_layers[this.model_layers.length-1].size(); i++) 
		{ 
			output[i] = this.model_layers[this.model_layers.length-1].get(i).getOutput(); 
		}
		return output;
		
	}
	
	private void insertInput(float[] input) {
		for (Neuron neuron : this.neurons) {
			neuron.reset();
		}
		for (int i = 0; i < input.length; i++)
		{
			this.model_layers[0].get(i).getInputs().add(input[i]);
		}
	}
	
	public float backPropagation(float[] target, boolean applyWeights) 
	{
	    this.targetData = target;
	    //first step of backpropagate to calculate the loss.
	    //use MSE loss 
	    //Extensibility cross entropy or similar??
	    
	    float output = calculateOutputErrorMSE(target);
	    
	    for (int i = model_layers.length - 1; i >= 0; i--) {
			for (int j = 0; j < model_layers[i].size(); j++)
			{
				float fixedAnswer = i == model_layers.length-1 ? target[j] : 0;
				model_layers[i].get(j).backpropagate(fixedAnswer);
			}
	    }

	    if (applyWeights) {
	        applyBatchMean();
	    }

	    return output;
	}

	public void applyBatchMean()
	{
		for (Neuron n : neurons)
		{
			n.applyBatchMean();
		}
	}

	private float calculateOutputErrorMSE(float[] target) 
	{
	    
		float outputError = 0;

	    for (int i = 0; i < target.length; i++) {
	        float neuronOutput = model_layers[model_layers.length - 1].get(i).getOutput();
	        outputError += Math.pow(neuronOutput - target[i], 2);
	        
	    }

	    return outputError;
	}
	
	public float[] getTargetData() {
		return targetData;
	}

	public void setTargetData(float[] targetData) {
		this.targetData = targetData;
	}
	
	public int getPrediction()
	{
		float val = Float.NEGATIVE_INFINITY;
		int out = -1;
		for (int i = 0; i < model_layers[model_layers.length-1].size(); i++)
		{
			if (val < model_layers[model_layers.length-1].get(i).getOutput())
			{
				val = model_layers[model_layers.length-1].get(i).getOutput();
				out = i;
			}
		}
		return out;
	}
	
	public float getLoss() {
		return loss;
	}

	public ArrayList<Neuron> getNeurons() {
		return neurons;
	}
	
	public String getLossFunction() {
		return lossFunction;
	}

	
	public void setLossFunction(String lossFunction) {
		this.lossFunction = lossFunction;
	}
	
}
