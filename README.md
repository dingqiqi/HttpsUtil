# HttpsUtil

[![](https://jitpack.io/v/dingqiqi/HttpsUtil.svg)](https://jitpack.io/#dingqiqi/HttpsUtil)

一个Https封装库

包含忽略认证以及Https双向认证

引用方式

allprojects {

    repositories {
    
        //配置 JitPack 插件的仓库地址
        
        maven { url "https://jitpack.io" }
    }
    
}

dependencies {

    compile 'com.github.dingqiqi:HttpsUtil:v1.4'
    
}

  使用方式

  //设置TLS版本

  HttpsUtil.getInstance().setTLSVersion("TLSv1");


  //设置客户端信任证书类型 （括号就是默认的类型）
  
  HttpsUtil.getInstance().setKeyStoreType("PKCS12");

  //设置服务端信任证书类型 （括号里的值就是默认的类型）
  
  HttpsUtil.getInstance().setTrustStoreType("bks");

  //获取信任全部的Https 的SSLSocketFactory
  
  SSLSocketFactory SSLSocketFactory = HttpsUtil.getInstance().getSocketFactory();

  //双向认证
  try {
  
      InputStream inks = getAssets().open("client.p12");
      
      InputStream ints = getAssets().open("service.bks");

      HttpsUtil.getInstance().getSocketFactory(inks,ints,"123456","123456");
      
  } catch (IOException e) {
      e.printStackTrace();
  }

  //引用SSLSocketFactory
  
  HttpsURLConnection.setDefaultSSLSocketFactory(SSLSocketFactory);

  //如果证书域名不一致  可以设置下面代码，不校验域名
  
  HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
      @Override
      public boolean verify(String s, SSLSession sslSession) {
          return true;
      }
  });
