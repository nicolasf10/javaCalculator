package MathHandler;

public class Token {
    public String tokenStr;
    public TokenType tokenType;
    public OpType opType;
    public int intVal;
    public double doubleVal;

    public Token(String tokenStr, TokenType tokenType) {
        this.tokenStr = tokenStr;
        this.tokenType = tokenType;
    }

    public Token(String tokenStr, TokenType tokenType, int intVal, double doubleVal) {
        this.tokenStr = tokenStr;
        this.tokenType = tokenType;

        if (tokenType == TokenType.INTEGER) {
            this.intVal = intVal;
        } else {
            this.doubleVal = doubleVal;
        }
    }

    public Token(TokenType tokenType, int intVal, double doubleVal) {
        this.tokenStr = "#";
        this.tokenType = tokenType;

        if (tokenType == TokenType.INTEGER) {
            this.intVal = intVal;
        } else {
            this.doubleVal = doubleVal;
        }
    }

    public Token(String tokenStr, TokenType tokenType, OpType opType) {
        this.tokenStr = tokenStr;
        this.tokenType = tokenType;
        this.opType = opType;
    }
}
