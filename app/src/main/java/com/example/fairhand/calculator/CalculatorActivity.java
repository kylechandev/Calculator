package com.example.fairhand.calculator;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    
    public static final String TAG = "CalculatorActivity";
    
    /**
     * 数字键0~9
     */
    private Button num0;
    
    private Button num1;
    
    private Button num2;
    
    private Button num3;
    
    private Button num4;
    
    private Button num5;
    
    private Button num6;
    
    private Button num7;
    
    private Button num8;
    
    private Button num9;
    
    /**
     * 操作符+ - × ÷ ()
     */
    private Button plus;
    
    private Button minus;
    
    private Button multiply;
    
    private Button divide;
    
    private Button openParentheses;
    
    private Button closeParentheses;
    
    /**
     * 功能键（回退一位，清除，小数点，等于）
     */
    private Button del;
    
    private Button allClear;
    
    private Button dot;
    
    private Button equal;
    
    /**
     * 显示计算结果后之前输入的算式
     */
    private TextView lastInputTextArea;
    
    /**
     * 输入的算式
     */
    private EditText inputText;
    
    /**
     * 判断是否按过=
     */
    private boolean ispressEqual = false;
    
    /**
     * 用于判断小数点是否按过的标记
     */
    private int flag = 1;
    
    /**
     * 当前编辑框的内容
     */
    private String currentText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        
        // 去掉标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        
        // 融合状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (Build.VERSION.SDK_INT >= 23) {
                decorView.setSystemUiVisibility(option | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decorView.setSystemUiVisibility(option);
            }
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        
        initView();
        
        initData();
        
        // 得到保存的数据
        SharedPreferences sharedPreferences = getSharedPreferences("result", MODE_PRIVATE);
        inputText.setText(sharedPreferences.getString("current", ""));
        inputText.setSelection(inputText.length());
        lastInputTextArea.setText(sharedPreferences.getString("last", ""));
        
    }
    
    @Override
    public void onBackPressed() {
        // 保存数据
        SharedPreferences.Editor editor = getSharedPreferences("result", MODE_PRIVATE).edit();
        editor.putString("current", String.valueOf(inputText.getText()));
        editor.putString("last", (String) lastInputTextArea.getText());
        editor.apply();
        super.onBackPressed();
    }
    
    /**
     * 初始化控件
     */
    private void initView() {
        // 自定义字体Consola
        AssetManager manager = getAssets();
        Typeface typeface = Typeface.createFromAsset(manager, "fonts/consola.ttf");
        
        lastInputTextArea = findViewById(R.id.last_count_textArea);
        lastInputTextArea.setTypeface(typeface);
        lastInputTextArea.setGravity(Gravity.END);
    
        inputText = findViewById(R.id.input_text_area);
        inputText.setTypeface(typeface);
        inputText.setGravity(Gravity.END);// 设置光标在右
        inputText.setCursorVisible(true);// 显示光标
    
        // 禁止弹出软键盘
        if (Build.VERSION.SDK_INT >= 21) {
            inputText.setShowSoftInputOnFocus(false);
        }
        
        
        num0 = findViewById(R.id.num0);
        num0.setTypeface(typeface);
        num1 = findViewById(R.id.num1);
        num1.setTypeface(typeface);
        num2 = findViewById(R.id.num2);
        num2.setTypeface(typeface);
        num3 = findViewById(R.id.num3);
        num3.setTypeface(typeface);
        num4 = findViewById(R.id.num4);
        num4.setTypeface(typeface);
        num5 = findViewById(R.id.num5);
        num5.setTypeface(typeface);
        num6 = findViewById(R.id.num6);
        num6.setTypeface(typeface);
        num7 = findViewById(R.id.num7);
        num7.setTypeface(typeface);
        num8 = findViewById(R.id.num8);
        num8.setTypeface(typeface);
        num9 = findViewById(R.id.num9);
        num9.setTypeface(typeface);
        
        plus = findViewById(R.id.operate_plus);
        num5.setTypeface(typeface);
        minus = findViewById(R.id.operate_minus);
        num5.setTypeface(typeface);
        multiply = findViewById(R.id.operate_multiply);
        num5.setTypeface(typeface);
        divide = findViewById(R.id.operate_divide);
        num5.setTypeface(typeface);
        
        allClear = findViewById(R.id.operate_AC);
        allClear.setTypeface(typeface);
        del = findViewById(R.id.operate_DEL);
        del.setTypeface(typeface);
        openParentheses = findViewById(R.id.operate_openParentheses);
        openParentheses.setTypeface(typeface);
        closeParentheses = findViewById(R.id.operate_closeParentheses);
        closeParentheses.setTypeface(typeface);
        dot = findViewById(R.id.dot);
        dot.setTypeface(typeface);
        equal = findViewById(R.id.operate_equal);
        equal.setTypeface(typeface);
    }
    
    /**
     * 初始化数据
     */
    private void initData() {
        num0.setOnClickListener(this);
        num1.setOnClickListener(this);
        num2.setOnClickListener(this);
        num3.setOnClickListener(this);
        num4.setOnClickListener(this);
        num5.setOnClickListener(this);
        num6.setOnClickListener(this);
        num7.setOnClickListener(this);
        num8.setOnClickListener(this);
        num9.setOnClickListener(this);
        
        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
        multiply.setOnClickListener(this);
        divide.setOnClickListener(this);
        
        allClear.setOnClickListener(this);
        del.setOnClickListener(this);
        openParentheses.setOnClickListener(this);
        closeParentheses.setOnClickListener(this);
        dot.setOnClickListener(this);
        equal.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        currentText = inputText.getText().toString();// 当前编辑框的内容
        
        switch (v.getId()) {
            case R.id.num0:
                setDigitalContent(num0);
                ispressEqual = false;
                break;
            case R.id.num1:
                setDigitalContent(num1);
                ispressEqual = false;
                break;
            case R.id.num2:
                setDigitalContent(num2);
                ispressEqual = false;
                break;
            case R.id.num3:
                setDigitalContent(num3);
                ispressEqual = false;
                break;
            case R.id.num4:
                setDigitalContent(num4);
                ispressEqual = false;
                break;
            case R.id.num5:
                setDigitalContent(num5);
                ispressEqual = false;
                break;
            case R.id.num6:
                setDigitalContent(num6);
                ispressEqual = false;
                break;
            case R.id.num7:
                setDigitalContent(num7);
                ispressEqual = false;
                break;
            case R.id.num8:
                setDigitalContent(num8);
                ispressEqual = false;
                break;
            case R.id.num9:
                setDigitalContent(num9);
                ispressEqual = false;
                break;
            case R.id.operate_plus:
                setOperatorContent(plus);
                break;
            case R.id.operate_minus:
                setOperatorContent(minus);
                break;
            case R.id.operate_multiply:
                setOperatorContent(multiply);
                break;
            case R.id.operate_divide:
                setOperatorContent(divide);
                break;
            case R.id.operate_openParentheses:
                setDigitalContent(openParentheses);
                ispressEqual = false;
                break;
            case R.id.operate_closeParentheses:
                setDigitalContent(closeParentheses);
                ispressEqual = false;
                break;
            case R.id.dot:
                if (flag == 1) {
                    inputText.append(dot.getText());
                    flag = 0;
                    ispressEqual = false;
                } else {
                    return;
                }
                break;
            case R.id.operate_equal:
                lastInputTextArea.setText(currentText);
                
                // 将×÷转换为*/
                String testExpression = currentText;
                testExpression = testExpression.replaceAll("×", "*");
                testExpression = testExpression.replaceAll("÷", "/");
                Log.d(TAG, currentText);
                
                // 使用JavaScript引擎判断表达式是否合法
                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByName("javascript");
                String obj = "illegal";
                try {
                    obj = String.valueOf(engine.eval(testExpression));
                } catch (ScriptException e) {
                    Log.d(TAG, "天哪,输的什么东西");
                    e.printStackTrace();
                }
                
                // 如果什么也没有输入，则只打印一条提示日志
                if (currentText.isEmpty()) {
                    Log.d(TAG, "nothing input.");
                    // 如果只输入了一个. 则设置结果为0
                } else if (currentText.equals(".")) {
                    inputText.setText(num0.getText());
                    inputText.setSelection(inputText.length());
                    return;
                } else {
                    // 如果输入的表达式不合法，则设置结果为Error
                    if (obj.equals("illegal")) {
                        Log.d(TAG, "表达式不合法");
                        inputText.setText(R.string.error);
                        inputText.setSelection(inputText.length());
                        ispressEqual = true;
                        return;
                    } else {
                        // 如果运算包含了-，就使用JavaScript的解（Me是被迫的...）
                        if (currentText.contains("-")) {
                            // 设置?.0为?（去除.0）
                            inputText.setText(formatResult(obj));
                            inputText.setSelection(inputText.length());
                            ispressEqual = true;
                            return;
                            // .?形式的转换为0.?的形式
                        } else if ((currentText.length() >= 2) && (currentText.charAt(0) == '.')
                                           && isDigital(currentText)) {
                            currentText = "0" + currentText;
                            inputText.setText(currentText);
                            inputText.setSelection(inputText.length());
                            ispressEqual = true;
                            return;
                        } else {
                            // 计算
                            computationExpression(convertPostfixExpression(matchRegular(currentText)));
                        }
                    }
                }
                Log.d(TAG, String.valueOf(inputText.getText()));
                ispressEqual = true;
                break;
            case R.id.operate_DEL:
                // 在输入区不为空的情况下执行删除操作
                if (TextUtils.isEmpty(inputText.getText())) {
                    Log.d(TAG, "别删了，没有了");
                    return;
                    // 若按下过等号，则设置结果为之间的表达式，清除显示表达式的区域
                } else if (ispressEqual) {
                    inputText.setText(lastInputTextArea.getText());
                    lastInputTextArea.setText("");
                    ispressEqual = false;
                    inputText.setSelection(inputText.length());
                    return;
                }
                // 如果最后一位是小数点，则将小数点的标记设为1，再进行删除操作
                if (currentText.split("")[currentText
                                                  .split("").length - 1].contentEquals(dot.getText())) {
                    flag = 1;
                }
                inputText.setText(currentText.substring(0, currentText.length() - 1));
                break;
            case R.id.operate_AC:
                // 清空所有
                inputText.setText("");
                lastInputTextArea.setText("");
                flag = 1;
                break;
        }
        inputText.setSelection(inputText.length());// 设置光标的位置
    }
    
    /**
     * 匹配正则，取出操作数和操作符
     *
     * @param originExpression 待匹配正则表达式的原始算式
     */
    private List<String> matchRegular(String originExpression) {
        List<String> infixExpression = new ArrayList<>();
        Pattern pattern = Pattern.compile("[+\\-×÷()]|\\d*\\.?\\d+%*");
        Matcher matcher = pattern.matcher(originExpression);
        
        // 开始匹配符合的字串
        while (matcher.find()) {
            infixExpression.add(matcher.group());// 将符合条件的字串加入到list
        }
        
        // 判断有负数的情况（已知bug：若在-前有数字会失效）
        /*if (infixExpression.contains("-")) {
            List<String> list_remove = new ArrayList<>();
            for (String temp : infixExpression) {
                if (temp.equals("-")) {
                    if (infixExpression.indexOf(temp) == 0
                            || infixExpression.get(infixExpression.indexOf(temp) - 1).equals("(")) {
                        int substractIndex = infixExpression.indexOf(temp);
                        infixExpression.set(substractIndex,
                                infixExpression.get(substractIndex) +
                                        infixExpression.get(substractIndex + 1));
                        list_remove.add(infixExpression.get(substractIndex + 1));
                    }
                }
            }
            infixExpression.removeAll(list_remove);
        }*/
        
        return infixExpression;
    }
    
    /**
     * 将中缀表达式转换为后缀表达式
     *
     * @param infixExpression 待转换的中缀表达式
     */
    private List<String> convertPostfixExpression(List<String> infixExpression) {
        Stack<String> stack = new Stack<>();// 操作符栈
        List<String> postfixExpression = new ArrayList<>();// 后缀表达式
        
        // 判断是否为空，空的话打印一条日志
        if (infixExpression.isEmpty()) {
            Log.d(TAG, "input nothing.");
        }
        
        for (String symbol : infixExpression) {
            if (isOpenParentheses(symbol)) {
                stack.push(symbol);// 是左括号则入操作符栈
            } else if (isCloseParentheses(symbol)) {
                // 是右括号就在遇到左括号之前把中间的操作符都取出放入后缀表达式中，左右括号不加入后缀表达式当中
                String operator = stack.pop();
                while (!isOpenParentheses(operator)) {
                    postfixExpression.add(operator);
                    operator = stack.pop();
                }
            } else if (isOperator(symbol)) {
                if (!stack.isEmpty()) {// 若此时栈不为空，则进行判断
                    String lastOperator = stack.peek();// 查看栈顶操作符
                    while ((lastOperator != null) &&
                                   (priority(lastOperator) >= priority(symbol))) {// 判断操作符的优先级
                        postfixExpression.add(lastOperator);// 将操作符加入到后缀表达式
                        stack.pop();// 从操作符栈弹出加入的操作符
                        if (!stack.isEmpty()) {
                            lastOperator = stack.peek();
                        } else {
                            lastOperator = null;
                        }
                    }
                }
                stack.push(symbol);// 将判断的操作符压入栈
            } else {
                postfixExpression.add(symbol);// 剩下的肯定都是操作数啦，加入加入
            }
        }
        
        // 最后若操作符栈不为空，就将栈内的操作符依次加入后缀表达式中
        while (!stack.isEmpty()) {
            postfixExpression.add(stack.pop());
        }
        
        return postfixExpression;
        
    }
    
    /**
     * 计算后缀表达式
     *
     * @param postfixExpression 待计算的后缀表达式
     */
    private void computationExpression(List<String> postfixExpression) {
        Stack<String> stack = new Stack<>();// 操作数栈
        
        // 判断如果后缀是空的，则直接返回什么也不做
        if (postfixExpression.isEmpty()) {
            inputText.setText(R.string.error);
            inputText.setSelection(inputText.length());
            ispressEqual = true;
            return;
        }
        
        for (String symbol : postfixExpression) {
            if (isOperator(symbol)) {
                // 连续弹出两个数
                double operand1 = 0d;
                double operand2 = 0d;
                if (!stack.isEmpty()) {
                    operand1 = Double.valueOf(stack.pop());
                }
                if (!stack.isEmpty()) {
                    operand2 = Double.valueOf(stack.pop());
                }
                // 转换为double类型（已舍弃%功能）
                /*double operand1;
                double operand2;
                // 做判断是否包含%
                if (temp1.contains("%")) {
                    int count = 0;//用于计数含有多少个%
                    Matcher matcher = Pattern.compile("%").matcher(temp1);
                    while (matcher.find()) {
                        count++;
                    }
                    operand1 = Double.valueOf(temp1);
                    for (int i = 0; i < count; i++) {
                        operand1 /= 100;
                    }
                } else {
                    operand1 = Double.valueOf(temp1);
                }
                if (temp2.contains("%")) {
                    int count = 0;//用于计数含有多少个%
                    Matcher matcher = Pattern.compile("%").matcher(temp2);
                    while (matcher.find()) {
                        count++;
                    }
                    operand2 = Double.valueOf(temp2);
                    for (int i = 0; i < count; i++) {
                        operand2 /= 100;
                    }
                } else {
                    operand2 = Double.valueOf(temp2);
                }*/
                double result = 0d;
                
                switch (symbol) {
                    case "+":
                        result = operand2 + operand1;
                        break;
                    case "-":
                        result = operand2 - operand1;
                        break;
                    case "×":
                        result = operand2 * operand1;
                        break;
                    case "÷":
                        result = operand2 / operand1;
                        break;
                    default:
                        break;
                }
                stack.push(String.valueOf(result));// 算出的结果再放入栈，因为后面肯定还会用到的啦
            } else {
                stack.push(symbol);// 不然肯定是操作数啦，进进进
            }
        }
        
        String finalResult = stack.pop();
        // 如果是单一的一个数字，直接设置结果
        if (finalResult.length() == 1) {
            inputText.setText(finalResult);
            inputText.setSelection(inputText.length());
        } else {
            inputText.setText(formatResult(finalResult));// 设置结果
            inputText.setSelection(inputText.length());
        }
    }
    
    /**
     * 格式化结果显示
     *
     * @param result 需要格式化的结果
     * @return 格式化后的结果
     */
    private String formatResult(String result) {
        // 设置?.0为?（去除.0）
        if (result.charAt(result.length() - 2) == '.' && result.endsWith("0")) {
            result = result.substring(0, result.length() - 2);
            // 小数点以后精确8位
        } else if (result.substring(result.indexOf(".") + 1,
                result.length()).length() > 8) {
            result =
                    String.valueOf(((double) Math.round(Double.valueOf(result) * 1E8) / 1E8));
        } else {
            return result;
        }
        return result;
    }
    
    /**
     * 设置操作数点击，点击数字键先判断有没有按过等号，按过等号就先把inputText清空后再显示数字（包括括号）
     *
     * @param digital 点击的相应的操作数按钮
     */
    private void setDigitalContent(Button digital) {
        if (ispressEqual) {
            inputText.setText("");
        }
        inputText.getText().insert(inputText.getSelectionStart(), digital.getText());
    }
    
    /**
     * 设置操作符点击，如果最后一次输入是操作符，
     * 则下次再点击操作符将会用下一次点击的操作符替换掉上一次的，从而不会连续出现两个操作符
     *
     * @param operator 点击的相应的操作符按钮
     */
    private void setOperatorContent(Button operator) {
        if (currentText.split("")[
                    currentText.split("").length - 1].contentEquals(plus.getText())
                    || currentText.split("")[
                               currentText.split("").length - 1].contentEquals(minus.getText())
                    || currentText.split("")[
                               currentText.split("").length - 1].contentEquals(multiply.getText())
                    || currentText.split("")[
                               currentText.split("").length - 1].contentEquals(divide.getText())) {
            inputText.setText(currentText.substring(0, currentText.length() - 1));
            inputText.append(operator.getText());
        } else {
            // 如果当前输入框中是非结果的提示，则先清空输入框的内容
            if (currentText.matches("[a-zA-Z]+")) {
                inputText.setText("");
            }
            inputText.append(operator.getText());
        }
        ispressEqual = false;
        flag = 1;
    }
    
    /**
     * 判断是否是数字
     *
     * @param expression 待判断的表达式
     * @return 是否为数字
     */
    private boolean isDigital(String expression) {
        for (int i = 1; i < expression.length(); i++) {
            if (Character.isDigit(expression.charAt(i))) {
                Log.d(TAG, "你是个数字，来下一个");
            } else {
                return false;
            }
        }
        return true;
    }
    
    
    /**
     * 判断是否为左括号
     *
     * @param symbol 待判断的符号
     * @return 是否为左括号
     */
    private boolean isOpenParentheses(String symbol) {
        return symbol.equals("(");
    }
    
    /**
     * 判断是否为右括号
     *
     * @param symbol 待判断的符号
     * @return 是否为右括号
     */
    private boolean isCloseParentheses(String symbol) {
        return symbol.equals(")");
    }
    
    /**
     * 判断是不是一个操作符
     *
     * @param operator 待判断的符号
     * @return 是否为操作符
     */
    private boolean isOperator(String operator) {
        return operator.equals("+") || operator.equals("-")
                       || operator.equals("×") || operator.equals("÷");
    }
    
    /**
     * 判断操作符的优先级
     *
     * @param symbol 待判断的操作符
     * @return 操作符的优先级
     */
    private int priority(String symbol) {
        switch (symbol) {
            case "×":
            case "÷":
                return 2;
            case "+":
            case "-":
                return 1;
        }
        return 0;
    }
}
