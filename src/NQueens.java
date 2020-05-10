import java.util.ArrayList;

public class NQueens {

    /*
    Thoughts/Notes:

        - Keeping track of a 2d array is much more expensive computationally than simply keeping track
        of the columns the queens are in, so if possible it would be better to have a 1D array of positions

        - Hill climbing would most likely be optimal here, since it's easier to find a local maximum multiple times,
        than it would be to spend a lot of time finding the global maximum through precise calculations. In other words,
        simply try to minimize conflicts with each queen

        - Can slightly speed up the process by initializing the board full of queens in random positions (only
        one per row, however)
     */


    public static void main(String[] args){
        //change this number to change number of queens
        int n = 400;

        //change the boolean if you want to display the board on a 2d grid
        solve(n, false);
    }





    //initialize the board in a random state with one queen per row
    public static void initializeBoard(Integer[] board){
        for(int i = 0; i < board.length; ++i){
            //zero index the queen positions
            board[i] = (int)(Math.random() * board.length);
        }
    }

    //translates the 1D board array into a 2d representation
    public static void printBoard(Integer[] board){
        for(int i = 0; i < board.length; ++i){
            for(int j = 0; j < board.length; ++j){
                //use dummy variable j to replicate empty spots
                //only print a 1 for a queen when j reaches the correct col
                if(j == board[i]) System.out.print(" 1");
                else System.out.print(" 0");
            }
            System.out.println();
        }

        //print the column numbers of the queens, 1-indexed
        String output = "(";
        for(int i = 0; i < board.length; ++i){
            output += (i == 0) ? (board[i] + 1) : " " + (board[i] + 1);
        }
        output += ")";
        System.out.println(output);
    }

    //only prints the columns of the solution, and not the 2d board
    public static void printBoardColsOnly(Integer[] board){
        //print the column numbers of the queens, 1-indexed
        String output = "(";
        for(int i = 0; i < board.length; ++i){
            output += (i == 0) ? (board[i] + 1) : " " + (board[i] + 1);
        }
        output += ")";
        System.out.println(output);
    }

    public static Integer[][] convertBoardTo2D(Integer[] board){
        Integer[][] boardOut = new Integer[board.length][board.length];
        for(int i = 0; i < board.length; ++i){
            for(int j = 0; j < board.length; ++j){
                if(j == board[i]) boardOut[i][j] = 1;
                else boardOut[i][j] = 0;
            }
        }
        return boardOut;
    }



    //returns number of queens that are threatened
    public static int getNumThreatenedQueens(Integer[] board){
        int threatenedQueens = 0;
        for(int i = 0; i < board.length - 1; ++i){
            for(int j = i + 1; j < board.length; ++j){

                //check if any queens are on the same col
                if(board[i] == board[j]){
                    ++threatenedQueens;
                    continue;
                }

                //check y = -x diagonal
                if(board[i] - board[j] == i - j){
                    ++threatenedQueens;
                    continue;
                }

                //check y = x diagonal
                if(board[i] - board[j] == j - i){
                    ++threatenedQueens;
                    continue;
                }
            }
        }
        return threatenedQueens;
    }

    //generates new board from current state with either the same amount of threatened queens, or less
    public static Integer[] generateNewBoard(Integer[] board){
        ArrayList<Integer> choice = new ArrayList<Integer>();
        int score;
        int numThreatenedQueens = getNumThreatenedQueens(board);
        int k;

        Integer[] boardOut = new Integer[board.length];

        //initialize output to a copy of board
        for(int i = 0; i < board.length; ++i){
            boardOut[i] = board[i];
        }

        for(int i = 0; i < board.length; ++i){
            choice.clear();

            choice.add(boardOut[i]);

            //initialize temp to be the value in board at current index
            int temp = boardOut[i];

            //move the queen in the current row to each column space and see if it
            //decreases the number of threatened queens
            for(int j = 0; j < board.length; ++j){
                boardOut[i] = j;
                k = getNumThreatenedQueens(boardOut);

                //if the new setup has the same number of threatened queens, add it to choices
                if(k == numThreatenedQueens) choice.add(j);

                //if the new setup has less threatened queens than before, clear previous choices,
                //add it to the array list, and set the new number to compare to to be the newly found k.
                //This is what minimizes the number of conflicts on the board
                if(k < numThreatenedQueens){
                    choice.clear();
                    choice.add(j);
                    numThreatenedQueens = k;
                }
            }
            //randomly select a viable position (col) form the list of possible positions which do not increase
            //the number of threatened queens, and set the queen in the current row to this col
            //aka set the current queen to the local "maximum" we just found. This is the hill climbing portion,
            //because you are randomly taking a step in the right direction instead of carefully calculating the
            //BEST possible step!
            boardOut[i] = choice.get((int)(Math.random() * choice.size()));
        }
        return boardOut;
    }

    //uses generateNewBoard to find a new possible state.
    //if this new state has less threatened queens, then replace current state with this new one
    public static boolean findNextState(Integer[] board){
        int currNumThreatenedQueens = getNumThreatenedQueens(board);
        Integer[] tempBoard = new Integer[board.length];

        //set the new board to a viable next state using generateNewBoard
        tempBoard = generateNewBoard(board);

        //if this new state has less threatened queens, replace current state with this new one
        if(getNumThreatenedQueens(tempBoard) < currNumThreatenedQueens){
            for(int i = 0; i < board.length; ++i){
                board[i] = tempBoard[i];
            }
            return true;
        }

        //if current state has equal number of threatened queens, return false
        return false;
    }

    //the main function that ties everything together!
    public static void solve(int n, boolean board2d){
        System.out.println("Solving...\n");
        long startTime = System.nanoTime();
        Integer[] board = new Integer[n];

        //initialize the board
        initializeBoard(board);

        //while the number of threatened queens is not 0, randomly take steps towards max
        while(getNumThreatenedQueens(board) != 0){
            if(!findNextState(board)){
                //if no next state could be found, reset all queens to random positions and start over
                //this means you've found a dead end, or a local max and not one of the global maximums -
                //a solved board
                initializeBoard(board);
            }
        }

        System.out.println("Solved board:\n");
        if(board2d) printBoard(board);
        else printBoardColsOnly(board);

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Time: " + totalTime/1e9 + " seconds");
        System.out.println("Note: Time can vary by a large % as there is randomness involved in the hill climb method implemented.");

    }




}
