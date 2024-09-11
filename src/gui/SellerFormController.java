package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable{
	
	private Seller sellDepen;
	
	private SellerService sellService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	public void setSeller(Seller sellDepen) {
		this.sellDepen = sellDepen;
	}
	
	public void setSellerService(SellerService sellService) {
		this.sellService = sellService;
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(sellDepen == null) {
			throw new IllegalStateException("was null");
		}
		
		if(sellService == null) {
			throw new IllegalStateException("service was null");
		}
		
		try {		
			sellDepen = getFormData();
			sellService.saveOrUpdate(sellDepen);
			notifyDataChangerListeners();
		}
		catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
		}
	}		
	
	private void notifyDataChangerListeners() {
		for(DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Seller getFormData() {
		Seller obj = new Seller();
		
		ValidationException valException = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getId()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			valException.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		
		if(valException.getErrors().size() > 0) {
			throw valException;
		}
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 20);
	}
	
	public void updateFormData() {
		if(sellDepen == null) {
			throw new IllegalStateException("sellDepen is null");
		}
		txtId.setText(String.valueOf(sellDepen.getId()));
		txtName.setText(sellDepen.getName());
	}
	
	private void setErrorMessages(Map<String, String> error) {
		Set<String> fields = error.keySet();
		
		if(fields.contains("name")) {
			labelErrorName.setText(error.get("name"));
			
		}
	}

}
