package com.example.movies;



import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    RadioButton radioButtons;
    RadioButton radioCalories;
    RadioButton radioKilojoules;
    RadioGroup radioGroup;
    EditText userInputNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        radioGroup = findViewById(R.id.radioGroup_measurement);
        userInputNumber = findViewById(R.id.txt_userInput);
        radioCalories = findViewById(R.id.radioButton_calories);
        radioKilojoules = findViewById(R.id.radioButton_Kilojoules);
    }

    public void Convert(View view) {
        try {

                int selectId = radioGroup.getCheckedRadioButtonId();
                radioButtons = findViewById(selectId);

                // validating radio buttons
                if (selectId == -1) {
                    SetErrorRadioButtons();
                }
                // validating TextFields
                else if(ValidateTextField()){
                    RemoveErrorRadioButtons();

                    double userInput = Integer.parseInt(String.valueOf(userInputNumber.getText()));
                     if (radioButtons.getText().toString().equals("Kilojoules")) {
                        String Message = "Kilojoules: " + CaloriesToKilojoules(userInput);
                        MessageBox(Message);

                    }
                    else{
                        String Message = "Calories: " + KilojoulesToCalories(userInput);
                        MessageBox(Message);

                    }
                }
                else{
                    RemoveErrorRadioButtons();
                }

        }
        catch (Exception exception) {
            String error = exception.getMessage();
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
        }
    }

    private int KilojoulesToCalories(double kilojoules) {
        return Integer.parseInt(String.valueOf(Math.round(kilojoules * 0.239)));
    }

    private int CaloriesToKilojoules(double calories) {
        return Integer.parseInt(String.valueOf(Math.round(calories / 0.239)));
    }
    private void MessageBox(String Message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Conversion Successful")
                .setMessage(Message)
                .setCancelable(true)
                .setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.cancel())
                .show();

        
    }
    private boolean ValidateTextField(){

        if(TextUtils.isEmpty(userInputNumber.getText())){
            Toast.makeText(MainActivity.this, "Please enter a number.", Toast.LENGTH_SHORT).show();
            userInputNumber.setError("Please Enter a number.");
            return false;
        }

        userInputNumber.setError(null);
        return true;
    }

    private void RemoveErrorRadioButtons(){
        radioCalories.setError(null);
        radioKilojoules.setError(null);
    }
    private void SetErrorRadioButtons(){
        Toast.makeText(MainActivity.this, "Please select a measurement", Toast.LENGTH_SHORT).show();
        radioCalories.setError("Please select a measurement.");
        radioKilojoules.setError("Please select a measurement.");
    }
}



