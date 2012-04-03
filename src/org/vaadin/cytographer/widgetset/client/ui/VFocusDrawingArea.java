package org.vaadin.cytographer.widgetset.client.ui;

import org.vaadin.gwtgraphics.client.DrawingArea;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.vaadin.terminal.gwt.client.VConsole;

public class VFocusDrawingArea extends DrawingArea implements ContextListener, HasAllKeyHandlers, HasFocusHandlers, MouseDownHandler {
	private final VCytographer cytographer;
	private Command delCommand = null;

	public VFocusDrawingArea(final VCytographer vCytographer, final int width, final int height) {
		super(width, height);
		cytographer = vCytographer;
		addMouseDownHandler(this);
	}

	@Override
	public HandlerRegistration addKeyUpHandler(final KeyUpHandler handler) {
		return addDomHandler(handler, KeyUpEvent.getType());
	}

	@Override
	public HandlerRegistration addKeyDownHandler(final KeyDownHandler handler) {
		return addDomHandler(handler, KeyDownEvent.getType());
	}

	@Override
	public HandlerRegistration addKeyPressHandler(final KeyPressHandler handler) {
		return addDomHandler(handler, KeyPressEvent.getType());
	}

	@Override
	public HandlerRegistration addFocusHandler(final FocusHandler handler) {
		return addDomHandler(handler, FocusEvent.getType());
	}

	@Override
	public Command[] getCommands() {
		return new Command[] { delCommand };
	}

	@Override
	public String getCommandName(final Command command) {
		return "Delete selected items";
	}

	@Override
	public void initCommands(final VContextMenu contextMenu) {
		delCommand = contextMenu.new ContextMenuCommand() {
			@Override
			public void execute() {
				super.execute();
				cytographer.deleteSelectedItems();
			}
		};
	}

	@Override
	public void onMouseDown(final MouseDownEvent event) {
		if (event.getNativeEvent().getButton() == NativeEvent.BUTTON_RIGHT) {
			VConsole.log("rightClick");
			final VContextMenu menu = new VContextMenu(VFocusDrawingArea.this);
			menu.showMenu(event.getClientX(), event.getClientY());
			cytographer.setCurrentMenu(menu);
		}
		event.stopPropagation();
	}
}
