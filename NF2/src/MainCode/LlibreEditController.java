package MainCode;

import Pojos.Llibre;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class LlibreEditController {

    @FXML
    private TextField llibreEditId, llibreEditTitol, llibreEditAutor, llibreEditEditorial, llibreEditPagines,
            llibreEditExemplars;
    @FXML
    private DatePicker llibreEditEdicio;

    private Stage viewgStage;
    private boolean okClicked =  false;
    private Llibre llibre = null;
    private String llibreHasChanged = "";
    private LocalDate data;


    @FXML
    private void initialize(){
        llibreEditId.setEditable(false);
        llibreEditExemplars.setEditable(false);
        data = LocalDate.now();
    }

    public void setViewgStage(Stage viewStage) {
        this.viewgStage = viewStage;
    }

    public boolean isOkClicked(){return okClicked;}

    public void setLlibre(Llibre l){

        llibre = l;

        if(llibre.getAutor()!=null){
            llibreHasChanged = llibre.toStringForTableView();
            llibreEditId.setText(String.valueOf(llibre.getId()));
            llibreEditTitol.setText(llibre.getTitol());
            llibreEditAutor.setText(llibre.getAutor());
            llibreEditEdicio.setValue(Main.convertToLocalDate(llibre.getAny_edicio()));
            llibreEditEditorial.setText(llibre.getEditorial());
            llibreEditPagines.setText(String.valueOf(llibre.getNombre_pagines()));
            llibreEditExemplars.setText(String.valueOf(llibre.getNombre_exemplars()));
        }
    }

    @FXML
    private void handleOk(ActionEvent event) {

        if(isInputValid()){

            llibre.setTitol(llibreEditTitol.getText());
            llibre.setAutor(llibreEditAutor.getText());
            llibre.setAny_edicio(Main.convertToDate(llibreEditEdicio.getValue()));
            llibre.setEditorial(llibreEditEditorial.getText());
            llibre.setNombre_pagines(Integer.valueOf(llibreEditPagines.getText()));

            okClicked = !llibreHasChanged.equals(llibre.toStringForTableView());

            viewgStage.close();
        }

    }

    @FXML
    private void handleCancel(ActionEvent event) {

        okClicked = false;
        viewgStage.close();
    }

    private boolean isInputValid(){

        String errorMessage = "";

        if (llibreEditEdicio.getValue() == null || (llibreEditEdicio.getValue().isAfter(data) ||
                llibreEditEdicio.getValue().isBefore(data.minus(120, ChronoUnit.YEARS)) ) ) {
            errorMessage += "Any de edició invàlid!\n";
        }
        if(!Main.isInt(llibreEditPagines.getText()) || llibreEditPagines.getText().length() > 5 || Integer.valueOf(llibreEditPagines.getText()) < 1){
            errorMessage += "Número de pàgines invàlid!\n";
        }
        if(llibreEditAutor.getText().isEmpty()){
            errorMessage += "El llibre ha de tenir un autor, encara que sigui Anónim!\n";
        }
        if(llibreEditTitol.getText().isEmpty()){
            errorMessage += "El llibre ha de tenir un Títol\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {

            return Main.campsInvalids(errorMessage);
        }
    }

    public String getLlibreOldData(){ return llibreHasChanged; }
}
