package com.example.askmathadmin;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.askmathadmin.R;
import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class CalculatorFragment extends Fragment implements View.OnClickListener {

    private TextView resultTv, solutionTv;
    private MaterialButton buttonC, buttonBrackOpen, buttonBrackClose;
    private MaterialButton buttonDivide, buttonMultiply, buttonPlus, buttonMinus, buttonEquals;
    private MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    private MaterialButton buttonAC, buttonDot;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calculator, container, false);

        resultTv = rootView.findViewById(R.id.result_tv);
        solutionTv = rootView.findViewById(R.id.solution_tv);

        buttonC = rootView.findViewById(R.id.button_c);
        buttonBrackOpen = rootView.findViewById(R.id.button_open_bracket);
        buttonBrackClose = rootView.findViewById(R.id.button_close_bracket);
        buttonDivide = rootView.findViewById(R.id.button_divide);
        buttonMultiply = rootView.findViewById(R.id.button_multiply);
        buttonPlus = rootView.findViewById(R.id.button_plus);
        buttonMinus = rootView.findViewById(R.id.button_minus);
        buttonEquals = rootView.findViewById(R.id.button_equals);
        button0 = rootView.findViewById(R.id.button_0);
        button1 = rootView.findViewById(R.id.button_1);
        button2 = rootView.findViewById(R.id.button_2);
        button3 = rootView.findViewById(R.id.button_3);
        button4 = rootView.findViewById(R.id.button_4);
        button5 = rootView.findViewById(R.id.button_5);
        button6 = rootView.findViewById(R.id.button_6);
        button7 = rootView.findViewById(R.id.button_7);
        button8 = rootView.findViewById(R.id.button_8);
        button9 = rootView.findViewById(R.id.button_9);
        buttonAC = rootView.findViewById(R.id.button_ac);
        buttonDot = rootView.findViewById(R.id.button_dot);

        setOnClickListeners();

        return rootView;
    }

    private void setOnClickListeners() {
        buttonC.setOnClickListener(this);
        buttonBrackOpen.setOnClickListener(this);
        buttonBrackClose.setOnClickListener(this);
        buttonDivide.setOnClickListener(this);
        buttonMultiply.setOnClickListener(this);
        buttonPlus.setOnClickListener(this);
        buttonMinus.setOnClickListener(this);
        buttonEquals.setOnClickListener(this);
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonAC.setOnClickListener(this);
        buttonDot.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTv.getText().toString();

        if (buttonText.equals("AC")) {
            if (!dataToCalculate.isEmpty()) {
                solutionTv.setText("");
                resultTv.setText("0");
            }
            return;
        }

        if (buttonText.equals("=")) {
            solutionTv.setText(resultTv.getText());
            return;
        }

        if (buttonText.equals("C")) {
            if (!dataToCalculate.isEmpty()) {
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
                solutionTv.setText(dataToCalculate);
            }
        } else {
            dataToCalculate = dataToCalculate + buttonText;
            solutionTv.setText(dataToCalculate);
        }

        String finalResult = getResult(dataToCalculate);

        if (!finalResult.isEmpty()) {
            resultTv.setText(finalResult);
        }
    }

    private String getResult(String data) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            Object result = context.evaluateString(scriptable, data, "Javascript", 1, null);
            if (result instanceof Number) {
                double numericResult = ((Number) result).doubleValue();
                if (Double.isNaN(numericResult) || Double.isInfinite(numericResult)) {
                    return "Infinity";
                }
                return String.valueOf(numericResult).replace(".0", "");
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }
}
