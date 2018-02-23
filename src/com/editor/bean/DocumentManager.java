package com.editor.bean;

import java.util.ArrayList;
import java.util.List;

public class DocumentManager {

	private List<String> documents;
	private DocumentManager documentManager;
	
	private DocumentManager(){
		documents = new ArrayList<>();
	}
	
	public DocumentManager getInstance(){
		
		if(null == documentManager){
			documentManager = new DocumentManager();
		}
		return documentManager;
	}

	public List<String> getDocuments() {
		return documents;
	}

	public void setDocuments(List<String> documents) {
		this.documents = documents;
	}

}
