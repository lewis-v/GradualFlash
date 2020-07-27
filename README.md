# GradualFlash
渐变动画效果控件

# 引入工程
```
implementation 'com.github.lewis-v:GradualFlash:v0.0.3'
```

# 使用
```
gf.setGradualFlash(
    GradualFlash.GFBuilder()
        .setLowColor(Color.RED)
        .setHeightColor(Color.BLACK)
        .setShapeMode(SHAPE_MODE_LINE)
        .setAnimDuration(1000)
        .setColorType(COLOR_TYPE_FLASH)
        .setDirection(DIRECTION_LEFT_TO_RIGHT)
        .setRepeatCount(ValueAnimator.INFINITE)
        .setRepeatMode(ValueAnimator.RESTART)
        .setXfermode(PorterDuff.Mode.SRC_IN)
        .setFlashWidthRatio(1f)//闪光的宽度占控件的宽度
        .setFlashHeightRatio(1f)//闪光的高度占控件的高度
        .setAnimStartXRatio(-1f)//闪光开始的x坐标占控件的宽度
        .setAnimStartYRatio(-1f)//闪光开始的y坐标占控件的高度
        .setAutoStart(true)
        .build()
)
```