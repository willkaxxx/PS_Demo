package ua.oleksii.shchetinin.ps.demo.exception;

public class SelfSubscribeException extends RuntimeException {
    public SelfSubscribeException(String message) {
        super(message);
    }
}
