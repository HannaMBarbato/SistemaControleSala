package com.example.sistemacontrolesala;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class ResourcesUtil {
    public static Drawable devolveDrawable(Context context, String drawableTexto) {
        Resources resources = context.getResources();
        int idDrawable = resources.getIdentifier(drawableTexto, "drawable", context.getPackageName());
        return resources.getDrawable(idDrawable);
    }

}
