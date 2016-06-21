package com.ubirouting.mozzservicecommunicator.msgdispatcher;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ubirouting.mozzservicecommunicator.MessageService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Yang Tao on 16/6/20.
 */
public abstract class FindableActivity extends AppCompatActivity implements Findable {

    private final Map<MessageId, MessageConsumeListener> mListenerList = new HashMap<>();
    private final Map<Class<? extends Message>, MessageConsumeListener> mTypeList = new HashMap<>();

    private final long id = System.currentTimeMillis();

    public final void sendMessage(Message msg, MessageConsumeListener l) {
        msg.generateMessageId(this);
        synchronized (mListenerList) {
            mListenerList.put(msg.getMessageId(), l);
        }

        FindableDispatcher.getInstance().register(this, msg);
        MessageService.getInstance().sendMessage(msg);
    }

    public final void registerListener(Class<? extends Message> msgClass, MessageConsumeListener l) {
        synchronized (mTypeList) {
            mTypeList.put(msgClass, l);
        }

        FindableDispatcher.getInstance().register(this, msgClass);
    }


    @Override
    public final long getFindableId() {
        return id;
    }

    @Override
    public final boolean hasBeenDestroyed() {
        return this.isDestroyed();
    }

    @Override
    public final void execute(final Message msg) {

        Log.d("LALA", this.toString() + "," + msg.toString());

        synchronized (mListenerList) {
            Iterator<Map.Entry<MessageId, MessageConsumeListener>> itr = mListenerList.entrySet().iterator();
            while (itr.hasNext()) {
                final Map.Entry<MessageId, MessageConsumeListener> entry = itr.next();
                if (entry.getKey().equals(msg.getMessageId())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            entry.getValue().consume(msg);

                        }
                    });
                    itr.remove();
                    return;
                }
            }
        }


        synchronized (mTypeList) {
            Iterator<Map.Entry<Class<? extends Message>, MessageConsumeListener>> itr2 = mTypeList.entrySet().iterator();
            while (itr2.hasNext()) {
                final Map.Entry<Class<? extends Message>, MessageConsumeListener> entry = itr2.next();

                if (entry.getKey().equals(msg.getClass())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            entry.getValue().consume(msg);
                        }
                    });
                    return;
                }
            }
        }
    }
}
