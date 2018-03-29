
public class IdObj {
	String idTxt = "";
	String type = "";
	String parentFunc = "";
	String paramType = "";
	boolean function = false;
	boolean array = false;
//	int arraySize;
	int funcScope;
	IdObj next;
	IdObj prev;
	
	public void Print(){
		System.out.println("ID Text: " + idTxt);
		System.out.println("ID Type: " + type);
		System.out.println("ID Scope: " + parentFunc);
		System.out.println("ID Function: " + function);
		System.out.println();
	}// Print
	
	public boolean Equals(IdObj target){
		boolean same = true;
		
		if (!idTxt.equals(target.idTxt)){
			same = false;
		}
		
		if (!parentFunc.equals(target.parentFunc)){
			same = false;
		}
		
		if (function != target.function){
			same = false;
		}

		if (funcScope != target.funcScope){
			same = false;
		}		
		
		return same;
	}
}// IdObj
