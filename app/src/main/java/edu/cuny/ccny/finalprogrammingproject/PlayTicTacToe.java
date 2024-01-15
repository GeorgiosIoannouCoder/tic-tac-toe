package edu.cuny.ccny.finalprogrammingproject;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.security.SecureRandom;
import io.github.muddz.styleabletoast.StyleableToast;

public class PlayTicTacToe extends AppCompatActivity {

    private boolean firstTurnPlayerOne;
    private boolean firstTurn;
    private boolean simulation;
    private boolean winner = false;
    private boolean draw = false;
    private boolean reset = false;
    private boolean flag = false;
    private int gridCount = 0;
    private int playerOnePoints = 0;
    private int playerTwoPoints = 0;
    private int winningSound;
    private int playerOneSymbolSound;
    private int playerTwoSymbolSound;
    private TextView textViewScore;
    private SecureRandom secRand = new SecureRandom();
    private SoundPool soundPool;
    private String[] playersNames;
    private String[] playersSymbols;
    private Button[][] gridButtons = new Button[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_tic_tac_toe);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        firstTurnPlayerOne = intent.getBooleanExtra("FIRST_TURN_PLAYER_ONE", true);
        simulation = intent.getBooleanExtra("SIMULATION", false);
        playersNames = intent.getStringArrayExtra("PLAYERS_NAMES");
        playersSymbols = intent.getStringArrayExtra("PLAYERS_SYMBOLS");

        firstTurn = firstTurnPlayerOne;

        TextView textViewPlayerOne = findViewById(R.id.etPlayerOne);
        textViewScore = findViewById(R.id.etScore);
        TextView textViewPlayerTwo = findViewById(R.id.etPlayerTwo);

        textViewPlayerOne.setText(playersNames[0]);
        textViewPlayerTwo.setText(playersNames[1]);
        textViewScore.setText("0 : 0");

