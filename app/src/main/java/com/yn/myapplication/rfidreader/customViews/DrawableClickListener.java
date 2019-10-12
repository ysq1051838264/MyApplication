package com.yn.myapplication.rfidreader.customViews;

public interface DrawableClickListener {
	public static enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT };
    public void onClick(DrawablePosition target); 
    }
