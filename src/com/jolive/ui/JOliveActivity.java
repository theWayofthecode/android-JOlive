package com.jolive.ui;


import com.jolive.omero.OOlive;

import uk.ac.rdg.resc.jstyx.StyxException;
import uk.ac.rdg.resc.jstyx.client.CStyxFile;
import uk.ac.rdg.resc.jstyx.client.StyxConnection;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;

public class JOliveActivity extends Activity {
	Handler ooHandler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigureLog4j.configure();
        
        ScrollView vRootComp = new ScrollView(this);
        vRootComp.addView(init("192.168.2.4", 4987)); 
        HorizontalScrollView hRootComp = new HorizontalScrollView(this);
        hRootComp.addView(vRootComp);
        setContentView(hRootComp);
    }
    
    ViewGroup init(String ip, int port) {
		FrameLayout rootComp = new FrameLayout(this);
		try {
			StyxConnection conn = new StyxConnection(ip, port);
			conn.connect();		
			CStyxFile root = conn.getRootDirectory();
			ooHandler = new Handler();
			(new OOlive(root.getFile("olive"), ooHandler)).start();
			CStyxFile main = root.getFile("main");
			JOPanel jp = new JOPanel(main, rootComp);
			jp.createUITree(main, rootComp);
		} catch (StyxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rootComp;
    }
}
