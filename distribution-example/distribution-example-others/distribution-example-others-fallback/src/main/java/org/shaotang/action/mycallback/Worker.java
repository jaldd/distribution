package org.shaotang.action.mycallback;

public class Worker {

    public void doWork() {

        Fetcher fetcher = new MyFetcher(new Data(0, 1));
        fetcher.fetchData(new FetcherCallback() {

            @Override
            public void onData(Data data) throws Exception {
                System.out.println(data);
            }

            @Override
            public void onError(Throwable cause) {
                System.out.println("cause:" + cause);
                cause.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {

        Worker worker = new Worker();
        worker.doWork();
    }
}
