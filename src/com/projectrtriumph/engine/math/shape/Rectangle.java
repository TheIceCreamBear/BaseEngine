package com.projectrtriumph.engine.math.shape;

import java.awt.geom.Rectangle2D;

public class Rectangle {
	private int x;
	private int y;
	private int width;
	private int height;
	
	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.width = height;
	}
	
	public Rectangle(Rectangle2D r) {
		this.x = (int) Math.round(r.getX());
		this.y = (int) Math.round(r.getY());
		this.width = (int) Math.round(r.getWidth());
		this.height = (int) Math.round(r.getHeight());
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public Rectangle translate(int dx, int dy) {
		this.x += dx;
		this.y += dy;
		return this;
	}
	
	public Rectangle scale(int scale) {
		this.x *= scale;
		this.y *= scale;
		this.width *= scale;
		this.height *= scale;
		return this;
	}
	
	public boolean intersects(Rectangle r) {
		int tw = this.width;
		int th = this.height;
		int rw = r.width;
		int rh = r.height;
		if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
			return false;
		}
		int tx = this.x;
		int ty = this.y;
		int rx = r.x;
		int ry = r.y;
		rw += rx;
		rh += ry;
		tw += tx;
		th += ty;
		//      overflow || intersect
		return ((rw < rx || rw > tx) &&
				(rh < ry || rh > ty) &&
				(tw < tx || tw > rx) &&
				(th < ty || th > ry));
	}
	
	public boolean intersects(Rectangle2D r) {
		int tw = this.width;
		int th = this.height;
		int rw = (int) Math.round(r.getWidth());
		int rh = (int) Math.round(r.getHeight());
		if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
			return false;
		}
		int tx = this.x;
		int ty = this.y;
		int rx = (int) Math.round(r.getX());
		int ry = (int) Math.round(r.getY());
		rw += rx;
		rh += ry;
		tw += tx;
		th += ty;
		//      overflow || intersect
		return ((rw < rx || rw > tx) &&
				(rh < ry || rh > ty) &&
				(tw < tx || tw > rx) &&
				(th < ty || th > ry));
	}
}