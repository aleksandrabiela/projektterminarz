
package terminarz;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class KontaktyController implements Initializable 
{
    @FXML private AnchorPane okno;
    @FXML private TextField imie;
    @FXML private TextField nazwisko;
    @FXML private TextField miasto;
    @FXML private TextField telefon;
    @FXML private Label naglowek;
    
    private void zapiszNowy()
    {
        if(imie.getText().length() == 0 || nazwisko.getText().length() == 0)
        {
            Alert a = new Alert(AlertType.ERROR, "imię i nazwisko kontaktu muszą być podane");
            a.showAndWait();
        }
        else 
        {
            Connection conn = null;
            Statement stmt = null;
            try 
            {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
                stmt = conn.createStatement();
                String sql = "insert into kontakty values(null, '" + imie.getText() + "', '" + nazwisko.getText() + "', '" + miasto.getText() + "', '" + telefon.getText() + "', " + Sesja.getId() + ")";
                stmt.executeUpdate(sql);
                Alert a = new Alert(AlertType.INFORMATION, "zapisano kontakt w bazie");
                a.showAndWait();
                anuluj();
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
                }
                catch(Exception e) 
                {
                }
            }
        }
    }
    
    private void nadpisz()
    {
        if(imie.getText().length() == 0 || nazwisko.getText().length() == 0)
        {
            Alert a = new Alert(AlertType.ERROR, "imię i nazwisko kontaktu muszą być podane");
            a.showAndWait();
        }
        else
        {
            Connection conn = null;
            Statement stmt = null;
            try 
            {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
                stmt = conn.createStatement();
                String sql = "update kontakty set imie = '" + imie.getText() + "', nazwisko = '" + nazwisko.getText() + "', miasto = '" + miasto.getText() + "', telefon = '" + telefon.getText() + "' where id = " + Sesja.getKontaktId();
                stmt.executeUpdate(sql);
                anuluj();
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
                }
                catch(Exception e) 
                {
                }
            }
        }
    }
    
    @FXML public void zapisz()
    {
        if(Sesja.getKontaktId() == null) zapiszNowy();
        else nadpisz();
    }
    
    @FXML public void anuluj()
    {
        Sesja.czyscKontakt();
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
    
    private void pobierzDane()
    {
        Connection conn = null;
        Statement stmt = null;
        ResultSet wyniki = null;
        try 
        {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
            stmt = conn.createStatement();
            String sql = "select * from kontakty where id = " + Sesja.getKontaktId();
            wyniki = stmt.executeQuery(sql);
            wyniki.next();
            imie.setText(wyniki.getString("imie"));
            nazwisko.setText(wyniki.getString("nazwisko"));
            miasto.setText(wyniki.getString("miasto"));
            telefon.setText(wyniki.getString("telefon"));
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        if(Sesja.getKontaktId() != null) 
        {
            naglowek.setText("edycja kontaktu");
            pobierzDane();
        }
    }    
    
}
