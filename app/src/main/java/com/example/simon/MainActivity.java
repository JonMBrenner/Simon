package com.example.simon;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

/**
 * Main activity for the app Simon which emulates the classic handheld game of the same name
 *
 * @author Jonathan Brenner
 * @version 1.0
 * @since 1.0
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<Integer> order = new ArrayList<>();
    int curIndex = 0;
    boolean listening = false;
    boolean playing = false;
    int green = Color.parseColor("#009900");
    int greenPrime = Color.parseColor("#7CFC00");
    int red = Color.parseColor("#B30000");
    int redPrime = Color.parseColor("#FF0000");
    int yellow = Color.parseColor("#FFBE00");
    int yellowPrime = Color.parseColor("#FFFF00");
    int blue = Color.parseColor("#303F9F");
    int bluePrime = Color.parseColor("#3399FF");

    /**
     * Initializes buttons and their colors
     * <p>
     * Turns on click listener for main four buttons, as well as the play button, then sets
     * each of the main four button colors to their respective color
     *
     * @param savedInstanceState bundle object containing the previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(this);
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(this);
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(this);
        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(this);
        Button playButton = findViewById(R.id.button5);
        playButton.setOnClickListener(this);
        button1.setBackgroundColor(green);
        button2.setBackgroundColor(red);
        button3.setBackgroundColor(yellow);
        button4.setBackgroundColor(blue);
    }

    /**
     * Determines which button was clicked and executes an action accordingly
     * <p>
     * First checks if the player is not currently in a game and that the button clicked was
     * button 5, the play button, and then calls start() method in this case.
     * Otherwise, if the player is playing a game, and the game is listening for the player to
     * repeat the pattern, then it checks for correctness and either advances the game or calls
     * gameOver() if the player was incorrect
     *
     * @param view the view element that was clicked
     */
    @Override
    public void onClick(View view) {
        if (!playing && getButtonNum(view.getId()) == 5) {
            start();
        } else if (playing && listening) {
            int buttonNum = getButtonNum(view.getId());
            flashButton(buttonNum, 100);
            if (buttonNum != order.get(curIndex)) {
                listening = false;
                gameOver();
            } else if (curIndex == order.size() - 1) {
                Button scoreButton = findViewById(R.id.button6);
                String score = "Score: " + Integer.toString(order.size());
                scoreButton.setText(score);
                curIndex = 0;
                listening = false;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        displayPattern();
                    }
                }, 1000);
            } else {
                curIndex++;
            }
        }
    }

    /**
     * starts the game activity
     * <p>
     * makes the play button invisible, sets score to 0 and then calls displayPattern() to show
     * the first button in the pattern
     */
    private void start() {
        playing = true;
        Button playButton = findViewById(R.id.button5);
        playButton.setVisibility(View.INVISIBLE);
        Button scoreButton = findViewById(R.id.button6);
        scoreButton.setText("Score: 0");
        displayPattern();
    }

    /**
     * Displays current button pattern for user
     * <p>
     * Adds a new random button to the order, and then calls the recursive showButton() method
     * with the first index, to then display the entire pattern with its new entry
     */
    private void displayPattern() {
        Random rand = new Random();
        int n = rand.nextInt(4) + 1;
        order.add(n);
        showButton(0);
    }

    /**
     * Recursive function that displays the given button for the user
     * <p>
     * First checks for base case at end of the pattern, then flashes the given button, and
     * lastly calls itself on the next button in the order after 750 milliseconds
     *
     * @param index the index in the order to be displayed
     */
    private void showButton(int index) {
        if (index == order.size()) {
            listening = true;
            return;
        }
        flashButton(order.get(index), 500);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                showButton(index + 1);
            }
        }, 750);
    }

    /**
     * Flashes one of the four buttons to display the pattern to the user
     * <p>
     * Depending on the int buttonNum, sets the corresponding button to its prime color to
     * show a flash, then sets it back to its original color on a delay
     *
     * @param buttonNum the number indicating which of the four buttons to flash
     * @param time the time in milliseconds to flash the button for
     */
    private void flashButton(int buttonNum, int time) {
        Button button;
        int oldColor;
        if (buttonNum == 1) {
            button = findViewById(R.id.button1);
            oldColor = green;
            button.setBackgroundColor(greenPrime);
        } else if (buttonNum == 2) {
            button = findViewById(R.id.button2);
            oldColor = red;
            button.setBackgroundColor(redPrime);
        } else if (buttonNum == 3) {
            button = findViewById(R.id.button3);
            oldColor = yellow;
            button.setBackgroundColor(yellowPrime);
        } else {
            button = findViewById(R.id.button4);
            oldColor = blue;
            button.setBackgroundColor(bluePrime);
        }
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setBackgroundColor(oldColor);
            }
        }, time);
    }

    /**
     * Takes in a button id and returns which button it is
     * <p>
     * returns 1 for top left, 2 for top right, 3 for bottom left, 4 for bottom right
     * and then 5 for the play button
     *
     * @param i the button id to identify
     * @return an int indicating which button was passed in
     */
    private int getButtonNum(int i) {
        if (i == R.id.button1) {
            return 1;
        } else if (i == R.id.button2) {
            return 2;
        } else if (i == R.id.button3) {
            return 3;
        } else if (i == R.id.button4) {
            return 4;
        } else {
            return 5;
        }
    }

    /**
     * ends the game activity
     * <p>
     * sets the score to display GAME OVER, clears the order list, and makes the play button
     * visible for the user to play the game again
     */
    private void gameOver() {
        playing = false;
        Button scoreButton = findViewById(R.id.button6);
        scoreButton.setText("GAME OVER");
        order.clear();
        curIndex = 0;
        Button playButton = findViewById(R.id.button5);
        playButton.setVisibility(View.VISIBLE);
    }
}
