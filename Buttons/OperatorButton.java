package Buttons;

import Buttons.CalculatorButton;

import java.awt.*;

import MathHandler.OpType;
import MathHandler.Token;
import MathHandler.TokenType;
import MyColors.MyColors;

// --------------------- OperatorButton ---------------------
// Buttons: ÷, ×, -, +, ., (, )
// ---------------------------------------------------------

public class OperatorButton extends CalculatorButton {
    public OperatorButton(String text, Color color) {
        super(text, color);
    }

    public OperatorButton(String text) {
        super(text);
        this.setBackground(MyColors.LIGHT_GRAY);
    }

    public OperatorButton(String text, Color color, boolean whiteColor) {
        super(text, color);
        if (whiteColor) {
            this.setForeground(Color.WHITE);
        }
    }

    @Override
    public void handleClick() {
        Token token;
        switch (this.getText()) {
            case "(":
                token = new Token(this.getText(), TokenType.OPERATOR, OpType.OPEN_PAREN);
                this.resultField.addToken(token);
                break;
            case ")":
                token = new Token(this.getText(), TokenType.OPERATOR, OpType.CLOSE_PAREN);
                this.resultField.addToken(token);
                break;
            case "÷":
                token = new Token(addSpaces(this.getText()), TokenType.OPERATOR, OpType.DIVIDE);
                this.resultField.addToken(token);
                break;
            case "×":
                token = new Token(addSpaces(this.getText()), TokenType.OPERATOR, OpType.MULTIPLY);
                this.resultField.addToken(token);
                break;
            case "-":
                if (isMinus(this.resultField.getLastToken())) {
                    token = new Token(this.getText(), TokenType.OPERATOR, OpType.MINUS);
                } else {
                    token = new Token(addSpaces(this.getText()), TokenType.OPERATOR, OpType.SUBTRACT);
                }
                this.resultField.addToken(token);
                break;
            case "+":
                token = new Token(addSpaces(this.getText()), TokenType.OPERATOR, OpType.ADD);
                this.resultField.addToken(token);
                break;
            case "%":
                token = new Token(addSpaces(this.getText()), TokenType.OPERATOR, OpType.MODULO);
                this.resultField.addToken(token);
                break;
            case "x^y":
                token = new Token("^", TokenType.OPERATOR, OpType.EXPONENT);
                this.resultField.addToken(token);
                break;
            case ".":
                token = new Token(this.getText(), TokenType.OPERATOR, OpType.DECIMAL);
                this.resultField.addToken(token);
                break;
            default:
                break;
        }
    }

    public static String addSpaces(String str) {
        return " " + str + " ";
    }


    public static boolean isMinus(Token lastToken) {
        // takes the last token on the expression and checks if the new token is a subtraction or a negative sign
        if (lastToken == null) {
            return true;
        }

        if (lastToken.tokenType == TokenType.OPERATOR && lastToken.opType != OpType.CLOSE_PAREN) {
            return true;
        }

        return false;
    }
}
