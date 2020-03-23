import java.util.Scanner;

public class ConnectFour {


    public static void main(String[] args) {
        final char PLAYER ='O';
        final char CPU = 'X';

        char gameBoard[][] = {{' ','|',' ','|',' ','|',' ','|',' ','|',' ','|',' '},
                {'-','+','-','+','-','+','-','+','-','+','-','+','-'},
                {' ','|',' ','|',' ','|',' ','|',' ','|',' ','|',' '},
                {'-','+','-','+','-','+','-','+','-','+','-','+','-'},
                {' ','|',' ','|',' ','|',' ','|',' ','|',' ','|',' '},
                {'-','+','-','+','-','+','-','+','-','+','-','+','-'},
                {' ','|',' ','|',' ','|',' ','|',' ','|',' ','|',' '},
                {'-','+','-','+','-','+','-','+','-','+','-','+','-'},
                {' ','|',' ','|',' ','|',' ','|',' ','|',' ','|',' '},
                {'-','+','-','+','-','+','-','+','-','+','-','+','-'},
                {' ','|',' ','|',' ','|','X','|',' ','|',' ','|',' '},
                {'1',' ','2',' ','3',' ','4',' ','5',' ','6',' ','7'}};

        printGameBoard(gameBoard);
        System.out.println(checkScore(gameBoard, 'O'));
        while(true){
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter the row you'd like to place your disk! (1-7)");
            int playerRow;
            try {
                playerRow = scan.nextInt();
                if(!placeDisk(gameBoard, PLAYER, playerRow)){
                    System.out.println("Please select another row!");
                    continue;
                }
            }
            catch(Exception e){
                System.out.println("Please enter a number from 1-7!");
                continue;
            }

            if(checkScore(gameBoard, PLAYER)==4) {
                printGameBoard(gameBoard);
                System.out.println("Congratulation, you win!");
                break;
            }

            if(gameTie(gameBoard)){
                printGameBoard(gameBoard);
                System.out.println("The game is tied!");
                break;
            }

            char[][] copyGameBoard = new char[gameBoard.length][gameBoard[0].length];
            for(int row=0; row<gameBoard.length; row++){
                for(int col=0; col<gameBoard[row].length; col++){
                    copyGameBoard[row][col] = gameBoard[row][col];
                }
            }

            int CPURow = bestMove(copyGameBoard, 8);
            System.out.println("CPU placed on "+CPURow+"th row");
            placeDisk(gameBoard, CPU, CPURow);
            printGameBoard(gameBoard);

            if(checkScore(gameBoard, CPU)==4){
                System.out.println("sorry, you lost... good luck next time");
                break;
            }
        }
    }

    //Prints the gameBoard
    public static void printGameBoard(char[][] gameBoard){
        for(char[] row : gameBoard){
            for (char col : row){
                System.out.print(col);
            }
            System.out.println();
        }
    }

    public static boolean colAvailable(char[][] gameBoard, int col){
        for (int i= 0; i<gameBoard.length-1 ; i+=2){
            if (gameBoard[i][(col-1)*2]==' ') {
                return true;
            }
        }
        return false;
    }

    public static boolean gameTie(char[][] gameBoard){
        for(int i=1; i<=gameBoard[0].length; i++){
            if (colAvailable(gameBoard, i))
                return false;
        }
        return true;
    }

    //Places the disk on the gameBoard. Return true if successfully placed, return false other wise.
    public static boolean placeDisk(char[][] gameBoard, char disk, int col){
        for (int i= gameBoard.length-2; i>=0 ; i-=2){
            if (gameBoard[i][(col-1)*2]==' '){
                gameBoard[i][(col-1)*2] = disk;
                return true;
            }
        }
        return false;
    }

    public static void displaceDisk(char[][] gameBoard, int col){
        for (int i=0; i<gameBoard.length; i+=2){
            if (gameBoard[i][(col-1)*2]!=' '){
                gameBoard[i][(col-1)*2] = ' ';
                break;
            }
        }
    }

