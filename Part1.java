import java.io.*;

public class Part1 {
	String builtWord = "";
	String builtNum = "";
	String builtErr = "";
	boolean actvWord = false;
	boolean actvNum = false;
	boolean actvFlt = false;
	boolean actvErr = false;
	boolean actvSciNot = false;
	TokenList tList = new TokenList();
	String test;

	public void Driver(String[] argArr) {
		try {
			File input = new File(argArr[0]);
			BufferedReader buffReader = new BufferedReader(new FileReader(input));
			String currLine;
			char currChar;
			char currCharType;
			Checker chex = new Checker();
			int actvComNum = 0;

			// Reads file line by line
			while ((currLine = buffReader.readLine()) != null) {
				currLine = currLine.trim();

				if (currLine.length() > 0) {
					// System.out.println("Input: " + currLine);
				}

				// Reads the line char by char
				for (int i = 0; i < currLine.length(); i++) {
					currChar = currLine.charAt(i);
					currCharType = chex.charType(currChar);
					if (actvComNum == 0) {
						switch (currCharType) {

						case 'L':
							if (actvNum) {
								if (currChar == 'E' && (Character.isDigit(currLine.charAt(i + 1))
										|| currLine.charAt(i + 1) == '-' || currLine.charAt(i + 1) == '+')) {
									builtNum += currChar;
									builtNum += currLine.charAt(i + 1);
									i++;
									actvFlt = true;
									actvSciNot = true;
								} else {
									actvErr = true;
									builtErr += builtNum;
									EndBuiltNum();
								}
							}

							if (!actvSciNot) {
								if (!actvErr) {
									if (actvWord == false) {
										actvWord = true;
									}
									builtWord += currChar;
								} else {
									builtErr += currChar;
								}
							}
							break;

						case 'D':
							if (actvWord) {
								actvErr = true;
								builtErr += builtWord;
								EndBuiltWord();
							}

							if (!actvErr) {
								if (actvNum == false) {
									actvNum = true;
								}
								builtNum += currChar;

								// Checks if there is a '.' and digit to
								// determine if it is a float
								if (!actvSciNot) {
									if (i < (currLine.length() - 2)) {
										if (currLine.charAt(i + 1) == '.') {
											if (Character.isDigit(currLine.charAt(i + 2))) {
												actvFlt = true;
												i++;
												currChar = currLine.charAt(i);
												builtNum += currChar;
												i++;
												currChar = currLine.charAt(i);
												builtNum += currChar;
											} else {
												builtErr += currChar;
											}
										}
									}
								}
							} else {
								builtErr += currChar;
							}

							break;

						case 'S':
							if (actvWord) {
								EndBuiltWord();
							}

							if (actvNum) {
								EndBuiltNum();
							}

							if (actvErr) {
								EndBuiltErr();
							}

							// Checks if there is enough of the
							// line remaining to have a two char symbol
							if (i < (currLine.length() - 1)) {
								// Checks if the symbol is a two char symbol
								if (currChar == '=' | currChar == '<' | currChar == '>') {
									if (currLine.charAt(i + 1) == '=') {
										// System.out.println(Character.toString(currChar)
										// + currLine.charAt(i + 1));
										tList.InsertLast("relop", currChar + "=");
										i++;
									} else {
										// System.out.println(currChar);
										if (currChar == '<' | currChar == '>') {
											tList.InsertLast("relop", Character.toString(currChar));
										} else {
											tList.InsertLast("symbol", Character.toString(currChar));
										}
									}

									// Checks if multi line comment symbol
								} else if (currChar == '/') {
									if (currLine.charAt(i + 1) == '*') {
										actvComNum++;
										i++;

										// Checks if single line comment symbol
									} else if (currLine.charAt(i + 1) == '/') {
										i = currLine.length();
									} else {
										// System.out.println(currChar);
										tList.InsertLast("mulop", Character.toString(currChar));
									}
								} else {
									// System.out.println(currChar);

									if (currChar == '*') {
										tList.InsertLast("mulop", Character.toString(currChar));
									} else if (currChar == '+' | currChar == '-') {
										tList.InsertLast("addop", Character.toString(currChar));
									} else {
										tList.InsertLast("symbol", Character.toString(currChar));
									}
								}
							} else {
								// System.out.println(currChar);

								if (currChar == '*' | currChar == '/') {
									tList.InsertLast("mulop", Character.toString(currChar));
								} else if (currChar == '+' | currChar == '-') {
									tList.InsertLast("addop", Character.toString(currChar));
								} else {
									tList.InsertLast("symbol", Character.toString(currChar));
								}
							}

							break;

						case 'I':
							if (actvWord) {
								EndBuiltWord();
							}

							if (actvNum) {
								EndBuiltNum();
							}

							if (i < (currLine.length() - 1)) {
								if (currChar == '!') {
									if (currLine.charAt(i + 1) == '=') {
										// System.out.println(Character.toString(currChar)
										// + currLine.charAt(i + 1));
										tList.InsertLast("relop", "!=");
										i++;
									} else {
										actvErr = true;
										builtErr += currChar;
									}
								} else {
									actvErr = true;
									builtErr += currChar;
								}
							} else {
								actvErr = true;
								builtErr += currChar;
							}
							break;

						case 'B':
							if (actvWord) {
								EndBuiltWord();
							}

							if (actvNum) {
								EndBuiltNum();
							}

							if (actvErr) {
								EndBuiltErr();
							}
							break;

						default:
							// System.out.println("Unknown Character: " +
							// currChar);
							break;
						}
					}
					// If there is an active comment
					else {
						if (currCharType == 'S') {
							if (i < (currLine.length() - 1)) {
								if (currChar == '*') {
									if (currLine.charAt(i + 1) == '/') {
										if (actvComNum >= 1) {
											actvComNum--;
											i++;
										}
									}
								} else if (currChar == '/') {
									if (currLine.charAt(i + 1) == '*') {
										actvComNum++;
										i++;
									}
								}
							}
						}
					}
				} // for

				if (actvWord) {
					EndBuiltWord();
				}

				if (actvNum) {
					EndBuiltNum();
				}

				if (actvErr) {
					EndBuiltErr();
				}

				if (currLine.length() > 0) {
					// System.out.println();
				}

			} // while
			buffReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	} // Driver

	public void EndBuiltWord() {
		if (builtWord.length() > 0) {
			switch (builtWord) {

			case "else":
				// System.out.println("Keyword: " + builtWord);
				tList.InsertLast("keyword", builtWord);
				builtWord = "";
				break;

			case "if":
				// System.out.println("Keyword: " + builtWord);
				tList.InsertLast("keyword", builtWord);
				builtWord = "";
				break;

			case "int":
				// System.out.println("Keyword: " + builtWord);
				tList.InsertLast("typeSpecifier", builtWord);
				builtWord = "";
				break;

			case "float":
				// System.out.println("Keyword: " + builtWord);
				tList.InsertLast("typeSpecifier", builtWord);
				builtWord = "";
				break;

			case "return":
				// System.out.println("Keyword: " + builtWord);
				tList.InsertLast("keyword", builtWord);
				builtWord = "";
				break;

			case "void":
				// System.out.println("Keyword: " + builtWord);
				tList.InsertLast("typeSpecifier", builtWord);
				builtWord = "";
				break;

			case "while":
				// System.out.println("Keyword: " + builtWord);
				tList.InsertLast("keyword", builtWord);
				builtWord = "";
				break;

			default:
				if (!actvErr) {
					// System.out.println("ID: " + builtWord);
					tList.InsertLast("id", builtWord);
				}
				builtWord = "";
				break;
			}
			actvWord = false;
		}
	} // endBuiltWord

	public void EndBuiltNum() {
		if (builtNum.length() > 0) {
			if (actvFlt == true) {
				actvFlt = false;
				if (!actvSciNot) {
					if (!actvErr) {
						// System.out.println("Float: " +
						// Float.parseFloat(builtNum));
						tList.InsertLast("num", builtNum);
						tList.last.floatNum = true;
					}
					builtNum = "";
					actvFlt = false;
				} else {
					if (!actvErr) {
						// System.out.println("Float: " + builtNum);
						tList.InsertLast("num", builtNum);
						tList.last.floatNum = true;
					}
					builtNum = "";
					actvSciNot = false;
				}
			} else {
				if (!actvErr) {
					// System.out.println("Int: " + Integer.parseInt(builtNum));
					tList.InsertLast("num", builtNum);
				}
				builtNum = "";
			}
			actvNum = false;
		}
	}// endBuiltInt

	public void EndBuiltErr() {
		if (builtErr.length() > 0) {
			// System.out.println("Error: " + builtErr);
			builtErr = "";
			actvErr = false;
		}
	}// endBuiltErr
} // Part1
