

# AlanMvvm

# Ventajas principales

***1. Fácil de aprender y aplicar rápidamente, con un costo de aprendizaje muy bajo***

***2. Organización y ejecución modular***

***3. Empaquetado y generación de otras aplicaciones en 30 segundos (ideal para crear clones/variantes)***

***4. Simple, fácil de usar y plug-and-play, ya entienden***

***5. Estructura clara y definida, con categorización precisa***



# Estructura del proyecto

<details>
<summary>1、app Entrada principal del proyecto/summary>

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
<summary>2、keystore Almacena tu archivo de clave y contraseña/summary>

```kotlin
//storePassword=alanpaine@163.com
//keyPassword=alanpaine@163.com
//keyAlias=Alan
//storeFile=../keystore/AlanSignature.jks
```

</details>

<details>
<summary>3、library Almacena los módulos de bibliotecas de soporte/summary>

```kotlin
//library-arms //基模块
//.........
```

</details>

<details>
<summary>4、module Almacena las funcionalidades de cada módulo, todos pueden ejecutarse de forma independiente/summary>

```kotlin
//module-main //所有使用参考module-main的使用
//.........
```
</details>
        
<details>
<summary>5、config.gradle Almacena las bibliotecas y archivos de configuración importados por las funcionalidades de cada módulo/summary>

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

# Guía de uso

- **1. En el build.gradle de la app, activa DataBinding y ViewBinding según sea necesario dentro del bloque android**

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
# Herencia de clases base
Por lo general, nuestros proyectos cuentan con un conjunto de clases base personalizadas que se ajustan a los requisitos del negocio ***BaseActivity/BaseFragment***, por lo que nuestras clases base deben **heredar de la clase Base de este marco de trabajo**

- Si no deseas usar DataBinding y ViewBinding-------puedes heredar de BaseVmActivity/BaseVmFragment
- Si usas DataBinding-----------puedes heredar de BaseVmDbActivity/BaseVmDbFragment**
- Si usas ViewBinding-----------puedes heredar de BaseVmVbActivity/BaseVmVbFragment**


## Agradecimientos
- [JetpackMvvm](https://github.com/hegaojian/JetpackMvvm)
- [Rxhttp](https://github.com/liujingxing/rxhttp)
- [LaunchAc](https://github.com/jccppp/LaunchAc)
## Contacto
- Grupo de chat QQ: 419581249
- Grupo de chat QQ: 929420228
