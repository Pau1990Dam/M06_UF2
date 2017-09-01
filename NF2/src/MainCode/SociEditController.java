package MainCode;

import Pojos.Soci;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by pau on 15/02/17.
 */
public class SociEditController {

    @FXML
    private TextField sEditId, sEditCognom, sEditDireccio, sEditTelefon;
    @FXML
    private DatePicker sEditEdat;


    private Stage viewgStage;
    private boolean okClicked =  false;
    private Soci soci = null;
    private LocalDate data;

    @FXML
    private void initialize(){
        sEditId.setEditable(false);
        data = LocalDate.now();
    }

    public void setViewgStage(Stage viewStage) {
        this.viewgStage = viewStage;
    }

    public boolean isOkClicked(){return okClicked;}

    public void setSoci(Soci soci){

        this.soci = soci;
        if(this.soci.getCognom()!=null) {
            sEditId.setText(String.valueOf(this.soci.getId()));
            sEditCognom.setText(this.soci.getCognom());
            sEditEdat.setValue(Main.convertToLocalDate(this.soci.getEdat()));
            sEditDireccio.setText(this.soci.getDireccio());
            sEditTelefon.setText(String.valueOf(this.soci.getTelefon()));
        }
    }

    @FXML
    private void handleOk(ActionEvent event) {

        if(isInputValid()){

            //id, cognom, edat, direccio, telefon
            soci.setCognom(sEditCognom.getText());
            soci.setEdat(Main.convertToDate(sEditEdat.getValue()));
            soci.setDireccio(sEditDireccio.getText());
            soci.setTelefon(Long.parseLong(sEditTelefon.getText()));

            okClicked = true;
            viewgStage.close();

        }

    }

    @FXML
    private  void handleCancel(ActionEvent event) {

        okClicked = false;
        viewgStage.close();
    }


    private boolean isInputValid(){

        String errorMessage = "";

        if (sEditEdat.getValue() == null || (sEditEdat.getValue().isAfter(data.minus(5, ChronoUnit.YEARS)) ||
                sEditEdat.getValue().isBefore(data.minus(120, ChronoUnit.YEARS)) ) ) {
            errorMessage += "Data no vàlida!\n";
        }
        if(!Main.isLong(sEditTelefon.getText()) || sEditTelefon.getText().length()< 9 || sEditTelefon.getText().length()> 12){
            errorMessage += "Telèfon no vàlid!\n";
        }
        if(sEditCognom.getText() == null || sEditCognom.getText().equals("")){
            errorMessage += "Cognom no vàlid!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {

            return Main.campsInvalids(errorMessage);
        }
    }


}
