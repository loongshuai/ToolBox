package com.lky.mytoolbox;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

public class ScientificCalculatorActivity extends AppCompatActivity {

    private  StringBuilder show_equation=new StringBuilder();//显示运算式
    private  ArrayList calculate_equation;//计算式
    private EditText result;
    private  int signal=0;//为0 时表示刚输入状态；为1 时表示当前在输出结果上继续输入
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scientificcalculator);
        //初始化
        show_equation=new StringBuilder();
        calculate_equation=new ArrayList<>();
        Button zero=(Button)findViewById(R.id.zero);
        Button one=(Button)findViewById(R.id.one);
        Button two=(Button)findViewById(R.id.two);
        Button three=(Button)findViewById(R.id.three);
        Button four=(Button)findViewById(R.id.four);
        Button five=(Button)findViewById(R.id.five);
        Button six=(Button)findViewById(R.id.six);
        Button seven=(Button)findViewById(R.id.seven);
        Button eight=(Button)findViewById(R.id.eight);
        Button nine=(Button)findViewById(R.id.nine);
        Button cls=(Button)findViewById(R.id.cls);
        Button div=(Button)findViewById(R.id.div);
        Button mul=(Button)findViewById(R.id.mul);
        Button backspace=(Button)findViewById(R.id.Backspace);
        Button sub=(Button)findViewById(R.id.sub);
        Button add=(Button)findViewById(R.id.add);
        Button square_root=(Button)findViewById(R.id.square_root);
        Button Triple_root=(Button)findViewById(R.id.Triple_root);
        Button XsquareY=(Button)findViewById(R.id.XsquareY);
        Button x_power_y=(Button)findViewById(R.id.x_power_y);
        Button x_power_2=(Button)findViewById(R.id.x_power_2);
        Button x_power_3=(Button)findViewById(R.id.x_power_3);
        Button left=(Button)findViewById(R.id.left);
        Button right=(Button)findViewById(R.id.right);
        Button pai=(Button)findViewById(R.id.pai);
        Button sin=(Button)findViewById(R.id.sin);
        Button cox=(Button)findViewById(R.id.cox);
        Button tan=(Button)findViewById(R.id.tan);

        final Button equal=(Button)findViewById(R.id.equal);
        final Button point=(Button)findViewById(R.id.spot);
        result = (EditText) findViewById(R.id.result);
        result.setCursorVisible(true);
        disableShowInput(result);
        //点击文本框时光标始终在文本末尾
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setSelection(result.getText().length());
            }
        });
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(!(show_equation.toString().equals("0"))){
                    if(signal==0){
                        show_equation.append("0");
                        //显示运算式
                        result.setText(show_equation);
                        //将光标定位到文本末尾
                        result.setSelection(result.getText().length());
                    }else{
                        show_equation.delete(0,show_equation.length());
                        show_equation.append("0");
                        result.setText(show_equation);
                        result.setSelection(result.getText().length());
                        signal=0;
                    }
                }
            }
        });
        //接下来1到9每个控件依次进行此设置
        //保证若是在结果上进行输入时清除结果然后显示点击的数字
        //若是正常输入则直接在运算式末尾加上点击的数字并显示
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    show_equation.append("1");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                }else{
                    show_equation.delete(0,show_equation.length());
                    show_equation.append("1");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                    signal=0;
                }
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    show_equation.append("2");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                }else{
                    show_equation.delete(0,show_equation.length());
                    show_equation.append("2");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                    signal=0;
                }
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    show_equation.append("3");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                }else{
                    show_equation.delete(0,show_equation.length());
                    show_equation.append("3");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                    signal=0;
                }
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    show_equation.append("4");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                }else{
                    show_equation.delete(0,show_equation.length());
                    show_equation.append("4");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                    signal=0;
                }
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    show_equation.append("5");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                }else{
                    show_equation.delete(0,show_equation.length());
                    show_equation.append("5");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                    signal=0;
                }
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    show_equation.append("6");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                }else{
                    show_equation.delete(0,show_equation.length());
                    show_equation.append("6");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                    signal=0;
                }
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    show_equation.append("7");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                }else{
                    show_equation.delete(0,show_equation.length());
                    show_equation.append("7");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                    signal=0;
                }
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    show_equation.append("8");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                }else{
                    show_equation.delete(0,show_equation.length());
                    show_equation.append("8");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                    signal=0;
                }
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0){
                    show_equation.append("9");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                }else{
                    show_equation.delete(0,show_equation.length());
                    show_equation.append("9");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                    signal=0;
                }
            }
        });
        //清屏
        cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_equation.delete(0,show_equation.length());
                calculate_equation.clear();
                signal=0;
                result.setText("");
            }
        });
        //后退键
        //若在结果上使用，则直接清屏
        //正常输入时使用，后退一格
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(show_equation.toString().equals(""))) {
                    if(signal==0){
                        show_equation.deleteCharAt(show_equation.length() - 1);
                        result.setText(show_equation);
                        result.setSelection(result.getText().length());
                    }else{
                        show_equation.delete(0,show_equation.length());
                        result.setText("");
                        signal=0;
                    }
                }
            }
        });
        //小数点的点击事件
        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //正常输入时点击小数点的处理逻辑
                if(signal==0){
                    //把运算式赋给字符串a
                    String a=show_equation.toString();
                    //运算式为空，直接加一个小数点并显示
                    if(a.equals("")){
                        show_equation.append(".");
                        result.setText(show_equation);
                        result.setSelection(result.getText().length());
                    }
                    //运算式不为空
                    else{
                        int i;
                        char t='0';
                        //从运算式末尾向前遍历，碰到'.''+''-''*''/'后结束遍历退出
                        for(i=a.length();i>0;i--){
                            t=a.charAt(i-1);
                            if(t=='.'||t=='+'||t=='-'||t=='*'||t=='/')
                                break;
                        }
                        //i==0表示遍历运算式没有发现'.''+''-''*''/'，则直接在运算式末尾加小数点
                        if(i==0){
                            show_equation.append(".");
                            result.setText(show_equation);
                            result.setSelection(result.getText().length());
                        }
                        //在碰到小数点前碰到了'+''-''*''/'，也直接在运算式末尾加小数点
                        else if(t=='+'||t=='-'||t=='*'||t=='/'){
                            show_equation.append(".");
                            result.setText(show_equation);
                            result.setSelection(result.getText().length());
                        }
                        //以上条件均不满足，若说明遍历碰到了小数点，因为一个数不能同时有两个小数点，所以此次点击小数点不做处理
                    }
                }
                //在结果上点击小数点，直接清屏然后加上小数点并显示
                else{
                    show_equation.delete(0,show_equation.length());
                    show_equation.append(".");
                    result.setText(".");
                    result.setSelection(result.getText().length());
                    signal=0;
                }
            }
        });

        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断用户是否输入了内容
                if (!show_equation.toString().equals("") && !show_equation.toString().equals("错误")) {
                    signal = 1; // 表示在输入结果上
                    char temp = show_equation.charAt(show_equation.length() - 1); // 把运算式的最后一个字符赋给temp
                    if (show_equation.charAt(0) == '-') // 如果运算式的第一个字符是'-'，说明用户是想输入一个负数
                        show_equation.insert(0, "0"); // 此时在运算式的最前面加一个0,用'0-运算数'表示负数
                    if (temp == '+' || temp == '-') // 若为加减
                        show_equation.append("0"); // 则结尾默认加减零
                    if (temp == '*' || temp == '/') // 若为乘除
                        show_equation.append("1"); // 则结尾默认乘除1

                    String expression = show_equation.toString();
                    while (true) {
                        int leftParenIndex = expression.lastIndexOf("(");
                        int rightParenIndex = expression.indexOf(")", leftParenIndex);

                        if (leftParenIndex == -1 || rightParenIndex == -1) break;

                        String subExpression = expression.substring(leftParenIndex + 1, rightParenIndex);
                        ArrayList<String> subEquation = parseExpression(subExpression);
                        String subResult = calculate(subEquation);

                        if (subResult.equals("错误")) {
                            handleError();
                            return;
                        }
                        StringBuilder sb = new StringBuilder(expression);
                        sb.replace(leftParenIndex, rightParenIndex + 1, subResult);
                        expression = sb.toString();
                    }

                    // 解析整个表达式
                    ArrayList<String> equation = parseExpression(expression);
                    equation.add("#");
                    // 调用calculate计算出结果返回给temp8
                    String temp8 = calculate(equation);
                    result.setText(temp8);
                    result.setSelection(result.getText().length());
                    // 不要清空show_equation，保留原始表达式
                    // show_equation.delete(0, show_equation.length());
                    // calculate_equation.clear();
                    // 将结果赋给运算式
                    // show_equation.append(temp8);
                }
            }


        });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断用户是否输入了内容
                if(!(show_equation.toString().equals(""))&&!(show_equation.toString().equals("错误"))) {
                    signal=0;
                    char temp=show_equation.charAt(show_equation.length()-1);
                    if(temp=='+'||temp=='-'||temp=='*'||temp=='/')
                    {
                        show_equation.deleteCharAt(show_equation.length()-1);
                        show_equation.append("+");
                    }
                    else
                        show_equation.append("+");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                }
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断用户是否输入了内容
                if(!(show_equation.toString().equals(""))&&!(show_equation.toString().equals("错误"))) {
                    signal=0;
                    char temp=show_equation.charAt(show_equation.length()-1);
                    if(temp=='+'||temp=='-'||temp=='*'||temp=='/')
                    {
                        show_equation.deleteCharAt(show_equation.length()-1);
                        show_equation.append("-");
                    }
                    else
                        show_equation.append("-");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                }
                else if(!(show_equation.toString().equals("错误"))){
                    signal=0;
                    show_equation.append("-");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                }
            }
        });

        mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断用户是否输入了内容
                if(!(show_equation.toString().equals(""))&&!(show_equation.toString().equals("错误"))) {
                    signal=0;
                    char temp=show_equation.charAt(show_equation.length()-1);
                    if(temp=='+'||temp=='-'||temp=='*'||temp=='/')
                    {
                        show_equation.deleteCharAt(show_equation.length()-1);
                        show_equation.append("*");
                    }
                    else
                        show_equation.append("*");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                }
            }
        });

        div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断用户是否输入了内容
                if(!(show_equation.toString().equals(""))&&!(show_equation.toString().equals("错误"))) {
                    signal=0;
                    char temp=show_equation.charAt(show_equation.length()-1);
                    if(temp=='+'||temp=='-'||temp=='*'||temp=='/')
                    {
                        show_equation.deleteCharAt(show_equation.length()-1);
                        show_equation.append("/");
                    }
                    else
                        show_equation.append("/");
                    result.setText(show_equation);
                    result.setSelection(result.getText().length());
                }
            }
        });

        // 平方根按钮
        square_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!show_equation.toString().isEmpty() && !show_equation.toString().equals("错误")) {
                    try {
                        String expression = show_equation.toString();
                        int lastParenthesisIndex = expression.lastIndexOf(')');
                        int startParenthesisIndex = -1;

                        // 检查是否以 ")" 结尾，并找到匹配的 "("
                        if (lastParenthesisIndex == expression.length() - 1) {
                            int parenthesisCount = 0;
                            for (int i = lastParenthesisIndex; i >= 0; i--) {
                                char c = expression.charAt(i);
                                if (c == ')') {
                                    parenthesisCount++;
                                } else if (c == '(') {
                                    parenthesisCount--;
                                    if (parenthesisCount == 0) {
                                        startParenthesisIndex = i;
                                        break;
                                    }
                                }
                            }

                            // 如果找到匹配的括号对
                            if (startParenthesisIndex != -1) {
                                // 提取括号内的子表达式
                                String subExpression = expression.substring(startParenthesisIndex + 1, lastParenthesisIndex);

                                // 计算括号内的值
                                ArrayList<String> subEquation = parseExpression(subExpression);
                                String subResult = calculate(subEquation);

                                if (!subResult.equals("错误")) {
                                    double subValue = Double.parseDouble(subResult);

                                    // 替换整个括号表达式为计算结果
                                    show_equation.replace(startParenthesisIndex, lastParenthesisIndex + 1, String.valueOf(subValue));

                                    // 继续执行平方逻辑
                                    double sqrt = Math.sqrt(subValue);
                                    if (Double.isNaN(sqrt)) {
                                        handleError();
                                    } else {
                                        replaceLastNumber(sqrt);
                                    }
                                    return;
                                } else {
                                    handleError();
                                    return;
                                }
                            } else {
                                // 括号不完整，显示错误
                                handleError();
                                return;
                            }
                        }
                        double num = getCurrentNumberFromResult();
                        double sqrt = Math.sqrt(num);
                        if (Double.isNaN(sqrt)) {
                            handleError();
                        } else {
                            replaceLastNumber(sqrt);
                        }
                    } catch (NumberFormatException e) {
                        handleError();
                    }
                }
            }
        });

