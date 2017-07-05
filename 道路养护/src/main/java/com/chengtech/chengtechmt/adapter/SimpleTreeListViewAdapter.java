package com.chengtech.chengtechmt.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.util.Node;


public class SimpleTreeListViewAdapter<T> extends TreeListViewAdapter<T> {

	public SimpleTreeListViewAdapter(ListView tree, Context context,
			List<T> datas, int defaultExpandLevel)
			throws IllegalAccessException, IllegalArgumentException {
		super(tree, context, datas, defaultExpandLevel);
	}

	@Override
	public View getConvertView(Node node, int position, View convertView,
							   ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView==null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.tree_listview_item, parent,false);
			holder.tv = (TextView) convertView.findViewById(R.id.tree_listview_tv);
			holder.iv = (ImageView) convertView.findViewById(R.id.tree_listview_iv);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (node.getIcon()==-1) {
			holder.iv.setVisibility(View.INVISIBLE);
		}else {
			holder.iv.setVisibility(View.VISIBLE);
			holder.iv.setImageResource(node.getIcon());
		}
		holder.tv.setText(node.getName());
		return convertView;
	}
	
	class ViewHolder{
		public TextView tv;
		public ImageView iv;
	}

}
