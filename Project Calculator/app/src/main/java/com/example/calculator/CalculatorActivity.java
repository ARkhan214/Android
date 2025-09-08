package com.example.calculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalculatorActivity extends AppCompatActivity {

    private TextView tvInput;
    private String input = "";
    private String operator = "";
    private double num1 = 0, num2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        // Edge to Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvInput = findViewById(R.id.tvInput);

        // Number Buttons
        int[] numberIds = {R.id.btn0,R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,R.id.btn5,R.id.btn6,R.id.btn7,R.id.btn8,R.id.btn9};
        for(int id : numberIds){
            Button btn = findViewById(id);
            if(btn!=null){
                btn.setOnClickListener(v -> {
                    input += ((Button)v).getText().toString();
                    tvInput.setText(input);
                });
            }
        }

        // Operators
        setupOperator(R.id.btnPlus,"+");
        setupOperator(R.id.btnMinus,"-");
        setupOperator(R.id.btnMultiply,"*");
        setupOperator(R.id.btnDivide,"/");

        // Equal
        Button btnEqual = findViewById(R.id.btnEqual);
        if(btnEqual!=null) btnEqual.setOnClickListener(v->calculate());

        // Clear
        Button btnClear = findViewById(R.id.btnClear);
        if(btnClear!=null) btnClear.setOnClickListener(v -> {
            input="";
            operator="";
            num1=num2=0;
            tvInput.setText("0");
        });

        // Delete
        Button btnDelete = findViewById(R.id.btnDelete);
        if(btnDelete!=null) btnDelete.setOnClickListener(v->{
            if(!input.isEmpty()){
                input=input.substring(0,input.length()-1);
                tvInput.setText(input.isEmpty()?"0":input);
            }
        });

        // Dot
        Button btnDot = findViewById(R.id.btnDot);
        if(btnDot!=null) btnDot.setOnClickListener(v->{
            if(!input.contains(".")){
                input+=".";
                tvInput.setText(input);
            }
        });
    }

    private void setupOperator(int id, String op){
        Button btn = findViewById(id);
        if(btn!=null){
            btn.setOnClickListener(v->setOperator(op));
        }
    }


    private void setOperator(String op){
        if(!input.isEmpty()){
            num1 = Double.parseDouble(input);
            operator = op;
            input = input + operator;
            tvInput.setText(input);
//            tvInput.setText(num1 + " " + operator); // for view operator
//            input = "";
        }
    }



    private void calculate(){
        if(!input.isEmpty() && !operator.isEmpty()){
            String[] parts = input.split(java.util.regex.Pattern.quote(operator)); //operotor diea devide
            if(parts.length > 1){
                num2 = Double.parseDouble(parts[1]); // second number
            }

            double result = 0;
            switch(operator){
                case "+": result = num1 + num2; break;
                case "-": result = num1 - num2; break;
                case "*": result = num1 * num2; break;
                case "/":
                    if(num2 != 0) result = num1 / num2;
                    else { tvInput.setText("Error"); return; }
                    break;
            }

            tvInput.setText(String.valueOf(result));
            input = String.valueOf(result); // store the result in input for the next calculation
            operator = "";
        }
    }

}