// 立方根按钮
        Triple_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!show_equation.toString().isEmpty() && !show_equation.toString().equals("错误")) {
                    try {
                        String expression = show_equation.toString();
                        int lastParenthesisIndex = expression.lastIndexOf(')');
                        int startParenthesisIndex = -1;

                        // 检查是否以 ")" 结尾，并找到匹配的 "("
                        if (lastParenthesisIndex == expression.length() - 1) {
                            int parenthesisCount = 0;
                            for (int i = lastParenthesisIndex; i >= 0; i--) {
                                char c = expression.charAt(i);
                                if (c == ')') {
                                    parenthesisCount++;
                                } else if (c == '(') {
                                    parenthesisCount--;
                                    if (parenthesisCount == 0) {
                                        startParenthesisIndex = i;
                                        break;
                                    }
                                }
                            }

                            // 如果找到匹配的括号对
                            if (startParenthesisIndex != -1) {
                                // 提取括号内的子表达式
                                String subExpression = expression.substring(startParenthesisIndex + 1, lastParenthesisIndex);

                                // 计算括号内的值
                                ArrayList<String> subEquation = parseExpression(subExpression);
                                String subResult = calculate(subEquation);

                                if (!subResult.equals("错误")) {
                                    double subValue = Double.parseDouble(subResult);

                                    // 替换整个括号表达式为计算结果
                                    show_equation.replace(startParenthesisIndex, lastParenthesisIndex + 1, String.valueOf(subValue));

                                    // 继续执行平方逻辑
                                    double cbrt = Math.cbrt(subValue);
                                    replaceLastNumber(cbrt);
                                    return;
                                } else {
                                    handleError();
                                    return;
                                }
                            } else {
                                // 括号不完整，显示错误
                                handleError();
                                return;
                            }
                        }
                        double num = getCurrentNumberFromResult();
                        double cbrt = Math.cbrt(num);
                        replaceLastNumber(cbrt);
                    } catch (NumberFormatException e) {
                        handleError();
                    }
                }
            }
        });

