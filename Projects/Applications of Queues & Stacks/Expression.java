//Starter code for Project 1
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayDeque;

/** Class to store a node of expression tree
 For each internal node, element contains a binary operator
 List of operators: +|*|-|/|%|^
 Other tokens: (|)
 Each leaf node contains an operand (long integer)
 */

public class Expression {
    public enum TokenType {  // NIL is a special token that can be used to mark bottom of stack
        PLUS, TIMES, MINUS, DIV, MOD, POWER, OPEN, CLOSE, NIL, NUMBER
    }

    public static class Token {
        TokenType token;
        int priority; // for precedence of operator
        Long number;  // used to store number of token = NUMBER
        String string;

        Token(TokenType op, int pri, String tok) {
            token = op;
            priority = pri;
            number = null;
            string = tok;
        }

        // Constructor for number.  To be called when other options have been exhausted.
        Token(String tok) {
            token = TokenType.NUMBER;
            number = Long.parseLong(tok);
            string = tok;
        }

        boolean isOperand() { return token == TokenType.NUMBER; }

        public long getValue() {
            return isOperand() ? number : 0;
        }

        public String toString() { return string; }
    }

    Token element;
    Expression left, right;

    // Create token corresponding to a string
    // tok is "+" | "*" | "-" | "/" | "%" | "^" | "(" | ")"| NUMBER
    // NUMBER is either "0" or "[-]?[1-9][0-9]*
    static Token getToken(String tok) {  // To do
        Token result;
        switch(tok) {
            case "+":
                result = new Token(TokenType.PLUS, 1, tok);  // modify if priority of "+" is not 1
                break;
            case "*":
                result = new Token(TokenType.TIMES, 2, tok);
                break;
            case "-":
                result = new Token(TokenType.MINUS, 1, tok);
                break;
            case "/":
                result = new Token(TokenType.DIV, 2, tok);
                break;
            case "%":
                result = new Token(TokenType.MOD, 2, tok);
                break;
            case "^":
                result = new Token(TokenType.POWER, 3, tok);
                break;
            case "(":
                result = new Token(TokenType.OPEN, -1, tok);
                break;
            case ")":
                result = new Token(TokenType.CLOSE, -1, tok);
                break;
            default:
                result = new Token(tok);
                break;
        }
        return result;
    }

    private Expression() {
        element = null;
    }

    private Expression(Token oper, Expression left, Expression right) {
        this.element = oper;
        this.left = left;
        this.right = right;
    }

    private Expression(Token num) {
        this.element = num;
        this.left = null;
        this.right = null;
    }

    // Given a list of tokens corresponding to an infix expression,
    // return the expression tree corresponding to it.
    public static Expression infixToExpression(List<Token> exp) {  // To do
        ArrayDeque<Token> ops = new ArrayDeque<>();
        ArrayDeque<Expression> values = new ArrayDeque<>();

        for (Token token : exp) {
            if (token.isOperand()) {
                values.push(new Expression(token));
            } else if (token.token == TokenType.OPEN) {
                ops.push(token);
            } else if (token.token == TokenType.CLOSE) {
                while (!ops.isEmpty() && ops.peek().token != TokenType.OPEN) {
                    Token op = ops.pop();
                    Expression right = values.pop();
                    Expression left = values.pop();
                    values.push(new Expression(op, left, right));
                }
                ops.pop();  // pop '('
            } else {
                while (!ops.isEmpty() && ops.peek().priority >= token.priority) {
                    Token op = ops.pop();
                    Expression right = values.pop();
                    Expression left = values.pop();
                    values.push(new Expression(op, left, right));
                }
                ops.push(token);
            }
        }

        while (!ops.isEmpty()) {
            Token op = ops.pop();
            Expression right = values.pop();
            Expression left = values.pop();
            values.push(new Expression(op, left, right));
        }

        return values.pop();
    }

    // Given a list of tokens corresponding to an infix expression,
    // return its equivalent postfix expression as a list of tokens.
    public static List<Token> infixToPostfix(List<Token> exp) {  // To do
        ArrayDeque<Token> ops = new ArrayDeque<>();
        List<Token> postfix = new LinkedList<>();

        for (Token token : exp) {
            if (token.isOperand()) {
                postfix.add(token);
            } else if (token.token == TokenType.OPEN) {
                ops.push(token);
            } else if (token.token == TokenType.CLOSE) {
                while (!ops.isEmpty() && ops.peek().token != TokenType.OPEN) {
                    postfix.add(ops.pop());
                }
                ops.pop();  // pop '('
            } else {
                while (!ops.isEmpty() && ops.peek().priority >= token.priority) {
                    postfix.add(ops.pop());
                }
                ops.push(token);
            }
        }

        while (!ops.isEmpty()) {
            postfix.add(ops.pop());
        }

        return postfix;
    }

    // Given a postfix expression, evaluate it and return its value.
    public static long evaluatePostfix(List<Token> exp) {  // To do
        ArrayDeque<Long> stack = new ArrayDeque<>();

        for (Token token : exp) {
            if (token.isOperand()) {
                stack.push(token.getValue());
            } else {
                long right = stack.pop();
                long left = stack.pop();
                long result = 0;

                switch (token.token) {
                    case PLUS:
                        result = left + right;
                        break;
                    case MINUS:
                        result = left - right;
                        break;
                    case TIMES:
                        result = left * right;
                        break;
                    case DIV:
                        result = left / right;
                        break;
                    case MOD:
                        result = left % right;
                        break;
                    case POWER:
                        result = (long) Math.pow(left, right);
                        break;
                }

                stack.push(result);
            }
        }

        return stack.pop();
    }

    // Given an expression tree, evaluate it and return its value.
    public static long evaluateExpression(Expression tree) {  // To do
        if (tree == null) {
            return 0;
        }

        if (tree.element.token == TokenType.NUMBER) {
            return tree.element.getValue();
        }

        long leftValue = evaluateExpression(tree.left);
        long rightValue = evaluateExpression(tree.right);

        switch (tree.element.token) {
            case PLUS:
                return leftValue + rightValue;
            case MINUS:
                return leftValue - rightValue;
            case TIMES:
                return leftValue * rightValue;
            case DIV:
                return leftValue / rightValue;
            case MOD:
                return leftValue % rightValue;
            case POWER:
                return (long) Math.pow(leftValue, rightValue);
            default:
                return 0;
        }
    }

    // sample main program for testing
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;

        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            in = new Scanner(System.in);
        }

        int count = 0;
        while(in.hasNext()) {
            String s = in.nextLine();
            List<Token> infix = new LinkedList<>();
            Scanner sscan = new Scanner(s);
            int len = 0;
            while(sscan.hasNext()) {
                infix.add(getToken(sscan.next()));
                len++;
            }
            if(len > 0) {
                count++;
                System.out.println("Expression number: " + count);
                System.out.println("Infix expression: " + infix);
                Expression exp = infixToExpression(infix);
                List<Token> post = infixToPostfix(infix);
                System.out.println("Postfix expression: " + post);
                long pval = evaluatePostfix(post);
                long eval = evaluateExpression(exp);
                System.out.println("Postfix eval: " + pval + " Exp eval: " + eval + "\n");
            }
        }
    }
}
