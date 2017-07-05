package com.chengtech.chengtechmt.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.chengtech.chengtechmt.util.Node;
import com.chengtech.chengtechmt.util.TreeHelper;

public abstract class TreeListViewAdapter<T> extends BaseAdapter {
	protected Context mContext;
	protected ListView mListView;
	protected List<Node> mAllNodes;
	protected List<Node> mVisibleNodes;
	protected LayoutInflater mInflater;
	
	public interface OnTreeNodeClickListener{
		void onClick(Node node, int position);
	};
	
	public interface onTreeNodeLongClickListener{
		void onLongClick(Node node, int position);
	}
	
	private onTreeNodeLongClickListener mLongClickListener;
	private OnTreeNodeClickListener mListener;
	
	public void setOnTreeNodeClickListener(OnTreeNodeClickListener mListener) {
		this.mListener = mListener;
	}
	
	public void setOnTreeNodeLongClickListener(onTreeNodeLongClickListener mLongClickListener){
		this.mLongClickListener = mLongClickListener;
	}

	public TreeListViewAdapter(ListView tree,Context context,List<T>datas,int defaultExpandLevel) throws IllegalAccessException, IllegalArgumentException {
		mContext = context;
		mListView = tree;
		mInflater = LayoutInflater.from(mContext);
		mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
		mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				expandOrCollapse(position);
				if (mListener!=null) {
					mListener.onClick(mVisibleNodes.get(position), position);
				}
			}
		});
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mLongClickListener!=null) {
					mLongClickListener.onLongClick(mVisibleNodes.get(position), position);
				}
				return true;
			}
		});
	}
	
	/**
	 * ���չ����������
	 * @author liufuyingwang
	 * 2015-5-19 ����3:28:01
	 * @param position
	 */
	protected void expandOrCollapse(int position) {
		Node node = mVisibleNodes.get(position);
		if (node!=null) {
			if (node.isLeaf()) 
				return ;
			node.setExpand(!node.isExpand());
			mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
			notifyDataSetChanged();
		}
		
	}

	@Override
	public int getCount() {
		return mVisibleNodes.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mVisibleNodes.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Node node = mVisibleNodes.get(position);
		convertView = getConvertView(node,position,convertView,parent);
		convertView.setPadding(node.getLevel()*50, 3, 3, 3);
		return convertView;
	}

	public abstract View getConvertView(Node node, int position, View convertView,
			ViewGroup parent);

}
