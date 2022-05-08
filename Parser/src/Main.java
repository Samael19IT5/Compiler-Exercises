import java.io.File;

public class Main {
	public static void main(String[] args) {
		String dir = System.getProperty("user.dir");
		try {
			Parser parser = new Parser();
			parser.readInput(new File(dir + "\\src\\input.txt"));
			parser.readParseTable(new File(dir + "\\src\\parseTableCformatted.txt"));
			parser.readGrammar(new File(dir + "\\src\\grammarC++.txt"));
			parser.parse();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
