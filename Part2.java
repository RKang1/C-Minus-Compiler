public class Part2 {
	boolean accept = true;
	TokenList tokList = new TokenList();
	TokenLink currentToken;
	IdObj newIdObj;
	IdObj newFuncObj;
	IdObj currIdObj;
	String currFunc = "";
	String currType;
	String retType;
	String callType;
	String currArgTypeList;
	String currParamTypeList;
	String currExprType;
	int scopeLevel = -1;
	boolean actvArg;
	boolean retNeed;
	HashTable idSymTab;

	public static void main(String[] args) {
		Part2 project2 = new Part2();
		
		if (!project2.Driver(args)){
			System.out.println("REJECT");
		} else {
			System.out.println("ACCEPT");
		}
	
	}// main

	public boolean Driver(String[] argArr) {
		Part1 project1 = new Part1();
		idSymTab = new HashTable();
		boolean pass;

		project1.Driver(argArr);

		tokList = project1.tList;
		currentToken = tokList.first.next;

		// tokList.PrintAll();

		if (tokList.first != null) {
			if (Program()) {
				// System.out.println("ACCEPT");
				pass = true;
			} else {
				// System.out.println("REJECT");
				pass = false;
			}
		} else {
			// System.out.println("REJECT");
			pass = false;
		}

		return pass;
	}// Driver

	public boolean Program() {
		boolean pass = true;
		pass = DeclarationList();

		if (currentToken != tokList.first) {
			pass = false;
		}
		return pass;
	}// Program

	public boolean DeclarationList() {
		boolean pass = true;

		if (currentToken.tokenTxt.equals("void") && currentToken.next.tokenTxt.equals("main")) {
			currentToken = currentToken.next.next;
			pass = mainDec();
		} else {
			pass = Declaration();
			if (pass == false) {
				return pass;
			}

			if (!currentToken.tokenTxt.equals(tokList.first.tokenTxt)) {
				pass = DeclarationList();
			} else {
				pass = false;
			}
		}
		return pass;
	}// DeclarationList

	public boolean mainDec() {
		boolean pass = true;
		newFuncObj = new IdObj();

		newFuncObj.type = "void";
		newFuncObj.idTxt = "main";
		newFuncObj.parentFunc = currFunc;
		newFuncObj.funcScope = scopeLevel;
		newFuncObj.function = true;
		currFunc = newFuncObj.idTxt;

		if (!currentToken.tokenTxt.equals("(")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;

		if (!currentToken.tokenTxt.equals("void")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;

		if (!currentToken.tokenTxt.equals(")")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;

		pass = idSymTab.AddId(newFuncObj);
		if (pass == false){
			return pass;
		}

		pass = CompStmt();

		if (retNeed) {
			pass = false;
		}

		currFunc = "";

		return pass;
	}

	public boolean Declaration() {
		boolean pass = true;

		pass = PartialDec();
		if (pass == false) {
			return pass;
		}

		if (currentToken.tokenTxt.equals(";") | currentToken.tokenTxt.equals("[") | currentToken.tokenTxt.equals("(")) {
			if (currentToken.tokenTxt.equals("[")) {
				if (newIdObj.type.equals("void")) {
					pass = false;
					return pass;
				}
				pass = VarArrDec();
			} else if (currentToken.tokenTxt.equals("(")) {
				currentToken = currentToken.next;
				pass = FunDec();
			} else {
				if (newIdObj.type.equals("void")) {
					pass = false;
					return pass;
				}
				currentToken = currentToken.next;
				pass = idSymTab.AddId(newIdObj);
				if (pass == false){
					return pass;
				}
			}
		} else {
			pass = false;
		}
		return pass;
	}// Declaration

	public boolean PartialDec() {
		boolean pass = true;

		if (!currentToken.type.equals("typeSpecifier")) {
			pass = false;
			return pass;
		}

		newIdObj = new IdObj();

		newIdObj.type = currentToken.tokenTxt;
		currentToken = currentToken.next;

		if (!currentToken.type.equals("id")) {
			pass = false;
			return pass;
		}
		newIdObj.idTxt = currentToken.tokenTxt;
		currentToken = currentToken.next;

		newIdObj.parentFunc = currFunc;
		newIdObj.funcScope = scopeLevel;

		return pass;
	}// PartialDec

	public boolean VarArrDec() {
		boolean pass = true;

		newIdObj.array = true;
		newIdObj.type += "[]";

		if (!currentToken.tokenTxt.equals("[")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;

		if (!currentToken.type.equals("num") || currentToken.floatNum) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;

		if (!currentToken.tokenTxt.equals("]")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;

		if (!currentToken.tokenTxt.equals(";")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;

		pass = idSymTab.AddId(newIdObj);
		if (pass == false){
			return pass;
		}

		return pass;
	}// VarDec

	public boolean FunDec() {
		boolean pass = true;
		newFuncObj = newIdObj;

		newFuncObj.function = true;
		currFunc = newFuncObj.idTxt;

		if (newFuncObj.type.equals("int") || newFuncObj.type.equals("float")) {
			retNeed = true;
		} else {
			retNeed = false;
		}

		pass = Params();
		if (pass == false) {
			return pass;
		}

		if (!currentToken.tokenTxt.equals(")")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;

		newFuncObj.paramType = currParamTypeList;
		currParamTypeList = "";

		pass = idSymTab.AddId(newFuncObj);
		if (pass == false){
			return pass;
		}

		pass = CompStmt();

		if (retNeed) {
			pass = false;
		}

		currFunc = "";

		return pass;
	}// FunDec

	public boolean Params() {
		boolean pass = true;
		currParamTypeList = "";

		if (currentToken.tokenTxt.equals("void")) {
			currentToken = currentToken.next;
			return pass;
		} else {
			pass = ParamList();
		}

		return pass;
	}// Params

	public boolean ParamList() {
		boolean pass = true;

		pass = Param();
		if (pass == false) {
			return pass;
		}

		currParamTypeList += newIdObj.type;

		if (currentToken.tokenTxt.equals(",")) {
			currentToken = currentToken.next;
			pass = ParamList();
		}

		return pass;
	}// ParamList

	public boolean Param() {
		boolean pass = true;

		if (!currentToken.type.equals("typeSpecifier")) {
			pass = false;
			return pass;
		}
		newIdObj = new IdObj();

		newIdObj.type = currentToken.tokenTxt;
		currentToken = currentToken.next;

		if (!currentToken.type.equals("id")) {
			pass = false;
			return pass;
		}
		newIdObj.idTxt = currentToken.tokenTxt;
		currentToken = currentToken.next;

		newIdObj.parentFunc = currFunc;

		// Needs to be modified with new array type []
		if (currentToken.tokenTxt.equals("[")) {
			newIdObj.array = true;
			newIdObj.type += "[]";

			currentToken = currentToken.next;

			if (!currentToken.tokenTxt.equals("]")) {
				pass = false;
				return pass;
			}
			currentToken = currentToken.next;

		}

		pass = idSymTab.AddId(newIdObj);
		if (pass == false){
			return pass;
		}

		return pass;
	}// Param

	public boolean CompStmt() {
		boolean pass = true;

		if (!currentToken.tokenTxt.equals("{")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;
		scopeLevel++;

		pass = LocalDec();
		if (pass == false) {
			return pass;
		}

		pass = StmtList();
		if (pass == false) {
			return pass;
		}

		if (!currentToken.tokenTxt.equals("}")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;
		scopeLevel--;

		return pass;
	}// CompStmt

	public boolean LocalDec() {
		boolean pass = true;

		if (currentToken.type.equals("typeSpecifier")) {
			pass = PartialDec();
			if (pass == false) {
				return pass;
			}

			if (!currentToken.tokenTxt.equals(";")) {
				pass = VarArrDec();
				if (pass == false) {
					return pass;
				}
			} else {
				pass = idSymTab.AddId(newIdObj);
				if (pass == false){
					return pass;
				}
				currentToken = currentToken.next;
			}
			pass = LocalDec();
		}

		return pass;
	}// LocalDec

	public boolean StmtList() {
		boolean pass = true;

		if (!currentToken.tokenTxt.equals("}")) {
			pass = Statement();

			if (pass == false) {
				return pass;
			}

			pass = StmtList();
		}
		return pass;
	}// StmtList

	public boolean Statement() {
		boolean pass = true;

		if (currentToken.type.equals("id")) {
			pass = ExprStmt();
		} else if (currentToken.tokenTxt.equals("while")) {
			pass = IterStmt();
		} else if (currentToken.tokenTxt.equals("{")) {
			pass = CompStmt();
		} else if (currentToken.tokenTxt.equals("if")) {
			pass = SelStmt();
		} else if (currentToken.tokenTxt.equals("return")) {
			pass = RetStmt();
		} else {
			pass = false;
		}

		return pass;
	}// Statement

	public boolean ExprStmt() {
		boolean pass = true;

		if (currentToken.tokenTxt.equals(";")) {
			currentToken = currentToken.next;
		} else {
			pass = Expression();
			if (pass == false) {
				return pass;
			}
			if (!currentToken.tokenTxt.equals(";")) {
				pass = false;
				return pass;
			}
			currentToken = currentToken.next;
		}

		return pass;
	}// ExprStmt

	public boolean SelStmt() {
		boolean pass = true;

		if (!currentToken.tokenTxt.equals("if")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;

		if (!currentToken.tokenTxt.equals("(")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;

		pass = Expression();
		if (pass == false) {
			return pass;
		}

		if (!currentToken.tokenTxt.equals(")")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;

		pass = Statement();
		if (pass == false) {
			return pass;
		}

		if (currentToken.tokenTxt.equals("else")) {
			currentToken = currentToken.next;
			pass = Statement();
		}

		return pass;
	}// SelStmt

	public boolean IterStmt() {
		boolean pass = true;

		if (!currentToken.tokenTxt.equals("while")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;

		if (!currentToken.tokenTxt.equals("(")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;

		pass = Expression();
		if (pass == false) {
			return pass;
		}

		if (!currentToken.tokenTxt.equals(")")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;

		pass = Statement();

		return pass;
	}// IterStmt

	public boolean RetStmt() {
		boolean pass = true;
		retNeed = false;
		IdObj srchObj;

		if (!currentToken.tokenTxt.equals("return")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;

		srchObj = idSymTab.Search(currFunc, "", -1);

		retType = srchObj.type;

		if (retType.equals("int") || retType.equals("float")) {
			if (currentToken.tokenTxt.equals(";")) {
				pass = false;
				return pass;
			}

			pass = Expression();
			if (pass == false) {
				return pass;
			}

			if (!currType.equals(retType)) {
				pass = false;
				return pass;
			}
			currType = "";

		}

		if (!currentToken.tokenTxt.equals(";")) {
			pass = false;
			return pass;
		}
		currentToken = currentToken.next;

		return pass;
	}// RetStmt

	public boolean Expression() {
		boolean pass = true;
		String assignType;

		if (currentToken.type.equals("id") && !currentToken.next.tokenTxt.equals("(")) {
			pass = Var();
			if (pass == false) {
				return pass;
			}

			if (currentToken.tokenTxt.equals("=")) {
				currentToken = currentToken.next;

				assignType = currType;

				pass = Expression();

				if (!assignType.equals(currType)) {
					pass = false;
					return pass;
				}

			} else {
				pass = SimpVarExpr();
			}

		} else {
			pass = SimpExpr();
		}

		return pass;
	}// Expression

	public boolean Var() {
		boolean pass = true;
		IdObj srchObj;
		String varType;

		if (!currentToken.type.equals("id")) {
			pass = false;
			return pass;
		}

		// Checks that the variable exists
		srchObj = idSymTab.Search(currentToken.tokenTxt, currFunc, scopeLevel);
		if (srchObj.idTxt.equals("0NotFound0")) {
			pass = false;
			return false;
		}
		currType = srchObj.type;
		varType = srchObj.type;
		currentToken = currentToken.next;

		if (currentToken.tokenTxt.equals("[")) {
			currentToken = currentToken.next;

			pass = Expression();
			if (pass == false) {
				return pass;
			}

			if (!currType.equals("int")) {
				pass = false;
				return pass;
			}

			if (!currentToken.tokenTxt.equals("]")) {
				pass = false;
				return pass;
			}
			currentToken = currentToken.next;

			varType = varType.substring(0, varType.length() - 2);
		}

		currType = varType;

		return pass;
	}// Var

	public boolean SimpExpr() {
		boolean pass = true;

		pass = AddExpr();
		if (pass == false) {
			return pass;
		}

		if (currentToken.type.equals("relop")) {
			currentToken = currentToken.next;

			pass = AddExpr();
			if (pass == false) {
				return pass;
			}
		}

		return pass;
	}// SimpExpr

	// Simple expression that starts with var
	public boolean SimpVarExpr() {
		boolean pass = true;
		String simpVarExprType;
		simpVarExprType = currType;

		if (currentToken.type.equals("mulop")) {
			currentToken = currentToken.next;
			pass = Term();
		}

		if (!currType.equals(simpVarExprType)) {
			pass = false;
			return pass;
		}

		if (currentToken.type.equals("addop")) {
			currentToken = currentToken.next;
			pass = AddExpr();
		}

		if (!currType.equals(simpVarExprType)) {
			pass = false;
			return pass;
		}

		if (currentToken.type.equals("relop")) {
			currentToken = currentToken.next;
			pass = AddExpr();
		}

		if (!currType.equals(simpVarExprType)) {
			pass = false;
			return pass;
		}

		return pass;
	}// SimpVarExpr

	public boolean AddExpr() {
		boolean pass = true;
		String addExprType;

		pass = Term();
		if (pass == false) {
			return pass;
		}
		addExprType = currType;

		if (currentToken.type.equals("addop")) {
			currentToken = currentToken.next;
			pass = AddExpr();
		}

		if (!addExprType.equals(currType)) {
			pass = false;
		}

		return pass;
	}// AddExpr

	public boolean Term() {
		boolean pass = true;
		String mulExprType;

		pass = Factor();
		if (pass == false) {
			return pass;
		}
		mulExprType = currType;

		if (currentToken.type.equals("mulop")) {
			currentToken = currentToken.next;
			pass = Term();
		}

		if (!mulExprType.equals(currType)) {
			pass = false;
		}

		return pass;
	}// Term

	public boolean Factor() {
		boolean pass = true;

		if (currentToken.tokenTxt.equals("(")) {
			currentToken = currentToken.next;
			pass = Expression();

			if (pass == false) {
				return pass;
			}

			if (!currentToken.tokenTxt.equals(")")) {
				pass = false;
				return pass;
			}
			currentToken = currentToken.next;

		} else if (currentToken.type.equals("id")) {

			if (currentToken.next.tokenTxt.equals("(")) {
				pass = Call();
				currType = callType;
			} else {
				pass = Var();
			}
		} else if (currentToken.type.equals("num")) {
			if (currentToken.floatNum) {
				currType = "float";
			} else {
				currType = "int";
			}
			currentToken = currentToken.next;
		} else {
			pass = false;
		}

		return pass;
	}// Factor

	public boolean Call() {
		boolean pass = true;
		IdObj srchObj;
		currArgTypeList = "";

		if (!currentToken.type.equals("id")) {
			pass = false;
			return false;
		}

		// Tests that the function being called exists
		srchObj = idSymTab.Search(currentToken.tokenTxt, currFunc, scopeLevel);
		if (srchObj.idTxt.equals("0NotFound0")) {
			pass = false;
			return false;
		}
		callType = srchObj.type;
		currentToken = currentToken.next;

		if (!currentToken.tokenTxt.equals("(")) {
			pass = false;
			return false;
		}
		currentToken = currentToken.next;

		pass = Args();
		if (pass == false) {
			return pass;
		}

		if (!currentToken.tokenTxt.equals(")")) {
			pass = false;
			return false;
		}
		currentToken = currentToken.next;

		if (!currArgTypeList.equals(srchObj.paramType)) {
			pass = false;
			return false;
		}
		currArgTypeList = "";

		return pass;
	}// Call

	public boolean Args() {
		boolean pass = true;

		if (!currentToken.tokenTxt.equals(")")) {
			pass = ArgList();
		}

		return pass;
	}

	public boolean ArgList() {
		boolean pass = true;

		actvArg = true;
		pass = Expression();
		if (pass == false) {
			return pass;
		}

		currArgTypeList += currType;

		if (currentToken.tokenTxt.equals(",")) {
			currentToken = currentToken.next;
			pass = ArgList();
		}

		return pass;
	}
}// Part2
