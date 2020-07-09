package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {

    	txtResult.clear();
    	
    	txtResult.appendText("Artisti connessi:\n"+this.model.artistiConnessi());
    	
    	this.btnCalcolaPercorso.setDisable(false);
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {

    	txtResult.clear();
    	
    	String input = this.txtArtista.getText();
    	
    	try {
    		int scelto = Integer.parseInt(input);
        	txtResult.appendText("Percorso massimo:\n"+this.model.risultato(scelto));

    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire un identificativo numerico positivo.");
    		return;
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	String ruolo = this.boxRuolo.getValue();
    	
    	if(ruolo == null) {
    		txtResult.setText("Selezionare un ruolo dal menù.");
    		return;
    	}

    	txtResult.appendText("Crea grafo...");
    	this.model.creaGrafo(ruolo);
    	txtResult.appendText("\n\n#VERTICI: "+this.model.numeroVertici());
    	txtResult.appendText("\n#ARCHI: "+this.model.numeroArchi());
    	
    	this.btnArtistiConnessi.setDisable(false);
    }

    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxRuolo.getItems().addAll(this.model.elencoRuoli());
		this.btnArtistiConnessi.setDisable(true);
		this.btnCalcolaPercorso.setDisable(true);
	}
}

