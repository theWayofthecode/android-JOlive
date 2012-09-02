package com.jolive.omero;

import java.util.HashMap;

import org.apache.mina.common.ByteBuffer;

import android.os.Handler;

import com.jolive.ui.JOPanel;
import uk.ac.rdg.resc.jstyx.StyxException;
import uk.ac.rdg.resc.jstyx.StyxUtils;
import uk.ac.rdg.resc.jstyx.client.CStyxFile;
import uk.ac.rdg.resc.jstyx.client.StyxConnection;

public class OOlive extends Thread {

	Handler ooHandler;
	public static HashMap<String, JOPanel> panelRegistry;

	CStyxFile oliveFD = null;
	
	public OOlive(CStyxFile oliveFD, Handler ooHandler) {
		this.oliveFD = oliveFD;
		panelRegistry = new HashMap<String, JOPanel>();
		this.ooHandler = ooHandler;
	}
	
	public void run() {
		System.err.println("OOlive started");
		OMeropCtl top = new OMeropCtl("/main", "top");
		byte[] td = new byte[top.packedsize()];
		top.pack().get(td);
		try {
			oliveFD.open(StyxUtils.ORDWR | StyxUtils.OTRUNC);
			oliveFD.writeAll(td, td.length);

		} catch (StyxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (;;) {
			ByteBuffer bf = null;
			try {
				bf = oliveFD.read(0);
			} catch (StyxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			OMerop e = null;
			if (bf.get(bf.position() + 4) == OMerop.UPDATETYPE) {
				e = new OMeropUpdate(bf);
			} else if (bf.get(bf.position() + 4) == OMerop.CTLTYPE) {
				e = new OMeropCtl(bf);
			} else {
				System.err.println("unkown type");
			}
			ooHandler.post(new OOliveEventHandler(e));
		}
	}

}
