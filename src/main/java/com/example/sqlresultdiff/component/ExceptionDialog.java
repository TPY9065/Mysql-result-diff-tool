package com.example.sqlresultdiff.component;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

import java.io.PrintWriter;
import java.io.StringWriter;


public class ExceptionDialog extends Alert
{
	private final TextArea textArea;

	public ExceptionDialog()
	{
		super(AlertType.ERROR);

		setTitle("Exception Dialog");
		setHeaderText(null);
		setContentText(null);

		Label label = new Label("Stack trace:");
		label.setFont(Font.font("Helvetica", 16));

		textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setFont(Font.font("Helvetica", 16));

		GridPane gridPane = new GridPane();
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		gridPane.add(label, 0, 0);
		gridPane.add(textArea, 0, 1);

		getDialogPane().setExpandableContent(gridPane);
		getDialogPane().setExpanded(true);

		hide();
	}

	public void setExceptionText(Exception e)
	{
		setHeaderText(e.getMessage());
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		textArea.setText(sw.toString());
	}
}
