package com.example.sqlresultdiff.controller;

import com.example.sqlresultdiff.SqlResultDiffApplication;
import com.example.sqlresultdiff.utils.Utils;
import com.example.sqlresultdiff.component.AnimationButton;
import com.example.sqlresultdiff.component.CustomTableCell;
import com.example.sqlresultdiff.component.ExceptionDialog;
import com.example.sqlresultdiff.component.WarningDialog;
import com.example.sqlresultdiff.model.Table;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.kordamp.ikonli.bootstrapicons.BootstrapIcons.ARROW_LEFT_SQUARE_FILL;
import static org.kordamp.ikonli.bootstrapicons.BootstrapIcons.ARROW_RIGHT_SQUARE_FILL;

public class EditorController
{
	@FXML
	private ToolBar toolBar;

	@FXML
	private TextArea leftTextArea;

	@FXML
	private TableView<Map<String, String>> leftResultTable;

	@FXML
	private TextArea rightTextArea;

	@FXML
	private TableView<Map<String, String>> rightResultTable;

	@FXML
	private Label differencesHintText;

	private SqlResultDiffApplication application;
	private ExceptionDialog exceptionDialog;
	private WarningDialog warningDialog;
	private Table leftTable;
	private Table rightTable;
	private List<Integer> differences = new ArrayList<>();

	public void init(SqlResultDiffApplication application)
	{
		this.application = application;

		exceptionDialog = new ExceptionDialog();
		warningDialog = new WarningDialog();

		AnimationButton executeLeftBtn = new AnimationButton(Color.STEELBLUE, Color.TRANSPARENT);
		executeLeftBtn.setIcon(ARROW_LEFT_SQUARE_FILL);
		executeLeftBtn.setOnMouseClicked(mouseEvent -> {
			String firstSql = Utils.getFirstQuery(leftTextArea.getText());
			leftTable = executeStatement(firstSql);
			constructTableViews(leftTable, null);
		});
		Tooltip executeLeftTooltip = new Tooltip("Execute left statement (ALT + ENTER when focusing on left editor)");
		executeLeftTooltip.setFont(Font.font("Helvetica", 16));
		Tooltip.install(executeLeftBtn, executeLeftTooltip);

		AnimationButton executeRightBtn = new AnimationButton(Color.STEELBLUE, Color.TRANSPARENT);
		executeRightBtn.setIcon(ARROW_RIGHT_SQUARE_FILL);
		executeRightBtn.setOnMouseClicked(mouseEvent -> {
			String firstSql = Utils.getFirstQuery(rightTextArea.getText());
			rightTable = executeStatement(firstSql);
			constructTableViews(null, rightTable);
		});
		Tooltip executeRightTooltip = new Tooltip("Execute right statement (ALT + ENTER when focusing on right editor)");
		executeRightTooltip.setFont(Font.font("Helvetica", 16));
		Tooltip.install(executeRightBtn, executeRightTooltip);

		AnimationButton executeBothBtn = new AnimationButton(Color.STEELBLUE, Color.TRANSPARENT);
		executeBothBtn.setOnMouseClicked(mouseEvent -> {
			String leftFirstSql = Utils.getFirstQuery(leftTextArea.getText());
			String rightFirstSql = Utils.getFirstQuery(rightTextArea.getText());
			leftTable = executeStatement(leftFirstSql);
			rightTable = executeStatement(rightFirstSql);
			constructTableViews(leftTable, rightTable);
		});
		Tooltip executeTooltip = new Tooltip("Execute both statement (ALT + SHIFT + ENTER)");
		executeTooltip.setFont(Font.font("Helvetica", 16));
		Tooltip.install(executeBothBtn, executeTooltip);

		toolBar.getItems().addAll(executeLeftBtn, executeRightBtn, executeBothBtn);

		leftResultTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		rightResultTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

		leftResultTable.selectionModelProperty().bindBidirectional(rightResultTable.selectionModelProperty());

		differencesHintText.setFont(Font.font("Helvetica", 20));
	}

	private Table executeStatement(String statement)
	{
		try
		{
			return Utils.emptyString(statement) ? null : application.execute(statement);
		}
		catch (SQLException e)
		{
			exceptionDialog.setExceptionText(e);
			exceptionDialog.showAndWait();
		}
		return null;
	}

	private void constructTableViews(Table table1, Table table2)
	{
		if (table1 != null && table2 != null)
		{
			differences = spotDifferences(table1, table2);
			differencesHintText.setText(differences.isEmpty() ?
					"Both tables are identical." :
					"There are " + differences.size() + " different rows between two tables.");
		}
		setTableResults(table1, leftResultTable);
		setTableResults(table2, rightResultTable);
	}

	private void setTableResults(Table table, TableView<Map<String, String>> resultGrid)
	{
		if (table != null && resultGrid != null)
		{
			List<TableColumn<Map<String, String>, String>> columns = new ArrayList<>();
			for (String header : table.headers())
			{
				TableColumn<Map<String, String>, String> column = new TableColumn<>(header);
				column.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(header)));
				column.setCellFactory(param -> new CustomTableCell(differences));
				columns.add(column);
			}
			resultGrid.getColumns().setAll(columns);
			resultGrid.getItems().setAll(table.rows());
		}
	}

	private List<Integer> spotDifferences(Table table1, Table table2)
	{
		if (table1.headers().size() != table2.headers().size())
		{
			warningDialog.setContentText("Both table have different number of columns!");
			warningDialog.showAndWait();
			return Collections.emptyList();
		}
		else if (!table1.headers().equals(table2.headers()))
		{
			warningDialog.setContentText("Both table have different columns");
			warningDialog.showAndWait();
			return Collections.emptyList();
		}

		Iterator<Map<String,String>> left = table1.rows().listIterator();
		Iterator<Map<String,String>> right = table2.rows().listIterator();

		int rowCount = 0;
		List<Integer> differentRows = new ArrayList<>();
		while (left.hasNext() && right.hasNext())
		{
			Map<String,String> leftRow = left.next();
			Map<String,String> rightRow = right.next();
			if (!leftRow.equals(rightRow))
			{
				differentRows.add(rowCount);
			}
			rowCount += 1;
		}

		return differentRows;
	}

	@FXML
	private void onKeyPressedOnLeftEditor(KeyEvent keyEvent)
	{
		if (keyEvent.isAltDown() && !keyEvent.isShiftDown() && KeyCode.ENTER.equals(keyEvent.getCode()))
		{
			leftTable = executeStatement(leftTextArea.getText());
			constructTableViews(leftTable, rightTable);
		}
		else if (keyEvent.isAltDown() && keyEvent.isShiftDown() && KeyCode.ENTER.equals(keyEvent.getCode()))
		{
			leftTable = executeStatement(leftTextArea.getText());
			rightTable = executeStatement(rightTextArea.getText());
			constructTableViews(leftTable, rightTable);
		}
	}

	@FXML
	private void onKeyPressedOnRightEditor(KeyEvent keyEvent)
	{
		if (keyEvent.isAltDown() && !keyEvent.isShiftDown() && KeyCode.ENTER.equals(keyEvent.getCode()))
		{
			rightTable = executeStatement(rightTextArea.getText());
			constructTableViews(leftTable, rightTable);
		}
		else if (keyEvent.isAltDown() && keyEvent.isShiftDown() && KeyCode.ENTER.equals(keyEvent.getCode()))
		{
			leftTable = executeStatement(leftTextArea.getText());
			rightTable = executeStatement(rightTextArea.getText());
			constructTableViews(leftTable, rightTable);
		}
	}
}
