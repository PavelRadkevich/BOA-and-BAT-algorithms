The program contains:
1. a BOA algorithm (Butterfly algorithm), with built-in Levy Flight instead of random walk, and with a genetic evolution algorithm after each iteration
2. BOA (Butterfly algorithm), the same as in the first point, but instead of moving towards the best individual, the movement is towards the individual that made the best move in the last iteration.
3. BAT algorithm

The program analyzes how changes in parameters will affect the result. It generates an EXCEL file (data.xlsx) with the results for each parameter value, as well as a graph (/wykresy) for each parameter showing the changes visually.

Six functions are analyzed:
1. Sphere
2. Ackley
3. Cigar
4. Schwefel 2.22
5. Zakharov
6. Step