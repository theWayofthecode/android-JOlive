package com.jolive.ui;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class TextLongClickListener implements OnLongClickListener {
	JOPanel jop;
	public TextLongClickListener(JOPanel jop) {
		this.jop = jop;
	}

	@Override
	public boolean onLongClick(View v) {
		TextView tv = (TextView)v;
		if (!tv.hasSelection())
			return false;
		String sel = getSelectedText(tv);

		PopupWindow pw = new PopupWindow(v.getContext());
		pw.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, 
							   ViewGroup.LayoutParams.WRAP_CONTENT);
		pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);		
		ImageView popupImg = new ImageView(v.getContext());
		popupImg.setImageResource(R.drawable.swipe_action);
		popupImg.setOnTouchListener(new ActionSwipeListener(pw, sel, jop));		
		pw.setContentView(popupImg);
		pw.showAsDropDown(v);
		return true;
	}

	private String getSelectedText(TextView tv) {
		String text = tv.getText().toString();
		int start = Math.min(tv.getSelectionStart(), tv.getSelectionEnd());
		int end = Math.max(tv.getSelectionStart(), tv.getSelectionEnd());
		return text.substring(start, end);
	}
}
