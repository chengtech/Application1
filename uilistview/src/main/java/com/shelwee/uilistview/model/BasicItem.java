package com.shelwee.uilistview.model;

public class BasicItem implements IListItem {

	private boolean mClickable = true;
	private int mDrawable = -1;
	private CharSequence mTitle;
	private CharSequence mSubtitle;
	private int mColor = -1;


	public BasicItem(CharSequence _title) {
		this.mTitle = _title;
	}

	public BasicItem(CharSequence _title, CharSequence _subtitle) {
		this.mTitle = _title;
		this.mSubtitle = _subtitle;
	}

	public BasicItem(CharSequence _title, CharSequence _subtitle, int _color) {
		this.mTitle = _title;
		this.mSubtitle = _subtitle;
		this.mColor = _color;
	}

	public BasicItem(CharSequence _title, CharSequence _subtitle, boolean _clickable) {
		this.mTitle = _title;
		this.mSubtitle = _subtitle;
		this.mClickable = _clickable;
	}

	public BasicItem(int _drawable, CharSequence _title) {
		this.mDrawable = _drawable;
		this.mTitle = _title;
	}

	public BasicItem(int _drawable, CharSequence _title, CharSequence _subtitle) {
		this.mDrawable = _drawable;
		this.mTitle = _title;
		this.mSubtitle = _subtitle;
	}

	public BasicItem(int _drawable, CharSequence _title, CharSequence _subtitle, int _color) {
		this.mDrawable = _drawable;
		this.mTitle = _title;
		this.mSubtitle = _subtitle;
		this.mColor = _color;
	}

	public int getDrawable() {
		return mDrawable;
	}

	public void setDrawable(int drawable) {
		this.mDrawable = drawable;
	}

	public CharSequence getTitle() {
		return mTitle;
	}

	public void setTitle(CharSequence title) {
		this.mTitle = title;
	}

	public CharSequence getSubtitle() {
		return mSubtitle;
	}

	public void setSubtitle(CharSequence summary) {
		this.mSubtitle = summary;
	}

	public int getColor() {
		return mColor;
	}

	public void setColor(int mColor) {
		this.mColor = mColor;
	}

	@Override
	public boolean isClickable() {
		return mClickable;
	}

	@Override
	public void setClickable(boolean clickable) {
		mClickable = clickable;
	}

}
