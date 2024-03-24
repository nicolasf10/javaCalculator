import Buttons.*;
import MathHandler.AnswerField;
import MathHandler.ResultField;
import MyColors.MyColors;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Frame {
    private JFrame frame;
    private JPanel panel;
    private JTextField resultField; // Display area for results

    public Frame() {
        // Frame setup
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(700, 400);
        frame.setResizable(false);

        // Generating layout
        panel = new JPanel();
        generateLayout(panel);

        // Adding panel to frame and setting visibility
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void generateLayout(JPanel panel) {
        // Create a new panel for the result and answer fields
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(2, 1));

        // Answer display area
        AnswerField answerField = new AnswerField();
        resultPanel.add(answerField);

        // Result display area
        ResultField resultField = new ResultField(answerField);
        resultPanel.add(resultField);

        // Add the result panel to the main panel
        panel.add(resultPanel, BorderLayout.NORTH);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 7, 1, 1)); // 5 rows, 4 columns, with padding
        buttonPanel.setBackground(MyColors.DARK_GRAY);

        // Angle mode buttons
        SpecialButton degButton = new SpecialButton("Deg");
        degButton.setBold();

        SpecialButton radButton = new SpecialButton("Rad");
        radButton.setNormal();

        degButton.setOpposite(radButton);
        radButton.setOpposite(degButton);

        // Invertable buttons
        FunctionButton cosButton = new FunctionButton("cos", "acos");
        FunctionButton sinButton = new FunctionButton("sin", "asin");
        FunctionButton tanButton = new FunctionButton("tan", "atan");

        SpecialButton invButton = new SpecialButton("Inv");



        // Adding buttons to panel
       ArrayList<CalculatorButton> calculatorButtons = new ArrayList<>(Arrays.asList(new CalculatorButton[] {
               radButton, degButton, new FunctionButton("abs"), new OperatorButton("("), new OperatorButton(")"), new SpecialButton("Del"), new SpecialButton("C"),
                invButton, sinButton, new FunctionButton("ln"), new NumberButton("7"), new NumberButton("8"), new NumberButton("9"), new OperatorButton("÷", MyColors.ORANGE),
                new SpecialButton("π"), cosButton, new FunctionButton("log"), new NumberButton("4"), new NumberButton("5"), new NumberButton("6"), new OperatorButton("×", MyColors.ORANGE),
                new SpecialButton("e"), tanButton, new FunctionButton("√"), new NumberButton("1"), new NumberButton("2"), new NumberButton("3"), new OperatorButton("-", MyColors.ORANGE),
                new SpecialButton("Ans", MyColors.DARK_GRAY, true), new SpecialButton("x^2"), new OperatorButton("x^y"), new NumberButton("0"), new OperatorButton(".", MyColors.NUMBERS_COLOR), new SpecialButton("=", MyColors.ORANGE), new OperatorButton("+", MyColors.ORANGE)
       }));

        for (CalculatorButton button : calculatorButtons) {
            button.setResultField(resultField);
            buttonPanel.add(button);
        }

        invButton.setButtons(calculatorButtons);

        // Add result and button panels to the main panel
        panel.setLayout(new BorderLayout());
        panel.add(resultPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
    }

    public JFrame getFrame() {
        return frame;
    }
}
