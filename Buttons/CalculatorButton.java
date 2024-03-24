package Buttons;

import MathHandler.ResultField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class CalculatorButton extends JButton {

    protected ResultField resultField;
    private boolean invertible;
    private String inverseText;


    public CalculatorButton(String text, Color color) {
        this(text);
        this.setBackground(color);
    }

    public CalculatorButton(String text) {
        super(text);

        this.invertible = false;
        this.setOpaque(true);
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        this.addActionListener(e -> handleClick());
    }

    public CalculatorButton(String text, Color color, String inverseText) {
        this(text, color);
        this.invertible = true;
        this.inverseText = inverseText;
    }

    public CalculatorButton(String text, String inverseText) {
        this(text);
        this.invertible = true;
        this.inverseText = inverseText;
    }

    public abstract void handleClick();

    public void setBold() {
        this.setFont(new Font("SansSerif", Font.BOLD, this.getFont().getSize()));
    }

    public void setNormal() {
        this.setFont(new Font("SansSerif", Font.PLAIN, this.getFont().getSize()));
    }

    public void setResultField(ResultField resultField) {
        this.resultField = resultField;
    }

    public void setInvertible(boolean invertible) {
        this.invertible = invertible;
    }

    public boolean isInvertible() {
        return this.invertible;
    }

    public void setInverseText(String inverseText) {
        this.inverseText = inverseText;
    }

    public String getInverseText() {
        return this.inverseText;
    }

}
