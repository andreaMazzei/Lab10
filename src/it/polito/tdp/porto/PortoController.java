package it.polito.tdp.porto;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {
	
	Model model = new Model();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private Button btnCoautori;

    @FXML
    private Button btnSequenza;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {
    	txtResult.clear();
    	Author a = boxPrimo.getValue();
    	if(a == null) {
    		txtResult.setText("Selezionare Autore!");
    		return;
    	}
    	List<Author> coautori = model.trovaCoautori(a);
    	for(Author c : coautori)
    		txtResult.appendText(c+"\n");
    	
    	List<Author> non_coautori = new ArrayList<Author>(model.getAutori());
    	non_coautori.remove(a);
    	non_coautori.removeAll(coautori);
    	
    	boxSecondo.getItems().clear();
    	boxSecondo.getItems().addAll(non_coautori);
    	boxSecondo.setDisable(false);
    	btnSequenza.setDisable(false);
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	txtResult.clear();
    	Author a1 = boxPrimo.getValue();// inserito per forza
    	Author a2 = boxSecondo.getValue();
    	if(a2 == null) {
    		txtResult.setText("Selezionare un secondo autore!");
    		return;
    	}
    	List<Paper> sequenzaArticoli = new LinkedList<Paper>(model.trovaSequenzaArticoli(a1, a2));
    	for(Paper p : sequenzaArticoli) {
    		txtResult.appendText(p+"\n");
    	}
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert btnCoautori != null : "fx:id=\"btnCoautori\" was not injected: check your FXML file 'Porto.fxml'.";
        assert btnSequenza != null : "fx:id=\"btnSequenza\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";
    }

	public void setModel(Model model) {
		this.model = model;
        boxPrimo.getItems().addAll(model.getAutori());
	}
}

