
public class Checker {
	public char charType(char input) {
		char type = 'I';
		if (Character.isLetter(input)) {
			type = 'L';
		} else if (Character.isDigit(input)) {
			type = 'D';
		} else {

			switch (input) {

			case ' ':
				type = 'B';
				break;

			case '\t':
				type = 'B';
				break;

			case '\n':
				type = 'B';
				break;

			case '+':
				type = 'S';
				break;

			case '-':
				type = 'S';
				break;

			case '*':
				type = 'S';
				break;

			case '/':
				type = 'S';
				break;

			case '<':
				type = 'S';
				break;

			case '>':
				type = 'S';
				break;

			case '=':
				type = 'S';
				break;

			case ';':
				type = 'S';
				break;

			case ',':
				type = 'S';
				break;

			case '(':
				type = 'S';
				break;

			case ')':
				type = 'S';
				break;

			case '[':
				type = 'S';
				break;

			case ']':
				type = 'S';
				break;

			case '{':
				type = 'S';
				break;

			case '}':
				type = 'S';
				break;
				
			case '!':
				type = 'I';
				break;
				
			default:
				type = 'I';
				break;
			}
		}
		return type;
	}
}
