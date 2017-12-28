package com.sunc.cube.dialog;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnShowListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.sunc.cube.R;

/**
 * Dialog for block user operation.
 */
public class SuccessDialog {
    private DialogPlus mDialog;
    private final TextView mContent;
    private final TextView mDescription;
    private final ImageView mClose;

    public SuccessDialog(DialogPlus dialog) {
        mDialog = dialog;
        mContent = (TextView) dialog.findViewById(R.id.content);
        mDescription = (TextView) dialog.findViewById(R.id.description);
        mClose = (ImageView) dialog.findViewById(R.id.close);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public static SuccessDialog create(Activity context) {
        Builder builder = new Builder(context);
        return builder.build();
    }

    public static SuccessDialog create(Activity context, OnBackPressListener onBackPressListener) {
        Builder builder = new Builder(context);
        builder.onBackPressListener(onBackPressListener);
        return builder.build();
    }

    public SuccessDialog content(CharSequence text) {
        if (text != null && text.length() > 0) {
            mContent.setVisibility(View.VISIBLE);
            mContent.setText(Html.fromHtml(String.valueOf(text)));
        }
        return this;
    }

    public SuccessDialog description(CharSequence text) {
        if (text != null && text.length() > 0) {
            mDescription.setVisibility(View.VISIBLE);
            mDescription.setText(text);
        }
        return this;
    }

    public SuccessDialog show() {
        mDialog.show();
        return this;
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public DialogPlus dialog() {
        return mDialog;
    }

    public static class Builder {

        private final DialogPlusBuilder mDialogPlusBuilder;

        public Builder(Activity activity) {
            mDialogPlusBuilder = DialogPlus.newDialog(activity)
                    .setContentHolder(new ViewHolder(R.layout.layout_jiang))
                    .setGravity(Gravity.CENTER)
                    .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        /**
         * 设置背景颜色
         */
        public Builder contentBackgroundResource(int resourceId) {
            mDialogPlusBuilder.setContentBackgroundResource(resourceId);
            return this;
        }

        /**
         * 设置点击返回键或对话框外部不能退出对话框
         */
        public Builder noncancelable() {
            mDialogPlusBuilder.setCancelable(false);
            return this;
        }

        public Builder header(@LayoutRes int layoutRes) {
            mDialogPlusBuilder.setHeader(layoutRes);
            return this;
        }

        public Builder header(View view) {
            mDialogPlusBuilder.setHeader(view);
            return this;
        }

        public Builder content(Holder holder) {
            mDialogPlusBuilder.setContentHolder(holder);
            return this;
        }

        /**
         * @param layoutRes 内容布局资源
         */
        public Builder content(@LayoutRes int layoutRes) {
            mDialogPlusBuilder.setContentHolder(new ViewHolder(layoutRes));
            return this;
        }

        public Builder content(View view) {
            mDialogPlusBuilder.setContentHolder(new ViewHolder(view));
            return this;
        }

        public Builder footer(@LayoutRes int layoutRes) {
            mDialogPlusBuilder.setFooter(layoutRes);
            return this;
        }

        public Builder footer(View view) {
            mDialogPlusBuilder.setFooter(view);
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

        public Builder onBackPressListener(OnBackPressListener listener) {
            mDialogPlusBuilder.setOnBackPressListener(listener);
            return this;
        }

        public SuccessDialog build() {
            return new SuccessDialog(mDialogPlusBuilder.create());
        }
    }
}
