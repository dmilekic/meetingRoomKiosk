package tr.com.aliok.meetingroomkiosk.android.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.cengalabs.flatui.views.FlatTextView;

import tr.com.aliok.meetingroomkiosk.android.R;

/**
 * A flat blinking text view for catching attraction.
 */
public class BlinkingTextView extends FlatTextView {

    public BlinkingTextView(Context context) {
        super(context);
    }

    public BlinkingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlinkingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(getResources().getInteger(R.integer.blinking_text_animation_duration)); //You can manage the time of the blink with this parameter
        anim.setStartOffset(getResources().getInteger(R.integer.blinking_text_animation_offset));
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        this.startAnimation(anim);
    }
}
