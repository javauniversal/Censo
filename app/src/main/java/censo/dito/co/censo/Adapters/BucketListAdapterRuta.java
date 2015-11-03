package censo.dito.co.censo.Adapters;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.List;

import censo.dito.co.censo.Entity.Ruta;
import censo.dito.co.censo.R;


public abstract class BucketListAdapterRuta<T> extends BaseAdapter {

    protected List<Ruta> elements;
    protected Activity ctx;
    protected Integer bucketSize;

    public BucketListAdapterRuta(Activity ctx, List<Ruta> elements) {
        this(ctx, elements, 1);
    }

    public BucketListAdapterRuta(Activity ctx, List<Ruta> elements, Integer bucketSize) {
        this.elements = elements;
        this.ctx = ctx;
        this.bucketSize = bucketSize;
    }

    public void enableAutoMeasure(float minBucketElementWidthDip) {
        float screenWidth = getScreenWidthInDip();

        if (minBucketElementWidthDip >= screenWidth) {
            bucketSize = 1;
        } else {
            bucketSize = (int) (screenWidth / minBucketElementWidthDip);
        }
    }
    @Override
    public int getCount() {
        return (elements.size() + bucketSize - 1) / bucketSize;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int bucketPosition, View convertView, ViewGroup parent) {
        ViewGroup bucket = (ViewGroup) View.inflate(ctx, R.layout.bucket, null);

        for (int i = (bucketPosition * bucketSize); i < ((bucketPosition * bucketSize) + bucketSize); i++) {
            FrameLayout bucketElementFrame = new FrameLayout(ctx);
            bucketElementFrame.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            bucketElementFrame.setPadding(3, 0, 3, 0);

            if (i < elements.size()) {
                View current = getBucketElement(i, elements.get(i));
                bucketElementFrame.addView(current);
            }

            bucket.addView(bucketElementFrame);
        }

        return bucket;
    }

    protected abstract View getBucketElement(final int position, Ruta currentElement);

    protected float getScreenWidthInDip() {
        WindowManager wm = ctx.getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth_in_pixel = dm.widthPixels;
        float screenWidth_in_dip = screenWidth_in_pixel / dm.density;

        return screenWidth_in_dip;
    }
}

class ViewHolder2 {

    private static FrameLayout card;

    ViewHolder2(View row) {
    }
}