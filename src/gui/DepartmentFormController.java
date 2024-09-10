package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
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
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable{
	
	private Department depDepen;
	
	private DepartmentService depService;
	
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
	
	public void setDepartment(Department depDepen) {
		this.depDepen = depDepen;
	}
	
	public void setDepartmentService(DepartmentService depService) {
		this.depService = depService;
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(depDepen == null) {
			throw new IllegalStateException("was null");
		}
		
		if(depService == null) {
			throw new IllegalStateException("service was null");
		}
		
		try {		
			depDepen = getFormData();
			depService.saveOrUpdate(depDepen);
			Utils.currentStage(event).close();
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
		}
	}		
	
	private Department getFormData() {
		Department obj = new Department();
		
		obj.setId(Utils.tryParseToInt(txtId.getId()));
		obj.setName(txtName.getText());
		
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
		if(depDepen == null) {
			throw new IllegalStateException("depDepen is null");
		}
		txtId.setText(String.valueOf(depDepen.getId()));
		txtName.setText(depDepen.getName());
	}

}
