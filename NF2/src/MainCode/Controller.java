package MainCode;

import DAO.DAO;
import Pojos.Llibre;
import Pojos.Prestec;
import Pojos.PrestecPK;
import Pojos.Soci;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

public class Controller {

    /**
     * Taules
     */
    @FXML
    private TableView taulaSocis;
    @FXML
    private TableView taulaLlibres;
    @FXML
    private TableView taulaPrestecs;
    /**
     * Camps de text, directament no editables, on es visualitzen les dades de la fila seleccionada
     */
    @FXML
    private TextField sID, sCognom, sDireccio, sTelefon, sNaixement, lID, lTitol, lEditorial, lEdicio, lAutor, lPagines,
            lExemplars;
    /**
     * ListView on es mostra el historial de prestecs del soci y el llibre seleccionat a la taula.
     */
    @FXML
    private ListView sPrestecs, lPrestecs;
    /**
     * Texfield editable situat sobre la taula que cerca les files d'aquesta que contenen les paraules introduides
     * en aquest.
     */
    @FXML
    private TextField tfCercador, tfCercadorLlibre, tfCercadorPrestec;

    /**
     * Observable list que contè els ObservableList corresponents a cada fila de la taula.
     */
    private ObservableList<ObservableList> socisData;
    private ObservableList<ObservableList> llibresData;
    private ObservableList<ObservableList> prestecsData;

    /**
     * ObservableList que conté els Strings corresponents a cada fila del ListView.
     */
    private ObservableList<String> listSociData;
    private ObservableList<String> listLlibreData;