// x的y次方按钮
        x_power_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!show_equation.toString().isEmpty() && !show_equation.toString().equals("错误")) {
                    String expression = show_equation.toString();
                    int lastParenthesisIndex = expression.lastIndexOf(')');
                    int startParenthesisIndex = -1;

                    // 检查是否以 ")" 结尾，并找到匹配的 "("
                    if (lastParenthesisIndex == expression.length() - 1) {
                        int parenthesisCount = 0;
                        for (int i = lastParenthesisIndex; i >= 0; i--) {
                            char c = expression.charAt(i);
                            if (c == ')') {
                                parenthesisCount++;
                            } else if (c == '(') {
                                parenthesisCount--;
                                if (parenthesisCount == 0) {
                                    startParenthesisIndex = i;
                                    break;
                                }
                            }
                        }

                        // 如果找到匹配的括号对
                        if (startParenthesisIndex != -1) {
                            // 提取括号内的子表达式
                            String subExpression = expression.substring(startParenthesisIndex + 1, lastParenthesisIndex);

                            // 计算括号内的值
                            ArrayList<String> subEquation = parseExpression(subExpression);
                            String subResult = calculate(subEquation);

                            if (!subResult.equals("错误")) {
                                double subValue = Double.parseDouble(subResult);

                                // 替换整个括号表达式为计算结果
                                show_equation.replace(startParenthesisIndex, lastParenthesisIndex + 1, String.valueOf(subValue));

                                showInputDialog("输入y的值", new InputCallback() {
                                    @Override
                                    public void onInput(String input) {
                                        try {
                                            double y = Double.parseDouble(input);
                                            if (y == 0) {
                                                handleError();
                                                return;
                                            }
                                            double result = Math.pow(subValue, y);
                                            replaceLastNumber(result);
                                        } catch (NumberFormatException e) {
                                            handleError();
                                        }
                                    }
                                });}
                        }
                    }

                    final double x = getCurrentNumberFromResult();
                    showInputDialog("输入y的值", new InputCallback() {
                        @Override
                        public void onInput(String input) {
                            try {
                                double y = Double.parseDouble(input);
                                if (y == 0) {
                                    handleError();
                                    return;
                                }
                                double result = Math.pow(x, y);
                                replaceLastNumber(result);
                            } catch (NumberFormatException e) {
                                handleError();
                            }
                        }
                    });
                }
            }
        });

