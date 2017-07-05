package com.chengtech.chengtechmt.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter;
import com.chengtech.chengtechmt.entity.Menu;

import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/7/20 15:37.
 */
public class SubMenuDialogFragment extends DialogFragment {

    public List<Menu> menus;
    private ExchangeDataListener listener;
    DisplayMetrics displayMetrics;

    public interface ExchangeDataListener{
        public void onExchangData(Object object);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        displayMetrics = getActivity().getResources().getDisplayMetrics();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.submenu_dialog_fragment,container,true);

//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int) (displayMetrics.heightPixels*0.5));
//        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
//        layoutParams.height = (int) (displayMetrics.widthPixels*0.5);
//        view.setLayoutParams(layoutParams);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);

//        ViewGroup.LayoutParams lp2 = recyclerView.getLayoutParams();
//        lp2.width = LinearLayout.LayoutParams.MATCH_PARENT;
//        lp2.height = (int) (displayMetrics.heightPixels*0.5);
//        recyclerView.setLayoutParams(lp2);

        Bundle bundle = getArguments();
        menus = (List<Menu>) bundle.getSerializable("data");
        RecycleViewAdapter adapter2 = new RecycleViewAdapter(getActivity(),menus,null,R.layout.item_recycle1);
        recyclerView.setAdapter(adapter2);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter2.setOnItemClickListener(new RecycleViewAdapter.OnItemClickListener() {
            @Override
            public void setOnItemClickListener(View view, int position) {
                if (listener!=null) {
                    listener.onExchangData(menus.get(position).itemName);
                }
                dismiss();
            }

            @Override
            public void setOnItemLongClickListener(View view, int position) {

            }
        });



        //设置dialog的位置和大小
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ExchangeDataListener) {
            listener = (ExchangeDataListener) activity;
        }
    }


    //    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
//        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
//        // Dialog dialog = new Dialog(getActivity());
//        Dialog dialog = new Dialog(getActivity(), R.style.SubMenuDialog);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
//
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.submenu_dialog_fragment, null, true);
//
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
//        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
//        layoutParams.height = (int) (displayMetrics.widthPixels*0.5);
//        view.setLayoutParams(layoutParams);
//
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
//
//        Bundle bundle = getArguments();
//        menus = (List<Menu>) bundle.getSerializable("data");
//        RecycleViewAdapter adapter2 = new RecycleViewAdapter(getActivity(), menus, null, R.layout.item_recycle1);
//        recyclerView.setAdapter(adapter2);
//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//
//        dialog.setContentView(view);
//        dialog.setCanceledOnTouchOutside(true);
//
//        // 设置宽度为屏宽、靠近屏幕底部。
//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams wlp = window.getAttributes();
//        wlp.gravity = Gravity.BOTTOM;
//        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
//
//        wlp.height = (int) (displayMetrics.heightPixels * 0.5);
//        window.setAttributes(wlp);
//
//        return dialog;
//    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.width = displayMetrics.widthPixels-20;
        lp.height = (int) (displayMetrics.heightPixels*0.5);
        getDialog().getWindow().setAttributes(lp);
    }
}
