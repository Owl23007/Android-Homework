package cn.woyioii.news.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.woyioii.news.Adapter.NewsAdapter;
import cn.woyioii.news.Pojo.NewsItem;
import cn.woyioii.news.R;

public class NewsFragment extends Fragment {

    private RecyclerView recyclerView; // RecyclerView 用于显示新闻列表
    private NewsAdapter adapter; // 适配器用于绑定数据
    private List<NewsItem> newsItems; // 新闻数据列表

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 加载布局文件
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        // 初始化 RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 初始化新闻数据
        newsItems = new ArrayList<>();
        // 添加示例数据
        newsItems.add(new NewsItem("新闻标题 1", "这是新闻描述 1"));
        newsItems.add(new NewsItem("新闻标题 2", "这是新闻描述 2"));
        newsItems.add(new NewsItem("新闻标题 3", "这是新闻描述 3"));

        // 初始化适配器并绑定数据
        adapter = new NewsAdapter(newsItems);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
