package org.shaotang.action.mycallback;

public interface FetcherCallback {

    void onData(Data dta) throws Exception;

    void onError(Throwable cause);
}
