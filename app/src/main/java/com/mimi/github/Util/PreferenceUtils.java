package com.mimi.github.Util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

/**
 * Created by zwb on 15-10-15.
 */
public class PreferenceUtils {

    /**
     * Preference to wrap lines of code
     */
    public static final String WRAP = "wrap";

    /**
     * Preference to render markdown
     */
    public static final String RENDER_MARKDOWN = "renderMarkdown";

    /**
     * Get code browsing preferences
     *
     * @param context
     * @return preferences
     */
    public static SharedPreferences getCodePreferences(final Context context) {
        return context.getSharedPreferences("code", MODE_PRIVATE);
    }

    private static boolean isEditorApplyAvailable() {
        return SDK_INT >= GINGERBREAD;
    }

    /**
     * Save preferences in given editor
     *
     * @param editor
     */
    public static void save(final SharedPreferences.Editor editor) {
        if (isEditorApplyAvailable())
            editor.apply();
        else
            editor.commit();
    }
}
