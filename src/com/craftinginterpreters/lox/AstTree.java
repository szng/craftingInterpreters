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
        return tree("group", expr.expression);
    }

    @Override
//  递归出口
    public String visitLiteralExpr(Expr.Literal expr) {
        String name;
        if (expr.value == null) {
            name = "nil";
        } else {
            name = expr.value.toString();
        }
        return tree(name, null);
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return tree(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitTernaryExpr(Expr.Ternary expr) {
        return tree("?", expr.condition, expr.left, expr.right);
    }

    private String tree(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < layer - 1; i++) {
            if (isLast[i]) {
                builder.append("    ");
            } else {
                builder.append("│   ");
            }
        }

        if (layer != 0) {
            if (isLast[layer - 1]) {
                builder.append("└── ");
            } else {
                builder.append("├── ");
            }
        }

        builder.append(name).append("\n");

        if (exprs != null) {
            // 单字符当作右子树
            for (int i = 0; i < exprs.length; i++) {
                if (i == exprs.length - 1) {
                    isLast[layer] = true;
                }
                layer++;

                builder.append(exprs[i].accept(this));

                layer--;
                isLast[layer] = false;
            }
        }
        return builder.toString();
    }

    private int layer = 0;
    private final boolean[] isLast = new boolean[100];

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
