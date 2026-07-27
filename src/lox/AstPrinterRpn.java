package lox;

class AstPrinterRpn implements Expr.Visitor<String> {

    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return "";
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        // RPN: Izquierda -> Derecha -> Operador
        return expr.left.accept(this) + " " +
                expr.right.accept(this) + " " +
                expr.operator.lexeme;
    }

    @Override
    public String visitConditionalExpr(Expr.Conditional expr) {
        return "";
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        // En RPN no hay paréntesis, solo visitamos la expresión interna
        return expr.expression.accept(this);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        // RPN: Operando -> Operador unario
        return expr.right.accept(this) + " " + expr.operator.lexeme;
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return "";
    }

    // --- Método Main para probar el AST ---
    public static void main(String[] args) {
        AstPrinterRpn printer = new AstPrinterRpn();

        // 1. Expresión original de tu AstPrinter: -123 * (45.67)
        Expr expression1 = new Expr.Binary(
                new Expr.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal(123)
                ),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(
                        new Expr.Literal(45.67)
                )
        );

        System.out.println("Expresion 1: -123 * (45.67)");
        System.out.println("Resultado RPN: " + printer.print(expression1));
        System.out.println("-----------------------------------");

        // 2. Expresión del desafío RPN: ( 1 + 2 ) * ( 4 - 3 )
        Expr expression2 = new Expr.Binary(
                // Izquierda: ( 1 + 2 )
                new Expr.Grouping(
                        new Expr.Binary(
                                new Expr.Literal(1),
                                new Token(TokenType.PLUS, "+", null, 1),
                                new Expr.Literal(2)
                        )
                ),
                // Operador central: *
                new Token(TokenType.STAR, "*", null, 1),
                // Derecha: ( 4 - 3 )
                new Expr.Grouping(
                        new Expr.Binary(
                                new Expr.Literal(4),
                                new Token(TokenType.MINUS, "-", null, 1),
                                new Expr.Literal(3)
                        )
                )
        );

        System.out.println("Expresion 2: ( 1 + 2 ) * ( 4 - 3 )");
        System.out.println("Resultado RPN: " + printer.print(expression2));
    }
}