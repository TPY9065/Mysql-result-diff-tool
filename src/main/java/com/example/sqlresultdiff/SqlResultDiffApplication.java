package com.example.sqlresultdiff;

import com.example.sqlresultdiff.component.ExceptionDialog;
import com.example.sqlresultdiff.controller.ConnectionController;
import com.example.sqlresultdiff.controller.EditorController;
import com.example.sqlresultdiff.model.MysqlConnection;
import com.example.sqlresultdiff.model.Table;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class SqlResultDiffApplication extends Application
{
	protected MysqlConnection connection;
	private FXMLLoader connectionLoader;
	private FXMLLoader editorLoader;
	private Scene connectionScene;
	private Scene editorScene;
	private Stage window;
	private ExceptionDialog exceptionDialog;

	@Override
	public void start(Stage stage) throws IOException
	{
		connection = new MysqlConnection();

		window = stage;
		window.setOnCloseRequest(windowEvent -> connection.close());

		preloadConnectionScene();
		preloadEditorScene();
		createAlertScene();
		injectControllerDependency();
		showConnectionScene();
	}

	private void preloadConnectionScene() throws IOException
	{
		connectionLoader = new FXMLLoader(SqlResultDiffApplication.class.getResource("connect-view.fxml"));
		connectionScene = new Scene(connectionLoader.load(), 800, 500);
	}

	private void preloadEditorScene() throws IOException
	{
		editorLoader = new FXMLLoader(SqlResultDiffApplication.class.getResource("editor-view.fxml"));
		editorScene = new Scene(editorLoader.load(), 1200, 900);
	}

	private void createAlertScene()
	{
		exceptionDialog = new ExceptionDialog();
	}

	private void injectControllerDependency()
	{
		ConnectionController connectionController = connectionLoader.getController();
		connectionController.init(this);

		EditorController editorController = editorLoader.getController();
		editorController.init(this);
	}

	protected void showConnectionScene()
	{
		window.setScene(connectionScene);
		window.setTitle("Connection");
		window.centerOnScreen();
		window.show();
	}

	protected void showEditorScene()
	{
		window.setScene(editorScene);
		window.setTitle("Editor");
		window.centerOnScreen();
		window.show();
	}

	public void connect(String url, String port, String schema, String username, String password)
	{
		try
		{
			connection.connect(url, port, schema, username, password);
			showEditorScene();
		}
		catch (SQLException e)
		{
			exceptionDialog.setContentText("Connection failed. Please try again.");
			exceptionDialog.setExceptionText(e);
			exceptionDialog.showAndWait();
		}
	}

	public Table execute(String sql) throws SQLException
	{
		return connection.execute(sql);
	}

	public static void main(String[] args)
	{
		launch();
	}
}