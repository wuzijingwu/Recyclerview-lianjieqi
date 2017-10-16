package test.bwie.com.wuzijing20171016;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//需要加lcalff类和LoggingInterceptor（拦截器）
public class Net {
    //封装一个okhttp3  10分呐
    private static final String AA = "User-Agent";

    private static Net net;
    private  static OkHttpClient client; //创建对象
    private final Gson gson;

    //单利模式封装
    public static Net getNet(){
        if(net == null){
            synchronized (Net.class){
                if(net == null){
                    net = new Net();
                }
            }
        }
        return  net;
    }

    //构造
    public Net(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
       // client = new OkHttpClient();


        builder.readTimeout(5, TimeUnit.SECONDS);//延迟
        builder.writeTimeout(5,TimeUnit.SECONDS);
        builder.connectTimeout(5,TimeUnit.SECONDS);

        //可以添加拦截器
        builder.addInterceptor(new LoggingInterceptor());
        client = builder.build();
  //      client.newBuilder().addNetworkInterceptor(new LoggingInterceptor());
        gson = new Gson();
    }

    /**
     *   get  qinqiu yi bu
     */

    public void GetDatas(String url, final Class c, final Icalff icalff) {



        final Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // icalff.Result(new Gson().fromJson(response.body().string(),c));
//                if(response.isSuccessful()){
//                    icalff.Result(response.body().string());
//                }else{
//                    icalff.Result("失败");
//                }

                icalff.Result(response.body().string());
            }



        });
    }

}

