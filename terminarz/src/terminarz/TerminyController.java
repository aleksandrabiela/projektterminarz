/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Aleksandra
 */
public class TerminyController implements Initializable {

    @FXML AnchorPane okno;
    @FXML TextField dzien;
    @FXML ComboBox osoba;
    @FXML Label naglowek;
    
    @FXML public void zapisz()
    {
        if(Sesja.getTerminId() == null) zapiszNowy();
        else nadpisz();
    }
    
    private Integer pobierzIdKontaktu()
    {
        Integer id = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet wyniki = null;
        try 
        {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
            stmt = conn.createStatement();
            String sql = "select id from kontakty where imie || ' ' || nazwisko = '" + (String) osoba.getValue() + "' and uzytkownik_id = " + Sesja.getId();
            wyniki = stmt.executeQuery(sql);
            wyniki.next();
            id =  wyniki.getInt("id");
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
        return id;
    }
    
    private void zapiszNowy()
    {
        if(dzien.getText().length() == 0 )
        {
            Alert a = new Alert(Alert.AlertType.ERROR, "podaj datę");
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
                String sql = "insert into terminy values(null, '" + dzien.getText() + "', " + Sesja.getId() + ", " + pobierzIdKontaktu() + ")";
                stmt.executeUpdate(sql);
                Alert a = new Alert(Alert.AlertType.INFORMATION, "zapisano termin");
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
        if(dzien.getText().length() == 0)
        {
            Alert a = new Alert(AlertType.ERROR, "podaj datę");
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
                String sql = "update terminy set dzien = '" + dzien.getText() + "', kontakt_id = " + pobierzIdKontaktu() + " where id = " + Sesja.getTerminId();
                stmt.executeUpdate(sql);
                Alert a = new Alert(AlertType.INFORMATION, "termin został zapisany");
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
    
    
    @FXML public void anuluj()
    {
        Sesja.czyscTermin();
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
    
    private void wypelnijListe()
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
            wyniki.next();
            osoba.getItems().add(wyniki.getString("imie") + " " + wyniki.getString("nazwisko"));
            osoba.setValue(wyniki.getString("imie") + " " + wyniki.getString("nazwisko"));
            while(wyniki.next())
            {
                osoba.getItems().add(wyniki.getString("imie") + " " + wyniki.getString("nazwisko"));
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        wypelnijListe();
        if(Sesja.getTerminId() != null)
        {
            Connection conn = null;
            Statement stmt = null;
            ResultSet wyniki = null;
            try 
            {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
                stmt = conn.createStatement();
                String sql = "select dzien, imie, nazwisko from terminy join kontakty on kontakt_id = kontakty.id where terminy.id = " + Sesja.getTerminId();
                wyniki = stmt.executeQuery(sql);
                wyniki.next();
                dzien.setText(wyniki.getString("dzien"));
                osoba.setValue(wyniki.getString("imie") + " " + wyniki.getString("nazwisko"));
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
    
}
