package com.abdulwaheed.keyboardnotifier;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.abdulwaheed.keyboardnotifier.callbacks.KeyboardNotifier;


/*
 * This class is used to register Android HardWare keyboard pops in/out
 * */

public class KeyboardUtility {

    private static final String TAG = KeyboardUtility.class.getSimpleName();

    /*
     * This variable is defined because as soon as registerMe method is called, keyboard popping is called by
     * Android System.To overcome this default behaviour, this boolean is defined
     * */
    private static boolean isObservingFirstTime = true;

    /*
     * This method is used to register the Activity on which keyboard pops in/out to be
     * observed.
     *
     * @param notifier ==> parameter holds the reference of the Activity on which keyboard
     * pops in/out to be observed
     * */

    public static void registerMe(final Context context, final KeyboardNotifier notifier, final View view) {
        if (context == null || notifier == null || view == null)
            Log.e(TAG, Errors.ERROR_KEYBOARD_VIEW_NULL);
        else {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //making this observation as regular, because first time there is no need to observe
                    if (isObservingFirstTime) {
                        isObservingFirstTime = false;
                        return;
                    }
                    Rect r = new Rect();
                    view.getWindowVisibleDisplayFrame(r);

                    int heightDiff = view.getRootView().getHeight() - (r.bottom - r.top);

                    float dp = heightDiff / context.getResources().getDisplayMetrics().density;

                    if (dp > Constants.KEYBOARD_THRESH_HOLD_VALUE)
                        notifier.onKeyboardChangeReceive(true);
                    else
                        notifier.onKeyboardChangeReceive(false);
                }
            });
        }
    }

    /**
     * Manually toggle soft keyboard visibility
     *
     * @param context calling context
     */
    public static void openKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Force closes the soft keyboard
     *
     * @param activeView the view with the keyboard focus
     */
    public static void closeKeyboard(View activeView) {
        InputMethodManager inputMethodManager = (InputMethodManager) activeView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
            inputMethodManager.hideSoftInputFromWindow(activeView.getWindowToken(), 0);
    }

    private static final class Constants {
        public static final short KEYBOARD_THRESH_HOLD_VALUE = 200;
    }

    private static final class Errors {
        public static final String ERROR_KEYBOARD_VIEW_NULL = "Context, KeyboardNotifier and View can't be null";
    }

}
