package censo.dito.co.censo.Activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;


public class EditTextRequired extends EditText {

    public boolean isRequire;

    public boolean isRequire() {
        return isRequire;
    }

    public void setIsRequire(boolean isRequire) {
        this.isRequire = isRequire;
    }

    public EditTextRequired(Context context) {
        super(context);
    }

    public EditTextRequired(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextRequired(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EditTextRequired(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


}
