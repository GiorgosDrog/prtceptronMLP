

import java.util.ArrayList;
import java.util.Random;

public class Neuron {

    private int id;
	private float[] weights;
    private float bias;
    private int[] IdList;
    private ArrayList<Float> inputs=new ArrayList<Float>();;
    private float unactivatedOutput;
    private float output;
	private Neuron[] forwardConnections;
    private String neuronType;
    private String activationFunction;
    private float InputGradient;
    private float learningStep = 1f;
	private boolean mini_batch;
    private ArrayList<Float>[] batchWeights;

    @SuppressWarnings("unchecked")
	public Neuron(int id, String activationFunction, int inputSize, int outputSize, String neuronType, boolean mini_batch) {
        this.id = id;
        this.neuronType = neuronType;
        this.mini_batch = mini_batch;
        this.activationFunction = activationFunction;
        
        forwardConnections = new Neuron[outputSize];
        weights = new float[inputSize];
        IdList = new int[inputSize];
        bias = new Random().nextFloat() * 2 - 1;
        setInputs(new ArrayList<>());
        
        
        for (int i = 0; i < IdList.length; i++) {
        	IdList[i] = -1;
        }
        
        if (mini_batch) 
        {
            batchWeights = new ArrayList[inputSize + 1]; // maybe causes problem in casting to float 

        }
        for (int i = 0; i < batchWeights.length; i++) {
            batchWeights[i] = new ArrayList<>();
        }
        
        initializeWeights();
    }

    private void initializeWeights() {
        Random random = new Random();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = random.nextFloat() * 2 - 1;
        }
    }

    public void addForwardConnection(Neuron neuron) {
        for (int i = 0; i < this.forwardConnections.length; i++) {
            if (this.forwardConnections[i] == null) {
                this.forwardConnections[i] = neuron;
                break;
            }
        }
    }
    
    
    public void addWeightConnection(Neuron neuron) {
        // Find the first available slot in the IdList array
        for (int i = 0; i < IdList.length; i++) {
            if (IdList[i] == -1) {
                IdList[i] = neuron.id;
                break;
            }
        }
    }
    
        
    public void forward() {
    	calculateOutput();
    	applyActivationFunction();
    	passOutputToNextLayer();
    }

    private void calculateOutput() {
        output = 0;
        for (int i = 0; i < weights.length; i++) 
        {
            output += inputs.get(i) * weights[i];
        }
        output += bias;
        unactivatedOutput = output;
    }

    private void applyActivationFunction() {
        if ("output".equals(neuronType)) {
            output = activationSigmoid(output);
            return;
        } else if ("relu".equals(activationFunction)) {
            output = activationRELU(output);
        } else if ("tanh".equals(activationFunction)) {
            output = activationTANH(output);
        }else if ("sigmoid".equals(activationFunction)) {
        	output = activationSigmoid(output);
        }
    }

    private void passOutputToNextLayer() {
        if (neuronType != "output") {
            for (Neuron nextNeuron : forwardConnections) {
                nextNeuron.inputs.add(output);
            }
        }
    }
    
    public void backpropagate(float target) {
        if ("output".equals(neuronType)) {
            for (int i = 0; i < weights.length; i++) {
                calculateOutputLayerDelta(target);
                float direction = inputs.get(i);
                float dCiDirection = direction * InputGradient;
                updateWeights(i, dCiDirection);
            }
            updateBias(InputGradient);
        } else {
            for (int i = 0; i < weights.length; i++) {
                calculateHiddenLayerDelta();
                float direction = inputs.get(i);
                float dCiDirection = direction * InputGradient;
                updateWeights(i, dCiDirection);
            }
            updateBias(InputGradient);
        }
    }

    private void calculateOutputLayerDelta(float target) {
        InputGradient = derivativeSigmoid(unactivatedOutput) * 2 * (output - target);
    }

    private void calculateHiddenLayerDelta() {
        float dCo = calculateDco();
        
        if ("relu".equals(activationFunction)) {
            InputGradient = derivativeRELU(unactivatedOutput);
        } else if ("tanh".equals(activationFunction)) {
            InputGradient = derivativeTANH(unactivatedOutput);
        }else if ("sigmoid".equals(activationFunction)) {
            InputGradient = derivativeSigmoid(unactivatedOutput);
        }
        InputGradient *= dCo;
    }

    private void updateWeights(int i, float dCiDirection) {
        if (mini_batch) {
            batchWeights[i].add(-learningStep * dCiDirection);
        } else {
            weights[i] -= learningStep * dCiDirection;
        }
    }

    private void updateBias(float dCi) {
        float biasUpdate = mini_batch ? -learningStep * dCi : learningStep * dCi;
        if (mini_batch) {
            batchWeights[batchWeights.length - 1].add(biasUpdate);
        } else {
            bias -= biasUpdate;
        }
    }

    private float calculateDco() {
        float dCo = 0;
        for (Neuron n : forwardConnections) {
            int pos = findPositionInIdList(n);
            dCo += n.InputGradient * n.weights[pos];
        }
        return dCo;
    }

    private int findPositionInIdList(Neuron n) {
        for (int k = 0; k < n.IdList.length; k++) {
            if (id == n.IdList[k]) {
                return k;
            }
        }
        return -1;
    }


    public void applyBatchMean() {
        if (!mini_batch) {
            return;
        }

        for (int i = 0; i < weights.length + 1; i++) {
            float sum = calculateSumOfBatchWeights(i);
            float mean = sum / batchWeights[i].size();
            applyMeanToWeightsOrBias(i, mean);
            batchWeights[i].clear();
        }
    }

    private float calculateSumOfBatchWeights(int index) {
        float sum = 0;
        for (float weightUpdate : batchWeights[index]) {
            sum += weightUpdate;
        }
        return sum;
    }

    private void applyMeanToWeightsOrBias(int index, float mean) {
        if (index == weights.length) {
            bias += mean;
        } else {
            weights[index] += mean;
        }
    }

    
    // --------- Sigmoid -------------------->
    private float activationSigmoid(float x) {
        return (float) (1 / (1 + Math.exp(-x)));
    }

    private float derivativeSigmoid(float x) {
        return activationSigmoid(x) * (1 - activationSigmoid(x));
    }
    // --------- Relu -------------------->
    private float activationRELU(float x) {
        return x > 0 ? x : 0;
    }

    private float derivativeRELU(float x) {
        return x > 0 ? 1 : 0;
    }
    // --------- TANH -------------------->
    private float activationTANH(float x) {
        return (float) Math.tanh(x);
    }

    private float derivativeTANH(float x) {
        return (float) (1 - Math.pow(Math.tanh(x), 2));
    }

    public void reset() {
        getInputs().clear();
    }
    
    public float getOutput() {
		return output;
	}
    

	public ArrayList<Float> getInputs() {
		return inputs;
	}

	
	public void setInputs(ArrayList<Float> inputs) {
		this.inputs = inputs;
	}
    
	public int getId() {
		return id;
	}
	
    public float getLearningStep() {
		return learningStep;
	}

	public void setLearningStep(float learningStep) {
		this.learningStep = learningStep;
	}

}
