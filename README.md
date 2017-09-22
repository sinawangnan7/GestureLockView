# GestureLockView

**GestureLockView**是一个提供给Android的、简单易用的、可扩展的手势解锁视图。

## 功能特性

- **GestureLockView** 内置了一个默认的手势解锁视图，实现了手势解锁的基础功能。

- **GestureLockView** 支持对内置视图进行简单的样式调整、动画设置、触摸震动设置。

- **GestureLockView** 支持自定义绘制，实现不同的样式效果。

- **GestureLockView** 支持插入图片代替自定义绘制、提升开发效率。

## 效果演示

### 完整效果

作者录制了完整的Demo视频演示：

**Youtube视频**[2分58秒] (需要翻墙): [https://youtu.be/-WRLJzNZ8sU](https://youtu.be/-WRLJzNZ8sU)

### 细节说明

**GestureLockView** 内置了一个默认的解锁视图，参考原型是**支付宝Android APP**，但作者对此原型进行了简化，效果:

![origin](https://github.com/sinawangnan7/GestureLockView/blob/master/gif/origin.gif)

**GestureLockView** 支持插入图片，代替原始绘制:

![copy_image](https://github.com/sinawangnan7/GestureLockView/blob/master/gif/copy_image.gif)

**GestureLockView** 支持自定义绘制，多数APP手势解锁效果都可以使用GestureLockView实现，举例：

![copy_360](https://github.com/sinawangnan7/GestureLockView/blob/master/gif/copy_360.gif)
![copy_jd](https://github.com/sinawangnan7/GestureLockView/blob/master/gif/copy_jd.gif)
![copy_alipay](https://github.com/sinawangnan7/GestureLockView/blob/master/gif/copy_alipay.gif)

**Demo地址:** [GestureLockViewDemo.apk](https://github.com/sinawangnan7/GestureLockView/blob/master/GestureLockViewDemo.apk)

## Gradle引入

在需要使用的module下，找到build.gradle文件添加库依赖:

```
dependencies {

    compile 'com.wangnan:gesturelockview:1.0.0'
}
```

## 使用方法

1.基础自定义属性请参看 [Github Wiki Attr](https://github.com/sinawangnan7/GestureLockView/wiki/Attr)

2.设置手势监听器请参看 [Github Wiki Listener](https://github.com/sinawangnan7/GestureLockView/wiki/Listener)

3.自定义绘制请参看 [Github Wiki CustomPainter](https://github.com/sinawangnan7/GestureLockView/wiki/CustomePainter)

## 问题反馈和建议

1.[GitHub Issues](https://github.com/sinawangnan7/GestureLockView/issues): 直接在Github上提问

2.[简书](http://www.jianshu.com/u/5ffe9ada44b0): 给作者发简信

## 开源协议

```
   Copyright 2017 wangnan

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
