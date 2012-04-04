package org.vaadin.cytographer.widgetset.client.ui;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

public class VSelectionBox extends Rectangle {

	private int selectionBoxStartY;
	private int selectionBoxStartX;
	private int selectionBoxStartYold;
	private int selectionBoxStartXold;
	private boolean selectionBoxVisible = false;
	private boolean selectionBoxRightHandSide = true;

	public VSelectionBox(final int x, final int y, final int width, final int height) {
		super(x, y, width, height);
	}

	public VSelectionBox() {
		super(0, 0, 0, 0);
	}

	public void drawSelectionBox(final DrawingArea canvas, final int currentX, final int currentY) {
		canvas.remove(this);
		int width = Math.abs(selectionBoxStartX - currentX);
		int height = Math.abs(selectionBoxStartY - currentY);

		if (selectionBoxRightHandSide && (currentX < selectionBoxStartX - 1 || currentY < selectionBoxStartY - 1)) {
			selectionBoxRightHandSide = false;
			selectionBoxStartXold = selectionBoxStartX;
			selectionBoxStartYold = selectionBoxStartY;
		} else if (!selectionBoxRightHandSide && (currentX > selectionBoxStartX + 1 || currentY > selectionBoxStartY + 1)) {
			selectionBoxRightHandSide = true;
			selectionBoxStartX = selectionBoxStartXold;
			selectionBoxStartY = selectionBoxStartYold;
		}
		if (!selectionBoxRightHandSide) {
			width = Math.abs(selectionBoxStartXold - currentX);
			height = Math.abs(selectionBoxStartYold - currentY);
			selectionBoxStartX = currentX;
			selectionBoxStartY = currentY;
		}
		setX(selectionBoxStartX);
		setY(selectionBoxStartY);
		setWidth(width);
		setHeight(height);
		setStrokeWidth(1);
		setStrokeOpacity(0.5);
		setStrokeColor("#ff0000");
		setFillOpacity(0.09);
		selectionBoxVisible = true;
		canvas.add(this);
	}

	public void setSelectionBoxStartYold(final int selectionBoxStartYold) {
		this.selectionBoxStartYold = selectionBoxStartYold;
	}

	public int getSelectionBoxStartYold() {
		return selectionBoxStartYold;
	}

	public int getSelectionBoxStartY() {
		return selectionBoxStartY;
	}

	public void setSelectionBoxStartY(final int selectionBoxStartY) {
		this.selectionBoxStartY = selectionBoxStartY;
	}

	public int getSelectionBoxStartX() {
		return selectionBoxStartX;
	}

	public void setSelectionBoxStartX(final int selectionBoxStartX) {
		this.selectionBoxStartX = selectionBoxStartX;
	}

	public int getSelectionBoxStartXold() {
		return selectionBoxStartXold;
	}

	public void setSelectionBoxStartXold(final int selectionBoxStartXold) {
		this.selectionBoxStartXold = selectionBoxStartXold;
	}

	public boolean isSelectionBoxVisible() {
		return selectionBoxVisible;
	}

	public void setSelectionBoxVisible(final boolean selectionBoxVisible) {
		this.selectionBoxVisible = selectionBoxVisible;
	}

	public boolean isSelectionBoxRightHandSide() {
		return selectionBoxRightHandSide;
	}

	public void setSelectionBoxRightHandSide(final boolean selectionBoxRightHandSide) {
		this.selectionBoxRightHandSide = selectionBoxRightHandSide;
	}
}
