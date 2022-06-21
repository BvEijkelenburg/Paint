package nl.bve.tekenapp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InstellingenDialoog extends Stage {
	private TextField hoogte, breedte;
	
	public InstellingenDialoog(Stage owner, double b, double h) {
		super(StageStyle.UTILITY);
		initOwner(owner);
		initModality(Modality.WINDOW_MODAL);
		this.setResizable(false);
		this.setTitle("Instellingen");
		
		Label hoogteLbl = new Label("hoogte: ");
		hoogte = new TextField("" + (int)h);
		hoogteLbl.setPrefWidth(100);
		
		Label breedteLbl = new Label("breedte: ");
		breedte = new TextField("" + (int)b);
		breedteLbl.setPrefWidth(100);
		
		Button annuleren = new Button("Annuleren");
		Button bevestigen = new Button("Ok");
		
		annuleren.setPrefWidth(75);
		bevestigen.setPrefWidth(75);
		
		annuleren.setOnAction(e -> {
			hoogte.setText("" + (int)h);
			breedte.setText("" + (int)b);
			this.close();
		});
		bevestigen.setOnAction(e -> this.close());
		
		FlowPane buttonBox = new FlowPane(10, 10);
		buttonBox.setPrefWidth(250);
		buttonBox.setAlignment(Pos.BASELINE_RIGHT);
		buttonBox.getChildren().addAll(annuleren, bevestigen);
		
		VBox dialogBox = new VBox(10);
		dialogBox.getChildren().add(new HBox(hoogteLbl, hoogte));
		dialogBox.getChildren().add(new HBox(breedteLbl, breedte));
		dialogBox.getChildren().add(buttonBox);
		dialogBox.setPadding(new Insets(15));
		
		this.setOnCloseRequest(e -> e.consume());
		Scene scene = new Scene(dialogBox);
		this.setScene(scene);
	}
	
	public int getBreedte() {
		return Integer.parseInt(breedte.getText());
	}

	public int getHoogte() {
		return Integer.parseInt(hoogte.getText());
	}
}
