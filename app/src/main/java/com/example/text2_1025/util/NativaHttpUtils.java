package com.example.text2_1025.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * Created by 张金瑞 on 2017/11/6.
 */

public class NativaHttpUtils extends AsyncTask<Void,Void,Message>{

    private static NativaHttpUtils httpUtils;
    private Context context;
    private HttpMethod method;
    private String url;
    private Map<String,String> map;
    private NativaHttpCallback httpCallback;

    private NativaHttpUtils (){
        this.context = context;

    }


    public static NativaHttpUtils getInstance(){

        if (httpUtils==null){

            synchronized (NativaHttpUtils.class){

                if (httpUtils==null){
                    httpUtils = new NativaHttpUtils();
                }
            }

        }

        return httpUtils;

    }

    public Object getRequest(String urlSite, Map<String,Object> params,NativaHttpCallback nativaHttpCallback){


            String path = urlSite;
            String newPath="";


        if (params!=null&&!params.isEmpty()){

            path+=("?");
            Set<Map.Entry<String, Object>> entries = params.entrySet();
            for (Map.Entry<String, Object> map : entries) {
                path+=(map.getKey() + "=" + map.getValue()).concat("&");
            }
            //把拼接好的网站去掉最后一个"&"符号
            newPath = path.substring(0, path.length() - 1);

        }

        try {
            URL url = new URL(newPath ==""?urlSite:newPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream inputStream = conn.getInputStream();

            byte[]by=new byte[1024];
            int len=0;
            StringBuffer sb=new StringBuffer();

            while((len=inputStream.read(by))!=-1) {
                String ss = new String(by, 0, len);
                sb.append(ss);
            }
            String request = sb.toString();



            return request;

            } catch (MalformedURLException e) {

            httpCallback.Faield(e);

        } catch (IOException e) {
            httpCallback.Faield(e);
        }


        return null;
    }


    @Override
    protected Message doInBackground(Void... params) {



        return null;
    }


    public void cancle(){

        cancel(true);
        context = null;
        httpCallback = null;
        url = null;

        if (map!=null){
            map.clear();
        }
        map = null;

    }


    /**
     * https访问 没有经过CA认证的   通过根证书来进行https的访问
     * @param context  上下文
     * @param urlPath   网址
     * @param httpCallback 自定义接口回调
     */
    public void getCertificateSafe(final Context context, final String urlPath, final NativaHttpCallback httpCallback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlPath);
                    HttpsURLConnection conn= (HttpsURLConnection) url.openConnection();

                    //"TLS"是ssl的第二版的加密方式
                    SSLContext tls = SSLContext.getInstance("TLS");

                    MyX509TrustManager myX509TrustManager = new MyX509TrustManager(getX509Certificate(context));
                    TrustManager[] trustManagers = {myX509TrustManager};
                    tls.init(null,trustManagers, new SecureRandom());
                    //ssl工厂
                    SSLSocketFactory factory  = tls.getSocketFactory();
                    //主机名称校验器
                    conn.setHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            if (hostname.equals("kyfw.12306.cn")){
                                return true;
                            }else {
                                return false;
                            }

                        }
                    });

                    conn.setSSLSocketFactory(factory);

                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.connect();
                    InputStream inputStream = conn.getInputStream();

                    StringBuilder sb=new StringBuilder();
                    int flag;
                    byte[] buf=new byte[1024];
                    while((flag=inputStream.read(buf))!=-1){
                        sb.append(new String(buf,0,flag));
                    }
                    String s = sb.toString();

                    httpCallback.Success(s);



                } catch (IOException e) {
                    e.printStackTrace();
                    httpCallback.Faield(e);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    X509Certificate getX509Certificate(Context context) throws IOException, CertificateException {
        InputStream in = context.getAssets().open("srca.cer");
        CertificateFactory instance = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate)instance.generateCertificate(in);

        return certificate;


    }
}
