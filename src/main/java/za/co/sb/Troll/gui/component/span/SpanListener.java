package za.co.sb.Troll.gui.component.span;


import java.util.EventListener;

public interface SpanListener extends EventListener {
	/**
	 * A Span was added to the SpanModel.
	 * @param sme The event
	 */
	void spanAdded(SpanEvent sme);

	/**
	 * A Span was removed from the SpanModel.
	 * @param sme The event
	 */
	void spanDeleted(SpanEvent sme);
}

