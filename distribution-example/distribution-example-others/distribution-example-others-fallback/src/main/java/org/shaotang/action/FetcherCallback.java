package org.shaotang.action;

public interface FetcherCallback {

    void onData(Data dta) throws Exception;

    void onError(Throwable cause);
}
