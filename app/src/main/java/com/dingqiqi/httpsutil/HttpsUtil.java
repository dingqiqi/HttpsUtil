package com.dingqiqi.httpsutil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.net.ssl.SSLSocketFactory;

public class HttpsUtil {

    private HttpsUtil() {
    }

    public static HttpsUtil getInstance() {
        return HttpsUtilInstance.mInstance;
    }

    private static class HttpsUtilInstance {
        private static HttpsUtil mInstance = new HttpsUtil();
    }

    /**
     * 信任全部Https请求
     *
     * @return SSLSocketFactory
     */
    public SSLSocketFactory getSocketFactory() {
        return SSLFactory.getSocketFactory();
    }

    /**
     * @param ksKey 客户端私钥
     * @param ksPsw 客户端私钥密码
     * @param tsKey 服务端公钥(或者转成bks格式私钥)
     * @param tsPsw cer公钥传null(服务端私钥证书密码)
     * @return SSLSocketFactory
     */
    public SSLSocketFactory getSocketFactoryForByte(byte[] ksKey, String ksPsw, byte[] tsKey, String tsPsw) {
        ByteArrayInputStream ksIn = null;
        if (ksKey != null) {
            ksIn = new ByteArrayInputStream(ksKey);
        }

        ByteArrayInputStream tsIn = null;
        if (tsKey != null) {
            tsIn = new ByteArrayInputStream(tsKey);
        }

        return getSocketFactory(ksIn, ksPsw, tsIn, tsPsw);
    }

    /**
     * @param ksIn  客户端私钥
     * @param ksPsw 私钥密码
     * @param tsIn  服务端公钥或者转成bks格式的私钥
     * @param tsPsw cer公钥传null(私钥证书密码)
     * @return SSLSocketFactory
     */
    public SSLSocketFactory getSocketFactory(InputStream ksIn, String ksPsw, InputStream tsIn,
                                             String tsPsw) {
        return SSLFactory.getSocketFactory(ksIn, ksPsw,
                tsIn, tsPsw);
    }


}
