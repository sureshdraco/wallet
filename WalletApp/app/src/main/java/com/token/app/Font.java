package com.token.app;

import android.content.Context;
import android.graphics.Typeface;
import java.lang.reflect.Field;

public final class Font {
    protected static void replaceFont(String str, Typeface typeface) {
        try {
            Field declaredField = Typeface.class.getDeclaredField(str);
            declaredField.setAccessible(true);
            declaredField.set(null, typeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }

    public static void setDefaultFont(Context context, String str, String str2) {
        replaceFont(str, Typeface.createFromAsset(context.getAssets(), str2));
    }
}
