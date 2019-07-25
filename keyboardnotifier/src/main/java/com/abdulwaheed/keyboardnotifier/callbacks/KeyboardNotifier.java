package com.abdulwaheed.keyboardnotifier.callbacks;

public interface KeyboardNotifier {
    /*
    * This method is called where there is change in keyboard opening and closing status.
    *
    * @param status ==> this params shows the status of keyboard. If status is "true", keyboard is
    * open and if status is "false", keyboard is off
    * */

    void onKeyboardChangeReceive(boolean status);
}
