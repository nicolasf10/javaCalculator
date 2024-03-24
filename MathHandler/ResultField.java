package MathHandler;

import javax.swing.*;
import java.awt.*;
import java.security.PublicKey;
import java.util.ArrayList;

public class ResultField extends JTextField {
    public String expression = "";
    protected ArrayList<Token> tokens;
    private AnswerField answerField;
    private boolean onAnswer;
    private Evaluator evaluator;

    public ResultField(AnswerField answerField) {
        super();
        expression = "0";

        this.answerField = answerField;

        this.evaluator = Evaluator.getInstance();

        this.resetExpression();

        this.setEditable(false);
        this.setForeground(Color.DARK_GRAY);
        this.setBackground(Color.WHITE);

        this.setFont(new Font("SansSerif", Font.BOLD, 24));
        this.setHorizontalAlignment(JTextField.RIGHT);
        this.setText(expression);
        this.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        this.onAnswer = false;
    }

    public void evaluate() {
        if (tokens.isEmpty()) {
            return;
        }

        try {
            Token result = evaluator.evaluate(tokens);
            answerField.setAnswer(result);

            resetExpression();

            this.addToken(result);
            this.onAnswer = true;
        } catch (Exception e) {
            answerField.setError(true);
            resetExpression();
            setExpression("Error");

            throw e;
        }
    }

    public void setExpression(String expression) {
        this.expression = expression;
        this.setText(expression);
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void addAnswer() {
        if (answerField == null) {
            return;
        }

        this.addToken(answerField.answerToken);
    }

    private void resetExpression() {
        try {
            expression = "0";
            this.tokens.clear();
        } catch (NullPointerException e) {
            this.tokens = new ArrayList<>();
        }

        this.setText(expression);
    }

    public void removeLastToken() {
        if (tokens.isEmpty()) {
            return;
        }

        tokens.remove(tokens.size() - 1);
        expression = "";

        for (Token t : tokens) {
            expression += t.tokenStr;
        }

        this.setText(expression);
    }

    public void addToken(Token token) {
        if (onAnswer && token.tokenType != TokenType.OPERATOR) {
            resetExpression();
        }

        tokens.add(token);
        expression = "";

        for (Token t : tokens) {
            expression += t.tokenStr;
        }

        this.setText(expression);

        onAnswer = false;
    }

    public Token getLastToken() {
        if (tokens.isEmpty()) {
            return null;
        }

        return tokens.get(tokens.size() - 1);
    }

    public void setAngleMode(angleModes mode) {
        evaluator.setAngleMode(mode);
    }

    public void deleteLast() {
        if (expression.length() <= 1) {
            resetExpression();
        } else {
            this.tokens.remove(tokens.size() - 1);

            expression = "";

            for (Token t : tokens) {
                expression += t.tokenStr;
            }

            this.setText(expression);
        }

        this.setText(expression);
    }

    public void clear() {
        tokens.clear();
        expression = "0";
        this.setText(expression);
    }
}
