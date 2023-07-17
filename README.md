# Non-Recursive-predictive-parsing
Project made for PREDICTIVE PARSING analysis


## The Problem

For the implementation of this program, we need it to:

  - Take a grammar as input and test the grammar to check if it is LL.
  - Create a First and Follow set of the grammar
  - Build the tabular predictive analysis table and display it on the screen
  - Perform the analysis simulation that checks whether a typed sentence is recognized or not, demonstrating the recognition step by step (stack automaton controlled by the analysis table)
  - Have a maximum number of 8 productions for each grammar

## Implementation

For better understanding and use of the program, the following rules will be used when running the code:

   - To enhance the user experience and make the program more complete and complex, the code is implemented so that the user enters the terminal and non-terminal symbols as well as their derivations
   - Non-terminals are represented by uppercase letters
   - Terminals can only be lowercase letters or digits
   - The letter E represents the empty word and cannot be part of the non-terminals
   - For the development of this program, we chose to use the Java programming language.
   - In this report, we will explain the implementation of the main methods. For more detailed information, please read the code comments.
