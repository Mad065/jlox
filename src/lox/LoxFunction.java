package lox;

import java.util.ArrayList;
import java.util.List;

class LoxFunction implements LoxCallable {
    private final String name;
    private final List<Token> parameters;
    private final List<Stmt> body;
    private final Environment closure;
    private final boolean isInitializer;
    private final boolean isGetter;

    // function declaration
    LoxFunction(Stmt.Function declaration, Environment closure, boolean isInitializer) {
        this.isInitializer = isInitializer;
        this.isGetter = (declaration.params == null); // null for getters
        this.name = declaration.name.lexeme;
        this.parameters = declaration.params != null ? declaration.params : new ArrayList<>();
        this.body = declaration.body;
        this.closure = closure;
    }

    // function expression (lambdas)
    LoxFunction(Expr.Function declaration, Environment closure, boolean isInitializer) {
        this.isInitializer = isInitializer;
        this.isGetter = false; // Las lambdas nunca son getters
        this.name = null;
        this.parameters = declaration.params;
        this.body = declaration.body;
        this.closure = closure;
    }

    // Constructor private for bind()
    private LoxFunction(String name, List<Token> parameters, List<Stmt> body, Environment closure, boolean isInitializer, boolean isGetter) {
        this.isInitializer = isInitializer;
        this.isGetter = isGetter;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
        this.closure = closure;
    }

    LoxFunction bind(LoxInstance instance) {
        Environment environment = new Environment(closure);
        environment.define("this", instance);
        return new LoxFunction(name, parameters, body, environment, isInitializer, isGetter);
    }

    // getters in java
    public boolean isGetter() {
        return isGetter;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);

        for (int i = 0; i < parameters.size(); i++) {
            environment.define(parameters.get(i).lexeme, arguments.get(i));
        }

        try {
            interpreter.executeBlock(body, environment);
        } catch (Return returnValue) {
            if (isInitializer) return closure.getAt(0, "this");
            
            return returnValue.value;
        }

        if (isInitializer) return closure.getAt(0, "this");
        return null;
    }

    @Override
    public int arity() {
        return parameters.size();
    }

    @Override
    public String toString() {
        if (name == null) return "<fn>";
        return "<fn " + name + ">";
    }
}
