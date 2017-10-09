package com.wzy.googleexplore;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private WebView result;
    private SwipeRefreshLayout swipe_container;
    private String url;
    private EditText toolbar_edittext;
    private ImageView toolbar_image;
    private Toolbar toolbar;
    private ProgressBar index_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initData() {
        //        RequestQueue mQueue = Volley.newRequestQueue(this, new ProxiedHurlStack());
        RequestQueue mQueue = Volley.newRequestQueue(this);
        //如果用https连接，可以用下面的代码
//        HTTPSTrustManager.allowAllSSL();
//        String url = URL + content.getText();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                gotoWeb(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(stringRequest);
    }

    private void gotoWeb(String response) {
        result.loadDataWithBaseURL("https://www.google.com.hk", response, "text/html", "utf-8", null);
        if (swipe_container.isRefreshing())
            swipe_container.setRefreshing(false);
    }

    private void initView() {
        result = (WebView) findViewById(R.id.result);
//        result.getSettings().setJavaScriptEnabled(false);
//        result.getSettings().setSupportZoom(false);
//        result.getSettings().setBuiltInZoomControls(false);
//        result.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        result.getSettings().setDefaultFontSize(18);
        result.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                index_progressBar.setProgress(newProgress);
                if (newProgress >= 100) {
                    index_progressBar.setVisibility(View.GONE);
                }
            }
        });
        result.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                String newurl = request.getUrl().toString();
                if (url.startsWith("https://www.google.com.hk") || url.isEmpty()) {
//                    initWeb(request.getUrl().toString().replace("https://www.google.com.hk","http://192.168.199.192:8080/GotoGoogle?GoogleRoot="));
//                    view.loadDataWithBaseURL("http://192.168.199.192:8080/GotoGoogle?GoogleRoot=", response, "text/html", "utf-8", null);
                    Toast.makeText(getApplicationContext(), "该版本仅支持谷歌搜索，更多功能请购买pro版", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    try {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        return true;
                    } catch (ActivityNotFoundException e) {
                        String url1 = url.replace("intent", "https");
                        Uri uri = Uri.parse(url1);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        return true;
                    }


                }

            }
        });
        result.getSettings().setJavaScriptEnabled(true);
        result.getSettings().setDomStorageEnabled(true);

        swipe_container = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        swipe_container.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright), getResources().getColor(R.color.holo_green_light), getResources().getColor(R.color.holo_orange_light), getResources().getColor(R.color.holo_red_light));
        toolbar_edittext = (EditText) findViewById(R.id.toolbar_edittext);
        toolbar_image = (ImageView) findViewById(R.id.toolbar_image);
        toolbar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edittext = toolbar_edittext.getText().toString().trim();
                if (TextUtils.isEmpty(edittext)) {
                    Toast.makeText(getApplicationContext(), "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), "正在搜索关键词：" + edittext, Toast.LENGTH_SHORT).show();
                    index_progressBar.setVisibility(View.VISIBLE);
                    url = "http://cncncn.tk:8080/GoogleSearch?search_content=" + edittext;
                    initData();
                }

            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.googlemenu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.buy:
                        result.loadUrl("http://cncncn.tk:8080/Buy.html");
                        break;
                    case R.id.about:
                        result.loadUrl("http://cncncn.tk:8080/AboutUs.html");
                        break;
                }
                return true;
            }
        });
        index_progressBar = (ProgressBar) findViewById(R.id.index_progressBar);
    }

    private void submit() {
        // validate
        String edittext = toolbar_edittext.getText().toString().trim();
        if (TextUtils.isEmpty(edittext)) {
            Toast.makeText(this, "请输入要搜索的内容", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
