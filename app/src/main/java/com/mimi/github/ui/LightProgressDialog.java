package com.mimi.github.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.mimi.github.R;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.FROYO;
import static android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;

/**
 * Created by zwb on 15-10-15.
 */
public class LightProgressDialog extends ProgressDialog {

    private LightProgressDialog(Context context, CharSequence message) {
        super(context, THEME_HOLO_LIGHT);
    }

    public static AlertDialog create(Context context, int resId) {
        return create(context, context.getResources().getString(resId));
    }

    public static AlertDialog create(Context context, CharSequence message) {
        if (SDK_INT > FROYO) {
            ProgressDialog dialog;
            if (SDK_INT >= ICE_CREAM_SANDWICH)
                dialog = new LightProgressDialog(context, message);
            else {
                dialog = new ProgressDialog(context);
                dialog.setInverseBackgroundForced(true);
            }
            dialog.setMessage(message);
            dialog.setIndeterminate(true);
            dialog.setProgressStyle(STYLE_SPINNER);
            dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.spinner));
            return dialog;
        } else {
            /*AlertDialog dialog = LightAlertDialog.create(context);
            dialog.setInverseBackgroundForced(true);
            View view = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null);
            ((TextView) view.findViewById(R.id.tv_loading)).setText(message);
            dialog.setView(view);
            return dialog;*/
            return null;
        }
    }
}
