package com.example.sqlresultdiff.component;

import javafx.scene.control.TableCell;

import java.util.List;
import java.util.Map;

public class CustomTableCell extends TableCell<Map<String, String>, String>
{
	private final List<Integer> differences;

	public CustomTableCell(List<Integer> differences)
	{
		super();
		this.differences = differences;
	}

	@Override
	protected void updateItem(String text, boolean empty)
	{
		super.updateItem(text, empty);
		setText(text);
		setStyle(!empty && differences.contains(getIndex()) ? "-fx-background-color: salmon;" : null);
	}
}