// x的平方按钮
        x_power_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!show_equation.toString().isEmpty() && !show_equation.toString().equals("错误")) {
                    try {
                        String expression = show_equation.toString();
                        int lastParenthesisIndex = expression.lastIndexOf(')');
                        int startParenthesisIndex = -1;

                        // 检查是否以 ")" 结尾，并找到匹配的 "("
                        if (lastParenthesisIndex == expression.length() - 1) {
                            int parenthesisCount = 0;
                            for (int i = lastParenthesisIndex; i >= 0; i--) {
                                char c = expression.charAt(i);
                                if (c == ')') {
                                    parenthesisCount++;
                                } else if (c == '(') {
                                    parenthesisCount--;
                                    if (parenthesisCount == 0) {
                                        startParenthesisIndex = i;
                                        break;
                                    }
                                }
                            }

                            // 如果找到匹配的括号对
                            if (startParenthesisIndex != -1) {
                                // 提取括号内的子表达式
                                String subExpression = expression.substring(startParenthesisIndex + 1, lastParenthesisIndex);

                                // 计算括号内的值
                                ArrayList<String> subEquation = parseExpression(subExpression);
                                String subResult = calculate(subEquation);

                                if (!subResult.equals("错误")) {
                                    double subValue = Double.parseDouble(subResult);

                                    // 替换整个括号表达式为计算结果
                                    show_equation.replace(startParenthesisIndex, lastParenthesisIndex + 1, String.valueOf(subValue));

                                    // 继续执行平方逻辑
                                    double squaredValue = subValue * subValue;
                                    replaceLastNumber(squaredValue);
                                    return;
                                } else {
                                    handleError();
                                    return;
                                }
                            } else {
                                // 括号不完整，显示错误
                                handleError();
                                return;
                            }
                        }
                        double num = getCurrentNumberFromResult();
                        double result = num * num;
                        replaceLastNumber(result);
                    } catch (NumberFormatException e) {
                        handleError();
                    }
                }
            }
        });
// x的立方按钮
        x_power_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!show_equation.toString().isEmpty() && !show_equation.toString().equals("错误")) {
                    try {
                        String expression = show_equation.toString();
                        int lastParenthesisIndex = expression.lastIndexOf(')');
                        int startParenthesisIndex = -1;

                        // 检查是否以 ")" 结尾，并找到匹配的 "("
                        if (lastParenthesisIndex == expression.length() - 1) {
                            int parenthesisCount = 0;
                            for (int i = lastParenthesisIndex; i >= 0; i--) {
                                char c = expression.charAt(i);
                                if (c == ')') {
                                    parenthesisCount++;
                                } else if (c == '(') {
                                    parenthesisCount--;
                                    if (parenthesisCount == 0) {
                                        startParenthesisIndex = i;
                                        break;
                                    }
                                }
                            }

                            // 如果找到匹配的括号对
                            if (startParenthesisIndex != -1) {
                                // 提取括号内的子表达式
                                String subExpression = expression.substring(startParenthesisIndex + 1, lastParenthesisIndex);

                                // 计算括号内的值
                                ArrayList<String> subEquation = parseExpression(subExpression);
                                String subResult = calculate(subEquation);

                                if (!subResult.equals("错误")) {
                                    double subValue = Double.parseDouble(subResult);

                                    // 替换整个括号表达式为计算结果
                                    show_equation.replace(startParenthesisIndex, lastParenthesisIndex + 1, String.valueOf(subValue));

                                    // 继续执行平方逻辑
                                    double result = subValue * subValue * subValue;
                                    replaceLastNumber(result);
                                    return;
                                } else {
                                    handleError();
                                    return;
                                }
                            } else {
                                // 括号不完整，显示错误
                                handleError();
                                return;
                            }
                        }
                        double num = getCurrentNumberFromResult();
                        double result = num * num * num;
                        replaceLastNumber(result);
                    } catch (NumberFormatException e) {
                        handleError();
                    }
                }
            }
        });

// x的y次方根按钮
        XsquareY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!show_equation.toString().isEmpty() && !show_equation.toString().equals("错误")) {
                    String expression = show_equation.toString();
                    int lastParenthesisIndex = expression.lastIndexOf(')');
                    int startParenthesisIndex = -1;

                    // 检查是否以 ")" 结尾，并找到匹配的 "("
                    if (lastParenthesisIndex == expression.length() - 1) {
                        int parenthesisCount = 0;
                        for (int i = lastParenthesisIndex; i >= 0; i--) {
                            char c = expression.charAt(i);
                            if (c == ')') {
                                parenthesisCount++;
                            } else if (c == '(') {
                                parenthesisCount--;
                                if (parenthesisCount == 0) {
                                    startParenthesisIndex = i;
                                    break;
                                }
                            }
                        }

                        // 如果找到匹配的括号对
                        if (startParenthesisIndex != -1) {
                            // 提取括号内的子表达式
                            String subExpression = expression.substring(startParenthesisIndex + 1, lastParenthesisIndex);

                            // 计算括号内的值
                            ArrayList<String> subEquation = parseExpression(subExpression);
                            String subResult = calculate(subEquation);

                            if (!subResult.equals("错误")) {
                                double subValue = Double.parseDouble(subResult);

                                // 替换整个括号表达式为计算结果
                                show_equation.replace(startParenthesisIndex, lastParenthesisIndex + 1, String.valueOf(subValue));

                                showInputDialog("输入y的值", new InputCallback() {
                                    @Override
                                    public void onInput(String input) {
                                        try {
                                            double y = Double.parseDouble(input);
                                            if (y == 0) {
                                                handleError();
                                                return;
                                            }
                                            double result = Math.pow(subValue, 1.0 / y);
                                            replaceLastNumber(result);
                                        } catch (NumberFormatException e) {
                                            handleError();
                                        }
                                    }
                                });
                                return;
                            } else {
                                handleError();
                                return;
                            }
                        } else {
                            // 括号不完整，显示错误
                            handleError();
                            return;
                        }
                    }



                    final double x = getCurrentNumberFromResult();
                    showInputDialog("输入y的值", new InputCallback() {
                        @Override
                        public void onInput(String input) {
                            try {
                                double y = Double.parseDouble(input);
                                if (y == 0) {
                                    handleError();
                                    return;
                                }
                                double result = Math.pow(x, 1.0 / y);
                                replaceLastNumber(result);
                            } catch (NumberFormatException e) {
                                handleError();
                            }
                        }
                    });
                }
            }
        });
// 左括号按钮
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleParenthesis("(");
            }
        });

// 右括号按钮
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleParenthesis(")");
            }
        });

