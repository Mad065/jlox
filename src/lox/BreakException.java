package lox;

class BreakException extends RuntimeException {
    BreakException() {
        super(null, null, false, false); // Desactiva el stack trace para que sea más rápido
    }
}
