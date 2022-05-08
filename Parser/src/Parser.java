import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Parser {

	HashMap<String, HashMap<String, String>> parseTable = new HashMap<String, HashMap<String, String>>();
	HashMap<String, String[]> grammarMap = new HashMap<String, String[]>();
	String startSymbol = "Program";
	List<Token> inputTokens;
	File file;

	void readInput(File f) {
		file = f;
		Lexeme lexeme = new Lexeme(file);
		inputTokens = lexeme.generateTokens();
	}

	void readParseTable(File f) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		BufferedReader buf = null;
		try {
			fis = new FileInputStream(f);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			buf = new BufferedReader(new InputStreamReader(dis));
			int line = 0;
			String[] tokens = {};
			String s = buf.readLine();
			while (s != null) {
				line++;
				String[] str = s.split("\t");
				if (line == 1) {
					tokens = new String[str.length - 1];
					for (int i = 1; i < str.length; i++) {
						tokens[i - 1] = str[i];
					}
				} else {
					String nonTerminal = str[0].replaceAll("\\u00a0", "");
					HashMap<String, String> production = new HashMap<String, String>();
					for (int i = 1; i < str.length; i++) {
						production.put(tokens[i - 1], str[i]);
					}
					for (int i = str.length - 1; i < tokens.length; i++) {
						production.put(tokens[i], "");
					}
					parseTable.put(nonTerminal, production);
				}
				s = buf.readLine();
			}
			fis.close();
			bis.close();
			dis.close();
			buf.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void readGrammar(File f) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		BufferedReader buf = null;
		try {
			fis = new FileInputStream(f);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			buf = new BufferedReader(new InputStreamReader(dis));
			String s = buf.readLine();
			while (s != null) {
				if (s.length() > 0) {
					String[] str = s.split("\\.");
					String num = str[0];
					String[] grammar = str[1].split("-->");
					String[] rhs = grammar[1].split("\\|");
					String[] production = rhs[0].split(" ");
					for (int j = 0; j < production.length; j++) {
						production[j] = production[j].replaceAll("\\u00a0", "");
					}
					grammarMap.put(num, production);
				}
				s = buf.readLine();
			}
			fis.close();
			bis.close();
			dis.close();
			buf.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	boolean isTerminal(String sym) {
		if (Character.isUpperCase(sym.charAt(0)))
			return false;
		else
			return true;
	}

	void parse() {
		int errorLine = 0;
		int inputPointer = 0;
		Stack<String> stack = new Stack<>();
		stack.push("$");
		stack.push(startSymbol);
		while (!stack.isEmpty() && inputPointer < inputTokens.size()) {
			String top = stack.pop();
			if (isTerminal(top)) {
				if (top.contains(inputTokens.get(inputPointer).getToken())) {
					inputPointer++;
				} else if (top.contains(",") && inputTokens.get(inputPointer).getToken().contains("\",\"")) {
					inputPointer++;
				} else {
					if (inputTokens.get(inputPointer).getLine() - inputTokens.get(inputPointer - 1).getLine() == 1) {
						errorLine = inputTokens.get(inputPointer).getLine() - 1;
					} else {
						errorLine = inputTokens.get(inputPointer).getLine();
					}
					break;
				}
			} else {
				HashMap<String, String> production = parseTable.get(top);
				if (production == null) {
					if (inputTokens.get(inputPointer).getLine() - inputTokens.get(inputPointer - 1).getLine() == 1) {
						errorLine = inputTokens.get(inputPointer).getLine() - 1;
					} else {
						errorLine = inputTokens.get(inputPointer).getLine();
					}
					break;
				} else {
					String[] rhs = grammarMap.get(production.get(inputTokens.get(inputPointer).getToken()));
					if (rhs != null) {
						for (int i = rhs.length - 1; i > -1; i--) {
							if (!rhs[i].equals("")) {
								stack.push(rhs[i]);
							}
						}
					}
				}
			}
		}
		if (!stack.isEmpty() && inputPointer < inputTokens.size()) {
			System.out.println("Syntax Error: Line # " + errorLine);
		} else {
			System.out.println("The program is parsed successfully");
		}

	}

}
