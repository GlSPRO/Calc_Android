package com.example.calc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Zero;
    private Button Minus, Plus, Division, Multiply, Result;
    private Button SquareRoot, Square, Percent, Clear;
    private TextView Formula, EndResult;

    private char Action;
    private double ResultValue = Double.NaN;
    private double Value;

    // Флаг для отслеживания ожидания второго числа после символа процента (%)
    private boolean isWaitingForSecondNumber = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
        setupNumberClickListeners();
        setupActionClickListeners();
        setupResultClickListener();
        setupAdditionalClickListeners();
    }

    private void setupView() {
        One = findViewById(R.id.One);
        Two = findViewById(R.id.Two);
        Three = findViewById(R.id.Three);
        Four = findViewById(R.id.Four);
        Five = findViewById(R.id.Five);
        Six = findViewById(R.id.Six);
        Seven = findViewById(R.id.Seven);
        Eight = findViewById(R.id.Eight);
        Nine = findViewById(R.id.Nine);
        Zero = findViewById(R.id.Zero);
        Minus = findViewById(R.id.Minus);
        Plus = findViewById(R.id.Plus);
        Result = findViewById(R.id.Result);
        Division = findViewById(R.id.Division);
        Multiply = findViewById(R.id.Multiply);
        EndResult = findViewById(R.id.EndResult);
        Formula = findViewById(R.id.Formula);

        SquareRoot = findViewById(R.id.SquareRoot);
        Square = findViewById(R.id.Square);
        Percent = findViewById(R.id.Percent);
        Clear = findViewById(R.id.Clear);
    }

    private void setupNumberClickListeners() {
        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
                if (isWaitingForSecondNumber) {
                    Formula.setText(button.getText().toString());
                    isWaitingForSecondNumber = false;
                } else {
                    Formula.append(button.getText().toString());
                }
            }
        };

        One.setOnClickListener(numberClickListener);
        Two.setOnClickListener(numberClickListener);
        Three.setOnClickListener(numberClickListener);
        Four.setOnClickListener(numberClickListener);
        Five.setOnClickListener(numberClickListener);
        Six.setOnClickListener(numberClickListener);
        Seven.setOnClickListener(numberClickListener);
        Eight.setOnClickListener(numberClickListener);
        Nine.setOnClickListener(numberClickListener);
        Zero.setOnClickListener(numberClickListener);
    }

    private void setupActionClickListeners() {
        View.OnClickListener actionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
                if (isWaitingForSecondNumber) {
                    Toast.makeText(MainActivity.this, "Ожидается второе число после процента", Toast.LENGTH_SHORT).show();
                    return;
                }
                calculate();
                Action = button.getText().charAt(0);
                Formula.append(String.valueOf(Action));
                EndResult.setText(null);
                if (Action == '%') {
                    isWaitingForSecondNumber = true;
                }
            }
        };

        Plus.setOnClickListener(actionClickListener);
        Minus.setOnClickListener(actionClickListener);
        Division.setOnClickListener(actionClickListener);
        Multiply.setOnClickListener(actionClickListener);
    }

    private void setupResultClickListener() {
        Result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
                Action = '=';
                EndResult.setText(String.valueOf(ResultValue));
                Formula.setText(null);
            }
        });
    }

    private void setupAdditionalClickListeners() {
        SquareRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateSquareRoot();
            }
        });

        Square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateSquare();
            }
        });

        Percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePercent();
            }
        });

        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
    }

    private void calculate() {
        String textFormula = Formula.getText().toString();
        if (!textFormula.isEmpty()) {
            try {
                if (textFormula.contains("%")) {
                    String[] parts = textFormula.split("%");
                    double baseNumber = Double.parseDouble(parts[0]);
                    double secondNumber = Double.parseDouble(parts[1]);
                    ResultValue = baseNumber * (secondNumber * 0.01);
                } else {
                    int index = textFormula.indexOf(Action);
                    if (index != -1) {
                        String numberValue = textFormula.substring(index + 1);
                        Value = Double.parseDouble(numberValue);
                        switch (Action) {
                            case '/':
                                if (Value == 0) {
                                    Toast.makeText(MainActivity.this, "Ошибка: деление на ноль", Toast.LENGTH_SHORT).show();
                                    Formula.setText("");
                                    return;
                                } else {
                                    ResultValue /= Value;
                                }
                                break;
                            case '*':
                                ResultValue *= Value;
                                break;
                            case '+':
                                ResultValue += Value;
                                break;
                            case '-':
                                ResultValue -= Value;
                                break;
                            case '=':
                                ResultValue = Value;
                                break;
                        }
                    } else {
                        ResultValue = Double.parseDouble(textFormula);
                    }
                }
            } catch (NumberFormatException e) {
                ResultValue = Double.NaN;
            }
        }

        Formula.setText(String.valueOf(ResultValue));
        EndResult.setText("");
    }

    private void calculateSquareRoot() {
        String formulaText = Formula.getText().toString();
        if (!formulaText.isEmpty()) {
            try {
                double number = Double.parseDouble(formulaText);
                if (number >= 0) {
                    double result = Math.sqrt(number);
                    Formula.setText(String.valueOf(result));
                    EndResult.setText(null);
                } else {
                    EndResult.setText("Ошибка");
                }
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Ошибка: неверный формат числа", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void calculateSquare() {
        String formulaText = Formula.getText().toString();
        if (!formulaText.isEmpty()) {
            try {
                double number = Double.parseDouble(formulaText);
                double result = Math.pow(number, 2);
                Formula.setText(String.valueOf(result));
                EndResult.setText(null);
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Ошибка: неверный формат числа", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void calculatePercent() {
        String formulaText = Formula.getText().toString().trim();
        if (!formulaText.isEmpty()) {
            // Добавляем символ % в Formula
            Formula.append("%");
        }
    }



    private void clear() {
        Formula.setText("");
        EndResult.setText("");
        ResultValue = Double.NaN;
        Value = Double.NaN;
        isWaitingForSecondNumber = false;
    }
}
