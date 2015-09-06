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
public class AdminActionFactory implements ActionFactory {

    public static AdminActionFactory getInstance() {
        return AdminActionFactoryHolder.INSTANCE;
    }

    private AdminActionFactory() {
    }

    @Override
    public Action getActionByName(String actionName) {
        switch (actionName) {
            case "register":
                return new RegisterAction();
            case "deleteUser":
                return new DeleteUserAction();
            case "changeUser":
                return new ChangeUserAction();
            default:
                throw new IllegalArgumentException("Invalid Action: " + actionName);
        }
    }

    private static class AdminActionFactoryHolder {

        private static final AdminActionFactory INSTANCE = new AdminActionFactory();
    }
}
