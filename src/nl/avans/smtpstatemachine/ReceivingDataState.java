package nl.avans.smtpstatemachine;
import nl.avans.SmtpContext;
public class ReceivingDataState implements SmtpStateInf {
    SmtpContext context;
    boolean crReceived; // What is this?
    boolean dotReceived = false;
    public ReceivingDataState(SmtpContext context) {
        this.context = context;
    }

    @Override
    public void Handle(String data) {
        /*if (dotReceived = true)
        {
            context.SetNewState(new WaitForMailFromState(context));
        }*/
        context.AddTextToBody(data);
        if (data.toUpperCase().endsWith("."))  {
            dotReceived = true;
            context.SendData("250 Ok: queued as 12345");
            context.SetNewState(new WaitForMailFromState(context));
            return;
        }
        /*else
        {
            dotReceived = false;
            context.SetNewState(this);
        }*/
        if (data.toUpperCase().startsWith("QUIT")) {
            context.SendData("221 Bye");
            context.DisconnectSocket();
        }
        context.SendData("...");
    }
}
