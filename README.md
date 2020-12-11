# AndroidUI的占位符控件（功能在增加中，还未实现）
该控件是作用：在加载UI的时候，预占位。从这个控件中去总结下View的生命周期以及在自定义View时需要注意的地方。

功能还在验证中！！！！

##  一  自定义View
因为这里编辑不是很方便，专门在cdsn下面专门写了一个博文
参照博客https://blog.csdn.net/nihaomabmt/article/details/109668801
## 二  View属性设置优先级的一点总结
参照博客https://blog.csdn.net/nihaomabmt/article/details/109800839

## 三 预占位UI
1. 在项目中增加依赖
```
dependencies {
    implementation project(':libs:WidgetPlaceholderlibrary')
}
```
2. 在代码中添加相应的代码
 （1）实例化placeHolder
 ```
      placeHolder = new PlaceHolder.Builder(PlaceHolderActivity.this)
        //设置为非圆角
       //.setPlaceHolderBackgroundColor(Color.YELLOW)
       //.setPlaceHolderBackgroundResource(R.drawable.bg)
       //可以设置圆角的
        .setPlaceHolderBackgroundCorner(Color.RED, 20)
        .build();
  ```
   如果没有设置背景的颜色，背景色则将会按照"使用文字默认的字体颜色>字体的背景色>DEFAULT_BACKGROUND",并且在上面增加一个透明度，防止字体颜色是黑色系的时候，显示的太难看
 （2）在setContentView()之后,需要预占位的控件完全设置完背景之后
  ```
      placeHolder.startPlaceHolderChild();
   ```
  （3）在完成之后,释放预占位的UI
  ```
  placeHolder.stopPlaceHolderChild();
  ```
