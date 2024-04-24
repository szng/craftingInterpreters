package com.craftinginterpreters.lox;

import java.util.List;

public class LoxArray extends LoxInstance {
    private final Object[] elements;

    LoxArray(int size) {
        super(null);
        elements = new Object[size];
    }

    @Override
    Object get(Token name) {
        switch (name.lexeme) {
            case "get":
                return new LoxCallable() {
                    @Override
                    public int arity() {
                        return 1;
                    }

                    @Override
                    public Object call(Interpreter interpreter,
                                       List<Object> arguments) {
                        int index = (int) (double) arguments.get(0);
                        return elements[index];
                    }
                };
            case "set":
                return new LoxCallable() {
                    @Override
                    public int arity() {
                        return 2;
                    }

                    @Override
                    public Object call(Interpreter interpreter,
                                       List<Object> arguments) {
                        int index = (int) (double) arguments.get(0);
                        Object value = arguments.get(1);
                        return elements[index] = value;
                    }
                };
            case "length":
                return (double) elements.length;
        }

        throw new RuntimeError(name, // [hidden]
                "Undefined property '" + name.lexeme + "'.");
    }

    @Override
    void set(Token name, Object value) {
        throw new RuntimeError(name, "Can't add properties to arrays.");
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[");
        for (int i = 0; i < elements.length; i++) {
            if (i != 0) buffer.append(", ");
            buffer.append(elements[i]);
        }
        buffer.append("]");
        return buffer.toString();
    }
}
