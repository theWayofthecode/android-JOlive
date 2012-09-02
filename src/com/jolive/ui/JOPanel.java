package com.jolive.ui;


import java.util.StringTokenizer;

import com.jolive.omero.OMerop;
import com.jolive.omero.OMeropCtl;
import com.jolive.omero.OMeropUpdate;
import com.jolive.omero.OOlive;
import com.jolive.omero.OAttrs;
import com.jolive.omero.OPanel;
import com.jolive.ui.JOPanel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import uk.ac.rdg.resc.jstyx.StyxException;
import uk.ac.rdg.resc.jstyx.client.CStyxFile;

public class JOPanel extends OPanel {
	private View viewComp;	

	public JOPanel(CStyxFile sf, ViewGroup parComp) {
		super(sf);
		initComponent(parComp.getContext());
		parComp.addView(this.viewComp);
		
		OOlive.panelRegistry.put(panelFd.getPath(), this);
		System.err.println("Registered as " + panelFd.getPath());
	}

	public void createUITree(CStyxFile cf, View parComp) throws StyxException {
		if (!cf.isDirectory())
			return;
		
		System.err.println(cf.getPath());
		JOPanel jop = new JOPanel(cf, (ViewGroup)parComp);
		for (CStyxFile f : cf.getChildren())
			createUITree(f, jop.getComponent());
	}

	
	public void removeUITree(CStyxFile cf) {
		try {
			if (!cf.isDirectory())
				return;
			for (CStyxFile f : cf.getChildren())
				removeUITree(f);
		} catch (StyxException e) {
			System.err.println("Perhaps file is not present. Removing JOPanel anyway.");
		}

		System.err.println("Removed:" + cf.getPath() + OOlive.panelRegistry.containsKey(cf.getPath()));	
		JOPanel jop = OOlive.panelRegistry.get(cf.getPath());
		assert(jop != null);
		ViewGroup vg = (ViewGroup)jop.getComponent().getParent();
		vg.removeView(jop.getComponent());
		vg.invalidate();
		OOlive.panelRegistry.remove(cf.getPath());
	}

	private void initComponent(Context uiCtx) {
		String type = getType();
		OAttrs attr = getAttrs();

		if ("col".equals(type) || attr.col) {
			viewComp = new TableLayout(uiCtx);
		} else if ("row".equals(type) || attr.row) {
			viewComp = new TableRow(uiCtx);
		} else if ("label".equals(type) 
				|| "tbl".equals(type)
				|| "text".equals(type) 
				|| "tag".equals(type)) {
			EditText comp = new EditText(uiCtx);
			comp.setText(getData());
			comp.setOnLongClickListener(new TextLongClickListener(this));
			viewComp = comp;
		} else if ("button".equals(type)) {
			Button b = new Button(uiCtx);
			b.setText(getName().split(":")[1]);
			viewComp = b;
		}
	}
	
	public View getComponent() {
		return viewComp;
	}
	
	public void omeroListener(OMerop e) {
		try {
			if (e.type == OMerop.UPDATETYPE) {
				omeroListenerUpdate((OMeropUpdate) e);
			} else if (e.type == OMerop.CTLTYPE) {
				omeroListenerCtl((OMeropCtl) e);
			}
		} catch (StyxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void omeroListenerCtl(OMeropCtl e) throws StyxException {
		StringTokenizer parser = new StringTokenizer(e.ctl);
		String s = parser.nextToken();
		if ("close".equals(s)) {
			removeUITree(panelFd);
		}
	}

	private void omeroListenerUpdate(OMeropUpdate e) throws StyxException {
		// TODO Auto-generated method stub
		
	}
	
	
}
