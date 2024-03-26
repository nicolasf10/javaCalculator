package MathHandler;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Stack;

public class Evaluator {
    private static Evaluator single_instance = null;
    private angleModes angleMode;

    private Evaluator(angleModes angleMode) {
        this.angleMode = angleMode;
    }

    // Singleton class
    public static synchronized Evaluator getInstance()
    {
        if (single_instance == null)
            single_instance = new Evaluator(angleModes.DEGREES);

        return single_instance;
    }

    public Token evaluate(ArrayList<Token> tokens) {
        // Prepare tokens for evaluation
        tokens = preprocessTokens(tokens);

        // Validating input expression
        if (!validate(tokens)) {
            JOptionPane.showMessageDialog(null, "Invalid expression", "Error", JOptionPane.WARNING_MESSAGE);
            return new Token("0", TokenType.INTEGER, 0, 0);
        }

        // Convert infix to postfix
        ArrayList<Token> postfixTokens = infixToPostfix(tokens);

        // Evaluate the postfix expression
        return evaluatePostfix(postfixTokens);
    }

    public Token evaluatePostfix(ArrayList<Token> postfixTokens) {
        Stack<Token> stack = new Stack<>();

        for (Token token : postfixTokens) {
            if (token.tokenType == TokenType.INTEGER || token.tokenType == TokenType.DOUBLE) {
                stack.push(token);
            } else if (token.tokenType == TokenType.OPERATOR) {
                // Correcting the order of operands for binary operations
                Token second = stack.pop();
                Token first = stack.pop();

                double firstOperand = (first.tokenType == TokenType.INTEGER) ? first.intVal : first.doubleVal;
                double secondOperand = (second.tokenType == TokenType.INTEGER) ? second.intVal : second.doubleVal;

                Token resultToken = performOperation(firstOperand, secondOperand, first.tokenType, second.tokenType, token.opType);
                stack.push(resultToken);
            }
        }

        Token result = stack.pop();

        if (result.tokenType== TokenType.DOUBLE) {
            result.doubleVal = Math.round(result.doubleVal * 1e10) / 1e10;
        }


        return result;
    }

    private Token performOperation(double firstOperand, double secondOperand, TokenType firstType, TokenType secondType, OpType operation) {
        double result;
        switch (operation) {
            case ADD:
                result = firstOperand + secondOperand;
                break;
            case SUBTRACT:
                result = firstOperand - secondOperand;
                break;
            case MULTIPLY:
                result = firstOperand * secondOperand;
                break;
            case DIVIDE:
                if (secondOperand == 0) throw new ArithmeticException("Division by zero.");
                result = firstOperand / secondOperand;
                break;
            case EXPONENT:
                result = Math.pow(firstOperand, secondOperand);
                break;
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operation);
        }

        double roundedResult = Math.round(result * 1e10) / 1e10;

