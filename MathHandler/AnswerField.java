package MathHandler;

import javax.swing.*;
import java.awt.*;
import java.security.PublicKey;
import java.util.ArrayList;

public class AnswerField extends JTextField {
    Token answerToken;
    private boolean isError;

    public AnswerField() {
        super();
        this.answerToken = new Token("0", TokenType.INTEGER, 0, 0);

        this.isError = false;

        this.setEditable(false);
        this.setForeground(Color.LIGHT_GRAY);
        this.setBackground(Color.WHITE);

        this.setFont(new Font("SansSerif", Font.BOLD, 14));
        this.setHorizontalAlignment(JTextField.RIGHT);
        this.setText(answerToken.tokenStr);
        this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
    }

    public void setAnswer(Token answer) {
        this.answerToken = answer;
        this.setText(answerToken.tokenStr);
    }

    public void setError(boolean isError) {
        this.isError = isError;
        this.setText("Error");
    }
}
