package com.woojeong.global.ifriend;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.util.logging.LogManager;

public class SpinnerReselect extends android.support.v7.widget.AppCompatSpinner {

    public SpinnerReselect(Context context) {
        super(context);
    }

    public SpinnerReselect(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinnerReselect(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setSelection(int position, boolean animate) {
        ignoreOldSelectionByReflection();
        super.setSelection(position, animate);
    }

    private void ignoreOldSelectionByReflection() {
        try {
            Class<?> c = this.getClass().getSuperclass().getSuperclass().getSuperclass();
            Field reqField = c.getDeclaredField("mOldSelectedPosition");
            reqField.setAccessible(true);
            reqField.setInt(this, -1);
        } catch (Exception e) {
            Log.i("Spinner", " " + e);
        }
    }

    @Override
    public void setSelection(int position) {
        ignoreOldSelectionByReflection();
        super.setSelection(position);
    }
}