package nl.avans.smtpstatemachine;

import nl.avans.SmtpContext;

public class WaitForMailFromState implements SmtpStateInf {
    SmtpContext context;

    public WaitForMailFromState(SmtpContext context) {
        this.context=context;
    }

    @Override
    public void Handle(String data) {
        if(data.toUpperCase().startsWith("MAIL FROM ")) {
            context.SetMailFrom(data.substring(10));
            context.SendData("250 Ok ");
            context.SetNewState(new WaitForRcptToState(context));
            return;
        }
        if(data.toUpperCase().startsWith("QUIT")) {
            context.SendData("221 Bye");
            context.DisconnectSocket();
            return;
        }
        context.SendData("503 Error...");
    }
}
