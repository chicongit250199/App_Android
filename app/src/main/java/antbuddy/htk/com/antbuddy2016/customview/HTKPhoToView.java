package antbuddy.htk.com.antbuddy2016.customview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 04/05/2016.
 */
public class HTKPhoToView extends ImageView {

    private ImageView iv_delete;
    private ImageView iv_photo;



    private final static int BUTTON_SIZE_DP = 30;
    private final static int SELF_SIZE_DP = 100;

    public HTKPhoToView(Context context) {
        super(context);
    }

    public HTKPhoToView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public HTKPhoToView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    private void init(Context context){
//        this.iv_delete = new ImageView(context);
//        this.iv_delete.setImageResource(R.drawable.ab_delete);
//        this.iv_delete.setTag("iv_delete");
//
//        this.iv_photo = new ImageView(context);
//        this.iv_photo.setTag("iv_photo");
//
//        int size = convertDpToPixel(SELF_SIZE_DP, getContext());
//
//        FrameLayout.LayoutParams this_params =
//                new FrameLayout.LayoutParams(
//                        size,
//                        size
//                );
//        this_params.gravity = Gravity.CENTER;
//
//        FrameLayout.LayoutParams iv_delete_params =
//                new FrameLayout.LayoutParams(
//                        convertDpToPixel(BUTTON_SIZE_DP, getContext()),
//                        convertDpToPixel(BUTTON_SIZE_DP, getContext())
//                );
//        iv_delete_params.gravity = Gravity.TOP | Gravity.RIGHT;
//
//        FrameLayout.LayoutParams iv_photo_params =
//                new FrameLayout.LayoutParams(
//                        convertDpToPixel(BUTTON_SIZE_DP, getContext()),
//                        convertDpToPixel(BUTTON_SIZE_DP, getContext())
//                );
//        iv_delete_params.gravity = Gravity.CENTER | Gravity.CENTER;
//
//        this.setLayoutParams(this_params);
//        this.addView(iv_delete, iv_delete_params);
//        this.addView(iv_photo, iv_photo_params);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int)px;
    }

    public ImageView getIv_photo() {
        return iv_photo;
    }

    public void setIv_photo(ImageView iv_photo) {
        this.iv_photo = iv_photo;
    }
}
