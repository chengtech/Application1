package com.chengtech.chengtechmt.util;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.chengtech.chengtechmt.R;

public class SpinnerPopupwindow extends PopupWindow implements OnItemClickListener{

	private Context mContext;
	private ListView mListView;
	private MyItemOnClickListener myItemOnClickListener;
	private ArrayAdapter<String> adapter;
	
	public SpinnerPopupwindow(Context mContext) {
		super();
		this.mContext = mContext;
		initView();
	}

	private void initView() {
		View view = LayoutInflater.from(mContext).inflate(R.layout.spinner_popup, null,false);
		setContentView(view);
		
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		
		setFocusable(true);
		setAnimationStyle(R.style.PopupAnimationVertical);
		
		ColorDrawable colorDrawable = new ColorDrawable(0x00);
		setBackgroundDrawable(colorDrawable);
		
		mListView = (ListView) view.findViewById(R.id.spinnerpopup_listview);
		mListView.setOnItemClickListener(this);
		
	}
	
	public void setOnItemClickListener(MyItemOnClickListener myItemOnClickListener){
		this.myItemOnClickListener = myItemOnClickListener;
	}
	
	public void setAdapter(ArrayAdapter<String> adapter){
		this.adapter = adapter;
		mListView.setAdapter(adapter);
	}
	
	public void refreshData(){
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if (myItemOnClickListener!=null) {
			dismiss();
			myItemOnClickListener.onItemClcik(position);
		}
		
	}
	
	public interface MyItemOnClickListener {
		public void onItemClcik(int position);
	}
	
	
}
