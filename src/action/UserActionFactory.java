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
public class UserActionFactory implements ActionFactory {

    public static UserActionFactory getInstance() {
        return UserActionFactoryHolder.INSTANCE;
    }

    private UserActionFactory() {
    }

    @Override
    public Action getActionByName(String actionName) {
        switch (actionName) {
            case "addTicket":
                return new AddTicketAction();
            case "deleteTicket":
                return new DeleteTicketAction();
            case "changeTicket":
                return new ChangeTicketAction();
            case "logout":
                return new LogoutAction();
            case "register":
                return new RegisterAction();
            case "deleteUser_from_account":
                return new DeleteUserAction();
            case "changeUser_from_account":
                return new ChangeUserAction();
            case "addComponent":
                return new AddComponentAction();
            case "changeComponent":
                return new ChangeComponentAction();
            case "deleteComponent":
                return new DeleteComponentAction();
            case "addComment":
                return new AddCommentAction();
            case "deleteComment":
                return new DeleteCommentAction();
            case "changeComment":
                return new ChangeCommentAction();
            case "addSprint":
                return new AddSprintAction();
            case "deleteSprint":
                return new DeleteSprintAction();
            case "changeSprint":
                return new ChangeSprintAction();
            case "startSprint":
                return new StartSprintAction();
            case "endSprint":
                return new EndSprintAction();
            default:
                return null;
        }
    }

    private static class UserActionFactoryHolder {

        private static final UserActionFactory INSTANCE = new UserActionFactory();
    }
}
