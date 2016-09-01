
package terminarz;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StronaGlownaController implements Initializable 
{
    @FXML private AnchorPane okno;
    @FXML private AnchorPane lewy;
    @FXML private AnchorPane prawy;
    @FXML private Label naglowek;
    
    @FXML public void wyloguj()
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("Logowanie.fxml"));
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
    
    @FXML public void kontakt()
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("kontakty.fxml"));
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
    
    public void edytujKontakt(MouseEvent e) 
    {
        Integer kontaktId = ((Link) e.getSource()).getKontaktId();
        Sesja.setKontaktId(kontaktId);
        kontakt();
    }
    
    public void usunKontakt(MouseEvent e)
    {
        Alert a = new Alert(AlertType.CONFIRMATION, "Czy na pewno usunąć wskazany kontakt oraz związane z nim terminy?");
        Optional<ButtonType> odp = a.showAndWait();
        if(odp.get() == ButtonType.OK)
        {
            Integer kontaktId = ((Link) e.getSource()).getKontaktId();
            Connection conn = null;
            Statement stmt = null;
            try 
            {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
                stmt = conn.createStatement();
                String sql = "delete from kontakty where id = " + kontaktId;
                stmt.executeUpdate(sql);
                sql = "delete from terminy where kontakt_id = " + kontaktId;
                stmt.executeUpdate(sql);
                wyswietlKontakty();
            }
            catch(Exception ex) 
            {
                Alert al = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                al.showAndWait();
            }
            finally 
            {
                try 
                {
                    if(stmt != null) stmt.close();
                    if(conn != null) conn.close();
                }
                catch(Exception ex) 
                {
                }
            }
        }
    }
    
    public void termin()
    {
        
    }
    
    private void wyswietlKontakty()
    {
        Connection conn = null;
        Statement stmt = null;
        ResultSet wyniki = null;
        try 
        {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
            stmt = conn.createStatement();
            String sql = "select * from kontakty where uzytkownik_id = " + Sesja.getId();
            wyniki = stmt.executeQuery(sql);
            lewy.getChildren().clear();
            VBox vb = new VBox();
            while(wyniki.next())
            {
                HBox wiersz = new HBox();
                Label kontakt = new Label(wyniki.getString("imie") + " " + wyniki.getString("nazwisko"));
                if(wyniki.getString("miasto").length() > 0) kontakt.setText(kontakt.getText() + ", " + wyniki.getString("miasto"));
                if(wyniki.getString("telefon").length() > 0) kontakt.setText(kontakt.getText() + ", tel. " + wyniki.getString("telefon"));
                kontakt.setPadding(new Insets(2, 2, 2, 2));
                wiersz.getChildren().add(kontakt);
                Link edytuj = new Link();
                edytuj.setText("(edytuj)");
                edytuj.setPadding(new Insets(2, 2, 2, 2));
                edytuj.setKontaktId(wyniki.getInt("id"));
                edytuj.setOnMouseClicked(this::edytujKontakt);
                wiersz.getChildren().add(edytuj);
                Link usun = new Link();
                usun.setText("(usun)");
                usun.setPadding(new Insets(2, 2, 2, 2));
                usun.setKontaktId(wyniki.getInt("id"));
                usun.setOnMouseClicked(this::usunKontakt);
                wiersz.getChildren().add(usun);
                vb.getChildren().add(wiersz);
            }
            lewy.getChildren().add(vb);
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
    
    public void pokazNaglowek()
    {
        naglowek.setText(naglowek.getText() + " " + Sesja.getLogin());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        pokazNaglowek();
        wyswietlKontakty();
    }    
    
}
