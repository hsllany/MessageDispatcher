package com.ubirouting.instantmsg.msgs;

import com.ubirouting.instantmsg.msgdispatcher.Findable;

public abstract class MessageImp implements Message {

    MessageId mId;

    public void generateMessageId(Findable findable) {
        mId = new MessageId(findable);
    }

    @Override
    public String toString() {
        return "[message:id=" + mId.toString() + "]";
    }

    public MessageId getMessageId() {
        return mId;
    }
}
