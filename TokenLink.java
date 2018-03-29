
public class TokenLink {
	TokenLink next;
	TokenLink prev;
	String type = "";
	String tokenTxt;
	boolean floatNum = false;
	
	TokenLink(String inputType, String inputTxt){
		type = inputType;
		tokenTxt = inputTxt;
	}
	
	public void PrintLink() {
		//System.out.print("Type: " + type + " Text: " + tokenTxt);
		
		if (!type.equals("symbol")){
			System.out.println(type);
		} else {
			System.out.println(tokenTxt);
		}
			
	}// PrintLink
}
