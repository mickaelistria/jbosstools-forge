package org.jboss.tools.aesh.core.ansi;

import org.jboss.tools.aesh.core.document.Document;


public class EraseInLine extends ControlSequence {

	public EraseInLine(String arguments) {}

	@Override
	public ControlSequenceType getType() {
		return ControlSequenceType.ERASE_IN_LINE;
	}
	
	@Override
	public void handle(Document document) {
		document.replace(
				document.getCursorOffset(), 
				document.getLength() - document.getCursorOffset(), 
				"");
		document.setDefaultStyleRange();
	}

}