// π按钮
        pai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expression = show_equation.toString();
                int lastParenthesisIndex = expression.lastIndexOf(')');
                int startParenthesisIndex = -1;

                // 检查是否以 ")" 结尾，并找到匹配的 "("
                if (lastParenthesisIndex == expression.length() - 1) {
                    int parenthesisCount = 0;
                    for (int i = lastParenthesisIndex; i >= 0; i--) {
                        char c = expression.charAt(i);
                        if (c == ')') {
                            parenthesisCount++;
                        } else if (c == '(') {
                            parenthesisCount--;
                            if (parenthesisCount == 0) {
                                startParenthesisIndex = i;
                                break;
                            }
                        }
                    }

                    // 如果找到匹配的括号对
                    if (startParenthesisIndex != -1) {
                        // 提取括号内的子表达式
                        String subExpression = expression.substring(startParenthesisIndex + 1, lastParenthesisIndex);

                        // 计算括号内的值
                        ArrayList<String> subEquation = parseExpression(subExpression);
                        String subResult = calculate(subEquation);

                        if (!subResult.equals("错误")) {
                            double subValue = Double.parseDouble(subResult);

                            // 替换整个括号表达式为计算结果
                            show_equation.replace(startParenthesisIndex, lastParenthesisIndex + 1, String.valueOf(subValue));

                            if (signal == 1) {
                                show_equation.setLength(0);
                                signal = 0;
                            }
                            String current = show_equation.toString();
                            if (!current.isEmpty() && (Character.isDigit(current.charAt(current.length() - 1)) || current.charAt(current.length() - 1) == ')')) {
                                show_equation.append("*");
                            }
                            replaceLastNumber(Math.PI);
                            return;
                        } else {
                            handleError();
                            return;
                        }
                    } else {
                        // 括号不完整，显示错误
                        handleError();
                        return;
                    }
                }
                if (signal == 1) {
                    show_equation.setLength(0);
                    signal = 0;
                }
                String current = show_equation.toString();
                if (!current.isEmpty() && (Character.isDigit(current.charAt(current.length() - 1)) || current.charAt(current.length() - 1) == ')')) {
                    show_equation.append("*");
                }
                replaceLastNumber(Math.PI);
            }
        });

// 正弦按钮
        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!show_equation.toString().isEmpty() && !show_equation.toString().equals("错误")) {
                    try {
                        String expression = show_equation.toString();
                        int lastParenthesisIndex = expression.lastIndexOf(')');
                        int startParenthesisIndex = -1;

                        // 检查是否以 ")" 结尾，并找到匹配的 "("
                        if (lastParenthesisIndex == expression.length() - 1) {
                            int parenthesisCount = 0;
                            for (int i = lastParenthesisIndex; i >= 0; i--) {
                                char c = expression.charAt(i);
                                if (c == ')') {
                                    parenthesisCount++;
                                } else if (c == '(') {
                                    parenthesisCount--;
                                    if (parenthesisCount == 0) {
                                        startParenthesisIndex = i;
                                        break;
                                    }
                                }
                            }

                            // 如果找到匹配的括号对
                            if (startParenthesisIndex != -1) {
                                // 提取括号内的子表达式
                                String subExpression = expression.substring(startParenthesisIndex + 1, lastParenthesisIndex);

                                // 计算括号内的值
                                ArrayList<String> subEquation = parseExpression(subExpression);
                                String subResult = calculate(subEquation);

                                if (!subResult.equals("错误")) {
                                    double subValue = Double.parseDouble(subResult);

                                    // 替换整个括号表达式为计算结果
                                    show_equation.replace(startParenthesisIndex, lastParenthesisIndex + 1, String.valueOf(subValue));

                                    // 继续执行平方逻辑
                                    double radians = Math.toRadians(subValue);
                                    double result = Math.sin(radians);
                                    replaceLastNumber(result);
                                    return;
                                } else {
                                    handleError();
                                    return;
                                }
                            } else {
                                // 括号不完整，显示错误
                                handleError();
                                return;
                            }
                        }

                        double degrees = getCurrentNumberFromResult();
                        double radians = Math.toRadians(degrees);
                        double result = Math.sin(radians);
                        replaceLastNumber(result);
                    } catch (NumberFormatException e) {
                        handleError();
                    }
                }
            }
        });

// 余弦按钮
        cox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!show_equation.toString().isEmpty() && !show_equation.toString().equals("错误")) {
                    try {
                        String expression = show_equation.toString();
                        int lastParenthesisIndex = expression.lastIndexOf(')');
                        int startParenthesisIndex = -1;

                        // 检查是否以 ")" 结尾，并找到匹配的 "("
                        if (lastParenthesisIndex == expression.length() - 1) {
                            int parenthesisCount = 0;
                            for (int i = lastParenthesisIndex; i >= 0; i--) {
                                char c = expression.charAt(i);
                                if (c == ')') {
                                    parenthesisCount++;
                                } else if (c == '(') {
                                    parenthesisCount--;
                                    if (parenthesisCount == 0) {
                                        startParenthesisIndex = i;
                                        break;
                                    }
                                }
                            }

                            // 如果找到匹配的括号对
                            if (startParenthesisIndex != -1) {
                                // 提取括号内的子表达式
                                String subExpression = expression.substring(startParenthesisIndex + 1, lastParenthesisIndex);

                                // 计算括号内的值
                                ArrayList<String> subEquation = parseExpression(subExpression);
                                String subResult = calculate(subEquation);

                                if (!subResult.equals("错误")) {
                                    double subValue = Double.parseDouble(subResult);

                                    // 替换整个括号表达式为计算结果
                                    show_equation.replace(startParenthesisIndex, lastParenthesisIndex + 1, String.valueOf(subValue));

                                    // 继续执行平方逻辑
                                    double radians = Math.toRadians(subValue);
                                    double result = Math.cos(radians);
                                    replaceLastNumber(result);
                                    return;
                                } else {
                                    handleError();
                                    return;
                                }
                            } else {
                                // 括号不完整，显示错误
                                handleError();
                                return;
                            }
                        }
                        double degrees = getCurrentNumberFromResult();
                        double radians = Math.toRadians(degrees);
                        double result = Math.cos(radians);
                        replaceLastNumber(result);
                    } catch (NumberFormatException e) {
                        handleError();
                    }
                }
            }
        });

