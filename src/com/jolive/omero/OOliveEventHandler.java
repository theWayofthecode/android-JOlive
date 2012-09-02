package com.jolive.omero;

import com.jolive.ui.JOPanel;

public class OOliveEventHandler implements Runnable {
	OMerop e;
	
	public OOliveEventHandler(OMerop e) {
		this.e = e;
	}

	public void run() {
		
		System.err.println("OOlive received:" + e.toString());
		JOPanel jp = OOlive.panelRegistry.get(e.path);
		if (jp != null) {
			System.err.println("Calling omeroListener...");
			jp.omeroListener(e);
		} else {
			System.err.println("OOlive jp null");
		}
	}
}