        // Determine if the result should be integer or double
        if (firstType == TokenType.INTEGER && secondType == TokenType.INTEGER && !operation.equals(OpType.DIVIDE)
                && !operation.equals(OpType.EXPONENT) || (result - (int)result) == 0.0) {
            return new Token(String.valueOf((int)result), TokenType.INTEGER, (int)result, 0.0);
        } else {
            return new Token(String.valueOf(roundedResult), TokenType.DOUBLE, 0, result);
        }
    }

    private static int getPrecedence(OpType opType) {
        switch (opType) {
            case ADD:
            case SUBTRACT:
                return 1;
            case MULTIPLY:
            case DIVIDE:
            case MODULO:
                return 2;
            case EXPONENT:
                return 3;
            default:
                return -1; // For non-operators like parentheses
        }
    }

    // Check if the operator is right associative
    private static boolean isRightAssociative(OpType opType) {
        return opType == OpType.EXPONENT;
    }

    public ArrayList<Token> infixToPostfix(ArrayList<Token> tokens) {
        Stack<Token> stack = new Stack<>();
        ArrayList<Token> output = new ArrayList<>();

        Token token;
        Token functionToken;

        // Iterating over tokens in the expression
        for (int i = 0; i < tokens.size(); i++) {
            token = tokens.get(i);

            // If it's a number, add it to the output
            if (token.tokenType == TokenType.INTEGER || token.tokenType == TokenType.DOUBLE) {
                output.add(token);
            } else if (token.tokenType == TokenType.FUNCTION) {
                functionToken = token;
                i++;

                // Get the function's arguments using a stack
                ArrayList<Token> functionArgs = new ArrayList<>();
                Stack<Token> parenthesesStack = new Stack<>();

                while (i < tokens.size()) {
                    token = tokens.get(i);

                    if ((token.tokenType == TokenType.OPERATOR && token.opType == OpType.OPEN_PAREN) || token.tokenType == TokenType.FUNCTION) {
                        parenthesesStack.push(token);
                    } else if (token.tokenType == TokenType.OPERATOR && token.opType == OpType.CLOSE_PAREN) {
                        if (parenthesesStack.isEmpty()) {
                            break; // End of function arguments
                        }
                        parenthesesStack.pop();
                    }

                    functionArgs.add(token);

                    i++;
                }

                Token functionArgument = evaluatePostfix(infixToPostfix(functionArgs));

                output.add(calculateFunction(functionToken, functionArgument));
            }
            else if (token.tokenType == TokenType.OPERATOR) {
                // Handling parentheses
                if (token.opType == OpType.OPEN_PAREN) {
                    stack.push(token);
                } else if (token.opType == OpType.CLOSE_PAREN) {
                    while (!stack.isEmpty() && stack.peek().opType != OpType.OPEN_PAREN) {
                        output.add(stack.pop());
                    }
                    // Removing the OPEN_PAREN from the stack
                    stack.pop();
                } else {
                    // Handling operators according to their precedence
                    while (!stack.isEmpty() && getPrecedence(token.opType) <= getPrecedence(stack.peek().opType) && !isRightAssociative(token.opType)) {
                        output.add(stack.pop());
                    }
                    stack.push(token);
                }
            }
        }

        while (!stack.isEmpty()) {
            output.add(stack.pop()); // Pop any remaining operators to the output
        }

        return output;
    }

    public Token calculateFunction(Token function, Token argument) {
        Token result;

        if (argument.tokenType == TokenType.INTEGER) {
            argument.doubleVal = argument.intVal;
        }

        // stop using string and use opType of token
        if (function.opType == OpType.SIN || function.opType == OpType.COS || function.opType == OpType.TAN) {
            if (angleMode == angleModes.DEGREES) {
                argument.doubleVal = Math.toRadians(argument.doubleVal);
            }
        }

        switch (function.opType) {
            case SIN:
                result = new Token(String.valueOf(Math.sin(argument.doubleVal)), TokenType.DOUBLE, 0, Math.sin(argument.doubleVal));
                break;
            case COS:
                result = new Token(String.valueOf(Math.cos(argument.doubleVal)), TokenType.DOUBLE, 0, Math.cos(argument.doubleVal));
                break;
            case TAN:
                result = new Token(String.valueOf(Math.tan(argument.doubleVal)), TokenType.DOUBLE, 0, Math.tan(argument.doubleVal));
                break;
            case ASIN:
                result = new Token(String.valueOf(Math.asin(argument.doubleVal)), TokenType.DOUBLE, 0, Math.asin(argument.doubleVal));
                break;
            case ACOS:
                result = new Token(String.valueOf(Math.acos(argument.doubleVal)), TokenType.DOUBLE, 0, Math.acos(argument.doubleVal));
                break;
            case ATAN:
                result = new Token(String.valueOf(Math.atan(argument.doubleVal)), TokenType.DOUBLE, 0, Math.atan(argument.doubleVal));
                break;
            case SQRT:
                result = new Token(String.valueOf(Math.sqrt(argument.doubleVal)), TokenType.DOUBLE, 0, Math.sqrt(argument.doubleVal));
                break;
            case LOG:
                result = new Token(String.valueOf(Math.log10(argument.doubleVal)), TokenType.DOUBLE, 0, Math.log10(argument.doubleVal));
                break;
            case LN:
                result = new Token(String.valueOf(Math.log(argument.doubleVal)), TokenType.DOUBLE, 0, Math.log(argument.doubleVal));
                break;
            case ABS:
                result = new Token(String.valueOf(Math.abs(argument.doubleVal)), TokenType.DOUBLE, 0, Math.abs(argument.doubleVal));
                break;
            default:
                throw new IllegalArgumentException("Unsupported function: " + function);
        }

        // Check if the result should be in degrees or radians for inverse trigonometric functions
        if (function.opType == OpType.ASIN || function.opType == OpType.ACOS || function.opType == OpType.ATAN) {
            System.out.println("angleMode: " + angleMode);
            if (angleMode == angleModes.DEGREES) {
                result.doubleVal = Math.toDegrees(result.doubleVal);
                result.tokenStr = String.valueOf(result.doubleVal);
            }

            System.out.println("result: " + result.doubleVal);
        }


        return result;
    }

    public ArrayList<Token> preprocessTokens(ArrayList<Token> inputTokens) {
        ArrayList<Token> processedTokens = new ArrayList<>();
        StringBuilder numberBuilder = new StringBuilder();

        for (int i = 0; i < inputTokens.size(); i++) {
            Token currentToken = inputTokens.get(i);

            // Handle numeric tokens and decimal points
            if (currentToken.tokenType == TokenType.INTEGER || currentToken.tokenType == TokenType.DOUBLE || (currentToken.tokenType == TokenType.OPERATOR && currentToken.opType == OpType.DECIMAL)) {
                numberBuilder.append(currentToken.tokenStr);

                // Check if the next token is part of the number (digit or decimal)
                if (i + 1 < inputTokens.size()) {
                    Token nextToken = inputTokens.get(i + 1);
                    if (!(nextToken.tokenType == TokenType.INTEGER || nextToken.tokenType == TokenType.DOUBLE || (nextToken.tokenType == TokenType.OPERATOR && nextToken.opType == OpType.DECIMAL))) {
                        // Next token is not part of the number, add the number to processedTokens
                        addNumberToken(processedTokens, numberBuilder.toString());
                        numberBuilder = new StringBuilder(); // Reset for next number
                    }
                } else {
                    // Last token is part of the number, add it to processedTokens
                    addNumberToken(processedTokens, numberBuilder.toString());
                }
            } else if (currentToken.tokenType == TokenType.OPERATOR && currentToken.opType == OpType.SUBTRACT || currentToken.opType == OpType.MINUS) {
                // Is negative sign
                if (currentToken.opType == OpType.MINUS) {
                    numberBuilder.append("-");
                } else {
                    // It's a subtraction operator, add any pending number and the operator
                    if (numberBuilder.length() > 0) {
                        addNumberToken(processedTokens, numberBuilder.toString());
                        numberBuilder = new StringBuilder();
                    }
                    processedTokens.add(currentToken);
                }
            } else {
                // For non-numeric tokens, add any pending number token first
                if (numberBuilder.length() > 0) {
                    addNumberToken(processedTokens, numberBuilder.toString());
                    numberBuilder = new StringBuilder();
                }

                // Add special tokens directly
                if (currentToken.tokenType == TokenType.SPECIAL) {
                    Token specialToken = new Token(currentToken.tokenStr, TokenType.DOUBLE, 0, currentToken.doubleVal);
                    processedTokens.add(specialToken);
                } else {
                    // Add operators directly
                    processedTokens.add(currentToken);
                }
            }
        }

        return processedTokens;
    }

    private void addNumberToken(ArrayList<Token> processedTokens, String numberStr) {
        if (numberStr.contains(".")) {
            processedTokens.add(new Token(numberStr, TokenType.DOUBLE, 0, Double.parseDouble(numberStr)));
        } else {
            processedTokens.add(new Token(numberStr, TokenType.INTEGER, Integer.parseInt(numberStr), 0.0));
        }
    }

    public boolean validate(ArrayList<Token> tokens) {
        // Empty expression is not valid
        if (tokens.isEmpty()) return false;

        Stack<Token> parenthesesStack = new Stack<>();
        boolean lastWasOperator = true; // Expression starting with an operator (except minus for negation) is invalid
        boolean lastWasOpenParen = false;

        for (Token token : tokens) {
            switch (token.tokenType) {
                case INTEGER:
                case DOUBLE:
                    if (!lastWasOperator && !lastWasOpenParen) {
                        return false; // Two consecutive numbers without an operator
                    }
                    lastWasOperator = false; // Reset flags
                    lastWasOpenParen = false;
                    break;
                case FUNCTION:
                    parenthesesStack.push(token);
                    lastWasOpenParen = true;
                    lastWasOperator = false; // Resetting since an open paren can be followed by an operator
                    break;
                case OPERATOR:
                    if (token.opType == OpType.OPEN_PAREN) {
                        parenthesesStack.push(token);
                        lastWasOpenParen = true;
                        lastWasOperator = false; // Resetting since an open paren can be followed by an operator
                    } else if (token.opType == OpType.CLOSE_PAREN) {
                        if (parenthesesStack.isEmpty()) {
                            return false; // Unmatched closing parenthesis
                        }
                        parenthesesStack.pop(); // Matching open parenthesis found
                        lastWasOpenParen = false; // Reset flag
                    } else {
                        if (lastWasOperator && token.opType != OpType.SUBTRACT && token.opType != OpType.MINUS) {
                            return false; // Two consecutive operators (except for a negation)
                        }
                        lastWasOperator = true; // It's a regular operator
                        lastWasOpenParen = false; // Reset flag
                    }
                    break;
            }
        }

        return !lastWasOperator && parenthesesStack.isEmpty(); // Expression should not end with an operator and all parentheses should be matched
    }

    public angleModes getAngleMode() {
        return angleMode;
    }

    public void setAngleMode(angleModes angleMode) {
        this.angleMode = angleMode;
    }
}
