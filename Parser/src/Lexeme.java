import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexeme {

	BufferedReader reader;
	char current;
	int line = 1;
	List<Token> tokenList = new ArrayList<>();
	public static final String KEY_WORDS[] = new String[] { "while", "if", "else", "for", "return", "break", "continue",
			"int", "float", "void" };

	public Lexeme(File file) {

		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		current = readNextChar();
	}

	List<Token> generateTokens() {
		Token token = readNextToken();
		while (token != null) {
			tokenList.add(token);
			token = readNextToken();
		}
		return tokenList;
	}

	Token readNextToken() {
		int state = 1;

		while (true) {
			if (current == (char) (-1)) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			switch (state) {
			case 1: {
				if (current == ' ' || current == '\t' || current == '\f' || current == '\b' || current == '\r') {
					current = readNextChar();
					continue;
				} else if (current == '\n') {
					line++;
					current = readNextChar();
					continue;
				} else if (current == ';') {
					current = readNextChar();
					return new Token(";", ";", line);
				} else if (current == '+') {
					current = readNextChar();
					return new Token("addOp", "+", line);
				} else if (current == '-') {
					current = readNextChar();
					return new Token("addOp", "-", line);
				} else if (current == '*') {
					current = readNextChar();
					return new Token("mulOp", "*", line);
				} else if (current == '/') {
					current = readNextChar();
					return new Token("mulOp", "/", line);
				} else if (current == '%') {
					current = readNextChar();
					return new Token("%", "%", line);
				} else if (current == '{') {
					current = readNextChar();
					return new Token("{", "{", line);
				} else if (current == '}') {
					current = readNextChar();
					return new Token("}", "}", line);
				} else if (current == '(') {
					current = readNextChar();
					return new Token("(", "(", line);
				} else if (current == ')') {
					current = readNextChar();
					return new Token(")", ")", line);
				} else if (current == ',') {
					current = readNextChar();
					return new Token("\",\"", ",", line);
				} else if (current == '=') {
					current = readNextChar();
					if (current == '=') {
						current = readNextChar();
						return new Token("relOp", "==", line);
					} else {
						return new Token("=", "=", line);
					}
				} else if (current == '<') {
					current = readNextChar();
					if (current == '=') {
						current = readNextChar();
						return new Token("relOp", "<=", line);
					} else {
						return new Token("relOp", "<", line);
					}
				} else if (current == '>') {
					current = readNextChar();
					if (current == '=') {
						current = readNextChar();
						return new Token("relOp", ">=", line);
					} else {
						return new Token("relOp", ">", line);
					}
				} else if (current == '!') {
					current = readNextChar();
					if (current == '=') {
						current = readNextChar();
						return new Token("not", "!=", line);
					} else
						return new Token("!", "!", line);
				} else if (current == '&') {
					current = readNextChar();
					if (current == '&') {
						current = readNextChar();
						return new Token("and", "&&", line);
					} else
						return new Token("&", "&", line);
				} else if (current == '|') {
					current = readNextChar();
					if (current == '|') {
						current = readNextChar();
						return new Token("or", "||", line);
					} else
						return new Token("|", "|", line);
				} else {
					state = 2;
					continue;
				}

			}
			case 2: {
				if (isNumber(current)) {
					String num = String.valueOf(current);
					for (;;) {
						current = readNextChar();
						if (isNumber(current) || current == '.') {
							num += String.valueOf(current);
						} else if (isE(current)) {
							num += String.valueOf(current);
							current = readNextChar();
							if (isNumber(current)) {
								num += String.valueOf(current);
							} else if (isNegativeorPositive(current)) {
								num += String.valueOf(current);
								current = readNextChar();
								if (isNumber(current)) {
									num += String.valueOf(current);
								} else {
									return new Token("Error", num, line);
								}
							} else {
								return new Token("Error", num, line);
							}
						} else {
							return new Token("num", num, line);
						}
					}
				} else
					state = 3;
			}
			case 3: {
				if (isLetter(current) || current == '_') {
					String word = String.valueOf(current);
					for (;;) {
						current = readNextChar();
						if (isLetter(current) || current == '_' || isNumber(current)) {
							word += String.valueOf(current);
						} else {
							List<String> key_words = Arrays.asList(KEY_WORDS);

							if (key_words.contains(word))
								return new Token(word, word, line);

							else
								return new Token("identifier", word, line);
						}
					}
				} else {
					String tmp = "" + current;
					current = readNextChar();
					return new Token("Error", tmp, line);
				}
			}
			}
		}
	}

	char readNextChar() {
		try {
			return (char) reader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (char) (-1);
	}

	boolean isNumber(char c) {
		if (c >= '0' && c <= '9')
			return true;

		return false;
	}

	boolean isLetter(char c) {
		if (c >= 'a' && c <= 'z')
			return true;
		if (c >= 'A' && c <= 'Z')
			return true;

		return false;

	}

	boolean isE(char c) {
		if (c == 'E')
			return true;

		return false;

	}

	boolean isNegativeorPositive(char c) {
		if (c == '-' || c == '+')
			return true;

		return false;

	}

}