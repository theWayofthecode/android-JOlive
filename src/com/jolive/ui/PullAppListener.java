package com.jolive.ui;

import com.jolive.omero.OOlive;

import uk.ac.rdg.resc.jstyx.StyxException;
import uk.ac.rdg.resc.jstyx.StyxUtils;
import uk.ac.rdg.resc.jstyx.client.CStyxFile;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;

public class PullAppListener implements OnMultiChoiceClickListener,
										OnClickListener {
	private String[] paths;
	private boolean[] checked;
	private CStyxFile root;
	private JOliveActivity activity;

	public PullAppListener(String[] paths, boolean[] checked, CStyxFile root, JOliveActivity activity) {
		this.paths = paths;
		this.checked = checked;
		this.root = root;
		this.activity = activity;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		CStyxFile myScreen = root.getFile("android");
		try {
			myScreen.openOrCreate(true, StyxUtils.OREAD);
		} catch (StyxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < paths.length; i++) {
			if (checked[i]) {
				CStyxFile app = root.getFile(paths[i]);
				try {
					CStyxFile ctl = app.openFile("ctl", StyxUtils.OWRITE | StyxUtils.ORCLOSE);
					ctl.write("copyto /android/row:wins/col:2".getBytes(), 0, false);
				} catch (StyxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		activity.setContentView(activity.initScreen(myScreen));
	}

	@Override
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		checked[which] = isChecked;
	}

}
