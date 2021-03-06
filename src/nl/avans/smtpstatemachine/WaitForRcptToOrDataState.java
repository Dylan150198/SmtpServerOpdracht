package nl.avans.smtpstatemachine;

import nl.avans.SmtpContext;

public class WaitForRcptToOrDataState implements SmtpStateInf {
    SmtpContext context;

    public WaitForRcptToOrDataState(SmtpContext context) {
        this.context = context;
    }

    @Override
    public void Handle(String data) {
        if(data.toUpperCase().startsWith("RCPT TO ")) {
            context.SendData("250 Ok ");
            context.AddRecipient(data.substring(8));
            context.SetNewState(new WaitForRcptToOrDataState(context));
            return;
        }
        if(data.toUpperCase().startsWith("DATA")) {
            context.SendData("354 End data with <CR><LF>.<CR><LF>");
            context.SetNewState(new ReceivingDataState(context));
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
