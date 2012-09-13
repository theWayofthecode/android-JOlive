package com.jolive.ui;


import java.util.StringTokenizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;


public class JDrawPanel extends View {

	/*TODO: 
	 * Hash table for colors
	 * Adjust to preferred size
	 * Add support for other instructions too*/

	private static final int[] OSTROKE = {1, 3, 6, 12};
	public String[] instructions;
	private Paint p;

    public JDrawPanel(String data, Context ctx) {
    	super(ctx);
        instructions = data.split("\n");

    	p = new Paint();
    	p.setColor(Color.BLACK);
    	p.setStyle(Paint.Style.STROKE);
    	p.setStrokeWidth(2);
    }

    protected void onMeasure(int width, int height) {
    	setMeasuredDimension(80, 80);
    }

    public void onDraw(Canvas c){
    	super.onDraw(c);
        for (int i = 0; i < instructions.length; i++) {
        	StringTokenizer instr = new StringTokenizer(instructions[i]);
        	String type = instr.nextToken();
        	if ("ellipse".equals(type)) {
        		ellipse(instr, c);
        	} else if ("fillellipse".equals(type)) {
        		fillellipse(instr, c);
        	} else if ("line".equals(type)) {
        		line(instr, c);
        	}
        }
    }
    
    private void ellipse(StringTokenizer instr, Canvas c) {
    	int cx = Integer.parseInt(instr.nextToken());
    	int cy = Integer.parseInt(instr.nextToken());
    	int rx = Integer.parseInt(instr.nextToken());
    	int ry = Integer.parseInt(instr.nextToken());
    	
    	RectF r = new RectF();
    	r.left	= cx - rx;
    	r.top	= cy - ry;
    	r.right	= cx + rx;
    	r.bottom= cy + ry;
   	
    	c.drawOval(r, p);
    }
    
    private void fillellipse(StringTokenizer instr, Canvas c) {
    	int cx = Integer.parseInt(instr.nextToken());
    	int cy = Integer.parseInt(instr.nextToken());
    	int rx = Integer.parseInt(instr.nextToken());
    	int ry = Integer.parseInt(instr.nextToken());
    	
    	//Have to use hashtable for color picking instead of if/else
    	Paint tmp = new Paint();
    	if (instr.hasMoreTokens()) {
    		String col = instr.nextToken();
    		if ("back".equals(col)) {
    			tmp.setColor(Color.LTGRAY);
    		} else if ("mback".equals(col)) {
    			tmp.setColor(Color.DKGRAY);
    		} else if ("bord".equals(col)) {
    			tmp.setColor(Color.RED);
    		}
    	}
    	RectF r = new RectF();
    	r.left	= cx - rx;
    	r.top	= cy - ry;
    	r.right	= cx + rx;
    	r.bottom= cy + ry;
   	
    	c.drawOval(r, tmp);
    }
    
    private void line(StringTokenizer instr, Canvas c) {
       	int ax = Integer.parseInt(instr.nextToken());
    	int ay = Integer.parseInt(instr.nextToken());
    	int bx = Integer.parseInt(instr.nextToken());
    	int by = Integer.parseInt(instr.nextToken());
    	
    	//ea
    	instr.nextToken();
    	//eb
    	instr.nextToken();
    	int r = Integer.parseInt(instr.nextToken());
    	r = JDrawPanel.OSTROKE[r];
    	
/*    	Graphics2D g2D = (Graphics2D)g;
    	g2D.setStroke(new BasicStroke(r));*/

    	c.drawLine(ax, ay, bx, by, p);
//    	g2D.setStroke(new BasicStroke(1));
    }
}
