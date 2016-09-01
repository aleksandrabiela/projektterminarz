
package terminarz;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Terminarz extends Application 
{
    
    private void checkDatabase()   
    {
        File f = new File("baza.db");
        if(!f.exists()) 
        {
            Connection conn = null;
            Statement stmt = null;
            try 
            {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
                stmt = conn.createStatement();
                String sql = "create table uzytkownicy(id integer primary key autoincrement, login text, haslo text)";
                stmt.executeUpdate(sql);
                sql = "create table kontakty(id integer primary key autoincrement, imie text, nazwisko text, miasto text, telefon text, uzytkownik_id integer, foreign key(uzytkownik_id) references uzytkownicy(id))";
                stmt.executeUpdate(sql);
                sql = "create table terminy(id integer primary key autoincrement, dzien text, uzytkownik_id integer, kontakt_id integer, foreign key(uzytkownik_id) references uzytkownicy(id), foreign key(kontakt_id) references kontakty(id))";
                stmt.executeUpdate(sql);
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
    
    @Override
    public void start(Stage stage) throws Exception 
    {
        checkDatabase();
        Parent root = FXMLLoader.load(getClass().getResource("Logowanie.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
    
}
