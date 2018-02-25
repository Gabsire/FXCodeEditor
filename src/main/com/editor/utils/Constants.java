package main.com.editor.utils;

import java.util.regex.Pattern;

public class Constants {

	public static final String NEWLINE = "\n";
	public static final String PATH_SEPARATOR = "\\";

	public static final String PERSISTENCE_FILE_PATH = "data\\persistence.dat";

	public static final String[] KEYWORDS = new String[] { "abstract", "assert", "boolean", "break", "byte", "case",
			"catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends",
			"final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface",
			"long", "native", "new", "package", "private", "protected", "public", "return", "short", "static",
			"strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
			"volatile", "while" };

	public static final String CODE_AREA_DEFAULT_STYLE = "-fx-font-family: consolas; -fx-font-size: 14pt; -fx-padding: 10, 0, 0, 0;";
	public static final String STAGE_DEFAULT_TITLE = "FXCodeEditor";

	public static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
	public static final String PAREN_PATTERN = "\\(|\\)";
	public static final String BRACE_PATTERN = "\\{|\\}";
	public static final String BRACKET_PATTERN = "\\[|\\]";
	public static final String SEMICOLON_PATTERN = "\\;";
	public static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
	public static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

	public static final Pattern PATTERN = Pattern.compile(
			"(?<KEYWORD>" + KEYWORD_PATTERN + ")" + "|(?<PAREN>" + PAREN_PATTERN + ")" + "|(?<BRACE>" + BRACE_PATTERN
					+ ")" + "|(?<BRACKET>" + BRACKET_PATTERN + ")" + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
					+ "|(?<STRING>" + STRING_PATTERN + ")" + "|(?<COMMENT>" + COMMENT_PATTERN + ")");
}
