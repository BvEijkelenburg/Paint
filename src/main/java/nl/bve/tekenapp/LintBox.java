package nl.bve.tekenapp;

import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;

public class LintBox extends HBox {
	private Rectangle huidigeKleur;
	private Color[] kleuren = {	Color.BLACK, Color.WHITE, Color.RED, Color.ORANGE, Color.YELLOW,
								Color.GRAY, Color.BLUE, Color.BROWN, Color.PURPLE, Color.GREEN };
	
	private ToggleButton lijn, rechthoek, cirkel, pen;
	public enum Vorm { LIJN, RECHTHOEK, CIRKEL, PEN };
	
	public LintBox() {
		super(5);
		this.setPadding(new Insets(5));
		HBox kleurenPalet = new HBox(5);

		ColorPicker colorPicker = new ColorPicker(Color.BLACK);
		colorPicker.setPrefWidth(100);
		colorPicker.setOnAction(e -> huidigeKleur.setFill(colorPicker.getValue()));

		huidigeKleur = new Rectangle(100, 22, colorPicker.getValue());
		TilePane standaardKleuren = new TilePane(5, 8);
		
		for (Color c : kleuren) {
			Rectangle rect = new Rectangle(22, 22, c);
			standaardKleuren.getChildren().add(rect);
			
			rect.setOnMouseClicked(e -> {
				huidigeKleur.setFill(c);
				colorPicker.setValue(c);
			});
		}
		kleurenPalet.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, CornerRadii.EMPTY, BorderStroke.THIN)));
		kleurenPalet.setPadding(new Insets(5));
		kleurenPalet.getChildren().addAll(new VBox(5, colorPicker, huidigeKleur), standaardKleuren);
		
		lijn = new ToggleButton(null, new Line(0, 0, 30, 0));
		
		Rectangle graph1 = new Rectangle(30, 10, Color.TRANSPARENT);
		graph1.setStroke(Color.BLACK);
		rechthoek = new ToggleButton(null, graph1);
		
		
		Ellipse graph2 = new Ellipse(15, 10);
		graph2.setFill(Color.TRANSPARENT);
		graph2.setStroke(Color.BLACK);
		cirkel = new ToggleButton(null, graph2);
		
		pen = new ToggleButton(null, new Polyline(0, 0, 5, 5, 6, 4, 8, 1, 9, 3, 20, 5, 25, 3, 30, 10));
		
		lijn.setPrefSize(80, 25);
		rechthoek.setPrefSize(80, 25);
		cirkel.setPrefSize(80, 25);
		pen.setPrefSize(80, 25);
		
		ToggleGroup vormGroep = new ToggleGroup();
		lijn.setToggleGroup(vormGroep);
		rechthoek.setToggleGroup(vormGroep);
		cirkel.setToggleGroup(vormGroep);
		pen.setToggleGroup(vormGroep);
		vormGroep.selectToggle(pen);
		
		GridPane vormGrid = new GridPane();
		vormGrid.add(lijn, 0, 0);
		vormGrid.add(rechthoek, 0, 1);
		vormGrid.add(cirkel, 1, 0);
		vormGrid.add(pen, 1, 1);
		
		vormGrid.setHgap(5);
		vormGrid.setVgap(5);
		vormGrid.setPadding(new Insets(5));
		vormGrid.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, CornerRadii.EMPTY, BorderStroke.THIN)));
		
		this.getChildren().add(kleurenPalet);
		this.getChildren().add(vormGrid);
	}

	public Paint getKleur() {
		return huidigeKleur.getFill();
	}
	
	public Vorm getVorm() {
		if (rechthoek.isSelected()) return Vorm.RECHTHOEK;
		else if (lijn.isSelected()) return Vorm.LIJN;
		else if (cirkel.isSelected()) return Vorm.CIRKEL;
		else return Vorm.PEN;
	}
}
