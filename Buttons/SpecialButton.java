package Buttons;

import Buttons.CalculatorButton;

import java.awt.*;
import java.util.ArrayList;

import MathHandler.OpType;
import MathHandler.Token;
import MathHandler.TokenType;
import MathHandler.angleModes;
import MyColors.MyColors;

// --------------------- SpecialButton ---------------------
// Buttons: Deg, Rad, C, Inv, =, Ans
// ---------------------------------------------------------

public class SpecialButton extends CalculatorButton {

    private SpecialButton opposite;
    private ArrayList<CalculatorButton> buttons;

    public SpecialButton(String text, Color color) {
        super(text, color);
    }

    public SpecialButton(String text) {
        super(text);
        this.setBackground(MyColors.LIGHT_GRAY);
    }

    public SpecialButton(String text, Color color, boolean whiteColor) {
        super(text, color);
        if (whiteColor) {
            this.setForeground(Color.WHITE);
        }
    }

    public void setButtons(ArrayList<CalculatorButton> buttons) {
        this.buttons = buttons;
    }

    @Override
    public void handleClick() {
        Token token;
        switch (this.getText()) {
            case "C":
                this.resultField.clear();
                break;
            case "=":
                this.resultField.evaluate();
                break;
            case "Ans":
                this.resultField.addAnswer();
                break;
            case "e":
                token = new Token(this.getText(), TokenType.SPECIAL, 0, Math.E);
                this.resultField.addToken(token);
                break;
            case "π":
                token = new Token(this.getText(), TokenType.SPECIAL, 0, Math.PI);
                this.resultField.addToken(token);
                break;
            case "Deg":
                this.resultField.setAngleMode(angleModes.DEGREES);
                this.setBold();
                this.opposite.setNormal();
                break;
            case "Rad":
                this.resultField.setAngleMode(angleModes.RADIANS);
                this.setBold();
                this.opposite.setNormal();
                break;
            case "Inv":
                for (CalculatorButton button : buttons) {
                    if (button.isInvertible()) {
                        String temp = button.getText();
                        button.setText(button.getInverseText());
                        button.setInverseText(temp);
                    }
                }
                break;
            case "Del":
                this.resultField.deleteLast();
                break;
            case "x^2":
                Token token1 = new Token("^", TokenType.OPERATOR, OpType.EXPONENT);
                this.resultField.addToken(token1);

                Token token2 = new Token("2", TokenType.INTEGER, 0, 2);
                this.resultField.addToken(token2);
        }
    }

    public void setOpposite(SpecialButton opposite) {
        this.opposite = opposite;
    }

    public SpecialButton getOpposite() {
        return this.opposite;
    }
}
