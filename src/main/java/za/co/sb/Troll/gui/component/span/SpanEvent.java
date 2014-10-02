package za.co.sb.Troll.gui.component.span;


import java.util.EventObject;

/**
 * An event fired by a SpanModel when some change occurs to it.
 * @author Jonathan Keatley
 */
public class SpanEvent extends EventObject {
	private static final long serialVersionUID = -8615270194976006509L;
	private Span span;

	/**
	 * Constructs a SpanEvent.
	 * @param source The span model firing the event.
	 * @param span The span associated with the event.
	 * @throws IllegalArgumentException if source is null.
	 */
	public SpanEvent(SpanModel source, Span span) {
		super(source);
		this.span = span;
	}

	/**
	 * Constructs a SpanEvent with no span.
	 * @param source The span model firing the event.
	 */
	public SpanEvent(SpanModel source) {
		this(source, null);
	}

	public Span getSpan() {
		return span;
	}
}

