package lox;

import java.util.List;

class LoxFunction implements LoxCallable {
    private final String name;
    private final List<Token> parameters;
    private final List<Stmt> body;
    private final Environment closure;

    // function declaration
    LoxFunction(Stmt.Function declaration, Environment closure) {
        this.name = declaration.name.lexeme;
        this.parameters = declaration.params;
        this.body = declaration.body;
        this.closure = closure;
    }

    // function expression (anonymous function, lambda)
    LoxFunction(Expr.Function declaration, Environment closure) {
        this.name = null;
        this.parameters = declaration.params;
        this.body = declaration.body;
        this.closure = closure;
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
            return returnValue.value;
        }
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
