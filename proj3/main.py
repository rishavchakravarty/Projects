import sys


sbox = {0x0: 0x6, 0x1: 0x5, 0x2: 0x1, 0x3: 0x0, 0x4: 0x3, 0x5: 0x2, 0x6: 0x7, 0x7: 0x4}
#input value is the input integer, inputSum is the number for which specific bits are to be summed
def sum_bits(inputValue, inputSum):
    input_sum_binary = bin(inputSum)[2:]  # Binary representation of inputSum
    result = 0
    for i, bit in enumerate(reversed(input_sum_binary)):
        if bit == '1':
            result ^= (inputValue >> i) & 1  # Extract the corresponding bit from inputValue and XOR it
    return result
def create_linear_approximation_table(sbox):
    table = [[0 for _ in range(8)] for _ in range(8)]
    for input_sum in range(8):#select an input sum
        for output_sum in range(8):#select an output sum
            count = 0
            for input_value in range(8): #parse the sbox for all the input values
                output_value = sbox[input_value]
                if (sum_bits(input_value, input_sum) == sum_bits(output_value, output_sum)):#math on the individual bits
                    count += 1
            table[input_sum][output_sum] = count - 4  # Subtract 4 from NL(a, b)
    
    return table

# Create linear approximation table
linear_table = create_linear_approximation_table(sbox)



# Print the linear approximation table
print("Linear Approximation Table:")
print("   ", end="") 
for i in range(len(linear_table[0])):
    print("{: >3}".format(i), end=" ")
print() 


for i, row in enumerate(linear_table):
    print("{: >2} ".format(i), end="")  # Print row label
    for element in row:
        print("{: >3}".format(element), end=" ")  # Print table element
    print()


csv_string = ""
for row in linear_table:
    csv_string += ",".join(map(str, row)) + "\n"
with open("linear_approximation_table.csv", "w") as file:
    file.write(csv_string)

#---------- PART 2 ----------#

# Function to map a 4-bit mask to a 3-bit S-box index
def map_mask_to_sbox_index(mask):
    # Map the mask based on the specific bit positions used in the S-box
    sbox_index = 0
    if mask & 0b1000:  # P1
        sbox_index |= 0b001
    if mask & 0b0100:  # P2
        sbox_index |= 0b010
    if mask & 0b0010:  # P4 
        sbox_index |= 0b100
    return sbox_index

# Specify the masks for P1, P2, P4, P5 and H1
input_mask = 0b1101  # Mask for P1, P2, P4, P5
output_mask = 0b0001  # Mask for H1

# Map the masks to the S-box indexes
input_sbox_index = map_mask_to_sbox_index(input_mask)
output_sbox_index = map_mask_to_sbox_index(output_mask)

# Calculate the bias for the trail from P1, P2, P4, P5 to H1
bias = linear_table[input_sbox_index][output_sbox_index] / 8  # Get the actual bias

print(f"Linear approximation trail from P1, P2, P4, P5 to H1:")
print(f"Input Mask S-box Index: {input_sbox_index}")
print(f"Output Mask S-box Index: {output_sbox_index}")
print(f"Bias: {bias}")

#---------- PART 3 ----------#

# Function to find the best linear approximation trail with a non-zero bias
def find_best_linear_trail(linear_table):
    best_bias = 0
    best_input_mask = 0
    best_output_mask = 0
    
    # Iterate over all possible input and output mask combinations
    for input_mask in range(1, 8):  # start from 1 to avoid the all-zero mask
        for output_mask in range(1, 8):  # start from 1 to avoid the all-zero mask
            bias = linear_table[input_mask][output_mask] / 8
            # Check if the current bias is the largest one found so far
            if abs(bias) > abs(best_bias):
                best_bias = bias
                best_input_mask = input_mask
                best_output_mask = output_mask
    
    return best_input_mask, best_output_mask, best_bias

# Use the function to find the best trail
best_input_mask, best_output_mask, best_bias = find_best_linear_trail(linear_table)

print(f"Best Linear Approximation Trail: Input Mask = {best_input_mask}, Output Mask = {best_output_mask}, Bias = {best_bias}")


#---------- PART 4 ----------#

# Given plaintext and ciphertext pairs
pairs = [
    ("100111", "100100"),
    ("000111", "110010"),
    ("001100", "111001"),
    ("011000", "011101"),
    ("001000", "001101"),
    ("011010", "101001")
]

# Extract the bit value from a binary string at a specified index
def extract_bit(binary_string, index):
    return int(binary_string[index])

# Calculate the counter values for each subkey guess
def calculate_counters(pairs, input_mask, output_mask, sbox, linear_table):
    # Initialize counters for each subkey guess
    counters = {bin(subkey)[2:].zfill(3): 0 for subkey in range(8)}
    
    for subkey_guess in range(8):
        for plaintext, ciphertext in pairs:
            # Calculate input sum based on the input mask
            input_sum = sum((extract_bit(plaintext, i) for i in range(6) if input_mask & (1 << i)))
            
            # Retrieve output bit H1 from the ciphertext (assuming H1 is the first bit of the ciphertext)
            H1 = extract_bit(ciphertext, 0)
            
            # Simulate the output of the S-box by applying the subkey to the input sum
            # and then applying the S-box transformation
            sbox_output = sbox[input_sum ^ subkey_guess]
            
            # Calculate output sum based on the output mask
            output_sum = sum((extract_bit(format(sbox_output, '03b'), i) for i in range(3) if output_mask & (1 << i)))
            
            # XOR the input sum with the output sum and H1
            if (input_sum ^ output_sum ^ H1) == 0:
                counters[bin(subkey_guess)[2:].zfill(3)] += 1
    
    return counters

# We are using the best linear trail from Part 3
input_mask_for_best_trail = 6  # input mask for P1, P2, P4, P5
output_mask_for_best_trail = 4  # output mask for H1

# Calculate the counters using the best linear trail
counters = calculate_counters(pairs, input_mask_for_best_trail, output_mask_for_best_trail, sbox, linear_table)

# Print the counter values
print("Counter values for each subkey guess based on the best linear approximation trail:")
for subkey, count in counters.items():
    print(f"Subkey {subkey}: Counter Value = {count}")