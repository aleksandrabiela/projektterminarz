/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terminarz;

import javafx.scene.control.Label;

/**
 *
 * @author Aleksandra
 */
public class Link extends Label {
    private Integer kontaktId;

    /**
     * @return the kontaktId
     */
    public Integer getKontaktId() {
        return kontaktId;
    }

    /**
     * @param kontaktId the kontaktId to set
     */
    public void setKontaktId(Integer kontaktId) {
        this.kontaktId = kontaktId;
    }
}
