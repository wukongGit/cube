package com.sunc.cube.eventbus;

public class NotifyType {
    public static final int UPLOAD_PICTURE_SUCCESS = 100;//成功上传图片


    private int mType;
    private Object mExtra;

    public NotifyType(int type) {
        this(type, null);
    }

    public NotifyType(int type, Object extra) {
        this.mType = type;
        this.mExtra = extra;
    }

    public int getType() {
        return mType;
    }

    public Object getExtra() {
        return mExtra;
    }

    public interface INotify {
        void onNotify(NotifyType type);
    }
}
