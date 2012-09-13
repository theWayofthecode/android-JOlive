package com.jolive.ui;


import com.jolive.omero.OOlive;

import uk.ac.rdg.resc.jstyx.StyxException;
import uk.ac.rdg.resc.jstyx.client.CStyxFile;
import uk.ac.rdg.resc.jstyx.client.StyxConnection;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TableLayout;

public class JOliveActivity extends Activity {
	public Handler ooHandler;
	public OOlive ooLive = null;
	public CStyxFile root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigureLog4j.configure();
		StyxConnection conn = new StyxConnection("192.168.2.4", 4987);
		try {
			conn.connect();
		} catch (StyxException e) {
			System.err.println("Could not reach PC");
			e.printStackTrace();
		}
		root = conn.getRootDirectory();
		ooHandler = new Handler();		
        setContentView(initScreen(root.getFile("main")));
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected (MenuItem item) {
    	CStyxFile appl = root.getFile("appl");
    	CStyxFile[] apps = new CStyxFile[0];
		try {
			apps = appl.getChildren();
		} catch (StyxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String[] appPaths = new String[apps.length];
    	boolean[] checked = new boolean[apps.length];
		for (int i = 0; i < apps.length; i++) {
			appPaths[i] = apps[i].getPath();
		}

		PullAppListener pl = new PullAppListener(appPaths, checked, this.root, this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMultiChoiceItems(appPaths, checked, pl);
		builder.setCancelable(false);
	    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                dialog.cancel();
	           }
	       });
	    builder.setPositiveButton("Pull", pl);
		AlertDialog alert = builder.create();
		alert.show();
    	return true;
    }

    public ViewGroup initScreen(CStyxFile screen) {
		FrameLayout rootComp = new FrameLayout(this);
        ScrollView vRootComp = new ScrollView(this);
        vRootComp.addView(rootComp); 
        HorizontalScrollView hRootComp = new HorizontalScrollView(this);
        hRootComp.addView(vRootComp);

		if (ooLive != null) {
			OOlive.panelRegistry.clear();
			ooLive.interrupt();
		}
		ooLive = new OOlive(root.getFile("olive"), ooHandler, screen.getPath());
		try { 
			JOPanel jp = new JOPanel(screen, rootComp);
			jp.createUITree(screen, rootComp);
		} catch (StyxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ooLive.start();
		return hRootComp;
    }
}
