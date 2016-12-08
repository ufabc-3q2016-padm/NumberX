package br.edu.ufabc.padm.numberx;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by nuntius on 07/12/16.
 */

public class BoardInterface {
    Activity context;

    public BoardInterface(Activity context) {
        this.context = context;
    }

    //Show a toast from the Board
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    //Long touch vibration from the Board
    @JavascriptInterface
    public void vibrate() {
        Vibrator vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }
}
