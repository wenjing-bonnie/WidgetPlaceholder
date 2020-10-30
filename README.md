# AndroidUI的占位符控件
该控件是作用：在加载UI的时候，预占位。从这个控件中去总结下View的生命周期以及在自定义View时需要注意的地方

## 一 View生命周期几个重要的方法
### 1.`onFinishInflate`
  该方法会在View从xml加载完之后，触发该方法。即在Activity的`onCreate`中调用`setContentView`时。该方法仅调用一次。
### 2.`onVisibilityChanged`
  该方法会在View的状态发生变化的时候，触发该方法。即在主动调用`setVisible` ,Activity的生命周期`onPause`、`onResume`被调用的时。该方法会随着这些方法调用而被调用。
### 3.`onAttachedToWindow`
  当View被加载到Window时，触发该方法。即在Activity第一次调用`onResume`时。该方法仅在第一次加载到Window时调用。与之对应的`onDetachedFromWindow`就是在View从Window上移除的时候，触发该方法。即在Activity调用`onDestroy`时。
### 4.`onMeasure`
  该方法用来测量该View以及子View的大小时被调用。该方法在View的状态为Gone的时候，并不会被调用到，但是状态为InVisible，会被调用到。
### 5.`onLayout`
  该方法用来确定View和子View的位置和尺寸的时被调用。同样该方法在View的状态为Gone的时候，并不会被调用到，但是状态为InVisible，会被调用到。
### 6.`onDraw`
  该方法用来渲染View的时候被调用。同样该方法在View的状态为Gone和InVisible的时候，都不会被调用到。
## 二 View生命周期
### 1.该View从xml加载时,默认的状态为Visible:
#### (1)加载完毕,显示出来:
       onCreate->onFinishInflate->onResume->onAttachedToWindow->onVisibilityChanged ->onMeasure->onLayout->onDraw
#### (2)状态从Visible变成InVisible:
       onVisibilityChanged    
##### 状态再由InVisible变成Visible:
       onVisibilityChanged->onDraw     
#### (3)状态从Visible变成Gone:
       onVisibilityChanged
##### 状态再由Gone变成Visible:
       onVisibilityChanged->onMeasure->onLayout->onDraw
### 2.该View从xml加载时,默认的状态为InVisible,加载完毕的时候:
      onCreate->onFinishInflate->onResume->onAttachedToWindow->onVisibilityChanged->onMeasure->onLayout
### 3.该View从xml加载时,默认的状态为Gone,加载完毕的时候:
      onCreate->onFinishInflate->onResume->onAttachedToWindow->onVisibilityChanged
### 4.总结           
（1）从生命周期方法的调用过程中，可以看到，`onVisibilityChanged`会在View的状态发生变化的时候就会被调用到，例如主动调用`setVisible` ,Activity的生命周期`onPause`、`onResume`被调用的时候，都会触发`onVisibilityChanged`。

（2）另外当View的可见状态的变化，也会触发不同的周期方法：状态由InVisible变成Visible，该控件只需要执行`onDraw`即可，因为该控件在Window上是有占位的，在初始化加载该控件的时候，已经将该控件放置到Window上(即执行完`onLayout`);相反的如果有Gone变为Visible，该控件需要依次进行重新测量、布局和绘制(即依次执行完`onMeasure`->`onLayout`->`onDraw`)，因为在初始化该控件的时候，并没有将该控件绘制到Window上。
## 三 自定义View
### 1.自定义View的方法
   我们在项目中使用自定义View的方法通常有以下几种方式：
  - 组合控件

