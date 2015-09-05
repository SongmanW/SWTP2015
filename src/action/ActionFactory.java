/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

/**
 *
 * @author erfier
 */
public interface ActionFactory {

    /**
     * @param actionName name of the action that should be searched for
     * @return action correponding to the provided name
     */
    public Action getActionByName(String actionName);

}
