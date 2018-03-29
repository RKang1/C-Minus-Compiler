
public class LineList {
	LineLink first;
	LineLink last;

	// Inserts link at the end
	public void InsertLast() {
		LineLink newLink = new LineLink();

		if (first == null) {
			first = newLink;
			last = newLink;
			newLink.next = newLink;
			newLink.prev = newLink;
		} else {
			last.next = newLink;
			newLink.prev = last;
			last = newLink;
			last.next = first;
			first.prev = last;
		}
	}// InsertLast
	
	public void PrintAll() {
		LineLink current = first;

		do {
			current.PrintLink();
			current = current.next;
		} while (current != first);
	}// PrintAll
}// LineList
