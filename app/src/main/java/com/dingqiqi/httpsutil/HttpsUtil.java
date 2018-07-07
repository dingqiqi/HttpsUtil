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
     * @return SSLSocketFactory
     */
    public SSLSocketFactory getSocketFactory() {

        return getSocketFactory(null, null, null, null);
    }

    /**
     * @param clientKey     服务器端需要验证的客户端证书，其实就是客户端的keystore
     * @param trustStoreKey 客户端信任的服务器端证书
     * @param clientPsw     客户端的keystore密码
     * @param trustStorePsw 服务端证书密码
     * @return SSLSocketFactory
     */
    public SSLSocketFactory getSocketFactoryForByte(byte[] clientKey, byte[] trustStoreKey,
                                                    String clientPsw, String trustStorePsw) {
        ByteArrayInputStream clientInput = null;
        if (clientKey != null) {
            clientInput = new ByteArrayInputStream(clientKey);
        }

        ByteArrayInputStream trustStoreInput = null;
        if (trustStoreKey != null) {
            trustStoreInput = new ByteArrayInputStream(trustStoreKey);
        }

        return getSocketFactory(clientInput, trustStoreInput, clientPsw, trustStorePsw);
    }

    /**
     * @param clientKey     服务器端需要验证的客户端证书，其实就是客户端的keystore
     * @param trustStoreKey 客户端信任的服务器端证书
     * @param clientPsw     客户端的keystore密码
     * @param trustStorePsw 服务端证书密码
     * @return SSLSocketFactory
     */
    public SSLSocketFactory getSocketFactoryForString(String clientKey, String trustStoreKey,
                                                      String clientPsw, String trustStorePsw) {
        ByteArrayInputStream clientInput = null;
        if (clientKey != null) {
            clientInput = new ByteArrayInputStream(clientKey.getBytes());
        }

        ByteArrayInputStream trustStoreInput = null;
        if (trustStoreKey != null) {
            trustStoreInput = new ByteArrayInputStream(trustStoreKey.getBytes());
        }

        return getSocketFactory(clientInput, trustStoreInput, clientPsw, trustStorePsw);
    }

    /**
     * @param clientInput     服务器端需要验证的客户端证书，其实就是客户端的keystore
     * @param trustStoreInput 客户端信任的服务器端证书
     * @param clientPsw       客户端的keystore密码
     * @param trustStorePsw   服务端证书密码
     * @return SSLSocketFactory
     */
    public SSLSocketFactory getSocketFactory(InputStream clientInput, InputStream trustStoreInput,
                                             String clientPsw, String trustStorePsw) {
        return SSLFactory.getSocketFactory(clientInput, trustStoreInput,
                clientPsw, trustStorePsw);
    }


    public HttpsUtil setKeyStoreType(String mKeyStoreType) {
        SSLFactory.setKeyStoreType(mKeyStoreType);

        return HttpsUtilInstance.mInstance;
    }

    public HttpsUtil setTrustStoreType(String mTrustStoreType) {
        SSLFactory.setTrustStoreType(mTrustStoreType);

        return HttpsUtilInstance.mInstance;
    }

    public HttpsUtil setTLVVersion(String mTLVVersion) {
        SSLFactory.setTLVVersion(mTLVVersion);

        return HttpsUtilInstance.mInstance;
    }

}
