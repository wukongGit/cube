package com.sunc.cube.dialog;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnShowListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.sunc.cube.R;
import com.sunc.cube.utils.AndroidUtils;
import com.sunc.cube.utils.EventUtil;


/**
 * 显示在窗口中间的 Dialog
 */
public class CenterDialog {
    private static final int DEFAULT_CLICK_DURATION = 500;// milliseconds
    private final DialogPlus mDialog;

    private final TextView mContent;
    private final TextView mTitle;

    private final TextView mNegative;
    private final TextView mPositive;

    private CenterDialog(DialogPlus dialog) {
        mDialog = dialog;

        mTitle = (TextView) mDialog.findViewById(R.id.title);
        mContent = (TextView) mDialog.findViewById(R.id.content);

        mNegative = (TextView) mDialog.findViewById(R.id.negative);
        mPositive = (TextView) mDialog.findViewById(R.id.positive);

    }

    public static CenterDialog create(Activity activity, String title, String content,
                                      String negative, OnClickListener negativeListener, String positive,
                                      OnClickListener positiveListener) {
        return create(activity, title, content, Gravity.CENTER, negative, negativeListener, positive, positiveListener);
    }

    public static CenterDialog create(Activity activity, String title, String content, int gravity,
                                      String negative, OnClickListener negativeListener, String positive,
                                      OnClickListener positiveListener) {
        CenterDialog centerDialog = new Builder(activity).footer().build().setContent(content, gravity);
        if (!TextUtils.isEmpty(title)) {
            centerDialog.getTitle().setVisibility(View.VISIBLE);
            centerDialog.setTitle(title);
        } else {
            centerDialog.getTitle().setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(negative)) {
            centerDialog.setNegative(negative, negativeListener);
        }
        if (!TextUtils.isEmpty(positive)) {
            centerDialog.setPositive(positive, positiveListener);
        }
        return centerDialog;
    }

    public static CenterDialog create(Activity activity, String title, String content, boolean isCancelable,
                                      String negative, OnClickListener negativeListener, String positive,
                                      OnClickListener positiveListener) {
        return create(activity, title, content, Gravity.CENTER, isCancelable, negative, negativeListener, positive, positiveListener);
    }

    public static CenterDialog create(Activity activity, String title, String content, int gravity, boolean isCancelable,
                                      String negative, OnClickListener negativeListener, String positive,
                                      OnClickListener positiveListener) {
        CenterDialog centerDialog = null;
        if (isCancelable) {
            centerDialog = new Builder(activity).footer().build().setContent(content, gravity);
        } else {
            centerDialog = new Builder(activity).footer().noncancelable().build().setContent(content, gravity);
        }
        if (!TextUtils.isEmpty(title)) {
            centerDialog.getTitle().setVisibility(View.VISIBLE);
            centerDialog.setTitle(title);
        } else {
            centerDialog.getTitle().setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(negative)) {
            centerDialog.setNegative(negative, negativeListener);
        }
        if (!TextUtils.isEmpty(positive)) {
            centerDialog.setPositive(positive, positiveListener);
        }
        return centerDialog;
    }

    public DialogPlus getDialog() {
        return mDialog;
    }

    public CenterDialog setNegative(CharSequence text, final OnClickListener listener) {
        mPositive.setBackgroundResource(R.drawable.bg_border_corners_br5_transparent_gray);
        mNegative.setVisibility(View.VISIBLE);
        mNegative.setText(text);
        mNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(EventUtil.isFastDoubleClick()) {
                    return;
                }
                if (listener != null) {
                    listener.onClick(mDialog, mPositive);
                } else {
                    mDialog.dismiss();
                }
            }
        });
//        RxView.clicks(mNegative)
//                .throttleFirst(DEFAULT_CLICK_DURATION, TimeUnit.MILLISECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        if (listener != null) {
//                            listener.onClick(mDialog, mNegative);
//                        } else {
//                            mDialog.dismiss();
//                        }
//                    }
//                });
        return this;
    }

    public CenterDialog setPositive(CharSequence text, final OnClickListener listener) {
        mPositive.setText(text);
        mPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(EventUtil.isFastDoubleClick()) {
                    return;
                }
                if (listener != null) {
                    listener.onClick(mDialog, mPositive);
                } else {
                    mDialog.dismiss();
                }
            }
        });
