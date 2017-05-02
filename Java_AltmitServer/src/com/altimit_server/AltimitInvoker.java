package com.altimit_server;

import java.util.*;

/**
 * Created by Malcolm on 5/1/2017.
 */
public class AltimitInvoker implements Runnable {
    private List<Object> mSentMessage;

    public AltimitInvoker(List<Object> _sentMessage) {
        mSentMessage = new ArrayList<>();
        mSentMessage.addAll(_sentMessage);
    }

    public void InvokeMessage(List<Object> sentMessage){
        String methodName = "";
        try {
            methodName = (String) sentMessage.get(0);
        }catch(ClassCastException e){
            System.out.println(e);
        }
        sentMessage.remove(0);

        if(methodName != "") {
            AltimitMethod.CallAltimitMethod(methodName, sentMessage.toArray());
        }
    }

    @Override
    public void run() {
        InvokeMessage(mSentMessage);
    }
}
