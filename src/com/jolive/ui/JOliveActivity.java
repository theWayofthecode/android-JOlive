package com.jolive.ui;


import uk.ac.rdg.resc.jstyx.StyxException;
import uk.ac.rdg.resc.jstyx.client.CStyxFile;
import uk.ac.rdg.resc.jstyx.client.StyxConnection;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TableLayout;

public class JOliveActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigureLog4j.configure();
        ViewGroup rootComp = init("192.168.2.4", 4987);
        setContentView(rootComp);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }
    
    ViewGroup init(String ip, int port) {
		TableLayout rootComp = new TableLayout(this);
		try {
			StyxConnection conn = new StyxConnection(ip, port);
			conn.connect();		
			CStyxFile root = conn.getRootDirectory();

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
