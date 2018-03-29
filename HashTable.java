
public class HashTable {
	IdObj[] hashArr;
	int arrSize;
	int numStored;

	HashTable() {
		arrSize = 101;
		hashArr = new IdObj[arrSize];
		numStored = 0;
	}// Constructor

	public boolean AddId(IdObj idObj) {
		int index;
		IdObj currObj;
		boolean added = false;

		if (!ObjExists(idObj)) {
			index = HashGen(idObj.idTxt);

			// Separate chaining collision resolution
			if (hashArr[index] == null) {
				hashArr[index] = idObj;
				added = true;
			} else {
				currObj = hashArr[index];

				while (currObj.next != null) {
					currObj = currObj.next;
				}

				idObj.prev = currObj;
				currObj.next = idObj;
				added = true;
			}
		}

		numStored++;
		return added;
	}// AddId

	// Searches for the target ID in the symbol table
	public IdObj Search(String target, String function, int scope) {
		int index = HashGen(target);
		IdObj currObj = new IdObj();
		boolean found = false;

		if (hashArr[index] != null) {
			currObj = hashArr[index];

			// Traverse to end of the linked list and search through it in
			// reverse so that global variables will be encountered last
			while (currObj.next != null){
				currObj = currObj.next;
			}

			if (currObj.idTxt.equals(target) && ((currObj.parentFunc.equals(function) && currObj.funcScope == scope)
					|| (currObj.parentFunc.equals("") && currObj.funcScope == -1))) {
				found = true;
			} else {
				for (int i = scope; i >= -1 && !found; i--) {
					if (currObj.idTxt.equals(target) && currObj.parentFunc.equals(function)
							&& currObj.funcScope == i) {
						found = true;
					}
				}
				
				while (currObj.prev != null && !found) {
					currObj = currObj.prev;
					if (currObj.idTxt.equals(target)
							&& ((currObj.parentFunc.equals(function) && currObj.funcScope == scope)
									|| (currObj.parentFunc.equals("") && currObj.funcScope == -1))) {
						found = true;
					} else {
						for (int i = scope; i >= -1 && !found; i--) {
							if (currObj.idTxt.equals(target) && currObj.parentFunc.equals(function)
									&& currObj.funcScope == i) {
								found = true;
							}
						}
					}
				}// while

			}
		}
		if (!found) {
			currObj = new IdObj();
			currObj.idTxt = "0NotFound0";
		}

		return currObj;
	}// Search

	// Checks if the target already exists in the symbol table
	public boolean ObjExists(IdObj target) {
		boolean found = false;
		int index = HashGen(target.idTxt);
		IdObj currObj = new IdObj();

		if (hashArr[index] != null) {
			currObj = hashArr[index];

			if (currObj.Equals(target)) {
				found = true;
			} else {
				while (currObj.next != null && !found) {
					currObj = currObj.next;
					if (currObj.Equals(target)) {
						found = true;
					}
				}
			}
		}

		return found;
	}// ObjExists

	public int HashGen(String key) {
		int hashVal = 0;
		int letter;

		for (int i = 0; i < key.length(); i++) {
			letter = key.charAt(i);
			hashVal = (hashVal * 26 + letter) % arrSize;
		}

		return hashVal;
	}// HashGen

	public void Print() {
		IdObj currObj;

		for (int i = 0; i < hashArr.length; i++) {
			if (hashArr[i] != null) {
				currObj = hashArr[i];
				while (currObj.next != null) {
					currObj.Print();
					currObj = currObj.next;
				}
				currObj.Print();
			}
		}
	}// Print
}// Hasher
