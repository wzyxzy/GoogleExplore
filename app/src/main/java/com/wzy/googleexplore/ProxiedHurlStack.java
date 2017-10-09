package com.wzy.googleexplore;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

/**
 * Created by zy on 2016/8/4.
 * 代理服务器
 */
public class ProxiedHurlStack extends HurlStack {

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {

        // Start the connection by specifying a proxy server
        Proxy proxy = new Proxy(Proxy.Type.HTTP,
                InetSocketAddress.createUnresolved("192.168.199.192", 1080));//the proxy server(Can be your laptop ip or company proxy)
        HttpURLConnection returnThis = (HttpURLConnection) url
                .openConnection(proxy);

        return returnThis;
    }
}
