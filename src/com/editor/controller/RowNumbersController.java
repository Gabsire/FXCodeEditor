package com.editor.controller;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

public class RowNumbersController {

	public void initialize(TextArea rowNumbers, TextArea codeText) {

		int numberOfLines = codeText.getText().split("\n").length;

		for (int i = 0; i < numberOfLines + 2; i++) {
			rowNumbers.appendText(i + 1 + "\n");
		}

	}

}
