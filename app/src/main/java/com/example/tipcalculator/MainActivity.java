


//Adrianna Mckeown
//Group 8
//Class02

package com.example.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    EditText editBillTotal;
    RadioGroup percentRadioGroup;
    TextView textViewCustomPercent;
    TextView textViewTipAmt;
    TextView textViewTotalAmt;
    RadioGroup splitRadioGroup;
    TextView textViewPerPerson;
    SeekBar seekbarPercent;
    Button btnClear;

    Double tipPercentage = 10.0; // initial 10% tip percentage is selected
    Double splitByAmt = 1.0; // initial total number of persons selected is 1
    Double initialAmt = 0.0;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editBillTotal = findViewById(R.id.enterBillTotal);
        percentRadioGroup = findViewById(R.id.percentRadioGroup);
        textViewCustomPercent = findViewById(R.id.customPercent);
        textViewTipAmt = findViewById(R.id.viewTipAmount);
        textViewTotalAmt = findViewById(R.id.viewTotalAmount);
        splitRadioGroup = findViewById(R.id.splitRadioGroup);
        textViewPerPerson = findViewById(R.id.totalPerPerson);
        seekbarPercent = findViewById(R.id.seekBar);
        btnClear = findViewById(R.id.buttonClear);

        // Resets the activity to the initial states on click
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Integer.parseInt(editBillTotal.getText().toString());
                } catch(Exception e) {
                    return;
                }
                editBillTotal.setText("");
                seekbarPercent.setProgress(40);
                percentRadioGroup.check(R.id.radioButton_10);
                splitRadioGroup.check(R.id.radioButton_1);
                textViewTipAmt.setText(currencyFormat.format(initialAmt));
                textViewTotalAmt.setText(currencyFormat.format(initialAmt));
                textViewPerPerson.setText(currencyFormat.format(initialAmt));
            }
        });


        editBillTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            // If the user updates or edits the bill total the tip, total, and total/person values are
            // updated to reflect their new values based on the selected tip and split by options.
            public void afterTextChanged(Editable s) {
                try {
                    Integer.parseInt(editBillTotal.getText().toString());
                } catch(Exception e) {
                    textViewTipAmt.setText(currencyFormat.format(initialAmt));
                    textViewTotalAmt.setText(currencyFormat.format(initialAmt));
                    textViewPerPerson.setText(currencyFormat.format(initialAmt));
                    return;
                }
                // Checks that the value is not empty before any calculations are done
                if (!editBillTotal.getText().toString().isEmpty()) {
                    calculateBillPlusTip();
                }
            }
        });

        seekbarPercent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            //textViewCustomPercent displays the current progress of the SeekBar as slider is moved
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = progress + "%";
                textViewCustomPercent.setText(text);
                if (percentRadioGroup.getCheckedRadioButtonId() == R.id.radioButton_custom) {
                    int percent = seekbarPercent.getProgress(); //Get seekbar progress value
                    tipPercentage = (double) percent;
                    calculateBillPlusTip();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // When the user selects a different tip option, the tip percentage is changed and the
        // total, and total/person values are calculated and updated based on the selected tip option
        percentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton_10) {
                    tipPercentage = 10.0;
                } else if (checkedId == R.id.radioButton_15) {
                    tipPercentage = 15.0;
                } else if (checkedId == R.id.radioButton_18) {
                    tipPercentage = 18.0;
                } else if (checkedId == R.id.radioButton_custom) {
                    int percent = seekbarPercent.getProgress(); //Get seekbar progress value
                    tipPercentage = (double) percent;
                }
                calculateBillPlusTip();
            }
        });

        // If a split by option is selected the total/person values should be calculated and update
        // based on the selected tip option, bill total and split by option
        splitRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton_1) {
                    splitByAmt = 1.0;
                } else if (checkedId == R.id.radioButton_2) {
                    splitByAmt = 2.0;
                } else if (checkedId == R.id.radioButton_3) {
                    splitByAmt = 3.0;
                } else if (checkedId == R.id.radioButton_4) {
                    splitByAmt  = 4.0;
                }
                calculateBillPlusTip();
            }
        });
    }

    private void calculateBillPlusTip() {
        try {
            Integer.parseInt(editBillTotal.getText().toString());
        } catch(Exception e) {
            return;
        }
        Double billAmt = Double.parseDouble(editBillTotal.getText().toString());
        Double tipAmt = billAmt * tipPercentage / 100;
        Double total = billAmt + tipAmt;
        Double totalPerPerson = total / splitByAmt;

        textViewTipAmt.setText(currencyFormat.format(tipAmt));
        textViewTotalAmt.setText(currencyFormat.format(total));
        textViewPerPerson.setText(currencyFormat.format(totalPerPerson));
    }
}