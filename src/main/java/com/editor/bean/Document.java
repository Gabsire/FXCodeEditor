package main.java.com.editor.bean;

public class Document {

	private String fileName;
	private String filePath;
	private String content;

	public Document(String fileName, String filePath, String content) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.content = content;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Document [fileName=" + fileName + ", filePath=" + filePath + ", content=" + content + "]";
	}

}
