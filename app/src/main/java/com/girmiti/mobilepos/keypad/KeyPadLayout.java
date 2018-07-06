package com.girmiti.mobilepos.keypad;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.girmiti.mobilepos.R;

/**
 * Created by aravind on 09-10-2017.
 */

public class KeyPadLayout extends FrameLayout{
    public KeyPadLayout(@NonNull Context context) {
        super(context);
    }

    public KeyPadLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyPadLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public KeyPadLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void initializeKeyViews()
    {
        btnpress(R.id.t9_key_0).setOnClickListener((View.OnClickListener) this);
        btnpress(R.id.t9_key_1).setOnClickListener((View.OnClickListener) this);
        btnpress(R.id.t9_key_2).setOnClickListener((View.OnClickListener) this);
        btnpress(R.id.t9_key_3).setOnClickListener((View.OnClickListener) this);
        btnpress(R.id.t9_key_4).setOnClickListener((View.OnClickListener) this);
        btnpress(R.id.t9_key_5).setOnClickListener((View.OnClickListener) this);
        btnpress(R.id.t9_key_6).setOnClickListener((View.OnClickListener) this);
        btnpress(R.id.t9_key_7).setOnClickListener((View.OnClickListener) this);
        btnpress(R.id.t9_key_8).setOnClickListener((View.OnClickListener) this);
        btnpress(R.id.t9_key_9).setOnClickListener((View.OnClickListener) this);
    }

    protected <T extends View> T btnpress(@IdRes int id) {
        return (T) super.findViewById(id);
    }
}
