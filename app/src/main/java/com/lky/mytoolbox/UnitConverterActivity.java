package com.lky.mytoolbox;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UnitConverterActivity extends AppCompatActivity {

    private EditText inputValue;
    private Spinner categorySpinner;
    private Spinner unitFromSpinner;
    private Spinner unitToSpinner;
    private Button switchButton;
    private TextView resultText;
    private Button seven;
    private Button eight;
    private Button nine;
    private Button backspace;
    private Button four;
    private Button five;
    private Button six;
    private Button one;
    private Button two;
    private Button three;
    private Button cls;
    private Button zero;
    private Button spot;
    private Map<String, Map<String, Double>> conversionRates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unitconverter);

        inputValue = findViewById(R.id.input_value);
        categorySpinner = findViewById(R.id.category_spinner);
        unitFromSpinner = findViewById(R.id.unit_from_spinner);
        unitToSpinner = findViewById(R.id.unit_to_spinner);
        switchButton = findViewById(R.id.switch_button);
        resultText = findViewById(R.id.result_text);
        // 初始化计算器按钮
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        nine = findViewById(R.id.nine);
        backspace = findViewById(R.id.backspace);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        cls = findViewById(R.id.cls);
        zero = findViewById(R.id.zero);
        spot = findViewById(R.id.spot);


        setupConversionRates();
        setupCategorySpinner();
        setupInputListener();
        setupSwitchButton();
        setupCalculatorButtons();
    }
    private void setupCalculatorButtons() {
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendToInput("7");
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendToInput("8");
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendToInput("9");
            }
        });

        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = inputValue.getText().toString();
                if (currentText.length() > 0) {
                    inputValue.setText(currentText.substring(0, currentText.length() - 1));
                }
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendToInput("4");
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendToInput("5");
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendToInput("6");
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendToInput("1");
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendToInput("2");
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendToInput("3");
            }
        });

        cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValue.setText("");
                resultText.setText("结果");
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendToInput("0");
            }
        });

        spot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendToInput(".");
            }
        });
    }
    private void appendToInput(String value) {
        String currentText = inputValue.getText().toString();
        inputValue.setText(currentText + value);
    }
    private void setupConversionRates() {
        conversionRates = new HashMap<>();

        Map<String, Double> lengthRates = new HashMap<>();
        lengthRates.put("纳米", 1e-9);
        lengthRates.put("微米", 1e-6);
        lengthRates.put("毫米", 1e-3);
        lengthRates.put("厘米", 1e-2);
        lengthRates.put("分米", 1e-1);
        lengthRates.put("米", 1.0);
        lengthRates.put("十米", 10.0);
        lengthRates.put("百米", 100.0);
        lengthRates.put("千米", 1000.0);
        lengthRates.put("英寸", 0.0254);
        lengthRates.put("英尺", 0.3048);
        lengthRates.put("码", 0.9144);
        lengthRates.put("海里", 1852.0);
        lengthRates.put("兆米", 1e6);
        conversionRates.put("长度", lengthRates);

        Map<String, Double> volumeRates = new HashMap<>();
        volumeRates.put("微升", 1e-9);
        volumeRates.put("滴", 5e-10); // 假设1滴 = 0.05毫升
        volumeRates.put("毫升", 1e-6);
        volumeRates.put("立方厘米", 1e-6);
        volumeRates.put("分升", 1e-4);
        volumeRates.put("升", 1e-3);
        volumeRates.put("立方米", 1.0);
        volumeRates.put("美制杯", 2.36588e-4);
        volumeRates.put("英制杯", 2.84131e-4);
        volumeRates.put("美制品脱", 0.00113652);
        volumeRates.put("英制品脱", 0.00142065);
        volumeRates.put("美制夸脱", 0.00454609);
        volumeRates.put("英制夸脱", 0.00473176);
        volumeRates.put("美制加仑", 0.00378541);
        volumeRates.put("英制加仑", 0.00454609);
        volumeRates.put("干", 0.00378541); // 美制加仑
        volumeRates.put("液", 0.00454609); // 英制加仑
        volumeRates.put("美国联邦和石油桶", 0.158987);
        volumeRates.put("英制桶", 0.153722);
        volumeRates.put("立方英寸", 1.63871e-5);
        volumeRates.put("立方英尺", 0.0283168);
        volumeRates.put("立方码", 0.764555);
        volumeRates.put("美国茶匙", 4.92892e-9);
        volumeRates.put("英制茶匙", 5.91939e-9);
        volumeRates.put("美国餐桌汤匙", 1.47868e-8);
        volumeRates.put("英制餐桌汤匙", 1.77582e-8);
        volumeRates.put("美制液体盎司", 2.95735e-5);
        volumeRates.put("英制液体盎司", 2.84131e-5);
        conversionRates.put("体积", volumeRates);

        // 面积
        Map<String, Double> areaRates = new HashMap<>();
        areaRates.put("平方毫米", 1e-6);
        areaRates.put("平方厘米", 1e-4);
        areaRates.put("平方分米", 1e-2);
        areaRates.put("平方米", 1.0);
        areaRates.put("平方十米", 100.0);
        areaRates.put("平方百米", 10000.0);
        areaRates.put("平方千米", 1e6);
        areaRates.put("公顷", 1e4);
        areaRates.put("平方英里", 2.58999e6);
        areaRates.put("英亩", 4046.86);
        areaRates.put("平方英寸", 6.4516e-4);
        areaRates.put("平方英尺", 0.092903);
        areaRates.put("平方码", 0.836127);
        areaRates.put("平方海里", 3.4299e7);
        areaRates.put("杜纳姆", 400.0);
        areaRates.put("坪", 3.3058);
        areaRates.put("石弓地", 2529.29);
        areaRates.put("平方公里", 1e6);
        conversionRates.put("面积", areaRates);

        // 温度
        Map<String, Double> temperatureRates = new HashMap<>();
        temperatureRates.put("摄氏度", 1.0);
        temperatureRates.put("华氏度", 33.8);
        conversionRates.put("温度", temperatureRates);

        // 速度
        Map<String, Double> speedRates = new HashMap<>();
        speedRates.put("厘米每秒", 1e-2);
        speedRates.put("米每秒", 1.0);
        speedRates.put("公里每小时", 0.001);
        speedRates.put("英里每小时", 0.000277778);
        speedRates.put("节", 0.000514444);
        speedRates.put("英尺每秒", 0.3048);
        speedRates.put("英尺每分钟", 0.00508);
        speedRates.put("英寸每秒", 0.0254);
        speedRates.put("马赫", 299792.458);
        conversionRates.put("速度", speedRates);
        // 角
        Map<String, Double> angleRates = new HashMap<>();
        angleRates.put("度", 1.0);
        angleRates.put("弧度", 0.0174533);
        angleRates.put("梯度", 0.015708);
        angleRates.put("转", 6.28319);
        angleRates.put("毫弧度", 0.0000174533);
        angleRates.put("弧度分", 0.000290888);
        angleRates.put("弧度秒", 0.00000484814);
        conversionRates.put("角", angleRates);

        // 压力
        Map<String, Double> pressureRates = new HashMap<>();
        pressureRates.put("大气压", 101325.0);
        pressureRates.put("托", 101325.0);
        pressureRates.put("帕斯卡", 1.0);
        pressureRates.put("千帕", 1000.0);
        pressureRates.put("千磅每平方英寸", 68947.6);
        pressureRates.put("psi", 6894.76);
        pressureRates.put("英尺海水（15C）", 9806.65);
        pressureRates.put("巴", 100000.0);
        pressureRates.put("千克每平方厘米", 98066.5);
        pressureRates.put("巴力", 100000.0);
        pressureRates.put("斯塔恩", 98066.5);
        pressureRates.put("毫米汞柱", 133.322);
        conversionRates.put("压力", pressureRates);


        // 功率
        Map<String, Double> powerRates = new HashMap<>();
        powerRates.put("瓦特", 1.0);
        powerRates.put("千瓦", 1000.0);
        powerRates.put("马力", 745.7);
        powerRates.put("每秒尔格", 1e-7);
        powerRates.put("每分钟英尺-磅", 0.0135582);
        powerRates.put("分贝毫瓦", 1.0);
        powerRates.put("每小时卡路里", 1.16222);
        powerRates.put("每小时BTU", 0.293071);
        powerRates.put("冷却吨", 3516.85);
        conversionRates.put("功率", powerRates);

        // 重量/质量
        Map<String, Double> weightRates = new HashMap<>();
        weightRates.put("克拉", 2e-11);
        weightRates.put("毫克", 1e-6);
        weightRates.put("厘克", 1e-5);
        weightRates.put("德克", 1e-4);
        weightRates.put("克", 1e-3);
        weightRates.put("代克", 1e-2);
        weightRates.put("千克", 1.0);
        weightRates.put("地球牛顿", 5.972e24);
        weightRates.put("吨", 1000.0);
        weightRates.put("盎司", 0.0283495);
        weightRates.put("磅", 0.453592);
        weightRates.put("英石", 6.35029);
        weightRates.put("斤", 0.5);
        weightRates.put("粒", 6.47989e-9);
        conversionRates.put("重量/质量", weightRates);

    }

    private void setupCategorySpinner() {
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(conversionRates.keySet()));
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                setupUnitSpinners(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupUnitSpinners(String category) {
        Map<String, Double> units = conversionRates.get(category);
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(units.keySet()));
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        unitFromSpinner.setAdapter(unitAdapter);
        unitToSpinner.setAdapter(unitAdapter);

        // 设置 OnItemSelectedListener
        unitFromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateAndDisplayResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        unitToSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateAndDisplayResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
    private void calculateAndDisplayResult() {
        String inputText = inputValue.getText().toString();
        if (inputText.isEmpty()) {
            resultText.setText("结果");
            return;
        }

        try {
            double inputValue = Double.parseDouble(inputText);
            String fromUnit = (String) unitFromSpinner.getSelectedItem();
            String toUnit = (String) unitToSpinner.getSelectedItem();
            String category = (String) categorySpinner.getSelectedItem();

            double result = convert(inputValue, fromUnit, toUnit, category);
            resultText.setText(String.format("%.4f", result));
        } catch (NumberFormatException e) {
            resultText.setText("无效的输入");
        }
    }
    private void setupInputListener() {
        inputValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputText = s.toString();
                if (inputText.isEmpty()) {
                    resultText.setText("结果");
                    return;
                }

                try {
                    double inputValue = Double.parseDouble(inputText);
                    String fromUnit = (String) unitFromSpinner.getSelectedItem();
                    String toUnit = (String) unitToSpinner.getSelectedItem();
                    String category = (String) categorySpinner.getSelectedItem();

                    double result = convert(inputValue, fromUnit, toUnit, category);
                    resultText.setText(String.format("%.4f", result));
                } catch (NumberFormatException e) {
                    resultText.setText("无效的输入");
                }
            }
        });
    }

    private void setupSwitchButton() {
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromUnit = (String) unitFromSpinner.getSelectedItem();
                String toUnit = (String) unitToSpinner.getSelectedItem();

                unitFromSpinner.setSelection(((ArrayAdapter<String>) unitFromSpinner.getAdapter()).getPosition(toUnit));
                unitToSpinner.setSelection(((ArrayAdapter<String>) unitToSpinner.getAdapter()).getPosition(fromUnit));

                // 清空输入框和结果
                inputValue.setText("");
                resultText.setText("结果");
            }
        });
    }

    private double convert(double value, String fromUnit, String toUnit, String category) {
        Map<String, Double> units = conversionRates.get(category);
        double fromRate = units.get(fromUnit);
        double toRate = units.get(toUnit);

        // Convert to base unit
        double baseValue = value * fromRate;

        // Convert from base unit to target unit
        return baseValue / toRate;
    }
}
