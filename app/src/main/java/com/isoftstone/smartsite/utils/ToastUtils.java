package com.isoftstone.smartsite.utils;

import android.widget.Toast;
import com.isoftstone.smartsite.common.App;


/**
 * Created by Tok on 2017/8/27.
 */

public class ToastUtils {

    private ToastUtils(){}

    private static Toast mToast;

    public static void showLong(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(App.getAppContext(), text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public static void showShort(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(App.getAppContext(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

}
