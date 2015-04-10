package org.spfk12.bestgroup.mancala;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
        int startPit1 = mPlayerOneScore;
        int startPit2 = mPlayerTwoScore;
        int startAfterPit1 = mBoard[0][5];
        int startAfterPit2 = mBoard[1][0];
        boolean goAgainCheck = false;


        if (player == 2)
            spot = player2Index(spot);
        // Checks if it's Player's 2 turn so it can change the spot appropriately
        // (Player 1 = Player 2) 1 = 6, 2 = 5, 3 = 4, 4 = 3, 5 = 2, 6 = 1
        // Sets counter equal to number of stones in that spot
        int counter = mBoard[player % 2][spot - 1];


        if (mBoard[player % 2][spot - 1] != 0) {
            mBoard[player % 2][spot - 1] = 0;
            updateDisplay();
            while (counter > 0) {
                if (player == 1) {
                    if (spot < mBoard[1].length) {
                        mBoard[1][spot] += 1;
                        counter--;
                        spot++;
                    } else if (spot == mBoard[1].length) {
                        if (startPlayer == player) {
                            mPlayerOneScore++;
                            counter--;
                            if (counter == 0) {
                                mBoard[0][5] -= 1;
                            }
                        }
                        player = 2;        // Loop up to Player 2's row
                    }
                }

                if (player == 2) {
                    if (spot > 0) {
                        // Makes sure it doesn't loop over the same spot twice
                        if (mBoard[0][spot - 1] == 0 && !flag && startPlayer == player) {
                            spot--;
                            flag = !flag;
                        } else {
                            mBoard[0][spot - 1] += 1;
                            spot--;
                            counter--;
                        }

                    } else if (spot == 0) {
                        if (startPlayer == player) {
                            mPlayerTwoScore++;
                            counter--;

                        }
                        //score2 = true;
                        player = 1;        // Loop down to Player 1's row
                    }
                }

            }
            goAgainCheck = checkRake(spot, player, startPlayer);
            if (mPlayerOneScore > startPit1 && startAfterPit1 == mBoard[0][5] && !goAgainCheck) {
                returnStatement = true;
            } else if (mPlayerTwoScore > startPit2 && startAfterPit2 == mBoard[1][0] && !goAgainCheck) {
                returnStatement = true;
            }
            updateDisplay();
        } else {
/*            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Select a spot containing stones!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            builder.setTitle("Invalid Move!");
            AlertDialog alert = builder.create();
            alert.show();*/
            returnStatement = true;
        }

//        goAgain(spot, player);
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
            return -20;
        }
    }

    private boolean checkRake(int finalSpot, int player, int startPlayer)//to be implemented in the move class
    {
        boolean returnValue = false;

        if (player == 1 && player == startPlayer && mBoard[1][finalSpot - 1] == 1 && mBoard[0][finalSpot - 1] > 0) {
            mPlayerOneScore += mBoard[0][finalSpot - 1];
            mPlayerOneScore += mBoard[1][finalSpot - 1];
            mBoard[1][finalSpot - 1] = 0;
            mBoard[0][finalSpot - 1] = 0;
            returnValue = true;
        }
        if (player == 2 && player == startPlayer && mBoard[0][finalSpot] == 1 && mBoard[1][finalSpot] > 0) {
            mPlayerTwoScore += mBoard[1][finalSpot];
            mPlayerTwoScore += mBoard[0][finalSpot];
            mBoard[0][finalSpot] = 0;
            mBoard[1][finalSpot] = 0;

            returnValue = true;

            //System.out.print("Falala");

        }

        checkWinner();
        return returnValue;

    }

    private int rakeConvert(int player, int spot) {
            /*if(player == 1) {
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
            }*/
        if (player == 2) {
            switch (spot) {
                case 1:
                    return 5;
                case 2:
                    return 4;
                case 3:
                    return 3;
                case 4:
                    return 2;
                case 5:
                    return 1;
                case 6:
                    return 0;
                default:
                    return -1;
            }
        }
        return -1;
    }

    public void checkWinner() {
        boolean repeatFlag = false;
        boolean repeatFlag2 = false;
        if (mPlayerOneScore >= 25) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Player One Has Won!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            builder.setTitle("Winner!");
            AlertDialog alert = builder.create();
            alert.show();
        } else if (mPlayerTwoScore >= 25) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Player One Has Won!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            builder.setTitle("Winner!");
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            for (int i = 0; i < mBoard[0].length; i++) {
                if (mBoard[0][i] > 0) {
                    repeatFlag = true;
                    break;
                }
            }
            for (int j = 0; j < mBoard[0].length; j++) {
                if (mBoard[1][j] > 0) {
                    repeatFlag2 = true;
                }
            }

            if (!repeatFlag || !repeatFlag2) {
                String winner;
                if (mPlayerOneScore > mPlayerTwoScore) {
                    winner = "One";
                } else if (mPlayerTwoScore > mPlayerOneScore) {
                    winner = "Two";
                } else {
                    winner = "tie";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Player " + winner + " Has Won!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                builder.setTitle("Winner!");
                AlertDialog alert = builder.create();
                alert.show();
            }

        }
    }

    private boolean goAgain(int finalCol, int player) {
        if (player == 1) {
            if (finalCol == 0) {
                Log.d("go again", "" + player);
                return true;
            }
        }

        if (player == 2) {
            if (finalCol == 1) {
                Log.d("go again", "" + player);
                return true;
            }
        }
        return false;
    }

    public int[][] getBoard() {
        return mBoard;
    }

}
