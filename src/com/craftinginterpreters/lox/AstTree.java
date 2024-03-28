package com.craftinginterpreters.lox;

import java.util.List;

// 仿照Linux tree命令在控制台画层次图
public class AstTree implements Expr.Visitor<String>,
                                Stmt.Visitor<String>{
    String print(List<Stmt> statements) {
        StringBuilder builder = new StringBuilder();
        for (Stmt statement : statements) {
            builder.append(draw(statement)).append("\n");
        }

        return builder.toString();
    }

    private String draw(Stmt statement) {
        return statement.accept(this);
    }

    private String draw(Expr expression) {
        return expression.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return tree(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitCallExpr(Expr.Call expr) {
        return null;
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
    public String visitLogicalExpr(Expr.Logical expr) {
        return tree(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return tree(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return tree(expr.name.lexeme + "=", expr.value);
    }

    @Override
    public String visitTernaryExpr(Expr.Ternary expr) {
        return tree("?", expr.condition, expr.left, expr.right);
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return tree("ID: "+ expr.name.lexeme, null);
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

    @Override
    public String visitBlockStmt(Stmt.Block stmt) {

        return "{\n" +
                print(stmt.statements) +
                "}\n";
    }

    @Override
    public String visitExpressionStmt(Stmt.Expression stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append(";").append("\n");

        isLast[layer++] = true;
        builder.append(draw(stmt.expression));
        isLast[--layer] = false;

        return builder.toString();
    }

    @Override
    public String visitFunctionStmt(Stmt.Function stmt) {
        return null;
    }

    @Override
    public String visitIfStmt(Stmt.If stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append("if").append("\n");
        // todo 在语句层面抽象tree命令
        layer++;
        builder.append(draw(stmt.condition));
        builder.append(draw(stmt.thenBranch));
        layer--;
        isLast[layer++] = true;
        builder.append(draw(stmt.elseBranch));
        isLast[--layer] = false;

        return builder.toString();
    }

    @Override
    public String visitPrintStmt(Stmt.Print stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append("print").append("\n");

        isLast[layer++] = true;
        builder.append(draw(stmt.expression));
        isLast[--layer] = false;

        return builder.toString();
    }

    @Override
    public String visitReturnStmt(Stmt.Return stmt) {
        return null;
    }

    @Override
    public String visitVarStmt(Stmt.Var stmt) {
        return null;
    }

    @Override
    public String visitWhileStmt(Stmt.While stmt) {
//        todo
        return null;
    }

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
                                new Expr.Variable(new Token(TokenType.IDENTIFIER, "a", null, 1)),
                                new Token(TokenType.PLUS, "+", null, 1),
                                new Expr.Literal(2))),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(
                        new Expr.Unary(
                                new Token(TokenType.MINUS, "-", null, 1),
                                new Expr.Literal(3))));

        System.out.println(new AstTree().draw(expression));
        System.out.println(new AstTree().draw(expression2));
    }
}
