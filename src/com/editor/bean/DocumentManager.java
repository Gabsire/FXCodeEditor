package com.editor.bean;

import java.util.ArrayList;
import java.util.List;

public class DocumentManager {

	private List<Document> documents;
	private static DocumentManager documentManager;
	private int currentOpenIndex;

	private DocumentManager() {
		documents = new ArrayList<>();
		currentOpenIndex = 0;
	}

	public static DocumentManager getInstance() {

		if (null == documentManager) {
			documentManager = new DocumentManager();
		}
		return documentManager;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public static DocumentManager getDocumentManager() {
		return documentManager;
	}

	public static void setDocumentManager(DocumentManager documentManager) {
		DocumentManager.documentManager = documentManager;
	}

	public int getCurrentOpenIndex() {
		return currentOpenIndex;
	}

	public void setCurrentOpenIndex(int currentOpenIndex) {
		this.currentOpenIndex = currentOpenIndex;
	}

}
