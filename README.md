# HttpsUtil

最新版本:[![](https://jitpack.io/v/dingqiqi/HttpsUtil.svg)](https://jitpack.io/#dingqiqi/HttpsUtil)

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

    compile 'com.github.dingqiqi:HttpsUtil:v1.5'
    
}

  使用方式

  //获取信任全部的Https 的SSLSocketFactory
  
  SSLSocketFactory SSLSocketFactory = HttpsUtil.getInstance().getSocketFactory();

  //双向认证 服务端公钥转成的bks私钥
  try {
  
      InputStream ksIn = getAssets().open("client.p12");
      
      InputStream tsIn = getAssets().open("service.bks");

      HttpsUtil.getInstance().getSocketFactory(ksIn,"123456",tsIn,"123456");
      
  } catch (IOException e) {
      e.printStackTrace();
  }

  //双向认证 服务端公钥
  try {
      InputStream ksIn = getAssets().open("client.p12");
      
      InputStream tsIn = getAssets().open("service.cer");

      HttpsUtil.getInstance().getSocketFactory(ksIn,"123456",tsIn,null);
      
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
