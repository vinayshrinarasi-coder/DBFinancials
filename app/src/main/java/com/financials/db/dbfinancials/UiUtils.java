package com.financials.db.dbfinancials;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class UiUtils {
    public static void animateClick(View view, Runnable endAction) {
        view.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() -> {
                    view.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .withEndAction(endAction)
                            .start();
                })
                .start();
    }
}
