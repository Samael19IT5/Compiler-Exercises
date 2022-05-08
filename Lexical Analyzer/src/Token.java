public class Token {
	String token;
	String lexeme;

	public Token(String token, String lexeme) {
		this.token = token;
		this.lexeme = lexeme;
	}

	public String toString() {
		return formateOutPut(lexeme, token);
	}

	String formateOutPut(String l, String t) {
		return t + " : " + l;
	}
}