# 帮助文档
此项目为海豚AndroidStudio版本Demo，clone后可直接run，下面将帮助您如何集成海豚到项目中去：

####第一步：
复制到sdk文件（.aar）到`主Moudle的libs目录`下，Demo中路径为HaiTunPaySmapleForAS/app/libs/haitunsdk-x.x.x.aar ([戳这里](/app/libs/))

####第二步：
在项目主Moudle下的build.gradle文件android标签下添加如下配置：[demo配置](/app/build.gradle)
```java
repositories {
    flatDir {
        dirs "libs"
    }
}
```
在dependencies标签下添加如下配置：[demo配置](/app/build.gradle)
```java
// haitunsdk-1.0.0为sdk在libs目录下的文件名
compile (name:'haitunsdk-1.0.0', ext:'aar');
```
####第三步：

AndroidManifest中添加如下配置：[demo配置](/app/src/main/AndroidManifest.xml)
```xml
<!-- ************微信支付配置start************ -->
<!-- 微信支付回调页 -->
<activity
    android:name=".wxapi.WXPayEntryActivity"
    android:exported="true"/>
<!-- 商户在海豚平台注册后获得的key -->
<meta-data android:name="HAITUN_WECHAT_KEY" android:value="07b9ca8e20293023a2a16525a1cc313e"/>
<!-- 商户在海豚平台注册后获得的merid -->
<meta-data android:name="HAITUN_WECHAT_MERID" android:value="10001"/>
<!-- ************微信支付配置end************ -->
```

所需权限配置：`<uses-permission android:name="android.permission.INTERNET"/>`

####第四步：
将Demo中`HaiTunPaySmapleForAS/app/src/main/java/com/longyou/haitunpay/`目录下的`wxapi`文件夹([demo目录位置戳这里](/app/src/main/java/com/longyou/haitunpay/))复制到您项目主Moudle下`src/main/java/包名(xxx.xxx.xxx)/`目录下

####第五步：
在自定义的Application的onCreate中进行SDK初始化 [demo代码](/app/src/main/java/com/longyou/haitunpay/App.java)
```java
//HaiTunPay.getInstance().initWithWechat("商户在海豚平台注册后获得的merId", "商户在海豚平台注册后获得的key", "");
// 如果在AndroidManifest文件中配置了HAITUN_WECHAT_KEY及HAITUN_WECHAT_MERID，
// 则直接调用无参init函数初始化即可，如果未在xm中配置，则调用上面的initWithWechat函数进行初始化
HaiTunPay.getInstance().init(this);
// 配置是否显示控制台日志，开启后便于开发者查看问题
HaiTunPay.setDebug(true);
```
到这里SDK配置就已经完成了
###如何调起支付
* 构建`PaymentBean`对象用于调起支付的必要参数
```java
PaymentBean paymentBean = new PaymentBean("商户自己生成的唯一订单ID", price/**支付金额，单位:元*/, "订单描述，不能为空", "支付服务端回调地址");
//paymentBean.setSjt_UserName("");// 备用参数，可用于设置渠道等其他
//paymentBean.setSjt_Paytype("");// 备用参数
```
* 调用以下代码进行调起支付 [demo代码](/app/src/main/java/com/longyou/haitunpay/MainActivity.java)
```java
HaiTunPay.getInstance().openWeChatPay(this, paymentBean);
```
* 支付回调函数位于[WXPayEntryActivity.java](/app/src/main/java/com/longyou/haitunpay/wxapi/WXPayEntryActivity.java)中，商户可在此回调函数中进行业务逻辑处理
```java
@Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            // resp.errCode == -1 原因：支付错误,可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等
            // resp.errCode == -2 原因 用户取消,无需处理。发生场景：用户不支付了，点击取消，返回APP
            if (resp.errCode == 0) // 支付成功
            {
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "取消支付 : " + resp.errCode, Toast.LENGTH_SHORT)
                        .show();
            }
            finish();
        }
    }
```

###混淆配置
[demo配置](/app/proguard-rules.pro)
```java
-dontwarn com.longyou.haitunsdk.**
-keep class com.longyou.haitunsdk.** { *; }
-dontwarn com.switfpass.pay.**
-keep class com.switfpass.pay.** { *; }
-dontwarn com.tencent.mm.**
-keep class com.tencent.mm.** { *; }
```


###***重要说明 x 3***
1. 商户包名必须为`com.longyou.haitunpay`
2. apk签名文件必须使用demo中的`haitunpay.jks`，签名信息如下：[demo配置](/app/build.gradle)
```java
keyAlias 'haitunpay'
storePassword 'haitunpay123'
keyPassword 'haitunpay123'
```
如果包名与签名不符，则会导致支付不成功，切记切记