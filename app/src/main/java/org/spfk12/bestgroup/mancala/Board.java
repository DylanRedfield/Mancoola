package org.spfk12.bestgroup.mancala;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

public class Board {
    private int mBoard[][];
    private int mPlayerOneScore;
    private int mPlayerTwoScore;
    private int mNumStones;
    private int mPlayerTurn;
    private GridView mGrid;
    private TextView mPlayerOneScoreText;
    private TextView mPlayerTwoScoreText;
    private Context mContext;

    public Board(int numStones, int playerTurn, Context c, GridView grid, TextView playerOne,
                 TextView playerTwo) {
        mBoard = new int[2][6];
        mNumStones = numStones;
        mPlayerOneScore = 0;
        mPlayerTwoScore = 0;
        mContext = c;
        mGrid = grid;
        mPlayerOneScoreText = playerOne;
        mPlayerTwoScoreText = playerTwo;
        mPlayerTurn = playerTurn;

        fillDefaultValues(mNumStones);

    }

    public void fillDefaultValues(int numStones) {
        for (int i = 0; i < mBoard.length; i++) {
            for (int j = 0; j < mBoard[i].length; j++) {
                mBoard[i][j] = numStones;
            }
        }
    }

    public void updateDisplay() {

        mGrid.setAdapter(new GridAdapter(mContext, mBoard, mPlayerTurn));
        mGrid.setBackgroundColor(Color.BLACK);
        mGrid.setVerticalSpacing(1);
        mGrid.setHorizontalSpacing(1);

        mPlayerOneScoreText.setText("" + mPlayerOneScore);
        mPlayerTwoScoreText.setText("" + mPlayerTwoScore);
/*        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    public void restartGame() {
        fillDefaultValues(mNumStones);
        mPlayerOneScore = 0;
        mPlayerTwoScore = 0;
        mPlayerTurn = 1;

        updateDisplay();
    }

    public void move(int spot, int player) {
        int[] array = matrixToArray(mBoard);

        int previousCellCounter = 0;
        int startSpot = spot;
        boolean playerOneScored = false;
        int stoneCounter = array[spot];
        int temp = 0;
        array[spot] = 0;
        int cellCounter = spot;
        boolean isStartUnder;
        boolean getOppositeCheck = false;
        boolean stopStartUnder = false;
        boolean score1 = false;
        boolean score2 = false;
        int scoreCount = 0;

        // Creates counter for stones
        while (stoneCounter > 0) {

            // Checks if the initial value is on the bottom row
            isStartUnder = startSpot > 5;

            // Needs to make sure if going form bottom to top to stop the startUnderCheck
            if (stopStartUnder) {
                isStartUnder = false;
            }

            // If the previous cell is the first cell of the bottom row needs to
            // increment the last of the first row
            previousCellCounter = cellCounter;
            cellCounter++;

            if (isStartUnder) {
                // Needs to reverse first counter
                cellCounter -= 2;
            }

            // Ladder to loop through all elemets of the array
            cellCounter %= 12;
            temp = cellCounter;

            //Used to loop right to left on bottom
            if (cellCounter > 5 && !isStartUnder) {
                temp = cellCounter;
                cellCounter = getOpposite(cellCounter);
                getOppositeCheck = true;
                score1 = true;
                Log.d("score", "1");
            }

            // if it was the last spot, tells method to increment first of the last spot
            // and make sure method knows it isnt startUnder
            if (previousCellCounter == 6 && !getOppositeCheck) {
                cellCounter = 0;
                temp = 0;
                stopStartUnder = !stopStartUnder;
                score2 = true;
                Log.d("score", "2");
            }
            array[cellCounter]++;
            cellCounter = temp;
            stoneCounter--;

        }
        mBoard = arrayToMatrix(array);
        if (score1 && score2) {

            Log.d("score", "3");
            if (player == 1) {
                mPlayerTwoScore++;
                Log.d("score", "qqqq" + player);
            }
            if (player == 2) {
                mPlayerOneScore++;
                Log.d("score", "" + player);
            }
        }
        updateDisplay();

    }

    public int getOpposite(int spot) {
        switch (spot) {
            case 6:
                return 11;
            case 7:
                return 10;
            case 8:
                return 9;
            case 9:
                return 8;
            case 10:
                return 7;
            case 11:
                return 6;
            default:
                return -1;
        }
    }

    public int[][] arrayToMatrix(int[] array) {
        int counter1 = 0;
        int counter2 = 0;
        int[][] matrix = new int[2][6];

        for (int i = 0; i < array.length; i++) {

            // has x counter loop only thrugh 6
            counter2 = i - counter1 * 6;
            matrix[counter1][counter2] = array[i];
            if (i > 4) {
                counter1 = 1;
            }
        }
        return matrix;
    }

    public int[] matrixToArray(int[][] array) {
        int counter = 0;
        int[] tempArray = new int[12];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                tempArray[counter] = array[i][j];
                counter++;
            }
        }
        return tempArray;
    }

    public void moveJ(int spot, int player) {
        int mover = 1;
        boolean flag = false;

        if (player == 2)
            spot = player2Index(spot);

        int counter = mBoard[player % 2][spot - 1];

        mBoard[player % 2][spot - 1] = 0;
        updateDisplay();

        while (counter > 0) {
            if (player == 1) {
                if (spot < mBoard[1].length) {
                    mBoard[1][spot] += 1;
                    //updateDisplay();
                    counter--;
                    spot++;
                    // checkRake();
                } else if (spot == mBoard[1].length)
                    player = 2;
            }

            if (player == 2) {
                if (spot > 0) {
                    if (mBoard[0][spot - 1] == 0 && flag == false) {
                        spot--;
                        flag = !flag;
                        // checkRank();
                    } else {
                        mBoard[0][spot - 1] += 1;
                        //updateDisplay();
                        spot--;
                        counter--;
                    }
                } else if (spot == 0)
                    player = 1;
            }
        }
        updateDisplay();
    }

    private int player2Index(int spot) {
        if (spot == 1)
            return 6;

        else if (spot == 2)
            return 5;

        else if (spot == 3)
            return 4;

        else if (spot == 4)
            return 3;

        else if (spot == 5)
            return 2;

        else
            return 6;
    }

    private void checkRake(int finalSpot, int player)//to be implemented in the move class
    {
        if (player == 1)
            if (mBoard[0][finalSpot] == 1 && mBoard[1][finalSpot] > 0) {
                mBoard[0][finalSpot] += mBoard[1][finalSpot];
                mBoard[1][finalSpot] = 0;
            }

        if (player == 2)
            if (mBoard[1][finalSpot] == 1 && mBoard[0][finalSpot] > 0) {
                mBoard[1][finalSpot] += mBoard[0][finalSpot];
                mBoard[0][finalSpot] = 0;
            }
    }

    private void goAgain(int finalCol, int player) {
        if (player == 1)
            if (finalCol == 0) {
                System.out.println("Enter the next spot to move");
                //import scanner
                //x=s.nI
                //move(x)
            }

        if (player == 2)
            if (finalCol == 1) {
                System.out.println("Enter the next spot to move");
                //x=s.nI
                //move(x)
            }
    }

    public int[][] getBoard() {
        return mBoard;
    }

}
