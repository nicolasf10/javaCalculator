package Buttons;

import Buttons.CalculatorButton;

import java.awt.*;

import MathHandler.Token;
import MathHandler.TokenType;
import MyColors.MyColors;

public class NumberButton extends CalculatorButton {
    public NumberButton(String text, Color color) {
        super(text, color);
    }

    public NumberButton(String text) {
        super(text);
        this.setBackground(MyColors.NUMBERS_COLOR);
    }

    @Override
    public void handleClick() {
        Token token = new Token(this.getText(), TokenType.INTEGER, Integer.parseInt(this.getText()), 0);
        this.resultField.addToken(token);
    }
}
