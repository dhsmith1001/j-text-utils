package dnl.utils.text.table;

public class TextTableExample {

	public static void main(String[] args) {
		String[] columnNames = {
				"First Name",
                "Last Name",
                "Sport",
                "# of Years",
                "Vegetarian"};


		Object[][] data = {
			    {"Kathy", "Smith",
			     "Snowboarding", 5, false},
			    {"John", "Doe", "Rowing", 3, true},
			    {"Sue", "Black", "Knitting", 2, false},
			    {"Jane", "White", "Speed reading", 20, true},
			    {"Joe", "Brown", "Pool", 10, false}
			};

		TextTable tt = new TextTable(columnNames, data);
		tt.setAddRowNumbering(true);
//		tt.addSeparatorPolicy(new LastRowSeparatorPolicy());
		tt.setSort(0);
		tt.printTable();
	}

}
