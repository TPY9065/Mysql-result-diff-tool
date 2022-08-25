package com.example.sqlresultdiff.utils;

public class Utils
{
	public static boolean emptyString(String s)
	{
		return s == null || s.length() == 0;
	}

	public static String getFirstQuery(String sql)
	{
		return sql.substring(0, sql.indexOf(";") + 1);
	}
}
