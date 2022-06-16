# AlanMvvm

# 主要优势

***1. 瞬间即可上手，学习成本极低***

***2. 模块化分类，模块化运行***

***3. 30秒打包生成为其他的App（马甲小能手）***

***4. 简单易用即插即用，你懂得***

***5. 结构清晰明了，分类明确***



# 项目结构

<details>
<summary>1、app 项目主入口/summary>

```kotlin
class App : BaseApp() {
        //配置每个模块的application
        private val applicationList = arrayListOf(
                CommonModuleInit::class.java.name,
                MainModuleInit::class.java.name)
        
        override fun onCreate() {
                super.onCreate()
                //初始化模块
                ModuleLifecycleConfig.instance.initModuleAhead(this,applicationList)
        }
}
```
</details>

<details>
<summary>2、keystore 存放你的秘钥文件及密钥/summary>

```kotlin
//storePassword=alanpaine@163.com
//keyPassword=alanpaine@163.com
//keyAlias=Alan
//storeFile=../keystore/AlanSignature.jks
```

</details>

<details>
<summary>3、library 存放library支持库模块/summary>

```kotlin
//library-arms //基模块
//.........
```

</details>

<details>
<summary>4、module 存放你各个模块功能，均可独立运行/summary>

```kotlin
//module-main //所有使用参考module-main的使用
//.........
```
</details>
        
<details>
<summary>5、config.gradle 存放你各个模块功能引入的库及配置文件/summary>

```kotlin
 app = [
            //////////////////DEMO快速换马甲//////////////////
            //App包名
            appId:"com.alan.demo",
            //签名路径
            keystore:"./keystore/keystore.properties",
            //版本号
            versionCode      : 1,
            //版本名称
            versionName      : "1.0.0",
    ]
```
</details>

# 使用帮助

- **1. 在app's build.gradle中，android 模块下按需开启DataBinding与ViewBinding**

``` gradle
AndroidStudio 4.0 以下版本------>
android {
    ...
    dataBinding {
        enabled = true 
    }
    viewBinding {
        enabled = true
    }
}

AndroidStudio 4.0及以上版本 ------>
android {
    ...
   buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}
 
```
# 继承基类
一般我们项目中都会有一套自己定义的符合业务需求的基类 ***BaseActivity/BaseFragment***，所以我们的基类需要**继承本框架的Base类**

- 不想用Databinding与ViewBinding-------可以继承 BaseVmActivity/BaseVmFragment
- 用Databinding-----------可以继承BaseVmDbActivity/BaseVmDbFragment**
- 用Viewbinding-----------可以继承BaseVmVbActivity/BaseVmVbFragment**


## 感谢
- [JetpackMvvm](https://github.com/hegaojian/JetpackMvvm)
- [Rxhttp](https://github.com/liujingxing/rxhttp)

## 联系
- QQ交流群：419581249
- QQ交流群：929420228
