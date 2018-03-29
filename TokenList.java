
public class TokenList {
	TokenLink first;
	TokenLink last;
	
	TokenList(){
		InsertLast("startId", "0startVar0");
	}

	// Inserts link at the end
	public void InsertLast(String inputType, String inputTxt) {
		TokenLink newLink = new TokenLink(inputType, inputTxt);

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
		TokenLink current = first;

		do {
			current.PrintLink();
			current = current.next;
		} while (current != first);
	}// PrintAll
}// TokenList