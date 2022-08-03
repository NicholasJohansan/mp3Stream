package co.carrd.njportfolio.mp3stream.Utils;

import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
        setGradientText(titleTextView);
    }

    /**
     * Sets text color of given textview to gradient
     */
    public static void setGradientText(TextView textView) {
        TextPaint textPaint = textView.getPaint();
        float textWidth = textPaint.measureText(textView.getText().toString());
        float textHeight = textView.getTextSize();
        Shader shader = new LinearGradient(0f, 0f, textWidth, textHeight, new int[] {
                ContextCompat.getColor(textView.getContext(), R.color.dark_orange),
                ContextCompat.getColor(textView.getContext(), R.color.orange)
        }, null, Shader.TileMode.REPEAT);
        textPaint.setShader(shader);
    }

    /**
     * Loads an image url onto given image view and given drawable to display when errored
     * @param imageView image view to load image on
     * @param imageUrl image url to load
     * @param errorDrawableId id of drawable displayed when errored
     */
    public static void loadImage(ImageView imageView, String imageUrl, int errorDrawableId) {

        // Create placeholder progressbar while loading image
        CircularProgressDrawable progressDrawable = new CircularProgressDrawable(imageView.getContext());
        imageView.setBackgroundColor(ResourcesCompat.getColor(imageView.getResources(), R.color.gray, null));
        progressDrawable.setColorFilter(ResourcesCompat.getColor(imageView.getResources(), R.color.orange, null), PorterDuff.Mode.SCREEN);
        progressDrawable.setStrokeWidth(5f);
        progressDrawable.setCenterRadius(40f);
        progressDrawable.start();

        // Load with glide
        imageUrl = imageUrl == null ? "" : imageUrl;
        Glide.with(imageView)
                .load(imageUrl)
                .error(errorDrawableId)
                .placeholder(progressDrawable)
                .override(Target.SIZE_ORIGINAL)
                .fitCenter()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setBackgroundColor(ResourcesCompat.getColor(imageView.getResources(), R.color.light_gray, null));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setBackgroundColor(ResourcesCompat.getColor(imageView.getResources(), R.color.light_gray, null));
                        return false;
                    }
                })
                .into(imageView);
    }

    /**
     * Loads an image url onto given image view
     * @param imageView image view to load image on
     * @param imageUrl image url to load
     */
    public static void loadImage(ImageView imageView, String imageUrl) {
        loadImage(imageView, imageUrl, R.drawable.ic_launcher_foreground);
    }
}
