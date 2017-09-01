package MainCode;

import HibernateResources.HibernateUtil;
import Pojos.Llibre;
import Pojos.Prestec;
import Pojos.Soci;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class Main extends Application {

    static Stage primaryStage;
    static String passingMessage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.setOnCloseRequest(e -> closeProgram());
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("hibernateMainModel.fxml"));
        primaryStage.setTitle("Biblioteca");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

    }


    public static boolean showSociEditview(Soci soci){
        try{

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("sociEditView.fxml"));

            AnchorPane rootView = (AnchorPane)loader.load();
            Stage viewStage = new Stage();
            viewStage.setTitle("Soci Editor");
            viewStage.initModality(Modality.WINDOW_MODAL);
            viewStage.initOwner(primaryStage);
            Scene scene = new Scene(rootView);
            viewStage.setScene(scene);

            SociEditController controller = loader.getController();
            controller.setViewgStage(viewStage);
            controller.setSoci(soci);

            // Show the dialog and wait until the user closes it
            viewStage.showAndWait();
            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean showLlibreEditview(Llibre llibre){
        try{

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("llibreEditView.fxml"));

            AnchorPane rootView = (AnchorPane)loader.load();
            Stage viewStage = new Stage();
            viewStage.setTitle("Llibre Editor");
            viewStage.initModality(Modality.WINDOW_MODAL);
            viewStage.initOwner(primaryStage);
            Scene scene = new Scene(rootView);
            viewStage.setScene(scene);

            LlibreEditController controller = loader.getController();
            controller.setViewgStage(viewStage);
            controller.setLlibre(llibre);

            // Show the dialog and wait until the user closes it
            viewStage.showAndWait();
            passingMessage = controller.getLlibreOldData();

            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean showSociEditPrestecView(Prestec prestec){

        try{

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("sociEditPrestecView.fxml"));

            AnchorPane rootView = (AnchorPane)loader.load();
            Stage viewStage = new Stage();
            viewStage.setTitle("Soci Prestec Editor");
            viewStage.initModality(Modality.WINDOW_MODAL);
            viewStage.initOwner(primaryStage);
            Scene scene = new Scene(rootView);
            viewStage.setScene(scene);

            SociEditPrestecController controller = loader.getController();
            controller.setViewgStage(viewStage);

            controller.setPrestec(prestec);
            viewStage.showAndWait();


            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void capSeleccio(String str){
        // Nothing selected.
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cap selecció");
        alert.setHeaderText(null);
        alert.setContentText("Siusplau selecciona un "+str);
        alert.showAndWait();
    }

    public static boolean campsInvalids(String errorMessage){
        // Show the error message.

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Camps Invàlids");
        alert.setHeaderText("Siusplau corregeix-hi els camps invàlids.");
        alert.setContentText(errorMessage);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();

        return false;
    }

    public static  boolean genericCustomConfirmation(String title, String header, String errorMessage, String okMessage){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType buttonOK = new ButtonType(okMessage);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonOK,buttonTypeCancel);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(errorMessage);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == buttonOK;
    }

    public static LocalDate convertToLocalDate (Date data){

        Instant instant = Instant.ofEpochMilli(data.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
    }

    public static Date convertToDate(LocalDate dp){

        if(dp == null)return null;

        Instant instant = Instant.from(dp.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);
    }

    public static boolean isLong(String s){

        try {
            long l = Long.parseLong(s);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static boolean isInt(String s){

        try {
            int l = Integer.parseInt(s);
        }catch (Exception e){
            return false;
        }
        return true;
    }


    public static String getPassingMessage(){ return passingMessage; }

    private void closeProgram() { HibernateUtil.closeSession(); }

    public static void main(String[] args) {
        launch(args);
    }

}
