
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
                wyswietlTerminy();
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
    
    @FXML public void termin()
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
            if(wyniki.next())
            {
                try
                {
                    Parent root = FXMLLoader.load(getClass().getResource("Terminy.fxml"));
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
                Alert a = new Alert(AlertType.ERROR, "Musisz posiadać jakieś kontakty, aby móc tworzyć terminy");
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
    
    private void usunTermin(MouseEvent e)
    {
        Alert a = new Alert(AlertType.CONFIRMATION, "Czy na pewno usunąć wskazany termin?");
        Optional<ButtonType> odp = a.showAndWait();
        if(odp.get() == ButtonType.OK)
        {
            Integer id = ((Link) e.getSource()).getTerminId();
            Connection conn = null;
            Statement stmt = null;
            try 
            {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
                stmt = conn.createStatement();
                String sql = "delete from terminy where id = " + id;
                stmt.executeUpdate(sql);
                wyswietlTerminy();
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
    
    private void edytujTermin(MouseEvent e)
    {
        Integer id = ((Link) e.getSource()).getTerminId();
        Sesja.setTerminId(id);
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("Terminy.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            Stage thisStage = (Stage) okno.getScene().getWindow();
            thisStage.close();
        }
        catch(Exception ex)
        {

        }
    }
    
    private void wyswietlTerminy()
    {
        Connection conn = null;
        Statement stmt = null;
        ResultSet wyniki = null;
        try 
        {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
            stmt = conn.createStatement();
            String sql = "select terminy.id as id, dzien, imie, nazwisko from terminy join kontakty on kontakt_id = kontakty.id";
            wyniki = stmt.executeQuery(sql);
            prawy.getChildren().clear();
            VBox vb = new VBox();
            while(wyniki.next())
            {
                HBox wiersz = new HBox();
                Label termin = new Label(wyniki.getString("imie") + " " + wyniki.getString("nazwisko") + ", " + wyniki.getString("dzien"));
                termin.setPadding(new Insets(2, 2, 2, 2));
                wiersz.getChildren().add(termin);
                Link edytuj = new Link();
                edytuj.setPadding(new Insets(2, 2, 2, 2));
                edytuj.setText("(edytuj)");
                edytuj.setTerminId(wyniki.getInt("id"));
                edytuj.setOnMouseClicked(this::edytujTermin);
                wiersz.getChildren().add(edytuj);
                Link usun = new Link();
                usun.setPadding(new Insets(2, 2, 2, 2));
                usun.setText("(usun)");
                usun.setTerminId(wyniki.getInt("id"));
                usun.setOnMouseClicked(this::usunTermin);
                wiersz.getChildren().add(usun);
                vb.getChildren().add(wiersz);
            }
            prawy.getChildren().add(vb);
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
        wyswietlTerminy();
    }    
    
}
