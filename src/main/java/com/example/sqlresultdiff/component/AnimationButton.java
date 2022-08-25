package com.example.sqlresultdiff.component;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import static org.kordamp.ikonli.bootstrapicons.BootstrapIcons.LIGHTNING_FILL;


public class AnimationButton extends Button
{
	public AnimationButton(Paint onEnteredColor, Paint onExitedColor)
	{
		super();
		setBackground(new Background(new BackgroundFill(onExitedColor, CornerRadii.EMPTY, Insets.EMPTY)));
		setGraphic(FontIcon.of(LIGHTNING_FILL, 20, Color.GOLD));
		setOnMouseEntered(mouseEvent -> setBackground(new Background(new BackgroundFill(onEnteredColor, CornerRadii.EMPTY, Insets.EMPTY))));
		setOnMouseExited(mouseEvent -> setBackground(new Background(new BackgroundFill(onExitedColor, CornerRadii.EMPTY, Insets.EMPTY))));
	}

	public void setIcon(BootstrapIcons icon)
	{
		setGraphic(FontIcon.of(icon, 20, Color.GOLD));
	}
}
