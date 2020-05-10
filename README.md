#FastNQueens

For my algorithms class I made an algorithm to solve the N-Queens problem using backtracking, but at around n=32 or so it started to slow down to >5 mins. I decided to make a faster version without backtracking.

Basically I looked through my old deep learning and neural network notes and rediscovered the hill climbing method. I thought this would be easy to implement in the N-Queens problem since any board with less conflicts than the last would be a step towards a global maximum - which there are multiple, since a global max in this case would be a solved board.

After implementing everything, I was able to solve n=30 in about 0.2 seconds, n=40 in 0.2 seconds, n=100 in 1.3 seconds, and n=14 in about 15 seconds. These times seems to vary by up to about 400% since the randomness involved can get "unlucky" but I still found it fun to look at the configurations of boards for n>40!

I estimated that run time was probably around O(n^3)
