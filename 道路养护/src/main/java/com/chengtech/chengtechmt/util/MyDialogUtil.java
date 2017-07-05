package com.chengtech.chengtechmt.util;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.loopj.android.http.AsyncHttpClient;

public class MyDialogUtil {

    /**
     * 返回一个加载框
     *
     * @param context
     * @param tip
     * @return
     * @author liufuyingwang
     * 2015-9-7 下午4:57:04
     */
    public static Dialog createDialog(final Context context, String tip) {
        View view = LayoutInflater.from(context).inflate(R.layout.progressdialog_no_deal, null, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        TextView textView = (TextView) view.findViewById(R.id.tipTextView);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.dialog_view);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim);
        imageView.setAnimation(animation);
        textView.setText(tip);

        Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(linearLayout, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));


        return dialog;
    }
}
