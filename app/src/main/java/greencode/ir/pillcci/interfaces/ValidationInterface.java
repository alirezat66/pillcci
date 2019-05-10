package greencode.ir.pillcci.interfaces;

public interface ValidationInterface {
    void onCodeReady(String validCode);

    void onError(String message);
}
