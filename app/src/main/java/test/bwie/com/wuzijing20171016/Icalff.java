package test.bwie.com.wuzijing20171016;

import okhttp3.Request;


public interface Icalff<T> {
    public   void Result(T response);
    public void onError(Request request, Exception e);
}
