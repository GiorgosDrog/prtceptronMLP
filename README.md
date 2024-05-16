<H2>pertceptronMLP</H2> 
A program for classification based on multilayer perceptron(MLP). The program receives randomly points in the x,y plane for values from -1 to 1 respectively on each axis.

<H2>perceptronMLP</H2>
For the compilation process I use the following commands:
<b>javac Neuron.java ModelMLP.java Loader.java DataGenerator.java Main.java</b>
and to run the program we use: 
<b>java Main</b>
You can also run the file <PlotScript.py> which displays error drop during training, with the command:
<b> python3 PlotScript.py </b>

<H2> Create and load data </H2>

• Initially, for the creation of the training data, the DataGenerator class which generates the points in the plane with a random number generator. 
The data is then written to a .txt file and are categorized according to their future use. As a result we have two files training_data.txt and validation_data.txt
which will be used for his training and evaluation model.
• Next we created the Loader class which will read the data of the aforementioned files.

<H2>Creating a Nerve class and Architecture of the model.</H2>

To implement the Architecture of the model we used two classes the Nueron class and modelMLP.
• The Neuron class is the core element of the model. The objects neuron are responsible for the basic functions of the program with
these form the neural network which is then trained and evaluate. In this class, its weight tables are stored network which are then 
updated and produce the news Results.

• The modelMLP class composes the architecture of the network, it is responsible for the correct connection of the neurons of the network. 
In it, the basic training functions (forward, backpropagation) with which the network acquires the ability through the process of learning to
categorizes points in space.

<H2> Observations from running the program </H2>
During the execution of the algorithm we experimented with combinations of parameters we analyzed previously and arrived at the following
conclusions:
Initially an important role in terms of the correct approach to the problem played the size of the neural network. Which is defined by
size of the input, output and intermediate hidden layers. The results we got we evaluated them based on their accuracy results and the time 
it took the model to complete process of education

<Η2> Results </H2>
The best result after training was about 89.5% with size of the hidden layers at value of 10.
In the training section we create plots to visualize the drop of the training loss and after the training we evaluate the trained model with validation dataset

Here we monitor a screenshot of the last training epochs and in the end appears the evaluation results with percentage 89.7%
![Screenshot (1499)](https://github.com/GiorgosDrog/prtceptronMLP/assets/72260809/448277ee-4d23-43b7-81f7-f2b5f1e7e16d)

Below we compare the model's results with different hidden_sizes = 15 for blue line , 10 for orange line, 8 for grey line and 4 for yellow line 


