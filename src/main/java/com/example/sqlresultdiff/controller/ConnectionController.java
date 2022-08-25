package com.example.sqlresultdiff.controller;

import com.example.sqlresultdiff.SqlResultDiffApplication;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class ConnectionController
{
	private SqlResultDiffApplication application;

	@FXML
	private TextField urlField;

	@FXML
	private TextField portField;

	@FXML
	private TextField usernameField;

	@FXML
	private TextField passwordField;

	@FXML
	private TextField schemaField;

	@FXML
	protected void onConnectButtonClick()
	{
		String url = urlField.getText();
		String port = portField.getText();
		String username = usernameField.getText();
		String password = passwordField.getText();
		String schema = schemaField.getText();

		application.connect(url, port, schema, username, password);
	}

	public void init(SqlResultDiffApplication application)
	{
		this.application = application;
	}
}