//        RxView.clicks(mPositive)
//                .throttleFirst(DEFAULT_CLICK_DURATION, TimeUnit.MILLISECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        if (listener != null) {
//                            listener.onClick(mDialog, mPositive);
//                        } else {
//                            mDialog.dismiss();
//                        }
//                    }
//                });
        return this;
    }

    public CenterDialog hideNegative() {
        mNegative.setVisibility(View.GONE);
        return this;
    }

    public TextView getContent() {
        return mContent;
    }

    public CenterDialog setContent(CharSequence text) {
        if(text == null || text.length() <= 0) {
            mContent.setVisibility(View.GONE);
            return this;
        }
        mContent.setText(text);
        return this;
    }

    public CenterDialog setContent(CharSequence text, int gravity) {
        if(text == null || text.length() <= 0) {
            mContent.setVisibility(View.GONE);
            return this;
        }
        mContent.setText(text);
        mContent.setGravity(gravity);
        return this;
    }

    public TextView getTitle() {
        return mTitle;
    }

    public CenterDialog setTitle(CharSequence text) {
        mTitle.setText(text);
        return this;
    }

    public CenterDialog hideTitle() {
        mTitle.setVisibility(View.GONE);
        return this;
    }

    public TextView getNegative() {
        return mNegative;
    }

    public TextView getPositive() {
        return mPositive;
    }

    public CenterDialog show() {
        mDialog.show();
        return this;
    }

    public static class Builder {

        private final DialogPlusBuilder mDialogPlusBuilder;

        public Builder(Activity activity) {
            mDialogPlusBuilder = DialogPlus.newDialog(activity)
                    .setContentBackgroundResource(R.drawable.bg_border_corners5_white)
                    .setContentWidth((int) (AndroidUtils.getWidth(activity) * 0.75))
                    .setMargin(0, -1, 0, -1)
                    .setCancelable(true)
                    .setGravity(Gravity.CENTER);
        }

        /**
         * 设置底部
         */
        public Builder footer() {
            mDialogPlusBuilder.setFooter(R.layout.dialog_center_footer);
            return this;
        }

        /**
         * 设置点击返回键或对话框外部不能退出对话框
         */
        public Builder noncancelable() {
            mDialogPlusBuilder.setCancelable(false);
            return this;
        }

        public Builder onBackPressListener(OnBackPressListener listener) {
            mDialogPlusBuilder.setOnBackPressListener(listener);
            return this;
        }

        /**
         * @param contentLayoutRes 内容布局资源
         */
        public Builder contentLayoutRes(@LayoutRes int contentLayoutRes) {
            mDialogPlusBuilder.setContentHolder(new ViewHolder(contentLayoutRes));
            return this;
        }

        public Builder contentView(View view) {
            mDialogPlusBuilder.setContentHolder(new ViewHolder(view));
            return this;
        }

        public Builder width(int width) {
            mDialogPlusBuilder.setContentWidth(width);
            return this;
        }

        public Builder height(int height) {
            mDialogPlusBuilder.setContentHeight(height);
            return this;
        }

        public Builder margin(int left, int top, int right, int bottom) {
            mDialogPlusBuilder.setMargin(left, top, right, bottom);
            return this;
        }

        public Builder onShowListener(OnShowListener listener) {
            mDialogPlusBuilder.setOnShowListener(listener);
            return this;
        }

        public Builder onDismissListener(OnDismissListener listener) {
            mDialogPlusBuilder.setOnDismissListener(listener);
            return this;
        }

        public CenterDialog build() {
            if (mDialogPlusBuilder.getHolder() == null) {
                mDialogPlusBuilder.setContentHolder(new ViewHolder(R.layout.dialog_center_content));
            }
            return new CenterDialog(mDialogPlusBuilder.create());
        }
    }
}
