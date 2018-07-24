package com.dingqiqi.httpsutil;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Https双向认证
 * Created by dingqq on 2018/4/16.
 */

final class SSLFactory {

    /**
     * SSLSocketFactory 信任全部https
     *
     * @return SSLSocketFactory
     */
    @SuppressLint("TrustAllX509TrustManager")
    public static SSLSocketFactory getSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.1");

            TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};

            //初始化SSLContext
            sslContext.init(null, trustManagers, new SecureRandom());

            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sslSocketFactory;
    }


    /**
     * SSLSocketFactory 客户端私钥  服务端公钥
     *
     * @param ksIn  客户端私钥流
     * @param ksPsw 私钥密码
     * @param tsIn  服务端公钥
     * @return SSLSocketFactory
     */
    public static SSLSocketFactory getSocketFactory(InputStream ksIn, String ksPsw, InputStream tsIn) {
        return getSocketFactory(ksIn, ksPsw, tsIn, null);
    }

    /**
     * SSLSocketFactory 客户端私钥  服务端私钥
     *
     * @param ksIn  客户端私钥流
     * @param ksPsw 私钥密码
     * @param tsIn  服务端私钥(由公钥转的)
     * @param tsPsw 服务端私钥密码
     * @return SSLSocketFactory
     */
    public static SSLSocketFactory getSocketFactory(InputStream ksIn, String ksPsw, InputStream tsIn, String tsPsw) {
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.1");

            TrustManager[] trustManagers;

            if (TextUtils.isEmpty(tsPsw)) {
                trustManagers = createTrustManager(tsIn);
            } else {
                trustManagers = createTrustManager(tsIn, tsPsw);
            }

            //初始化SSLContext
            sslContext.init(createKeyManagers(ksIn, ksPsw), trustManagers, new SecureRandom());

            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sslSocketFactory;
    }

    /**
     * 加载客户端私钥
     *
     * @param ksInputStream 私钥文件流
     * @param psw           密码
     * @return KeyManager
     */
    private static KeyManager[] createKeyManagers(InputStream ksInputStream, String psw) {
        char[] pswChars = psw.toCharArray();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            keyStore.load(ksInputStream, pswChars);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");

            keyManagerFactory.init(keyStore, pswChars);

            return keyManagerFactory.getKeyManagers();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ksInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 加载服务端公钥
     *
     * @param tsInputStream 公钥流
     * @return TrustManager
     */
    private static TrustManager[] createTrustManager(InputStream tsInputStream) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //加载客户端公钥证书
            trustStore.load(null);

            trustStore.setCertificateEntry("0", certificateFactory.generateCertificate(tsInputStream));

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(trustStore);

            return trustManagerFactory.getTrustManagers();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                tsInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 加载服务端密钥 (公钥转私钥,常用bks格式)
     *
     * @param tsInputStream 公钥流
     * @param psw           密码
     * @return TrustManager
     */
    private static TrustManager[] createTrustManager(InputStream tsInputStream, String psw) {
        try {
            KeyStore trustStore = KeyStore.getInstance("bks");

            trustStore.load(tsInputStream, psw.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(trustStore);

            return trustManagerFactory.getTrustManagers();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                tsInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
