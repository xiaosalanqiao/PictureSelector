package com.luck.picture.lib.tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;


import com.luck.picture.lib.R;

public class PermissionNoticeUtils {
    /**
     * 标识是否已经有弹框，不重复弹框
     */
    private static boolean isShowDialog = false;

    public interface NoticeResultCallback {
        void confirm();

        void cancel();
    }

    public static void noticePermissionUse(Activity activity, String permission, String content, NoticeResultCallback callback) {
        if (ActivityCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED) {
            callback.confirm();
        } else {
            if (!isShowDialog) {
                customizeNoticeDialog(activity, content, "开启", "暂不开启", callback);
                isShowDialog = true;
            }
        }
    }

    public static void customizeNoticeDialog(Activity activity, String content, String confirmText, String cancelText, NoticeResultCallback callBack) {
        Dialog dialog = new Dialog(activity, R.style.picture_notice_dialog);
        View view = View.inflate(activity, R.layout.picture_dialog_notice, null);
        TextView tvContent = view.findViewById(R.id.tv_content);
        tvContent.setText(content);

        TextView confirm = view.findViewById(R.id.btn_confirm);
        confirm.setText(confirmText);
        confirm.setOnClickListener(v -> {
            callBack.confirm();
            dialog.dismiss();
        });

        TextView cancel = view.findViewById(R.id.btn_cancel);
        cancel.setText(cancelText);
        cancel.setOnClickListener(v -> {
            callBack.cancel();
            dialog.dismiss();
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void noticeSetting(Activity activity, String content){
        customizeNoticeDialog(activity, content, "去设置", "取消", new NoticeResultCallback() {
            @Override
            public void confirm() {
                gotoSetting(activity);
            }

            @Override
            public void cancel() {

            }
        });
    }

    public static void gotoSetting(Activity activity){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.fromParts("package", activity.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}
