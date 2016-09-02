/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terminarz;

/**
 *
 * @author Aleksandra
 */
public class Sesja {

    private static Integer id;
    private static String login;
    private static Integer kontaktId;
    private static Integer terminId;

    /**
     * @return the id
     */
    public static Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public static void setId(Integer id) {
        Sesja.id = id;
    }

    /**
     * @return the login
     */
    public static String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public static void setLogin(String login) {
        Sesja.login = login;
    }
    
    public static void czyscSesje() {
        Sesja.id = null;
        Sesja.login = null;
        Sesja.kontaktId = null;
    }

    /**
     * @return the kontaktId
     */
    public static Integer getKontaktId() {
        return kontaktId;
    }

    /**
     * @param aKontaktId the kontaktId to set
     */
    public static void setKontaktId(Integer aKontaktId) {
        kontaktId = aKontaktId;
    }
    
    public static void czyscKontakt() {
        Sesja.kontaktId = null;
    }

    /**
     * @return the terminId
     */
    public static Integer getTerminId() {
        return terminId;
    }

    /**
     * @param aTerminId the terminId to set
     */
    public static void setTerminId(Integer aTerminId) {
        terminId = aTerminId;
    }
    
    public static void czyscTermin() {
        terminId = null;
    }
}
