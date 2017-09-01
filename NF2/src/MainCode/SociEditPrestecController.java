package MainCode;

import DAO.DAO;
import Pojos.Llibre;
import Pojos.Prestec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

public class SociEditPrestecController {

    @FXML
    private TextField sEditPrestecSoci;
    @FXML
    private DatePicker  sEditPrestecDinici, sEditPrestecDfi, sEditPrestecDentrega;
    @FXML
    private MenuButton sEditPrestecLlibre;

    private ObservableList<MenuItem> oBllibres;
    private Stage viewgStage;
    private boolean okClicked =  false;
    private Prestec prestec = null;
    private LocalDate data;
    private boolean isNouPrestec = false;

    private String errorMessage;

    @FXML
    private void initialize(){


    }

    public void setViewgStage(Stage viewStage) {
        this.viewgStage = viewStage;
    }

    public boolean isOkClicked(){return okClicked;}

    public void setPrestec (Prestec prestec){

        this.prestec = prestec;
        if(this.prestec.getData_Final() != null) {
            isNouPrestec = false;
            sEditPrestecSoci.setText(String.valueOf(this.prestec.getSoci().getId()));
            sEditPrestecLlibre.setText(String.valueOf(this.prestec.getLlibre().getId()));
            sEditPrestecDinici.setValue(convertToLocalDate(this.prestec.getData_Inici()));
            sEditPrestecDfi.setValue(convertToLocalDate(this.prestec.getData_Final()));
            configureFieldsForFinalizePrestec();

        }else{
            isNouPrestec = true;
            sEditPrestecSoci.setText(String.valueOf(prestec.getSoci().getId()));
            sEditPrestecDinici.setValue(LocalDate.now());
            configureFieldForNewPrestec();
            fillMenuButtons();

        }

    }


    private void configureFieldForNewPrestec(){

        sEditPrestecDfi.setDisable(false);
        sEditPrestecLlibre.setDisable(false);
        sEditPrestecDinici.setDisable(true);
        sEditPrestecDentrega.setDisable(true);
        sEditPrestecSoci.setDisable(true);

    }

    private void configureFieldsForFinalizePrestec(){

        sEditPrestecDinici.setDisable(true);
        sEditPrestecLlibre.setDisable(true);
        sEditPrestecDfi.setDisable(true);
        sEditPrestecLlibre.setDisable(true);
        sEditPrestecDentrega.setDisable(false);

    }

    private void fillMenuButtons(){

        oBllibres  = FXCollections.observableArrayList();

        ArrayList <Llibre> llibres = new ArrayList<>();
        llibres.addAll(DAO.obtenirLlibresPrestables());

        for(Llibre exemplar: llibres){
            MenuItem item = new MenuItem(exemplar.getId()+" "+exemplar.getTitol()+" "+exemplar.getAutor());
            addMenuItemListener(item);
            oBllibres.add(item);
        }

        sEditPrestecLlibre.getItems().addAll(oBllibres);

    }


    private void addMenuItemListener(MenuItem item){
        item.setOnAction(event -> {
            MenuItem item1 = (MenuItem) event.getSource();
            sEditPrestecLlibre.setText(item1.getText().substring(0,item1.getText().indexOf(" ")));
        });
    }


    @FXML
    private void handleOk(ActionEvent event) {

        if(isInputValid()){

            if(isNouPrestec){
                prestec.setLlibre(DAO.obtenirLlibre(Long.parseLong(sEditPrestecLlibre.getText())));
                prestec.setData_Inici(convertToDate(sEditPrestecDinici.getValue()));
                prestec.setData_Final(convertToDate(sEditPrestecDfi.getValue()));
            }else{
                prestec.setData_Entrega_Efectiva(convertToDate(sEditPrestecDentrega.getValue()));
            }

            okClicked = true;
            viewgStage.close();
        }else{
            Main.campsInvalids(errorMessage);
        }

    }

    @FXML
    public void handleCancel(ActionEvent event) {

        okClicked = false;
        viewgStage.close();
    }

    public static Date convertToDate(LocalDate dp){

        if(dp == null)return null;

        Instant instant = Instant.from(dp.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);

    }

    public static LocalDate convertToLocalDate (Date data){

        Instant instant = Instant.ofEpochMilli(data.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
    }

    private boolean isLong(String s){

        try {
            long l = Long.parseLong(s);
        }catch (Exception e){
            return false;
        }
        return true;
    }


    private boolean isInputValid(){

        errorMessage = "";

        if(isNouPrestec){
            if (sEditPrestecDfi.getValue() == null || (sEditPrestecDfi.getValue().isBefore(LocalDate.now().plus(1,ChronoUnit.DAYS)) ||
                    sEditPrestecDfi.getValue().isAfter(LocalDate.now().plus(6,ChronoUnit.MONTHS)) ) ) {
                errorMessage += "La data d'entrega programada (DATA FI) " +
                        "a de ser d'almenys 1 día y de no més de 6 mesos" +
                        " des d'avui.\n";
            }
            if(sEditPrestecLlibre.getText() == null || !isLong(sEditPrestecLlibre.getText())){
                errorMessage += "Falta escollir el camp LLIBRE ID.";
            }

        }else {
            //sEditPrestecDentrega.getValue().isBefore(LocalDate.now().plus(1,ChronoUnit.DAYS)
            if((sEditPrestecDentrega.getValue().isBefore(LocalDate.now())) ||
                    (sEditPrestecDentrega.getValue().isAfter(LocalDate.now().plus(1,ChronoUnit.DAYS)))){
                errorMessage += "La data d'entrega efectiva ha de ser a de ser d'avui, que es quan el llibre ha  sigut " +
                        "entregat, i ho has de registrar!\n";
            }
        }

            return errorMessage.length() == 0;


    }
}
