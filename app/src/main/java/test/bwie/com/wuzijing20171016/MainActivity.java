package test.bwie.com.wuzijing20171016;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.util.List;

import okhttp3.Request;
import test.bwie.com.wuzijing20171016.bean.Music;
import test.bwie.com.wuzijing20171016.bean.News;

public class MainActivity extends AppCompatActivity {
//    List<Music.SongListBean> song_list = new ArrayList<Music.SongListBean>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private String path = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billList&type=1&size=10&offset=0";

    private String path1 = "http://apiv3.yangkeduo.com/v5/newlist?page=&size=20";
    private int page = 1;
    private ImageLoader imageLoader;
    private final int Type1 = 1;
    private final int Type2 = 2;
    List<News.GoodsListBean> goods_list;
    private MyAdapter myAdapter;
    Handler handler = new Handler();
    List<Music.SongListBean> song_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperw);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if(lastVisibleItemPosition==song_list.size()-1){


                }


            }
        });



        getDates();
    }

    public void getDates() {

        Net.getNet().GetDatas(path, Music.class, new Icalff() {
            @Override
            public void Result(final Object response) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Music music = new Gson().fromJson(response.toString(), Music.class);

                        song_list.addAll(music.getSong_list());
                        myAdapter = new MyAdapter(song_list);
                        recyclerView.setAdapter(myAdapter);
                    }
                });

            }

            @Override
            public void onError(Request request, Exception e) {

            }
        });


    }

    public class MyAdapter extends RecyclerView.Adapter {
        List<Music.SongListBean> song_list;
        //        List<News.GoodsListBean> goods_list;
        private MyViewHolder myviewholder;
        private final DisplayImageOptions options;
        private View inflate;

        public MyAdapter(List<Music.SongListBean> song_list) {
            this.song_list = song_list;
            imageLoader = ImageLoader.getInstance();
            File file = new File(Environment.getExternalStorageDirectory(), "Bwei");
            if (!file.exists())
                file.mkdirs();

            ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(MainActivity.this)
                    .diskCache(new UnlimitedDiskCache(file))
                    .build();

            imageLoader.init(configuration);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_launcher)
                    .cacheOnDisk(true)
                    .build();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.item1, parent, false);
            return new MyViewHolder(view2);
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {


            TextView text_item1;
            ImageView image_item1;


            public MyViewHolder(View itemView) {
                super(itemView);
                text_item1 = itemView.findViewById(R.id.text_item1);
                image_item1 = itemView.findViewById(R.id.image_item1);
            }
        }


        @Override
        public int getItemCount() {
            return song_list != null ? song_list.size() : 0;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            MyViewHolder holder1 = (MyViewHolder) holder;
            holder1.text_item1.setText(song_list.get(position).getArtist_name());
            getimage(song_list.get(position).getPic_big(), holder1.image_item1);
        }
    }

    public void getimage(String path, ImageView imageView) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .build();
        ImageLoader.getInstance().displayImage(path, imageView, options);


    }


}
