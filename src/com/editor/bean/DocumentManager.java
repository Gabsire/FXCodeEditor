package com.editor.bean;

import java.util.ArrayList;
import java.util.List;

public class DocumentManager {

	private static DocumentManager documentManager;
	private List<Document> documents = new ArrayList<>();
	private int currentOpenIndex;

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

	@Override
	public String toString() {
		return "DocumentManager [documents=" + documents + ", currentOpenIndex=" + currentOpenIndex + "]";
	}

}
