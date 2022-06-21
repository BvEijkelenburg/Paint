package nl.bve.tekenapp;

import java.util.Stack;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class TekenCanvas extends Canvas {
	private ContextMenu contextMenu = new ContextMenu();
	private Stack<WritableImage> undoStack = new Stack<WritableImage>();
	private LintBox hetLint;
	private boolean wijzigingen = false;
	private double startX, startY; 
	
	public TekenCanvas(LintBox hetLint, int breedte, int hoogte) {
		super(breedte, hoogte);
		this.hetLint = hetLint;
		
		MenuItem item = new MenuItem("Wissen");
		item.setOnAction(wissen);
		contextMenu.getItems().add(item);

		this.setOnMousePressed(tekenenStart);
		this.setOnMouseDragged(tekenenSlepen);
		this.setOnMouseReleased(tekenenStop);
		this.setOnMouseClicked(e -> contextMenu.hide());
		this.setOnContextMenuRequested(e -> contextMenu.show(this, e.getScreenX(), e.getScreenY()));
	}
	
	private EventHandler<MouseEvent> tekenenStart = e -> {
		if (e.getButton() == MouseButton.PRIMARY && !contextMenu.isShowing()) {
			wijzigingen = true;
			
			SnapshotParameters params = new SnapshotParameters();
			params.setFill(Color.TRANSPARENT);
			undoStack.push(this.snapshot(params, null));
			GraphicsContext gc = this.getGraphicsContext2D();
			gc.setStroke(hetLint.getKleur());
			
			if (hetLint.getVorm() == LintBox.Vorm.PEN) {
				gc.beginPath();
			} else {
				startX = e.getX();
				startY = e.getY();
			}
		}
	};
	
	private EventHandler<MouseEvent> tekenenSlepen = e -> {
		if (e.getButton() == MouseButton.PRIMARY && !contextMenu.isShowing()) {
			GraphicsContext gc = this.getGraphicsContext2D();
			if (hetLint.getVorm() == LintBox.Vorm.PEN) {
				gc.lineTo(e.getX(), e.getY());
				gc.stroke();
			} else {
				herstelVorigeAfbeelding(false);
				tekenVorm(hetLint.getVorm(), startX, startY, e.getX(), e.getY());
			}
		}
	};
	
	private EventHandler<MouseEvent> tekenenStop = e -> {
		if (e.getButton() == MouseButton.PRIMARY && !contextMenu.isShowing()) {
			GraphicsContext gc = this.getGraphicsContext2D();
			
			if (hetLint.getVorm() == LintBox.Vorm.PEN) {
				gc.lineTo(e.getX(), e.getY());
				gc.stroke();
				gc.closePath();
			} else {
				tekenVorm(hetLint.getVorm(), startX, startY, e.getX(), e.getY());
			}
		}
	};
	
	private EventHandler<ActionEvent> wissen = e -> {
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		undoStack.push(this.snapshot(params, null));
		
		getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
	};
	
	private void herstelVorigeAfbeelding(boolean laatsteAfbeeldingVerwijderen) {
		if (!undoStack.isEmpty()) {
			GraphicsContext gc = this.getGraphicsContext2D();
			gc.clearRect(0, 0, this.getWidth(), this.getHeight());
			
			if (laatsteAfbeeldingVerwijderen) {
				wijzigingen = true;
				gc.drawImage(undoStack.pop(), 0, 0);
			} else gc.drawImage(undoStack.peek(), 0, 0);
		}
	}
	
	private void tekenVorm(LintBox.Vorm vorm, double startX, double startY, double endX, double endY) {
		GraphicsContext gc = this.getGraphicsContext2D();
		double x = startX, y = startY;
		double breedte = Math.abs(endX - startX);
		double hoogte = Math.abs(endY - startY);
		
		if (x > endX) { x = endX; }
		if (y > endY) {	y = endY; }
		
		if (hetLint.getVorm() == LintBox.Vorm.RECHTHOEK) {
			gc.strokeRect(x, y, breedte, hoogte);
		} else if (hetLint.getVorm() == LintBox.Vorm.CIRKEL) {
			gc.strokeOval(x, y, breedte, hoogte);
		} else if (hetLint.getVorm() == LintBox.Vorm.LIJN){
			gc.strokeLine(startX, startY, endX, endY);
		}
	}
	
	public void ongedaanMaken() {
		herstelVorigeAfbeelding(true);
	}
	
	public boolean zijnErWijzigingen() {
		return wijzigingen;
	}
	
	public void resetWijzigingen() {
		wijzigingen = false;
	}
}
