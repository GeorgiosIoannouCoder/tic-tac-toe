package edu.cuny.ccny.finalprogrammingproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import io.github.muddz.styleabletoast.StyleableToast;

public class PlayersNames extends AppCompatActivity {

    private boolean firstTurnPlayerOne = true;
    private EditText playerOneNam;
    private EditText playerOneSymbo;
    private EditText playerTwoNam;
    private EditText playerTwoSymbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.players_names);

        playerOneNam = findViewById(R.id.etPlayerOneName);
        playerOneSymbo = findViewById(R.id.etPlayerOneSymbol);
        playerTwoNam = findViewById(R.id.etSimulatorName);
        playerTwoSymbo = findViewById(R.id.etSimulatorSymbol);
    }

    public void radioButtonFirstTurn(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        if (view.getId() == R.id.rbPlayerOne && checked) {
            firstTurnPlayerOne = true;
        }
        else if (view.getId() == R.id.rbSimulator && checked) {
            firstTurnPlayerOne = false;
        }
    }

    public void startButton(View view) {
        String playerOneName = playerOneNam.getText().toString();
        String playerOneSymbol = playerOneSymbo.getText().toString();
        String playerTwoName = playerTwoNam.getText().toString();
        String playerTwoSymbol = playerTwoSymbo.getText().toString();

        if (playerOneName.equals("") || playerOneSymbol.equals("") || playerTwoName.equals("") || playerTwoSymbol.equals("")) {
            showToast("PLEASE FILL ALL INFORMATION", R.style.styleableToastError);
        }
        else if (playerOneName.length() > 10 || playerTwoName.length() > 10) {
            showToast("PLAYER NAME MUST BE AT MOST 10 CHARACTERS", R.style.styleableToastError);
        }
        else if (playerOneSymbol.length() > 1 || playerTwoSymbol.length() > 1) {
            showToast("PLAYER SYMBOL MUST BE AT MOST 1 CHARACTER", R.style.styleableToastError);
        }
        else if (!playerOneSymbol.matches("[a-zA-Z]") || !playerTwoSymbol.matches("[a-zA-Z]")) {
            showToast("PLAYER SYMBOL MUST BE FROM THE ENGLISH ALPHABET", R.style.styleableToastError);
        }
        else {
            Intent intent = new Intent(PlayersNames.this, PlayTicTacToe.class);
            intent.putExtra("PLAYERS_NAMES", new String[]{playerOneName, playerTwoName});
            intent.putExtra("PLAYERS_SYMBOLS", new String[]{playerOneSymbol, playerTwoSymbol});
            intent.putExtra("FIRST_TURN_PLAYER_ONE", firstTurnPlayerOne);
            intent.putExtra("SIMULATION", false);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void homeButton(View view) {
        Intent intent = new Intent(PlayersNames.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void showToast(String message, int style) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        StyleableToast styleableToast = StyleableToast.makeText(context, message, duration, style);
        styleableToast.show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("FIRST_TURN_PLAYER_ONE", firstTurnPlayerOne);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        firstTurnPlayerOne = savedInstanceState.getBoolean("FIRST_TURN_PLAYER_ONE");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}