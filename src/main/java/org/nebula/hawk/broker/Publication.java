package org.nebula.hawk.broker;

import org.nebula.hawk.Message;
import org.nebula.hawk.Topic;

public class Publication implements Command{
    private Message message;
    private Topic topic;
}
