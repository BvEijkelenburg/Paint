package tekenapp;

import java.awt.image.RenderedImage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TekenApp extends Application {
	private Stage stage;
	private File huidigBestand = null;
	private BorderPane hoofdPanel = new BorderPane();
	private LintBox hetLint = new LintBox();
	private Rectangle canvasBackground = new Rectangle(430, 300);
	private TekenCanvas tekenCanvas = new TekenCanvas(hetLint, 430, 300);
	
	public void start(Stage stage) {
		MenuBar menuBar = new MenuBar();
        Menu menuBestand = new Menu("_Bestand");
        Menu menuBewerken = new Menu("B_ewerken");
        Menu menuOpties = new Menu("Opti_es");
        
        MenuItem miBestNieuw = new MenuItem("_Nieuw");
        MenuItem miBestOpen = new MenuItem("_Open");
        MenuItem miBestOpslaan = new MenuItem("Op_slaan");
        MenuItem miBestOpslaanAls = new MenuItem("Opslaan _als");
        MenuItem miBewOngedaanMaken = new MenuItem("Ongedaan maken");
        MenuItem miOptiesInstellingen = new MenuItem("_Instellingen");
        
        miBestNieuw.setOnAction(e -> {
        	if (wijzigingenAfgehandeld())
        		nieuwCanvas(430, 300);
        });
        miBestOpen.setOnAction(e -> afbeeldingOpenen());
        miBestOpslaan.setOnAction(e -> afbeeldingOpslaan(false));
        miBestOpslaanAls.setOnAction(e -> afbeeldingOpslaan(true));
        miBewOngedaanMaken.setOnAction(e -> tekenCanvas.ongedaanMaken());
        miOptiesInstellingen.setOnAction(e -> toonInstellingen());

        miBestNieuw.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        miBestOpslaan.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        miBewOngedaanMaken.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        
        menuBestand.getItems().addAll(miBestNieuw, miBestOpen, miBestOpslaan, miBestOpslaanAls);
        menuBewerken.getItems().addAll(miBewOngedaanMaken);
        menuOpties.getItems().addAll(miOptiesInstellingen);
        menuBar.getMenus().addAll(menuBestand, menuBewerken, menuOpties);
		
		hoofdPanel.setTop(new VBox(menuBar, hetLint));
		hoofdPanel.setCenter(new StackPane(canvasBackground, tekenCanvas));
		
		canvasBackground.setFill(Color.TRANSPARENT);
		canvasBackground.setStroke(Color.BLACK);

		stage.setOnCloseRequest(e -> {
			if (!wijzigingenAfgehandeld())
				e.consume();
		});
		
		Scene scene = new Scene(hoofdPanel);
		stage.setScene(scene);
		
		this.stage = stage;
		stage.setTitle("TekenApp 2.0");
		stage.show();
	}
	
    private void nieuwCanvas(int breedte, int hoogte) {
    	tekenCanvas = new TekenCanvas(hetLint, breedte, hoogte);
    	hoofdPanel.setCenter(new StackPane(canvasBackground, tekenCanvas));
    	stage.sizeToScene();
    }
    
    private void afbeeldingOpenen() {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Open");
    	File openFile = fileChooser.showOpenDialog(stage);
    	
    	if (openFile != null && wijzigingenAfgehandeld()) {
    		URI uri = openFile.toURI();
    		Image afbeelding = new Image(uri.toString());
    		
    		int breedte = (int)afbeelding.getWidth();
    		int hoogte = (int)afbeelding.getHeight();
    		
    		tekenCanvas = new TekenCanvas(hetLint, breedte, hoogte);
        	hoofdPanel.setCenter(tekenCanvas);
        	huidigBestand = openFile;
    		
    		tekenCanvas.getGraphicsContext2D().drawImage(afbeelding, 0, 0);
    		stage.sizeToScene();
    	}
    };
    
    private boolean afbeeldingOpslaan(boolean dialoogAltijdTonen) {
    	boolean opgeslagen = false;
    	
    	if (huidigBestand == null || dialoogAltijdTonen) {
    		FileChooser fileChooser = new FileChooser();
        	fileChooser.setTitle("Opslaan");
        	fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        	huidigBestand = fileChooser.showSaveDialog(stage);
    	}
		
		if (huidigBestand != null) {
			try {
				SnapshotParameters params = new SnapshotParameters();
				params.setFill(Color.TRANSPARENT);
				WritableImage img = tekenCanvas.snapshot(params, null);
				RenderedImage img2 = SwingFXUtils.fromFXImage(img, null);
				ImageIO.write(img2, "png", huidigBestand);
				tekenCanvas.resetWijzigingen();
				opgeslagen = true;
	   		} catch (IOException ioe) { ioe.printStackTrace(); }
		}
		
		return opgeslagen;
    }
	
    private void toonInstellingen() {
    	InstellingenDialoog inst = new InstellingenDialoog(stage, tekenCanvas.getWidth(), tekenCanvas.getHeight());
    	inst.showAndWait();
    	
    	canvasBackground.setWidth(inst.getBreedte());
    	canvasBackground.setHeight(inst.getHoogte());
    	
    	tekenCanvas.setWidth(inst.getBreedte());
    	tekenCanvas.setHeight(inst.getHoogte());

    	stage.sizeToScene();
    }
    
    private boolean wijzigingenAfgehandeld() {
    	if (tekenCanvas.zijnErWijzigingen()) {
    		Alert bevestiging = new Alert(AlertType.CONFIRMATION, "Wilt u de wijzigingen opslaan?", ButtonType.CANCEL, ButtonType.NO, ButtonType.YES);
    		bevestiging.setTitle("Wilt u de wijziging opslaan?");
    		bevestiging.setHeaderText(null);
    		Optional<ButtonType> antwoord = bevestiging.showAndWait();

	    	if (antwoord.get().equals(ButtonType.YES))
	    		return afbeeldingOpslaan(false);
	    	
	    	if (antwoord.get().equals(ButtonType.CANCEL))
	    		return false;
    	}
    	
    	return true;
    }
    
	public static void main(String[] args) {
		Application.launch(args);
	}
}
