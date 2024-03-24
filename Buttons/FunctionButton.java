package Buttons;

import Buttons.CalculatorButton;

import java.awt.*;

import MathHandler.OpType;
import MathHandler.Token;
import MathHandler.TokenType;
import MyColors.MyColors;

public class FunctionButton extends CalculatorButton {
    public FunctionButton(String text, Color color) {
        super(text, color);
    }

    public FunctionButton(String text) {
        super(text);
        this.setBackground(MyColors.LIGHT_GRAY);
    }

    public FunctionButton(String text, String inverseText) {
        super(text, inverseText);
        this.setBackground(MyColors.LIGHT_GRAY);
    }

    public FunctionButton(String text, Color color, String inverseText) {
        super(text, color, inverseText);
    }

    @Override
    public void handleClick() {
        OpType opType;

        switch (this.getText()) {
            case "sin":
                opType = OpType.SIN;
                break;
            case "cos":
                opType = OpType.COS;
                break;
            case "tan":
                opType = OpType.TAN;
                break;
            case "asin":
                opType = OpType.ASIN;
                break;
            case "acos":
                opType = OpType.ACOS;
                break;
            case "atan":
                opType = OpType.ATAN;
                break;
            case "log":
                opType = OpType.LOG;
                break;
            case "ln":
                opType = OpType.LN;
                break;
            case "√":
                opType = OpType.SQRT;
                break;
            case "abs":
                opType = OpType.ABS;
                break;
            default:
                return;
        }

        Token token = new Token(this.getText() + "(", TokenType.FUNCTION, opType);
        this.resultField.addToken(token);
    }

    public void setInverse() {
        if (this.getText().equals("sin")) {
            this.setText("asin");
        } else if (this.getText().equals("cos")) {
            this.setText("acos");
        } else if (this.getText().equals("tan")) {
            this.setText("atan");
        } else if (this.getText().equals("asin")) {
            this.setText("sin");
        } else if (this.getText().equals("acos")) {
            this.setText("cos");
        } else if (this.getText().equals("atan")) {
            this.setText("tan");
        }
    }
}
