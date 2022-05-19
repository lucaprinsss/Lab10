/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.rivers;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import it.polito.tdp.rivers.model.Flow;
import it.polito.tdp.rivers.model.Model;
import it.polito.tdp.rivers.model.River;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;
	private Map<String, River> idMapInversa;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxRiver"
    private ComboBox<String> boxRiver; // Value injected by FXMLLoader

    @FXML // fx:id="txtStartDate"
    private TextField txtStartDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtEndDate"
    private TextField txtEndDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtNumMeasurements"
    private TextField txtNumMeasurements; // Value injected by FXMLLoader

    @FXML // fx:id="txtFMed"
    private TextField txtFMed; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doInfoRiver(ActionEvent event) {
    	String s = boxRiver.getValue();
    	River r=idMapInversa.get(s);
    	List<Flow> lista=r.getFlows();
    	LocalDate prima=lista.get(0).getDay();           
    	LocalDate ultima=lista.get(lista.size()-1).getDay();
    	for(Flow f:lista) {                 //dovrebbero essere ordinate ma per essere sicuri faccio un controllo
    		if(f.getDay().isBefore(prima))
    			prima=f.getDay();
    		if (f.getDay().isAfter(ultima))
    			ultima=f.getDay();
    	}
    	txtStartDate.setText(prima.toString());
    	txtEndDate.setText(ultima.toString());
    	txtNumMeasurements.setText(Integer.toString(lista.size()));
    	txtFMed.setText(String.format("%,4.1f",r.getFlowAvg()));
    }
    
    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	String s = boxRiver.getValue();
    	River r=idMapInversa.get(s);
    	String f=txtK.getText();
    	if(f.isEmpty()) {
    		txtResult.setText("Inserire fattore di scala\n");
    		return;
    	}
    	int k;
    	try {
			k=Integer.parseInt(f);
		} catch (Exception e) {
			txtResult.setText("Errore nell'inserimento del fattore di scala\n");
			return;
		}
    	if(k<=0) {
    		txtResult.setText("Inserire un fattore di scala maggiore di zero\n");
    		return;
    	}
    	
    	int ris=model.simula(k,r);
    	double mediaC=model.getMediaC();
    	
    	txtResult.setText("Giorni non garantiti: "+ris+"\n");
    	txtResult.appendText("La capienza media è stata di "+String.format("%,.2f",mediaC)+"\n");
    	
    	calcolaStatistiche(500,k,r);  //calcolo media min e max su 500 simulazioni
    }
    

    private void calcolaStatistiche(int i, int k, River r) {
    	int n=i;
    	int min=Integer.MAX_VALUE;
    	int max=Integer.MIN_VALUE;
    	int somma=0;
    	for(int j=0;j<n;j++) {
    		int numero=model.simula(k, r);
    		somma+=numero;
    		if(numero>max)
    			max=numero;
    		if(numero<min)
    			min=numero;
    	}
    	txtResult.appendText("\nLanciate  "+n+" simulazioni\nMedia giorni non garantiti: "+(somma/n)+"  Min: "+min+"  Max: "+max+"\n");
		
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxRiver != null : "fx:id=\"boxRiver\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtStartDate != null : "fx:id=\"txtStartDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtEndDate != null : "fx:id=\"txtEndDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNumMeasurements != null : "fx:id=\"txtNumMeasurements\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtFMed != null : "fx:id=\"txtFMed\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxRiver.getItems().clear();
    	idMapInversa=new HashMap<String, River>();
    	for(River r : model.getAllRivers()) {
    		boxRiver.getItems().add(r.getName());
    		idMapInversa.put(r.getName(), r);
    	}
    }
}
