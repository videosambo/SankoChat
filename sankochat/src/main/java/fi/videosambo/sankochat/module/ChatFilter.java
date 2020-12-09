package fi.videosambo.sankochat.module;

import fi.videosambo.sankochat.Message;
import org.pf4j.ExtensionPoint;

public interface ChatFilter extends ExtensionPoint {

    //This is interface for module extension to use

    public void init();
    public Message filterMessage(Message msg);
}
