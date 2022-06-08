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

```java
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
```java
storePassword=alanpaine@163.com
keyPassword=alanpaine@163.com
keyAlias=Alan
storeFile=../keystore/AlanSignature.jks
```
</details>

<details>
<summary>3、library 存放library支持库模块/summary>
```java
library-arms //基模块
.........
```
</details>

<details>
<summary>3、module 存放你各个模块功能，均可独立运行/summary>
```java
module-main //所有使用参考module-main的使用
.........
```
</details>