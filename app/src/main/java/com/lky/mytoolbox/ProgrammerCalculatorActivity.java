package com.lky.mytoolbox;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ProgrammerCalculatorActivity extends AppCompatActivity {

    private EditText resultEditText;
    private String currentInput = "";
    private String operator = "";
    private double firstOperand = 0;
    private int currentBase = 10; // 默认十进制
    private double previousResult = 0;
    private double previousPreviousResult = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programmer_calculator);

        resultEditText = findViewById(R.id.result);

        // 数字按钮
        setNumberButton(R.id.zero, "0");
        setNumberButton(R.id.zero2, "00");
        setNumberButton(R.id.one, "1");
        setNumberButton(R.id.two, "2");
        setNumberButton(R.id.three, "3");
        setNumberButton(R.id.four, "4");
        setNumberButton(R.id.five, "5");
        setNumberButton(R.id.six, "6");
        setNumberButton(R.id.seven, "7");
        setNumberButton(R.id.eight, "8");
        setNumberButton(R.id.nine, "9");


        // 清除按钮
        findViewById(R.id.cls).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentInput = "";
                operator = "";
                firstOperand = 0;
                currentBase = 10;
                previousResult = 0;
                previousPreviousResult = 0;
                resultEditText.setText("");
            }
        });

        // M1 按钮
        findViewById(R.id.m1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previousResult != 0) {
                    currentInput = Double.toString(previousResult);
                    resultEditText.setText(currentInput);
                }
            }
        });

        // M2 按钮
        findViewById(R.id.m2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previousPreviousResult != 0) {
                    currentInput = Double.toString(previousPreviousResult);
                    resultEditText.setText(currentInput);
                }
            }
        });

        // 回退按钮
        findViewById(R.id.Backspace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentInput.length() > 0) {
                    currentInput = currentInput.substring(0, currentInput.length() - 1);
                    resultEditText.setText(currentInput);
                }
            }
        });


        // 等于按钮
        findViewById(R.id.equal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentInput.isEmpty() && !operator.isEmpty()) {
                    double secondOperand = parseDouble(currentInput, currentBase);
                    double result = 0;

                    switch (operator) {
                        case "AND":
                            result = (long) firstOperand & (long) secondOperand;
                            break;
                        case "OR":
                            result = (long) firstOperand | (long) secondOperand;
                            break;
                        case "XOR":
                            result = (long) firstOperand ^ (long) secondOperand;
                            break;
                        case "<<":
                            result = (long) firstOperand << (int) secondOperand;
                            break;
                        case ">>":
                            result = (long) firstOperand >> (int) secondOperand;
                            break;
                    }

                    // 更新上一次和上上次的结果
                    previousPreviousResult = previousResult;
                    previousResult = result;

                    currentInput = Double.toString(result);
                    resultEditText.setText(currentInput);
                    operator = "";
                }
            }
        });

        // 进制转换按钮
        setBaseButton(R.id.bin, 2);
        setBaseButton(R.id.oct, 8);
        setBaseButton(R.id.dec, 10);
        setBaseButton(R.id.hex, 16);

        // 哈希函数按钮
        setHashButton(R.id.md5, "MD5");
        setHashButton(R.id.sha1, "SHA-1");
        setHashButton(R.id.sha256, "SHA-256");

        // danwei 按钮
        findViewById(R.id.danwei).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentInput.isEmpty()) {
                    try {
                        double number = parseDouble(currentInput, currentBase);
                        double result = Div(number, 1028.0);
                        currentInput = Double.toString(result);
                        resultEditText.setText(currentInput);
                    } catch (NumberFormatException e) {
                        resultEditText.setText("Error");
                    }
                }
            }
        });
    }

    private void setNumberButton(int id, final String number) {
        Button button = findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidNumber(number)) {
                    currentInput += number;
                    resultEditText.setText(currentInput);
                }
            }
        });
    }

    private void setOperatorButton(int id, final String op) {
        Button button = findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentInput.isEmpty()) {
                    firstOperand = parseDouble(currentInput, currentBase);
                    operator = op;
                    currentInput = "";
                }
            }
        });
    }

    private void setBaseButton(int id, final int base) {
        Button button = findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentInput.isEmpty()) {
                    try {
                        long number = Long.parseLong(currentInput, currentBase);
                        currentInput = Long.toString(number, base);
                        resultEditText.setText(currentInput);
                        currentBase = base;
                    } catch (NumberFormatException e) {
                        resultEditText.setText("Error");
                    }
                }
            }
        });
    }

    private void setHashButton(int id, final String hashType) {
        Button button = findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentInput.isEmpty()) {
                    try {
                        String hash = getHash(currentInput, hashType);
                        resultEditText.setText(hash);
                    } catch (NoSuchAlgorithmException e) {
                        resultEditText.setText("Error");
                    }
                }
            }
        });
    }

    private boolean isValidNumber(String number) {
        if (currentBase == 10) {
            return true;
        }
        try {
            Long.parseLong(number, currentBase);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String getHash(String input, String hashType) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(hashType);
        byte[] hashBytes = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private double parseDouble(String input, int base) {
        if (base == 10) {
            return Double.parseDouble(input);
        } else {
            long longValue = Long.parseLong(input, base);
            return (double) longValue;
        }
    }

    public static Double Div(Double d1, Double d2) {
        if (d1 == Double.NEGATIVE_INFINITY || d1 == Double.POSITIVE_INFINITY || d2 == Double.NEGATIVE_INFINITY || d2 == Double.POSITIVE_INFINITY) {
            return d1 / d2;
        }
        if (String.valueOf(d1).equals("NaN") || String.valueOf(d2).equals("NaN")) {
            return d1 / d2;
        }
        if (d1 == 0.0 && d2 == 0.0) {
            return Double.NaN;
        }
        if (d2 == 0.0) {
            return d1 / d2;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.divide(b2, 8, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
