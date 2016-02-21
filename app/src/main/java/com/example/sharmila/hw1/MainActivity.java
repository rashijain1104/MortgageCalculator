package com.example.sharmila.hw1;

import android.inputmethodservice.Keyboard;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MainActivity extends ActionBarActivity {
    /* declaring all the widgets*/
    private Button btncal;
    private EditText editText1, editText2, editText3;
    private SeekBar seekbar;
    private RadioGroup radioGroup;
    private RadioButton radio1, radio2, radio3;
    private CheckBox checkbox;

    /* declaring other required variables*/
    int max = 100;
    int min = 0;
    int intitialvalue = (int) 5.0;
    private TextView tv1, tv2;
    double seekvalue;
    private String val;
    private int loanterm;
    private boolean tax;
    private double amountborrowed;
    private double monthlypayment;
    private double intrestrate = 5.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* getting values of all input*/
        btncal = (Button) findViewById(R.id.button);
        editText1 = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        seekbar = (SeekBar) findViewById(R.id.seek);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radio1 = (RadioButton) findViewById(R.id.radioButton1);
        radio2 = (RadioButton) findViewById(R.id.radioButton2);
        radio3 = (RadioButton) findViewById(R.id.radioButton3);
        checkbox = (CheckBox) findViewById(R.id.checkBox);

        //seekbar function
        seekbar.setMax(max);
        seekbar.setProgress(intitialvalue);
        editText3.setText(Float.toString(intitialvalue) + "%");
        //seekbar listener method
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {   //Method for seekbar
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (float) (progress/10.0);
                seekvalue = value;
                editText3.setText(Float.toString((float) seekvalue) + "%");


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override

            //on sto of touch save the value in variable
            public void onStopTrackingTouch(SeekBar seekBar) {
                intrestrate = seekvalue;
            }


        });
        //lstening to radio group
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            //switch case for 3 radio buttons
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                 switch (checkedId) {
                     case R.id.radioButton1: {
                         loanterm = 7 * 12;

                         Toast.makeText(getApplicationContext(),"Loan Term : 7 Years" , Toast.LENGTH_SHORT).show();
                     }
                     break;
                     case R.id.radioButton2:
                     {
                         loanterm=15*12;
                         Toast.makeText(getApplicationContext(), "Loan Term : 15 Years" , Toast.LENGTH_SHORT).show();

                     }
                     break;
                     case R.id.radioButton3: {
                         loanterm=30*12;
                         Toast.makeText(getApplicationContext(), "Loan Term : 30 Years" , Toast.LENGTH_SHORT).show();

                     }
                     break;


                 }

            }
        });
        // to input value in edit text, if its not infocus hide the keyborad
        editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    KeyboardHiding(v);

                }
            }
        });

        //listener for button
        btncal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                val= editText1.getText().toString(); //getting value from the edit text 1

                if (val.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Amount borrow can not be empty", Toast.LENGTH_SHORT).show(); //check for input
                    editText2.setText("");

                }
                else{
                    amountborrowed = Float.parseFloat(val);
                }
                if (check() == true) {

                    Toast.makeText(getApplicationContext(), "Please select loan term in years", Toast.LENGTH_SHORT).show(); //check for loan term
                    editText2.setText("");
                   
                    return;


                }
                monthlypayment =  calMortgage(amountborrowed, loanterm, intrestrate, checkbox.isChecked()); //function to calculate mortgage
                editText2.setText("$" + Float.toString((float) monthlypayment));
                //Toast.makeText(getApplicationContext(), "Result" + monthlypayment, Toast.LENGTH_LONG).show();

            }
        });
    }

    private double calMortgage(double amountborrowed, int loanterm, double intrestrate, boolean checked) {


        double monthlypayment = min;

        Double tax = Double.valueOf(taxCal( amountborrowed, checked));

        if (intrestrate == 0.0)
        {
            monthlypayment = (float)(amountborrowed/loanterm)+tax;

        }
        else
        {
            int z= loanterm;
            Double monthlyintrestrate = intrestrate/1200;
            //Double x = 1/( 1 + intrestrate);

           // Double y = (Double) Math.pow(x, z);

            monthlypayment = ((Double.parseDouble(editText1.getText().toString())) * (monthlyintrestrate/(1 - (Math.pow((1 + monthlyintrestrate), -loanterm))))) + tax;

           // monthlypayment = (Double) (amountborrowed * (monthlyintrestrate / (1 - y))) + tax;


        }



        return monthlypayment;
    }

    //to check for radio button input
    private boolean check() {

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);

        if (rg.getCheckedRadioButtonId() == -1)

        {
            return true;


        } else {

            return false;



        }
    }

   //function to calculate the tax id check ox is checked
    private float taxCal(double amountborrowed, boolean checked) {
         float tax;
        if (checked){
            tax = (float) ((amountborrowed*0.1)/100); //tax is 0.1% of amount borrowed
        }
else
        {
            tax = 0;
        }

return tax;
    }


    private void KeyboardHiding(View v) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE); // Checks Changed Focus

            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);



    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hw1:
                clear();
                return true;

            default:
                return super.onOptionsItemSelected((android.view.MenuItem) item);
        }
    }
    //function to clear all the input feilds
    private void clear() {

        editText2.setText("");
        editText1.setText("");
        checkbox.setChecked(false);
        seekbar.setProgress(50);
        radioGroup.clearCheck();
    }
}



