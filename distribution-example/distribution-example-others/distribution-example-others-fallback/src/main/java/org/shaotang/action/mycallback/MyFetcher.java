package org.shaotang.action.mycallback;

public class MyFetcher implements Fetcher {

    private Data data;

    public MyFetcher(Data data) {
        this.data = data;
    }

    @Override
    public void fetchData(FetcherCallback callback) {

        try {
            callback.onData(data);
        } catch (Exception e) {
            callback.onError(e);
        }
    }
}
