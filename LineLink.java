public class LineLink {
	LineLink next;
	LineLink prev;
	TokenList tList;

	LineLink() {
		tList = new TokenList();
	}

	public void PrintLink() {
		if (tList.first != null) {
			tList.PrintAll();
			System.out.print('\n');
		}
	}// PrintLink
}// LineLink