        String btnID;
        int id;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                btnID = "btn" + i + j;
                id = getResources().getIdentifier(btnID, "id", getPackageName());
                gridButtons[i][j] = findViewById(id);
            }
        }

        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        winningSound = soundPool.load(PlayTicTacToe.this, R.raw.winning_sound, 1);
        playerOneSymbolSound = soundPool.load(PlayTicTacToe.this, R.raw.player_one_symbol_sound, 1);
        playerTwoSymbolSound = soundPool.load(PlayTicTacToe.this, R.raw.player_two_symbol_sound, 1);

        if (simulation && !firstTurn) {
            simulate();
            gridCount += 1;
            firstTurn = true;
        }
    }

    public void gridButtonClick(View view) {
        if (reset) {
            resetGrid();
            if (simulation && !firstTurnPlayerOne) {
                showToast("TOUCH ON AN EMPTY GRID FOR THE SIMULATOR TO PLAY", R.style.styleableToastSimulator);
                flag = true;
            }
            return;
        }

        if (!((Button) view).getText().toString().equals("")) {
            return;
        }

        if (firstTurn) {
            ((Button) view).setText(playersSymbols[0]);
            soundPool.play(playerOneSymbolSound, 1, 1, 0, 0, 1);
            if (simulation && (gridCount == 0 || gridCount == 1) && !flag) {
                showToast("TOUCH ON AN EMPTY GRID FOR THE SIMULATOR TO PLAY", R.style.styleableToastSimulator);
            }
        }
        else {
            if (simulation && gridCount < 9) {
                simulate();
            }
            else {
                ((Button) view).setText(playersSymbols[1]);
                soundPool.play(playerTwoSymbolSound, 1, 1, 0, 0, 1);
            }
        }

        gridCount += 1;
        winner = isWinner();

        if (winner) {
            if (firstTurn) {
                playerOneWinner();
            }
            else {
                playerTwoWinner();
            }
        }
        else if (gridCount == 9) {
            draw = isDraw();
        }
        else {
            if (firstTurn) {
                firstTurn = false;
            }
            else {
                firstTurn = true;
            }
        }
    }

    public int generateRandomNumber() {
        return secRand.nextInt(3);
    }

    public void simulate() {
        int i = generateRandomNumber();
        int j = generateRandomNumber();

        while (!gridButtons[i][j].getText().toString().equals("")) {
            i = generateRandomNumber();
            j = generateRandomNumber();
        }

        gridButtons[i][j].setText(playersSymbols[1]);
        SystemClock.sleep(200);
        soundPool.play(playerTwoSymbolSound, 1, 1, 0, 0, 1);
    }

    public boolean isWinner() {
        String[][] grid = new String[3][3];

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                grid[i][j] = gridButtons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; ++i) {
            if (grid[i][0].equals(grid[i][1]) && grid[i][0].equals(grid[i][2]) && !grid[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; ++i) {
            if (grid[0][i].equals(grid[1][i]) && grid[0][i].equals(grid[2][i]) && !grid[0][i].equals("")) {
                return true;
            }
        }

        if (grid[0][0].equals(grid[1][1]) && grid[0][0].equals(grid[2][2]) && !grid[0][0].equals("")) {
            return true;
        }

        if (grid[0][2].equals(grid[1][1]) && grid[0][2].equals(grid[2][0]) && !grid[0][2].equals("")) {
            return true;
        }
        return false;
    }

    public boolean isDraw() {
        showToast("DRAW", R.style.styleableToastDraw);
        showToast("TOUCH GRID TO PLAY AGAIN", R.style.styleableToastPlayAgain);
        reset = true;
        return true;
    }

    public void playerOneWinner() {
        playerOnePoints += 1;
        updateScore();
        showToast(playersNames[0] + " WINS!", R.style.styleableToastPlayerOneWinner);
        soundPool.play(winningSound, 1, 1, 0, 0, 1);
        showToast("TOUCH GRID TO PLAY AGAIN", R.style.styleableToastPlayAgain);
        reset = true;
    }

    public void playerTwoWinner() {
        playerTwoPoints += 1;
        updateScore();
        showToast(playersNames[1] + " WINS!", R.style.styleableToastPlayerTwoWinner);
        soundPool.play(winningSound, 1, 1, 0, 0, 1);
        showToast("TOUCH GRID TO PLAY AGAIN", R.style.styleableToastPlayAgain);
        reset = true;
    }

    public void updateScore() {
        String scoreOfPlayers = playerOnePoints + " : " + playerTwoPoints;
        textViewScore.setText(scoreOfPlayers);
    }

    public void resetGrid() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                gridButtons[i][j].setText("");
            }
        }

        gridCount = 0;
        firstTurn = firstTurnPlayerOne;
        reset = false;
    }

    public void showToast(String message, int style) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        StyleableToast styleableToast = StyleableToast.makeText(context, message, duration, style);
        styleableToast.show();
    }

    public void resetButton(View view) {
        playerOnePoints = 0;
        playerTwoPoints = 0;

        updateScore();
        resetGrid();
    }

    public void homeButton(View view) {
        Intent intent = new Intent(PlayTicTacToe.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("FIRST_TURN", firstTurn);
        outState.putBoolean("WINNER", winner);
        outState.putBoolean("DRAW", draw);
        outState.putBoolean("RESET", reset);
        outState.putBoolean("FLAG", flag);
        outState.putInt("GRID_COUNT", gridCount);
        outState.putInt("PLAYER_ONE_POINTS", playerOnePoints);
        outState.putInt("PLAYER_TWO_POINTS", playerTwoPoints);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        firstTurn = savedInstanceState.getBoolean("FIRST_TURN");
        winner = savedInstanceState.getBoolean("WINNER");
        draw = savedInstanceState.getBoolean("DRAW");
        reset = savedInstanceState.getBoolean("RESET");
        flag = savedInstanceState.getBoolean("FLAG");
        gridCount = savedInstanceState.getInt("GRID_COUNT");
        playerOnePoints = savedInstanceState.getInt("PLAYER_ONE_POINTS");
        playerTwoPoints = savedInstanceState.getInt("PLAYER_TWO_POINTS");

        if (winner || draw) {
            resetGrid();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}