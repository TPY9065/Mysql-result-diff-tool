package com.example.sqlresultdiff.component;

import javafx.scene.control.Alert;


public class WarningDialog extends Alert
{
	public WarningDialog()
	{
		super(AlertType.WARNING);
		setTitle("Warning Dialog");
		setHeaderText("Warning");
	}
}
