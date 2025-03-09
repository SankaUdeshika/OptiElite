package model;

public class InputController {

    static {
        System.loadLibrary("InputControl"); // Load your native library
    }

    private native void disableInput();

    private native void enableInput();

    public void disable_MOUSEKEYBOARD() {
        disableInput();
    }

    public void enable_MOUSEKEYBOARD() {
        enableInput();
    }

}
