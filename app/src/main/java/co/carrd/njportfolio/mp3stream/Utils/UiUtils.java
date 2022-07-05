package co.carrd.njportfolio.mp3stream.Utils;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;

import co.carrd.njportfolio.mp3stream.R;

public class UiUtils {

    /**
     * Sets the title text color of a toolbar in a fragment to a gradient
     */
    public static void setToolbarGradientTitle(View fragmentView) {
        MaterialToolbar toolbar = fragmentView.findViewById(R.id.toolbar);
        // TextView which title resides in is known to be first child
        TextView titleTextView = (TextView) toolbar.getChildAt(0);
        TextPaint titleTextPaint = titleTextView.getPaint();
        float textWidth = titleTextPaint.measureText(titleTextView.getText().toString());
        float textHeight = titleTextView.getTextSize();
        Shader shader = new LinearGradient(0f, 0f, textWidth, textHeight, new int[] {
                ContextCompat.getColor(toolbar.getContext(), R.color.dark_orange),
                ContextCompat.getColor(toolbar.getContext(), R.color.orange)
        }, null, Shader.TileMode.REPEAT);
        titleTextPaint.setShader(shader);
    }
}
