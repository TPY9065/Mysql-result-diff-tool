package com.example.sqlresultdiff.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MysqlConnection
{
	private static final String URL_PREFIX = "jdbc:mysql://";
	private static final String DEFAULT_HOST = "localhost";
	private static final String DEFAULT_PORT = "3306";
	private static final String DEFAULT_SCHEMA = "hktv_psos";
	private static final String DEFAULT_USERNAME = "root";
	private static final String DEFAULT_PASSWORD = "root";

	private Connection connection;

	public void connect(String host, String port, String schema, String username, String password) throws SQLException
	{
		String url = URL_PREFIX + (host == null || host.length() == 0 ? DEFAULT_HOST : host) + ":" +
				(port == null || port.length() == 0 ? DEFAULT_PORT : port) + "/" +
				(schema == null || schema.length() == 0 ? DEFAULT_SCHEMA : schema);
		username = username == null || username.length() == 0 ? DEFAULT_USERNAME : username;
		password = password == null || password.length() == 0 ? DEFAULT_PASSWORD : password;
		connection = DriverManager.getConnection(url, username, password);
	}

	public Table execute(String sql) throws SQLException
	{
		List<String> headers = new ArrayList<>();
		List<Map<String, String>> rows = new ArrayList<>();

		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql))
		{
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			final int columnCount = resultSetMetaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++)
			{
				headers.add(resultSetMetaData.getColumnName(i));
			}

			while (resultSet.next())
			{
				Map<String, String> row = new HashMap<>();
				for (String header : headers)
				{
					row.put(header, resultSet.getString(header));
				}
				rows.add(row);
			}
		}

		return new Table(headers, rows);
	}

	public void close()
	{
		if (connection != null)
		{
			try
			{
				connection.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
}
