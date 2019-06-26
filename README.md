## 写在前面

2017年的时候因项目需要整理了一篇关于[长截图的文章](https://www.jianshu.com/p/980a75a31f2f)。看到很多同学有留言，所以现在决定把它重新梳理一下，做成一个开源的库。

### ScreenShotTools

`ScreenShotTools`是一个Android长截图工具。目的是轻松搞定常见的View截图功能。
目前功能有：

```
1. ScrollView的截图
2. RecyclerView的截图
3. WebView的截图
4. View的截图
5. 各截图提供拼接头部和尾部功能
```
#### 使用方法：
##### gradle配置
1. 在最外层的`build.gradle`中添加maven地址
```
allprojects {
    repositories {
       ..
        maven { url 'https://dl.bintray.com/missmydearbear/maven' }
    }
}

```
2. 在`app`目录下的`build.gradle`中添加


```
implementation "com.bear:ScreenShotTools:1.0"
```
##### Api


```
//1.只截传入的View
 fun takeCapture(context: Context, view: View, callBack: IScreenShotCallBack?) 
//2.拼接头部图片
 fun takeCapture(context: Context, view: View, topBitmap: Bitmap?, callBack: IScreenShotCallBack?) 
//3.拼接头部和底部图片
 fun takeCapture(
        context: Context,
        view: View,
        topBitmap: Bitmap?,
        bottomBitmap: Bitmap?,
        callBack: IScreenShotCallBack?
    ) 
//4. 拼接头部和底部图片，且传入图片的宽度
 fun takeCapture(
        context: Context,
        view: View,
        topBitmap: Bitmap?,
        bottomBitmap: Bitmap?,
        width: Int,
        callBack: IScreenShotCallBack?
    ) 

```

#### demo
以RecyclerView为例
```
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        tv.setOnClickListener {
            ScreenShotTools.instance.takeCapture(this, recycler_view, object : IScreenShotCallBack {
                override fun onResult(screenBitmap: ScreenBitmap?) {
                    //todo do your things
                }

            })
        }
        loadData()
    }
```


### 写在最后
具体使用效果可看本项目demo。欢迎大家提issu。