    public static  int checkScore(char[][] gameBoard, char disk){
        int highest=0;
        for (int row=0; row < gameBoard.length; row+=2){
            for (int col=0; col < gameBoard[row].length; col+=2){
                boolean hori = col+6<gameBoard[0].length;
                boolean vert = row+6<gameBoard.length;
                boolean diag1 = row+6<gameBoard.length && col+6<gameBoard[0].length;
                boolean diag2 = row+6<gameBoard.length && col-6>=0;
                int[] scores = {0,0,0,0};
                for(int index=0; index<=6; index+=2){
                    if(hori){
                        if(gameBoard[row][col+index]==disk)
                            scores[0]++;
                        else if (gameBoard[row][col+index]!=' ')
                            scores[0]-=2;
                    }
                    if(vert){
                        if(gameBoard[row+index][col]==disk)
                            scores[1]++;
                        else if (gameBoard[row+index][col]!=' ')
                            scores[1]-=2;
                    }
                    if(diag1){
                        if(gameBoard[row+index][col+index]==disk)
                            scores[2]++;
                        else if (gameBoard[row+index][col+index]!=' ')
                            scores[2]-=2;
                    }
                    if(diag2){
                        if(gameBoard[row+index][col-index]==disk)
                            scores[3]++;
                        else if (gameBoard[row+index][col-index]!=' ')
                            scores[3]-=2;
                    }
                    int high = 0;
                    for (int k=0; k<scores.length; k++){
                        high = Math.max(high, scores[k]);
                    }
                    highest = Math.max(highest, high);
                }
                if (highest==4){
                    return highest;
                }
            }
        }
        return highest;
    }


    public static int evalGameBoard(char[][] gameBoard){
        int playerScore = checkScore(gameBoard, 'O');
        int CPUScore = checkScore(gameBoard, 'X');
        if(CPUScore == 4){
            return 100;
        }
        else if(playerScore == 4){
            return -100;
        }
        return CPUScore-10*playerScore;
    }

    public static int miniMax(char[][] gameBoard, int depth, boolean maximizePlayer, int alpha, int beta){
        int num_col = gameBoard[0].length/2+1;
        if(depth==0 || checkScore(gameBoard, 'O')==4 || checkScore(gameBoard, 'X')==4){
            return evalGameBoard(gameBoard);
        }

        if(maximizePlayer){
            int maxEval  = (int)Double.NEGATIVE_INFINITY;
            for (int i=1; i<=num_col; i++){
                if(colAvailable(gameBoard,i)) {
                    placeDisk(gameBoard, 'X', i);
                    int eval = miniMax(gameBoard, depth - 1, false, alpha, beta);
                    displaceDisk(gameBoard, i);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, maxEval);
                    if(alpha>=beta)
                        break;
                }
            }
            return maxEval;
        }

        else{
            int minEval = (int)Double.POSITIVE_INFINITY;
            for(int i=1; i<=num_col; i++){
                if(colAvailable(gameBoard, i)){
                    placeDisk(gameBoard, 'O', i);
                    int eval = miniMax(gameBoard, depth-1, true, alpha, beta);
                    displaceDisk(gameBoard, i);
                    minEval = Math.min(minEval, eval);
                    beta= Math.max(alpha, minEval);
                    if(alpha>=beta)
                        break;
                }
            }
            return minEval;
        }
    }

    public static int bestMove(char[][] gameBoard, int depth){
        int MIN = -2000;
        int MAX = 2000;
        int bestMove = 0;
        int bestScore = (int)Double.NEGATIVE_INFINITY;
        int num_col = gameBoard[0].length/2+1;
        for(int i=1; i<=num_col; i++){
            if(colAvailable(gameBoard, i)){
                placeDisk(gameBoard, 'X', i);
                int score = miniMax(gameBoard, depth, false, MIN, MAX) - Math.abs(4-i);
                displaceDisk(gameBoard, i);
                if (score > bestScore){
                    bestScore = score;
                    bestMove = i;
                }
                System.out.println("Row"+i+" Score: "+score);
            }
        }
        return bestMove;
    }
}



