package com.craftinginterpreters.lox;

// 仿照Linux tree命令在控制台画层次图
public class AstTree implements Expr.Visitor<String> {
    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return tree(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return expr.expression.accept(this);
    }

    @Override
//  递归出口
    public String visitLiteralExpr(Expr.Literal expr) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < layer - 1; i++) {
            if (isLeftandBinary[i]) {
                builder.append("│   ");
            } else {
                builder.append("    ");
            }
        }

        if (layer != 0) {
            if (isLeftandBinary[layer - 1]) {
                builder.append("├── ");
            } else {
                builder.append("└── ");
            }
        }

        String value;
        if (expr.value == null) {
            value = "nil";
        } else {
            value = expr.value.toString();
        }
        builder.append(value).append("\n");
        return builder.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return tree(expr.operator.lexeme, expr.right);
    }

    private String tree(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < layer - 1; i++) {
            if (isLeftandBinary[i]) {
                builder.append("│    ");
            } else {
                builder.append("    ");
            }
        }

        if (layer != 0) {
            if (isLeftandBinary[layer - 1]) {
                builder.append("├── ");
            } else {
                builder.append("└── ");
            }
        }

        builder.append(name).append("\n");

        // 单字符当作右子树
        isLeftandBinary[layer] = exprs.length != 1;
        for (Expr expr : exprs) {
            layer++;
            builder.append(expr.accept(this));
            layer--;
            isLeftandBinary[layer] = false;
        }
        return builder.toString();
    }

    private int layer = 0;
    private final boolean[] isLeftandBinary = new boolean[100];

    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
                new Expr.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Binary(
                                new Expr.Literal(124),
                                new Token(TokenType.PLUS, "+", null, 1),
                                new Expr.Literal(567))),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(
                        new Expr.Literal(45.67)));
        Expr expression2 = new Expr.Binary(
                new Expr.Grouping(
                        new Expr.Binary(
                                new Expr.Literal(1),
                                new Token(TokenType.PLUS, "+", null, 1),
                                new Expr.Literal(2))),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(
                        new Expr.Unary(
                                new Token(TokenType.MINUS, "-", null, 1),
                                new Expr.Literal(3))));

        System.out.println(new AstTree().print(expression));
        System.out.println(new AstTree().print(expression2));
    }
}
