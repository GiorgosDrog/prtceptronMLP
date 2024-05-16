import json
import matplotlib.pyplot as plt
def plot_loss_values(file_path):
    epochs = []
    losses = []

    # Read data from the file
    with open(file_path, 'r') as file:
        for line in file:
            epoch, loss = map(float, line.split())
            epochs.append(epoch)
            losses.append(loss)

    # Plotting
    plt.plot(epochs, losses, linestyle='-', color='b')
    plt.title('Training Loss Over Epochs')
    plt.xlabel('Epoch')
    plt.ylabel('Loss')
    plt.grid(True)
    plt.show()

def plot_points(file_path):
    x1_values = []
    x2_values = []
    expected_values = []
    guessed_values = []

    # Read data from the file
    with open(file_path, 'r') as file:
        for line in file:
            line = line.strip()

            # Extract x1, x2, expected, and guessed values from the line
            try:
                # Split the line into parts
                parts = line.split(',')

                # Extract x1 and x2 values
                x1 = float(parts[0].split('[')[1].strip())
                x2 = float(parts[1].split(']')[0].strip())
                
                # Extract expected and guessed values
                expected = int(parts[2].split('Expected: ')[1])
                guessed = int(parts[3].split('Guessed: ')[1])
                
                # Append values to lists
                x1_values.append(x1)
                x2_values.append(x2)
                expected_values.append(expected)
                guessed_values.append(guessed)
            except (ValueError, IndexError):
                print(f"Ignoring line with invalid data: {line}")
                continue

    # Plotting
    for x1, x2, expected, guessed in zip(x1_values, x2_values, expected_values, guessed_values):

        if(expected == 0): #blue
            if(expected == guessed):
                plt.scatter(x1,x2,marker = '+',color = 'm')
            else:
                plt.scatter(x1,x2,marker = 'x',color = 'm')
                
        elif(expected == 1): #green
            if(expected == guessed):
                plt.scatter(x1,x2,marker = '+',color = 'b')
            else:
                plt.scatter(x1,x2,marker = 'x',color = 'b')
                
        elif(expected == 2): #red
            if(expected == guessed):
                plt.scatter(x1,x2,marker = '+',color = 'g')
            else:
                plt.scatter(x1,x2,marker = 'x',color = 'g')
                
        elif(expected == 3):  # yellow
            if expected == guessed:
                plt.scatter(x1, x2, marker='+', color='y')
            else:
                plt.scatter(x1, x2, marker='x', color='y')
        

    plt.title('Scatter Plot of Points')
    plt.xlabel('X1-axis')
    plt.ylabel('X2-axis')
    plt.legend()
    plt.grid(True)
    plt.show()


if __name__ == "__main__":
    #loss_file_path = "loss_values.txt"  
    visualaze = "visualize.txt" 
    # plot_loss_values(loss_file_path)
    plot_points(visualaze)