package com.example.sqlresultdiff.model;

import java.util.List;
import java.util.Map;


public class Table
{
	private final List<String> headers;
	private final List<Map<String, String>> rows;

	public Table(List<String> headers, List<Map<String, String>> rows)
	{
		this.headers = headers;
		this.rows = rows;
	}

	public List<String> headers()
	{
		return headers;
	}

	public List<Map<String, String>> rows()
	{
		return rows;
	}
}
