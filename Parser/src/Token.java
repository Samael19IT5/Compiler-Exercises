public class Token {
	private String token;
	private String lexeme;
	private int line;

	public Token(String token, String lexeme, int line) {
		this.token = token;
		this.lexeme = lexeme;
		this.line = line;
	}

	public String getToken() {
		return token;
	}

	public String getLexeme() {
		return lexeme;
	}

	public int getLine() {
		return line;
	}
}