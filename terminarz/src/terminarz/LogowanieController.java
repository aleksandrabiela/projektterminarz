package terminarz;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LogowanieController implements Initializable 
{
    @FXML public TextField login;
    @FXML public PasswordField haslo;
    @FXML public AnchorPane okno;

    @FXML public void zaloguj()
    {
        if(login.getText().length() == 0 || haslo.getText().length() == 0)
        {
            Alert a = new Alert(AlertType.ERROR, "pola nie mogą być puste");
            a.showAndWait();
        }
        else 
        {
            Connection conn = null;
            Statement stmt = null;
            ResultSet wyniki = null;
            try 
            {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
                stmt = conn.createStatement();
                String sql = "select * from uzytkownicy where login = '" + login.getText() + "' and haslo = '" + haslo.getText() + "'";
                wyniki = stmt.executeQuery(sql);
                if(wyniki.next())
                {
                    Sesja.setId(wyniki.getInt("id"));
                    Sesja.setLogin(wyniki.getString("login"));
                    try
                    {
                        Parent root = FXMLLoader.load(getClass().getResource("StronaGlowna.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
                        Stage thisStage = (Stage) okno.getScene().getWindow();
                        thisStage.close();
                    }
                    catch(Exception e)
                    {

                    }
                }
                else
                {
                    Alert a = new Alert(AlertType.ERROR, "błędne dane logowania");
                    a.showAndWait();
                }
            }
            catch(Exception e) 
            {
                Alert a = new Alert(Alert.AlertType.ERROR, e.getMessage());
                a.showAndWait();
            }
            finally 
            {
                try 
                {
                    if(stmt != null) stmt.close();
                    if(conn != null) conn.close();
                    if(wyniki != null) wyniki.close();
                }
                catch(Exception e) 
                {
                }
            }
        }
    }
    
    @FXML public void utworzKonto()
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("Rejestracja.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            Stage thisStage = (Stage) okno.getScene().getWindow();
            thisStage.close();
        }
        catch(Exception e)
        {
            
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        Sesja.czyscSesje();
    }    
    
}
