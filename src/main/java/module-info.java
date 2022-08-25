module com.example.sqlresultdiff {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.ikonli.bootstrapicons;
	requires java.sql;

	opens com.example.sqlresultdiff to javafx.fxml;
	exports com.example.sqlresultdiff;
	exports com.example.sqlresultdiff.model;
	opens com.example.sqlresultdiff.model to javafx.fxml;
	exports com.example.sqlresultdiff.component;
	opens com.example.sqlresultdiff.component to javafx.fxml;
	exports com.example.sqlresultdiff.controller;
	opens com.example.sqlresultdiff.controller to javafx.fxml;
	exports com.example.sqlresultdiff.utils;
	opens com.example.sqlresultdiff.utils to javafx.fxml;
}