    /**
     * Creem la taula; afegim el nom de les columnes d'aquesta; afegim un listener que detecta la fila escollida i
     * introdueix les seves dades en els texfields i el Listview que estan al costat d'aquesta; omplim la taula amb la
     * info de la bd i afegim un listener al texfield situat sobre la taula que fa de cercador.
     */
    @FXML
    public void initialize() {

        socisData = FXCollections.observableArrayList();
        llibresData = FXCollections.observableArrayList();
        prestecsData = FXCollections.observableArrayList();

        listSociData = FXCollections.observableArrayList();
        listLlibreData = FXCollections.observableArrayList();
        /**
         * Creem les columnes de les 3 taules
         */
        setTableColumns(DAO.getSociColumns(), taulaSocis);
        setTableColumns(DAO.getLlibreColums(), taulaLlibres);
        setTableColumns(DAO.getPrestecColumns(), taulaPrestecs);
        /**
         * Afegim un listener que detecti la fila que assenyalem amb un clic i fem que aquest cridi la funció getSociRowData()
         * per a que ompli els TextFields del costat de la taula
         */
        taulaSocis.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> getSociRowData(newValue));
        taulaLlibres.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> getLlibreRowData(newValue));

        /**
         * Introduim les dades de la  bd a dintre les tableView
         */
        getAllSocis();
        getAllLlibres();
        getAllPrestecs();

        /**
         * Afegim un listener al textfiel tfCercador que comprova cada cop que inserim una lletra si existeix algún o
         * alguns camps en la tableview
         */
        //ObservableList<ObservableList>data, TableView tabla, TextField cercador
        afegirCercador(socisData, taulaSocis,tfCercador);
        afegirCercador(llibresData, taulaLlibres,tfCercadorLlibre);
        afegirCercador(prestecsData, taulaPrestecs,tfCercadorPrestec);
        //AQUESTES FUNCIONS DONAVEN PROBLEMES
        /**
         * Afegim un altre listener a la taula socis que marqui aquells socis q tenen un o més prestecs pendents de
         * retornar
         */
        //afegirEstatDelSoci();

        /**
         * Afegim un listener al listview sPrestecs per a que canvi el color de les seves cel·les segons si el prestec
         * a sigut retornat a temps, si s'ha retornat fora de temps o si en fora de temps encara a sigut retornat.
         */
        //afegirEstatDelPrestec();


    }

    /**
     * Afegim les columnes corresponents i les omplim amb la info de la bd extreta a través d'hibernate.
     */

    /**
     * Afegim les columnes corresponents i les omplim amb la info de la bd extreta a través d'hibernate.
     */
    @FXML
    private void setTableColumns(String [] nomsColumnes, TableView tabla) {

        String[] columnas = nomsColumnes;

        int i = 0;

        for (String columna : columnas) {
            final int j = i;
            TableColumn column = new TableColumn(columna.toUpperCase());

            column.setCellValueFactory(param -> {
                String var;

                if(((TableColumn.CellDataFeatures<ObservableList, String>) param).getValue().get(j) == null)
                    var = "null";
                else
                    var = ((TableColumn.CellDataFeatures<ObservableList, String>) param).getValue().get(j).toString();

                return new SimpleStringProperty(var);
            });

            tabla.getColumns().add(column);
            i++;
        }

    }

    /**
     * Obte tots els obj socis de la bd; fica cada obj en un ObservableList<String> que representa un fila de la taula;
     * fica l'observableList corresponent a cada obj i fila en la variable global "data" que no es més que un
     * ObservableList<ObservableList> que representa totes les files de la taula soci.
     */
    @FXML
    private void getAllSocis(){

        List<Soci> llistaSocis = DAO.obtenirSocis();

        for(Soci s: llistaSocis){

            ObservableList<String> row = FXCollections.observableArrayList();
            row.add(String.valueOf(s.getId()));
            row.add(s.getCognom());
            row.add(String.valueOf(s.getEdat()));
            row.add(s.getDireccio());
            row.add(String.valueOf(s.getTelefon()));

            socisData.add(row);
        }


        taulaSocis.setItems(socisData);

    }
    /**
     * Obte tots els obj Llibres de la bd; fica cada obj en un ObservableList<String> que representa un fila de la taula;
     * fica l'observableList corresponent a cada obj i fila en la variable global "libresData" que no es més que un
     * ObservableList<ObservableList> que representa totes les files de la taula soci.
     */
    @FXML
    private void getAllLlibres(){

        List<Llibre> llistaLlibres = DAO.obtenirLlibres();

        for(Llibre l: llistaLlibres){

            ObservableList<String> row = FXCollections.observableArrayList();
            row.add(String.valueOf(l.getId()));
            row.add(l.getTitol());
            row.add(String.valueOf(l.getNombre_exemplars()));
            row.add(l.getEditorial());
            row.add(String.valueOf(l.getNombre_pagines()));
            row.add(String.valueOf(l.getAny_edicio()));
            row.add(l.getAutor());

            llibresData.add(row);
        }


        taulaLlibres.setItems(llibresData);

    }

    @FXML
    private void getAllPrestecs(){

        List<Prestec> llistaPrestecs = DAO.obtenirPrestecs();

        for(Prestec p: llistaPrestecs){

            Llibre l = p.getLlibre();
            Soci s = p.getSoci();

            ObservableList<String> row = FXCollections.observableArrayList();

            row.add(String.valueOf(l.getId()));
            row.add(l.getTitol());
            row.add(l.getAutor());

            row.add(String.valueOf(s.getId()));
            row.add(s.getCognom());
            row.add(s.getDireccio());

            row.add(String.valueOf(p.getData_Inici()));
            row.add(String.valueOf(p.getData_Final()));
            row.add(String.valueOf(p.getData_Entrega_Efectiva()));

            prestecsData.add(row);
        }


        taulaPrestecs.setItems(prestecsData);

    }

    /**
     * Aquesta funció només la crida el listener de la Taula.
     * Agafem l'objecte soci corresponent a la fila de la taula seleccionada demanant a la bd l'obj que tingui la mateixa
     * id. Omplim els Texfields del costat de la taula amb la info del soci demanat a la bd. Esborrem el listview que mostra
     * els prestecs i l'omplim amb l'historial de prestecs que tingui l'objecte soci.
     *
     * @param newValue ObservableList<String> que contè les dades de la fila seleccionada
     */
    @FXML
    private void getSociRowData(Object newValue) {

        listSociData.removeAll(listSociData);

       // sPrestecs.getItems().removeAll(0,sPrestecs.getItems().size());
        if(newValue!=null){

            String capçalera = String.format("||%-8.8s||%-8.8s||%-27.27s||%-27.27s||%-27.27s||",
                    "SociID","LLibreID","Data Inici","Data Fi","Data Entrega");
            listSociData.add(capçalera);

            Soci soci = DAO.obtenirSoci(Long.parseLong(((ObservableList<String>)newValue).get(0)));
            sID.setText(String.valueOf(soci.getId()));
            sCognom.setText(soci.getCognom());
            sDireccio.setText(soci.getDireccio());
            sTelefon.setText(String.valueOf(soci.getTelefon()));
            sNaixement.setText(String.valueOf(soci.getEdat()));

            for (Prestec p: soci.getPrestecs()){
                listSociData.add(p.toStringFormated());
            }

            sPrestecs.setItems(listSociData);

        } else {
            sID.setText("");
            sCognom.setText("");
            sDireccio.setText("");
            sTelefon.setText("");
            sNaixement.setText("");
            listSociData.removeAll(listSociData);
         //   sPrestecs.getItems().removeAll(0,sPrestecs.getItems().size());
        }

    }

    /**
     * Aquesta funció només la crida el listener de la Taula.
     * Agafem l'objecte soci corresponent a la fila de la taula seleccionada demanant a la bd l'obj que tingui la mateixa
     * id. Omplim els Texfields del costat de la taula amb la info del soci demanat a la bd. Esborrem el listview que mostra
     * els prestecs i l'omplim amb l'historial de prestecs que tingui l'objecte soci.
     *
     * @param newValue ObservableList<String> que contè les dades de la fila seleccionada
     */
    @FXML
    private void getLlibreRowData(Object newValue) {

        listLlibreData.removeAll(listLlibreData);

        // sPrestecs.getItems().removeAll(0,sPrestecs.getItems().size());
        if(newValue!=null){

            String capçalera = String.format("||%-8.8s||%-8.8s||%-27.27s||%-27.27s||%-27.27s||",
                    "SociID","LLibreID","Data Inici","Data Fi","Data Entrega");
            listLlibreData.add(capçalera);

            Llibre llibre = DAO.obtenirLlibre(Long.parseLong(((ObservableList<String>)newValue).get(0)));

            lID.setText(String.valueOf(llibre.getId()));
            lTitol.setText(llibre.getTitol());
            lEditorial.setText(llibre.getEditorial());
            lEdicio.setText(String.valueOf(llibre.getAny_edicio()));
            lAutor.setText(llibre.getAutor());
            lPagines.setText(String.valueOf(llibre.getNombre_pagines()));
            lExemplars.setText(String.valueOf(llibre.getNombre_exemplars()));

            for (Prestec p: llibre.getPrestecs()){
                listLlibreData.add(p.toStringFormated());
            }

            lPrestecs.setItems(listLlibreData);

        } else {
            lID.setText("");
            lTitol.setText("");
            lEditorial.setText("");
            lEdicio.setText("");
            lAutor.setText("");
            lPagines.setText("");
            lExemplars.setText("");
            //   sPrestecs.getItems().removeAll(0,sPrestecs.getItems().size());
        }

    }


    /**
     *Listener del textfiel que fa de cercador.
     * Compara les paraules introduides al textfield amb les de les columnes de cada fila de la taula i fa que la
     *  taula només mostri aquelles files que continguin les mateixes paraules.
     */
    @FXML
    private void afegirCercador(ObservableList<ObservableList>data, TableView tabla, TextField cercador){

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<ObservableList> filteredData = new FilteredList<>(data, param -> true);

        cercador.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(row -> {
                // If filter text is empty, display all rows.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                HashSet<String> fila = new HashSet<String>();
                StringTokenizer cerca = new StringTokenizer(newValue.trim(), ";");
                StringTokenizer columna = new StringTokenizer(row.toString().replace("[","").replace("]","").trim(), ",");
                while (columna.hasMoreTokens()) {
                    fila.add(columna.nextToken().trim());
                }

                while(cerca.hasMoreTokens()){
                    if(!fila.contains(cerca.nextToken().trim())) {
                        return false;
                    }
                }
                return true;
            });
        });

        SortedList<ObservableList> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tabla.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tabla.setItems(sortedData);
    }

    /**
     * Listener del Listview que marca els prestecs amb un color segons els següents criteris:
     * Cap - Prestect entregat dintre de la data convenida.
     * Verd - Prestec encara no Entregat per al qual encara no ha expirat la data fi.
     * Taronja - Prestec entregat tard, es a dir, for de la data fi.
     * Vermell - Prestec no entregat que ha superat la seva data fi.
     */
    @FXML
    private void afegirEstatDelPrestec(){

        sPrestecs.setCellFactory(sprestec -> new ListCell<String>() {
            @Override
            protected void updateItem(String s, boolean b) {
                super.updateItem(s, b);
                if(b ||s == null ){
                    setText(null);
                    setGraphic(null);
                    setStyle(null);
                    return;
                }

                setText(s);
                if(s.contains("SociID")) return;
                Timestamp data_fi = null;
                String entrega = "";
                StringTokenizer token = new StringTokenizer(s,"||");
                for(int i = 0; i < 5; i++){
                    if(i == 3) {
                        data_fi = Timestamp.valueOf(token.nextToken().trim());
                        continue;
                    }
                    if(i == 4){
                        entrega = token.nextToken().trim();
                        break;
                    }
                    token.nextToken();
                }

                if (data_fi.after(Timestamp.from(Instant.now()))){
                    if(entrega.contains("null"))
                        setStyle("-fx-background-color: linear-gradient(#007F0E 0%, #FFFFFF 90%, #eaeaea 90%);");

                }else if(entrega.contains("null")){
                    setStyle("-fx-background-color: linear-gradient(#FF0000 0%, #FFFFFF 90%, #eaeaea 90%);");

                }else if (Timestamp.valueOf(entrega).after(data_fi)){
                    setStyle("-fx-background-color: linear-gradient(#FF8000 0%, #FFFFFF 90%, #eaeaea 90%);");
                }
              //
            }
        });
    }

    /**
     * Listener de la taula socis que marca els socis d'un color segons els següents criteris:
     * Cap - El soci no te cap prestec pendent d'entregar i/o els ha entregat tots a temps.
     * Verd - El soci te prestecs pendents d'entregar però, no ha expirat cap data fi de cap prestec.
     * Groc - El soci ha entregat alguna vegada algun prestec tard.
     * Vermell - El soci te algun prestec pendent d'entregar per al qual ha expirat la data fi.
     */
    @FXML
    private void afegirEstatDelSoci(){
        taulaSocis.setRowFactory(paramP -> new TableRow<ObservableList>() {
            @Override
            protected void updateItem(ObservableList paramT, boolean empty) {

                super.updateItem(paramT,empty);
                textProperty().unbind();
                if(paramT != null && !empty){

                    String status = "";
                    Soci soci = DAO.obtenirSoci(Long.parseLong((String) paramT.get(0)));

                        for(Prestec p: soci.getPrestecs()){

                            if(p.getData_Entrega_Efectiva() == null){

                                if(p.getData_Final().before(new java.util.Date())){
                                    //"-fx-control-inner-background: #;"
                                    //"-fx-background-color: linear-gradient(#FF0000 0%);"
                                    setStyle("-fx-control-inner-background: #FF0000;");
                                    return;
                                }
                                status +=" good ";

                            }else{

                                if(p.getData_Entrega_Efectiva().after(p.getData_Final())){
                                    status += " bad ";
                                }
                            }
                        }

                        if(status.contains("bad")){
                            setStyle("-fx-control-inner-background: #F4FA58;");

                        }else if(status.contains("good")){
                            setStyle("-fx-control-inner-background: #80FF00;");

                        }
                    }
                }

            });
    }


    /**
     * Listener dels botons "Nou" que detecta si es
     * @param event
     * @see#Main.showSociEditview(Soci soci)
     */
    @FXML
    private void handleNou(ActionEvent event) {

        String id = ((Button)event.getSource()).getId();

        switch (id){

            case "sNou":
                Soci soci = new Soci();
                if(Main.showSociEditview(soci)) {
                    DAO.persistSoci(soci);

                    socisData.add(newSociItem(soci));
                }
                break;

            case "lNou":
                Llibre llibre = new Llibre();
               if(Main.showLlibreEditview(llibre)){
                   DAO.persistLlibre(llibre);
                    //Título, editorial, pagina, edicion, autor
                   llibresData.add(newLlibreItem(llibre, null, "nou"));
                }
                break;
        }

    }

    @FXML
    private void handleEditar(ActionEvent event) {

        String id = ((Button)event.getSource()).getId();
        int selectedIndex;

        switch (id) {

            case "sEditar":

                selectedIndex = taulaSocis.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {

                    Soci soci = new Soci();
                    soci.setId(Long.parseLong(sID.getText()));
                    soci.setCognom(sCognom.getText());
                    soci.setEdat(Date.valueOf(sNaixement.getText()));
                    soci.setDireccio(sDireccio.getText());
                    soci.setTelefon(Long.parseLong(sTelefon.getText()));

                    if (Main.showSociEditview(soci)) {
                        DAO.updateSoci(soci);

                        socisData.set(taulaSocis.getSelectionModel().getSelectedIndex(), newSociItem(soci));

                        ArrayList <Prestec> prestecs = new ArrayList<>();
                        prestecs.addAll(DAO.obtenirSoci(soci.getId()).getPrestecs());

                       if(!prestecs.isEmpty())
                            updateTaulaPrestecsRows(soci);


                    }
                }else{
                    Main.capSeleccio("soci de la taula.");
                }
                break;

            case "lEditar":
                selectedIndex = taulaLlibres.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {
                    Llibre llibre = new Llibre();
                    llibre.setId(Long.parseLong(lID.getText()));
                    llibre.setTitol(lTitol.getText());
                    llibre.setAutor(lAutor.getText());
                    llibre.setAny_edicio(Date.valueOf(lEdicio.getText()));
                    llibre.setEditorial(lEditorial.getText());
                    llibre.setNombre_exemplars(Integer.valueOf(lExemplars.getText()));
                    llibre.setNombre_pagines(Integer.valueOf(lPagines.getText()));

                    if(Main.showLlibreEditview(llibre)){
                        DAO.updateLlibre(llibre);

                        llibresData.set(taulaLlibres.getSelectionModel().getSelectedIndex(), newLlibreItem(llibre,
                                Main.getPassingMessage(),"editExemplar"));

                        ArrayList <Prestec> prestecs = new ArrayList<>();
                        prestecs.addAll(DAO.obtenirLlibre(llibre.getId()).getPrestecs());

                        if(!prestecs.isEmpty()) {

                            updateTaulaPrestecsRows(llibre);
                        }
                    }
                }else{
                    Main.capSeleccio("soci de la taula.");
                }
                break;

        }

    }

    @FXML
    private void handleBorrar(ActionEvent event) {

        String id = ((Button)event.getSource()).getId();
        int selectedIndex;

        switch (id){

            case "sBorrar":
                selectedIndex = taulaSocis.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {

                    Soci soci = new Soci();
                    soci.setId(Long.parseLong(sID.getText()));
                    soci.setCognom(sCognom.getText());
                    soci.setEdat(Date.valueOf(sNaixement.getText()));
                    soci.setDireccio(sDireccio.getText());
                    soci.setTelefon(Long.parseLong(sTelefon.getText()));

                    DAO.deleteSoci(soci);
                    socisData.remove(selectedIndex);

                    prestecsData.removeAll(prestecsData);
                    getAllPrestecs();


                } else {
                    Main.capSeleccio("soci de la taula.");
                }
                break;

            case "lBorrar":
                selectedIndex = taulaLlibres.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {

                    Llibre llibre = new Llibre();
                    llibre.setId(Long.parseLong(lID.getText()));
                    llibre.setTitol(lTitol.getText());
                    llibre.setAutor(lAutor.getText());
                    llibre.setAny_edicio(Date.valueOf(lEdicio.getText()));
                    llibre.setEditorial(lEditorial.getText());
                    llibre.setNombre_exemplars(Integer.valueOf(lExemplars.getText()));
                    llibre.setNombre_pagines(Integer.valueOf(lPagines.getText()));

                    if(!DAO.esUnLlibrePrestable(llibre)){

                        if(!Main.genericCustomConfirmation("Llibre No Retornat!","El llibre està en prestec.",
                                "Estas segur que vols eliminar de la biblioteca un llibre que encara no s'ha retornat?"
                                ,"Eliminar LLibre"))
                            break;
                    }

                    updateTaulaLLibresExemplars(String.valueOf(llibre.getId()),llibre.toStringForTableView(), null, "borrar");
                    DAO.deleteLlibre(llibre);
                    llibresData.remove(selectedIndex);

                    prestecsData.removeAll(prestecsData);
                    getAllPrestecs();


                } else {
                    Main.capSeleccio("llibre de la taula.");
                }
                break;
        }

    }

    @FXML
    private void handleNouPrestec(ActionEvent event) {

        int selectedIndex = taulaSocis.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {

            Prestec prestec = new Prestec();
            prestec.setSoci(DAO.obtenirSoci(Long.parseLong(sID.getText())));
            if(Main.showSociEditPrestecView(prestec)){
                DAO.persistPrestec(prestec);
                ObservableList<String> newValue = FXCollections.observableArrayList();
                newValue.add(String.valueOf(prestec.getSoci().getId()));
                getSociRowData(newValue);

                prestecsData.add(newPrestecItem(prestec));
            }
        }else {
            Main.capSeleccio("soci de la taula.");
        }

    }

    @FXML
    private void handleFinalitzarPrestec(ActionEvent event) {

        int selectedIndex = sPrestecs.getSelectionModel().getSelectedIndex();
        if(selectedIndex >= 1 && ((String) sPrestecs.getSelectionModel().getSelectedItem()).substring(80,84).equals("null")){

            String prestecString = (String) sPrestecs.getSelectionModel().getSelectedItem();
            StringTokenizer token = new StringTokenizer(prestecString,"||");

            PrestecPK  pk = new PrestecPK();

            pk.setSoci(DAO.obtenirSoci(Long.parseLong(token.nextToken().trim())));

            pk.setLlibre(DAO.obtenirLlibre(Long.parseLong(token.nextToken().trim())));

            pk.setData_Inici(Timestamp.valueOf(token.nextToken().trim()));
            pk.setData_Final(Timestamp.valueOf(token.nextToken().trim()));
            Prestec prestec = DAO.obtenirPrestec(pk);

            if(Main.showSociEditPrestecView(prestec)){
                DAO.updatePrestec(prestec);
                ObservableList<String> newValue = FXCollections.observableArrayList();
                newValue.add(String.valueOf(prestec.getSoci().getId()));
                getSociRowData(newValue);

                updateTaulaPrestecsRows(prestec);
            }

        }else{
            Main.capSeleccio("prestec no finalitzat de la llista .");
        }

    }

    private ObservableList<String> newSociItem(Soci soci){

        ObservableList<String> row= FXCollections.observableArrayList();
        row.add(String.valueOf(soci.getId()));
        row.add(soci.getCognom());
        row.add(String.valueOf(Main.convertToLocalDate(soci.getEdat())));
        row.add(soci.getDireccio());
        row.add(String.valueOf(soci.getTelefon()));

        return row;
    }

    private ObservableList<String> newLlibreItem(Llibre llibre, String beforeEditing, String mode){


        ObservableList<String> row= FXCollections.observableArrayList();
        row.add(String.valueOf(llibre.getId()));
        row.add(llibre.getTitol());
        row.add(String.valueOf(updateTaulaLLibresExemplars(row.get(0),llibre.toStringForTableView(), beforeEditing, mode)));
        row.add(llibre.getEditorial());
        row.add(String.valueOf(llibre.getNombre_pagines()));
        row.add(String.valueOf(Main.convertToLocalDate(llibre.getAny_edicio())));
        row.add(llibre.getAutor());

        return row;
    }

    private ObservableList<String> newPrestecItem(Prestec  p){

        ObservableList<String> row= FXCollections.observableArrayList();

        Llibre l = p.getLlibre();
        Soci s = p.getSoci();

        row.add(String.valueOf(l.getId()));
        row.add(l.getTitol());
        row.add(l.getAutor());

        row.add(String.valueOf(s.getId()));
        row.add(s.getCognom());
        row.add(s.getDireccio());

        row.add(String.valueOf(p.getData_Inici()));
        row.add(String.valueOf(p.getData_Final()));
        row.add(String.valueOf(p.getData_Entrega_Efectiva()));

        return row;
    }



    private int updateTaulaLLibresExemplars(String llibreIdToIgnore, String exemplar, String beforeEditing, String mode){

        int numExemplars = 0;
        int addOrSubstract = 0;
        long idToUpdateExemplars = -1;
        long idToUpdateExemplarsAfterEditing = -1;

        switch (mode){

            case "borrar":
                addOrSubstract = -1;
                break;
            case "editExemplar":
            case "nou":
                addOrSubstract = 1;
                break;
        }

        for(ObservableList row: llibresData){
            if((""+row.get(0)).equals(llibreIdToIgnore)) continue;

            if((row.get(1)+";"+row.get(3)+";"+row.get(4)+";"+row.get(5)+";"+row.get(6)).equals(exemplar)){

                numExemplars = (Integer.valueOf((String)row.get(2))+addOrSubstract);
                row.set(2,String.valueOf(numExemplars));
                idToUpdateExemplars = Long.valueOf(""+row.get(0));
                System.out.println("ID 1 "+idToUpdateExemplars);

            }

            if(mode.equals("editExemplar") &&
                    (row.get(1)+";"+row.get(3)+";"+row.get(4)+";"+row.get(5)+";"+row.get(6)).equals(beforeEditing) ){

                row.set(2,String.valueOf(Integer.valueOf(""+row.get(2)) -1));
                idToUpdateExemplarsAfterEditing = Long.valueOf(""+row.get(0));

            }
        }

        if(idToUpdateExemplars != -1 ) {

            DAO.updateExemplars(DAO.obtenirLlibre(idToUpdateExemplars));
        }
        if(idToUpdateExemplarsAfterEditing !=-1 ) {

            DAO.updateExemplars(DAO.obtenirLlibre(idToUpdateExemplarsAfterEditing));
        }
        return (numExemplars == 0)?1:numExemplars;
    }

    private void updateTaulaPrestecsRows(Object obj){
        String id;

        if(obj instanceof Soci){

            id = String.valueOf(((Soci) obj).getId());

            for(ObservableList row: prestecsData){

                if((""+row.get(3)).equals(id)){
                    row.set(4,((Soci) obj).getCognom());
                    row.set(5,((Soci) obj).getDireccio());
                }
            }

        }else if(obj instanceof Llibre){

            id = String.valueOf(((Llibre) obj).getId());

            for(ObservableList row: prestecsData){

                if((""+row.get(0)).equals(id)){
                    row.set(1,((Llibre) obj).getTitol());
                    row.set(2,((Llibre) obj).getAutor());
                }
            }

        }else{

            id = ((Prestec)obj).toStringChangeChecker();

            for(ObservableList row: prestecsData){

                if(((row.get(0))+";"+row.get(3)+";"+row.get(6)+";"+row.get(7)).equals(id)){
                    row.set(8,((Prestec)obj).getData_Entrega_Efectiva());
                }
            }
        }
        taulaPrestecs.refresh();
    }



}
