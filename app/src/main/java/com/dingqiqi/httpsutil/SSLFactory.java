package com.dingqiqi.httpsutil;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
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

    private static String mKeyStoreType = "PKCS12";

    private static String mTrustStoreType = "bks";

    private static String mTLVVersion = "TLSv1";

    static void setKeyStoreType(String mKeyStoreType) {
        SSLFactory.mKeyStoreType = mKeyStoreType;
    }

    static void setTrustStoreType(String mTrustStoreType) {
        SSLFactory.mTrustStoreType = mTrustStoreType;
    }

    static void setTLVVersion(String mTLVVersion) {
        SSLFactory.mTLVVersion = mTLVVersion;
    }

    static SSLSocketFactory getSocketFactory(InputStream ksIn, InputStream tsIn, String clientPsw, String servicePsw) {

        SSLSocketFactory sslSocketFactory = null;

        KeyStore keyStore = null;
        KeyStore trustStore = null;

        try {
            // 服务器端需要验证的客户端证书，其实就是客户端的keystore
            if (ksIn != null) {
                keyStore = KeyStore.getInstance(mKeyStoreType);
                //加载客户端公钥证书
                keyStore.load(ksIn, clientPsw.toCharArray());
                ksIn.close();
            }

            if (tsIn != null) {
                // 客户端信任的服务器端证书
                trustStore = KeyStore.getInstance(mTrustStoreType);
                //加载服务端端私钥证书
                trustStore.load(tsIn, servicePsw.toCharArray());
                tsIn.close();
            }

            //初始化SSLContext
            SSLContext sslContext = SSLContext.getInstance(mTLVVersion);

            KeyManagerFactory keyManagerFactory = null;

            if (keyStore != null) {
                keyManagerFactory = KeyManagerFactory.getInstance("X509");
                keyManagerFactory.init(keyStore, clientPsw.toCharArray());
            }

            TrustManagerFactory trustManagerFactory = null;
            if (trustStore != null) {
                trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(trustStore);
            }

            KeyManager[] KeyManagers = null;
            if (keyManagerFactory != null) {
                KeyManagers = keyManagerFactory.getKeyManagers();
            }

            TrustManager[] trustManager;
            if (trustManagerFactory != null) {
                trustManager = trustManagerFactory.getTrustManagers();
            } else {
                trustManager = new TrustManager[]{new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }};
            }

            sslContext.init(KeyManagers, trustManager, new SecureRandom());

            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sslSocketFactory;
    }

}
