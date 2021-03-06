package org.spfk12.bestgroup.mancala;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    private GridView mGridView;
    private GridAdapter mGridAdapter;
    private TextView mPlayerOneScore;
    private TextView mPlayerTwoScore;
    private TextView mPlayerOne;
    private TextView mPlayerTwo;
    private Board mBoard;
    private int mPlayerTurn;
    private int mStoneCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instantiateObjects();
        makeGrid();
        setListeners();
    }

    public void instantiateObjects() {
        mStoneCount = 4;

        mGridView = (GridView) findViewById(R.id.board);
        mPlayerTurn = 1;

        // Set default style to bold because player one always starts
        mPlayerOneScore = (TextView) findViewById(R.id.player_one_text);
        mPlayerOneScore.setTextColor(Color.RED);

        mPlayerTwoScore = (TextView) findViewById(R.id.player_two_text);
        mPlayerTwoScore.setTextColor(Color.BLUE);
        mPlayerOneScore.setTypeface(null, Typeface.BOLD);

        mPlayerOne = (TextView) findViewById(R.id.player_one_overview);
        mPlayerOne.setTextColor(Color.RED);

        mPlayerTwo = (TextView) findViewById(R.id.player_two_overview);
        mPlayerTwo.setTextColor(Color.BLUE);
        mPlayerOne.setTypeface(null, Typeface.BOLD);

        mBoard = new Board(mStoneCount, mPlayerTurn, MainActivity.this,
                mGridView, mPlayerOneScore, mPlayerTwoScore);

    }

    public void makeGrid() {
        mGridAdapter = new GridAdapter(this, mBoard.getBoard(), mPlayerTurn);
        mGridView.setAdapter(mGridAdapter);

        //Creates grid pattern
        mGridView.setBackgroundColor(Color.BLACK);
        mGridView.setVerticalSpacing(1);
        mGridView.setHorizontalSpacing(1);
    }

    public void setListeners() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (mPlayerTurn == 2 && position <= 5) {
                    // mBoard.move();

                    //Swaps turn and bold


                    // input paremeters are spots 1-6
                    position = player2Index(position);
                    if (!mBoard.moveJ(position, 2)) {
                        mPlayerTurn--;
                        mPlayerOne.setTypeface(null, Typeface.BOLD);
                        mPlayerTwo.setTypeface(null, Typeface.NORMAL);

                        mPlayerOneScore.setTypeface(null, Typeface.BOLD);
                        mPlayerTwoScore.setTypeface(null, Typeface.NORMAL);

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("It is still player two's turn!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                    }
                                });
                        builder.setTitle("Go Again!");
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    makeGrid();

                    // Needs to update grid to swap bold inside grid
                } else if (mPlayerTurn == 1 && position > 5) {


                    position -= 5;

                    if (!mBoard.moveJ(position, 1)) {
                        mPlayerTurn++;
                        mPlayerTwo.setTypeface(null, Typeface.BOLD);
                        mPlayerOne.setTypeface(null, Typeface.NORMAL);

                        mPlayerTwoScore.setTypeface(null, Typeface.BOLD);
                        mPlayerOneScore.setTypeface(null, Typeface.NORMAL);

                        makeGrid();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("It is still player one's turn!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                    }
                                });
                        builder.setTitle("Go Again!");
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                } else {
                    // TODO make into dialog so no toast spam
                    String player;
                    if (mPlayerTurn == 1) {
                        player = "one";
                    } else {
                        player = "two";
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("It is player " + player + "'s turn!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    builder.setTitle("Not Your Turn");
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }

    private int player2Index(int spot) {
        if (spot == 0)
            return 6;

        else if (spot == 1)
            return 5;

        else if (spot == 2)
            return 4;

        else if (spot == 3)
            return 3;

        else if (spot == 4)
            return 2;

        else if (spot == 5)
            return 1;
        else {
            return -1;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                mBoard.restartGame();
                mPlayerTurn = 1;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
