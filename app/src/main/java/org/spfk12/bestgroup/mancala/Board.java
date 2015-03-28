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

    public boolean moveJ(int spot, int player) {
        int startPlayer = player;
        boolean flag = false;
        boolean returnStatement = false;

        if (player == 2)
            spot = player2Index(spot);
        // Checks if it's Player's 2 turn so it can change the spot appropriately
        // (Player 1 = Player 2) 1 = 6, 2 = 5, 3 = 4, 4 = 3, 5 = 2, 6 = 1
        int counter = mBoard[player % 2][spot - 1];                // Sets counter equal to number of stones in that spot

        mBoard[player % 2][spot - 1] = 0;

        while (counter > 0) {
            if (player == 1) {
                if (spot < mBoard[1].length) {
                    mBoard[1][spot] += 1;
                    checkRake(spot, player);
                    counter--;
                    spot++;
/*                    if (checkRake(spot, startPlayer) == true) {
                        //goAgain(spot, player);
                        //returnStatement = true;
                        Log.d("go again", "" + player);
                    }*/
                    updateDisplay();
                } else if (spot == mBoard[1].length) {
                    mPlayerOneScore++;
                    player = 2;        // Loop up to Player 2's row
                }
            }

            if (player == 2) {
                if (spot > 0) {
                    if (mBoard[0][spot - 1] == 0 && flag == false) // Makes sure it doesn't loop over the same spot twice
                    {
                        spot--;
                        flag = !flag;
                    } else {
                        mBoard[0][spot - 1] += 1;
                        checkRake(spot, player);
                        spot--;
                        counter--;

                        updateDisplay();
                    }
                } else if (spot == 0) {
                    mPlayerTwoScore++;
                    //score2 = true;
                    player = 1;        // Loop down to Player 1's row
                }
            }
        }
        return returnStatement;
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

        else if (spot == 6)
            return 1;
        else {
            return -10;
        }
    }

    private boolean checkRake(int finalSpot,int player)//to be implemented in the move class
    {
        Log.d("rake", "spot: " + finalSpot + " Player: " + player);
        int oppositeSpot = 0;
        if(player==1) {
            oppositeSpot = rakeConvert(player, finalSpot);
            if (mBoard[0][finalSpot] == 1 && mBoard[1][finalSpot] > 0) {
                mBoard[0][finalSpot] += mBoard[1][finalSpot];
                mBoard[1][finalSpot] = 0;
                return true;
            }
        }

        if(player==2) {
            oppositeSpot = rakeConvert(player, finalSpot);
            if (mBoard[1][finalSpot] == 1 && mBoard[0][finalSpot] > 0) {
                mBoard[1][finalSpot] += mBoard[0][finalSpot];
                mBoard[0][finalSpot] = 0;
                return true;
            }
        }

        return false;
    }
        private int rakeConvert(int player, int spot) {
            if(player == 1) {
                switch (spot) {
                    case 5:
                        return 1;
                    case 4:
                        return 2;
                    case 3:
                        return 3;
                    case 2:
                        return 4;
                    case 1:
                        return 5;
                    case 0:
                        return 6;
                    default:
                        return -1;

                }
            }
            if(player == 2) {
                switch (spot) {
                    case 1 : return 5;
                    case 2: return 4;
                    case 3: return 3;
                    case 4: return 2;
                    case 5: return 1;
                    case 6: return 0;
                    default: return -1;
                }
            }
            return -1;
    }

    private void goAgain(int finalCol, int player) {
    }

    public int[][] getBoard() {
        return mBoard;
    }

}
