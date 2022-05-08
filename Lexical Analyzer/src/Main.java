import java.io.File;
import java.util.List;

public class Main {
	public static void main(String[] args) {

		String dir = System.getProperty("user.dir");
		File file = null;

		try {
			file = new File(dir + "\\src\\sample.txt");
			Lexeme lexeme = new Lexeme(file);
			List<Token> tokenList = lexeme.generateTokens();
			System.out.println("Class : Lexeme");
			for (int i = 0; i < tokenList.size(); i++) {
				System.out.println(tokenList.get(i).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}