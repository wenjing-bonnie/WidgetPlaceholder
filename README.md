# AndroidUI的占位符控件
自定义一个在加载UI的时候，预占位的一个控件。从这个控件中去回顾下View的生命周期以及在自定义View时需要注意的地方

## 生命周期
### 1.该View从xml加载时,默认的状态为Visible:
#### （1)加载完毕,显示出来:
    onCreate->onFinishInflate->onResume->onAttachedToWindow->onVisibilityChanged ->onMeasure->onLayout->onDraw
#### (2)状态从Visible变成InVisible:
        onVisibilityChanged
     
      (3) 状态再由InVisible变成Visible:
      onVisibilityChanged->onDraw
#### 3)状态从Visible变成Gone:
      onVisibilityChanged
      状态再由Gone变成Visible:
      onVisibilityChanged->onMeasure->onLayout->onDraw
### (2)该View从xml加载时,默认的状态为InVisible,加载完毕的时候:
      onCreate->onFinishInflate->onResume->onAttachedToWindow->onVisibilityChanged
 ->onMeasure->onLayout
### (3)该View从xml加载时,默认的状态为Gone,加载完毕的时候:
      onCreate->onFinishInflate->onResume->onAttachedToWindow->onVisibilityChanged
