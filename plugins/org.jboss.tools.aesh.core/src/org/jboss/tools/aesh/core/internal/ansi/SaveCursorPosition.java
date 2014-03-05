package org.jboss.tools.aesh.core.internal.ansi;

import org.jboss.tools.aesh.core.ansi.AnsiDocument;


public class SaveCursorPosition extends AbstractControlSequence {

	public SaveCursorPosition(String arguments) {}

	@Override
	public ControlSequenceType getType() {
		return ControlSequenceType.SAVE_CURSOR_POSITION;
	}
	
	@Override
	public void handle(AnsiDocument document) {
		document.saveCursor();
	}

}