// 正切按钮
        tan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!show_equation.toString().isEmpty() && !show_equation.toString().equals("错误")) {
                    try {
                        String expression = show_equation.toString();
                        int lastParenthesisIndex = expression.lastIndexOf(')');
                        int startParenthesisIndex = -1;

                        // 检查是否以 ")" 结尾，并找到匹配的 "("
                        if (lastParenthesisIndex == expression.length() - 1) {
                            int parenthesisCount = 0;
                            for (int i = lastParenthesisIndex; i >= 0; i--) {
                                char c = expression.charAt(i);
                                if (c == ')') {
                                    parenthesisCount++;
                                } else if (c == '(') {
                                    parenthesisCount--;
                                    if (parenthesisCount == 0) {
                                        startParenthesisIndex = i;
                                        break;
                                    }
                                }
                            }

                            // 如果找到匹配的括号对
                            if (startParenthesisIndex != -1) {
                                // 提取括号内的子表达式
                                String subExpression = expression.substring(startParenthesisIndex + 1, lastParenthesisIndex);

                                // 计算括号内的值
                                ArrayList<String> subEquation = parseExpression(subExpression);
                                String subResult = calculate(subEquation);

                                if (!subResult.equals("错误")) {
                                    double subValue = Double.parseDouble(subResult);

                                    // 替换整个括号表达式为计算结果
                                    show_equation.replace(startParenthesisIndex, lastParenthesisIndex + 1, String.valueOf(subValue));

                                    // 继续执行平方逻辑
                                    double radians = Math.toRadians(subValue);
                                    double result = Math.tan(radians);
                                    replaceLastNumber(result);
                                    return;
                                } else {
                                    handleError();
                                    return;
                                }
                            } else {
                                // 括号不完整，显示错误
                                handleError();
                                return;
                            }
                        }
                        double degrees = getCurrentNumberFromResult();
                        double radians = Math.toRadians(degrees);
                        double result = Math.tan(radians);
                        replaceLastNumber(result);
                    } catch (NumberFormatException e) {
                        handleError();
                    }
                }
            }
        });

    }
    // 辅助方法：解析表达式
    private ArrayList<String> parseExpression(String expression) {
        ArrayList<String> equation = new ArrayList<>();
        StringBuilder temp1 = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isDigit(c) || c == '.' || c == 'E' || c == '∞') {
                temp1.append(c);
            } else {
                if (temp1.length() > 0) {
                    equation.add(temp1.toString());
                    temp1.setLength(0);
                }
                equation.add(Character.toString(c));
            }
        }

        if (temp1.length() > 0) {
            equation.add(temp1.toString());
        }
        return equation;
    }
    // 辅助方法：处理错误
    private void handleError() {
        result.setText("错误");
        show_equation.setLength(0);
        show_equation.append("错误");
        signal = 1;
    }
    // 辅助方法：获取当前数值（运算符号后面的数字）
    private double getCurrentNumberFromResult() throws NumberFormatException {
        String expression = show_equation.toString();
        int lastOperatorIndex = -1;
        int parenthesisCount = 0;
        int lastParenthesisIndex = -1;
        int startParenthesisIndex = -1;

        // 找到最后一个运算符的位置，同时处理括号
        for (int i = expression.length() - 1; i >= 0; i--) {
            char c = expression.charAt(i);
            if (c == '(') {
                parenthesisCount++;
                if (parenthesisCount == 1) {
                    startParenthesisIndex = i;
                }
            } else if (c == ')') {
                parenthesisCount--;
                if (parenthesisCount == 0) {
                    lastParenthesisIndex = i;
                }
            }
            if ((c == '+' || c == '-' || c == '*' || c == '/' || c == '^') && parenthesisCount == 0) {
                lastOperatorIndex = i;
                break;
            }
        }

        if (parenthesisCount == 0 && lastParenthesisIndex != -1 && startParenthesisIndex != -1) {
            // 如果有完整的括号对，计算括号内的表达式
            String subExpression = expression.substring(startParenthesisIndex + 1, lastParenthesisIndex);
            if (subExpression.trim().isEmpty()) {
                // 括号内为空，抛出错误
                handleError();
                throw new NumberFormatException("错误");
            }
            ArrayList<String> subEquation = parseExpression(subExpression);
            String subResult = calculate(subEquation);
            if (!subResult.equals("错误")) {
                return Double.parseDouble(subResult);
            } else {
                handleError();
                throw new NumberFormatException("错误");
            }
        } else if (parenthesisCount == 1 && startParenthesisIndex != -1) {
            // 如果只有左括号，处理括号后的数字或表达式
            int numberStartIndex = startParenthesisIndex + 1;
            while (numberStartIndex < expression.length() &&
                    (Character.isDigit(expression.charAt(numberStartIndex)) ||
                            expression.charAt(numberStartIndex) == '.')) {
                numberStartIndex++;
            }
            if (numberStartIndex == startParenthesisIndex + 1) {
                // 如果括号后没有数字，处理错误
                handleError();
                throw new NumberFormatException("错误");
            }
            String numberStr = expression.substring(startParenthesisIndex + 1, numberStartIndex);
            return Double.parseDouble(numberStr);
        } else if (parenthesisCount == 0 && lastParenthesisIndex == -1) {
            // 如果没有括号，直接获取整个表达式的最后一个数字
            if (lastOperatorIndex == -1) {
                // 整个表达式就是一个数字
                return Double.parseDouble(expression);
            } else {
                // 从最后一个运算符之后开始查找数字
                int numberStartIndex = lastOperatorIndex + 1;
                while (numberStartIndex < expression.length() &&
                        (Character.isDigit(expression.charAt(numberStartIndex)) ||
                                expression.charAt(numberStartIndex) == '.')) {
                    numberStartIndex++;
                }
                String numberStr = expression.substring(lastOperatorIndex + 1, numberStartIndex);
                return Double.parseDouble(numberStr);
            }
        } else {
            // 处理括号后面直接点击按钮的情况
            if (lastParenthesisIndex != -1) {
                // 计算括号内的表达式
                String subExpression = expression.substring(startParenthesisIndex + 1, lastParenthesisIndex);
                if (subExpression.trim().isEmpty()) {
                    // 括号内为空，抛出错误
                    handleError();
                    throw new NumberFormatException("错误");
                }
                ArrayList<String> subEquation = parseExpression(subExpression);
                String subResult = calculate(subEquation);
                if (!subResult.equals("错误")) {
                    return Double.parseDouble(subResult);
                } else {
                    handleError();
                    throw new NumberFormatException("错误");
                }
            } else {
                // 整个表达式就是一个数字
                return Double.parseDouble(expression);
            }
        }
    }


    // 辅助方法：更新结果
    private void updateResult(double value) {
        show_equation.setLength(0);
        show_equation.append(value);
        signal = 1;
        result.setText(show_equation.toString());
    }
    // 辅助方法：获取当前数值
    private double getCurrentNumber() throws NumberFormatException {
        return Double.parseDouble(show_equation.toString());
    }
    // 辅助方法：显示输入对话框
    private void showInputDialog(String title, InputCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);
        builder.setPositiveButton("确定", (dialog, which) -> callback.onInput(input.getText().toString()));
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    // 辅助方法：处理括号输入
    private void handleParenthesis(String parenthesis) {
        if (signal == 1) {
            // 如果当前在输出结果上继续输入，清空show_equation
            show_equation.setLength(0);
            signal = 0;
        }
        show_equation.append(parenthesis);
        result.setText(show_equation.toString());
        result.setSelection(result.getText().length());
    }
    // 辅助方法：替换最后一个数字
    private void replaceLastNumber(double newNumber) {
        String expression = show_equation.toString();
        int lastOperatorIndex = -1;
        int parenthesisCount = 0;
        int lastParenthesisIndex = -1;
        int startParenthesisIndex = -1;

        // 找到最后一个运算符的位置，同时处理括号
        for (int i = expression.length() - 1; i >= 0; i--) {
            char c = expression.charAt(i);
            if (c == '(') {
                parenthesisCount++;
                if (parenthesisCount == 1) {
                    startParenthesisIndex = i;
                }
            } else if (c == ')') {
                parenthesisCount--;
                if (parenthesisCount == 0) {
                    lastParenthesisIndex = i;
                }
            }
            if ((c == '+' || c == '-' || c == '*' || c == '/' || c == '^') && parenthesisCount == 0) {
                lastOperatorIndex = i;
                break;
            }
        }

        if (parenthesisCount == 0 && lastParenthesisIndex != -1 && startParenthesisIndex != -1) {
            // 如果有完整的括号对
            String subExpression = expression.substring(startParenthesisIndex + 1, lastParenthesisIndex);
            if (subExpression.isEmpty()) {
                // 处理空括号的情况
                show_equation.replace(startParenthesisIndex, lastParenthesisIndex + 1, "(" + newNumber + ")");
            } else {
                ArrayList<String> subEquation = parseExpression(subExpression);
                String subResult = calculate(subEquation);
                if (!subResult.equals("错误")) {
                    double subValue = Double.parseDouble(subResult);
                    // 替换括号内的表达式为计算结果
                    show_equation.replace(startParenthesisIndex, lastParenthesisIndex + 1, subResult);
                    // 再次替换为新的数字
                    replaceLastNumber(newNumber);
                } else {
                    handleError();
                    return;
                }
            }
        } else if (parenthesisCount == 1 && startParenthesisIndex != -1) {
            // 如果只有左括号，处理括号后的数字或表达式
            int numberStartIndex = startParenthesisIndex + 1;
            while (numberStartIndex < expression.length() &&
                    (Character.isDigit(expression.charAt(numberStartIndex)) ||
                            expression.charAt(numberStartIndex) == '.')) {
                numberStartIndex++;
            }
            if (numberStartIndex == startParenthesisIndex + 1) {
                // 如果括号后没有数字，处理错误
                handleError();
                return;
            }
            String numberStr = expression.substring(startParenthesisIndex + 1, numberStartIndex);
            double number = Double.parseDouble(numberStr);
            // 替换括号后的数字为新的数字
            show_equation.replace(startParenthesisIndex + 1, numberStartIndex, String.valueOf(newNumber));
        } else if (lastOperatorIndex == -1) {
            // 如果没有找到运算符，则整个表达式就是一个数字
            show_equation.setLength(0);
            show_equation.append(newNumber);
        } else {
            // 从最后一个运算符之后开始查找数字
            int numberStartIndex = lastOperatorIndex + 1;
            while (numberStartIndex < expression.length() &&
                    (Character.isDigit(expression.charAt(numberStartIndex)) ||
                            expression.charAt(numberStartIndex) == '.')) {
                numberStartIndex++;
            }
            // 替换最后一个数字
            show_equation.replace(lastOperatorIndex + 1, numberStartIndex, String.valueOf(newNumber));
        }

        signal = 0;
        result.setText(show_equation.toString());
        result.setSelection(result.getText().length());
    }

    // 辅助方法：应用三角函数
    private void applyTrigFunction(DoubleUnaryOperator function) {
        if (!show_equation.toString().isEmpty() && !show_equation.toString().equals("错误")) {
            try {
                double radians = getCurrentNumber();
                double result = function.applyAsDouble(radians);
                if (Double.isNaN(result)) {
                    handleError();
                } else {
                    updateResult(result);
                }
            } catch (NumberFormatException e) {
                handleError();
            }
        }
    }

    interface InputCallback {
        void onInput(String input);
    }

    protected boolean operatorPriorityCompare(char operator1, char operator2) {
        Map<Character, Integer> priority = new HashMap<>();
        priority.put('+', 1);
        priority.put('-', 1);
        priority.put('*', 2);
        priority.put('/', 2);
        priority.put('^', 3);
        priority.put('(', 0);

        // 处理右括号的特殊情况
        if (operator2 == '(') return false;
        if (operator1 == '(') return false;

        return priority.getOrDefault(operator1, 0) > priority.getOrDefault(operator2, 0);
    }

    //相加
    public static Double Add(Double d1,Double d2) {
        if(d1==Double.NEGATIVE_INFINITY||d1==Double.POSITIVE_INFINITY||d2==Double.NEGATIVE_INFINITY||d2==Double.POSITIVE_INFINITY){
            return d1+d2;
        }
        if(String.valueOf(d1).equals("NaN")||String.valueOf(d1).equals("NaN")){
            //如果两个运算数只要有一个是非数'NaN'，就直接运算即可
            return d1+d2;
        }
        //BigDecimal为精确计算的一个数据类型，你可以理解为使用它进行计算结果将更准确
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        //进行计算并将结果转为double返回
        return b1.add(b2).doubleValue();
    }
    //相减
    public static Double Sub(Double d1,Double d2){
        if(d1==Double.NEGATIVE_INFINITY||d1==Double.POSITIVE_INFINITY||d2==Double.NEGATIVE_INFINITY||d2==Double.POSITIVE_INFINITY){
            return d1-d2;
        }
        if(String.valueOf(d1).equals("NaN")||String.valueOf(d1).equals("NaN")){
            return d1-d2;
        }
        if(String.valueOf(d1).equals("NaN")||String.valueOf(d1).equals("NaN")){
            return d1*d2;
        }
        BigDecimal b1=new BigDecimal(Double.toString(d1));
        BigDecimal b2=new BigDecimal(Double.toString(d2));
        return b1.subtract(b2).doubleValue();
    }
    //相乘
    public static Double Mul(Double d1,Double d2){
        if(d1==Double.NEGATIVE_INFINITY||d1==Double.POSITIVE_INFINITY||d2==Double.NEGATIVE_INFINITY||d2==Double.POSITIVE_INFINITY){
            return d1*d2;
        }
        if(String.valueOf(d1).equals("NaN")||String.valueOf(d1).equals("NaN")){
            return d1*d2;
        }
        BigDecimal b1=new BigDecimal(Double.toString(d1));
        BigDecimal b2=new BigDecimal(Double.toString(d2));
        // 指定舍入模式为 ROUND_HALF_UP
        return b1.multiply(b2).setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    //相除
    public static Double Div(Double d1,Double d2){
        if(d1==Double.NEGATIVE_INFINITY||d1==Double.POSITIVE_INFINITY||d2==Double.NEGATIVE_INFINITY||d2==Double.POSITIVE_INFINITY){
            return d1/d2;
        }
        if(String.valueOf(d1).equals("NaN")||String.valueOf(d1).equals("NaN")){
            return d1/d2;
        }
        if(d1==0.0&&d2==0.0){
            return Double.NaN;
        }
        if(d2==0.0){
            return d1/d2;
        }
        BigDecimal b1=new BigDecimal(Double.toString(d1));
        BigDecimal b2=new BigDecimal(Double.toString(d2));
        // 指定舍入模式为 ROUND_HALF_UP
        return b1.divide(b2,8,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    //这个方法就是用来计算结果的，在344行被调用，参数为计算式
    //里面过程比较复杂，你需先弄懂后缀表达式，然后可对照理解大概流程意思即可
    protected String calculate(ArrayList<String> equation) {
        List<String> operator = new ArrayList<>();
        List<Double> operand = new ArrayList<>();

        try {
            for (int i = 0; i < equation.size(); i++) {
                String token = equation.get(i);

                // 处理负数情况
                if (token.equals("-") && (i == 0 || isOperator(equation.get(i-1)) || equation.get(i-1).equals("("))) {
                    operand.add(-Double.parseDouble(equation.get(++i)));
                    continue;
                }

                if ("(".equals(token)) {
                    operator.add(token);
                } else if (")".equals(token)) {
                    while (!operator.isEmpty() && !"(".equals(operator.get(operator.size()-1))) {
                        applyOperator(operator.remove(operator.size()-1), operand);
                    }
                    operator.remove(operator.size()-1); // 移除左括号
                } else if (isOperator(token)) {
                    while (!operator.isEmpty() && operatorPriorityCompare(operator.get(operator.size()-1).charAt(0), token.charAt(0))) {
                        applyOperator(operator.remove(operator.size()-1), operand);
                    }
                    operator.add(token);
                } else if ("#".equals(token)) {
                    while (!operator.isEmpty()) {
                        applyOperator(operator.remove(operator.size()-1), operand);
                    }
                } else {
                    try {
                        operand.add(Double.parseDouble(token));
                    } catch (NumberFormatException e) {
                        return "错误";
                    }
                }
            }

            // 处理剩余的操作符
            while (!operator.isEmpty()) {
                applyOperator(operator.remove(operator.size() - 1), operand);
            }

            if (operand.isEmpty()) {
                return "错误";
            }

            double finalResult = operand.get(0);
            if (Double.isNaN(finalResult)) return "NaN";
            if (finalResult == Double.NEGATIVE_INFINITY) return "-∞";
            if (finalResult == Double.POSITIVE_INFINITY) return "∞";
            return Double.toString(finalResult);
        } catch (Exception e) {
            return "错误";
        }
    }
    // 修改计算逻辑
    private void applyOperator(String op, List<Double> operand) {
        if (operand.size() < 2) {
            operand.clear();
            operand.add(Double.NaN);
            return;
        }
        double b = operand.remove(operand.size() - 1);
        double a = operand.remove(operand.size() - 1);
        double result = 0;
        switch (op) {
            case "+":
                result = Add(a, b);
                break;
            case "-":
                result = Sub(a, b);
                break;
            case "*":
                result = Mul(a, b);
                break;
            case "/":
                result = Div(a, b);
                break;
            case "^":
                result = Math.pow(a, b);
                break;
            default:
                result = Double.NaN;
        }
        operand.add(result);
    }


    //当API最低版小于21时使用这个函数实现点击文本框不弹出键盘
    public void disableShowInput(EditText et) {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(et, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isOperator(String token) {
        return token.matches("[+\\-*/^]");
    }

}

