# SWING-SPY

![](https://p.sda1.dev/25/63fc14cc288cbf096fa5afd591a8b09e/idea64_KQEGEVVAm0.png)
一款基于java-agent，用于调试屎山Java Swing代码时快速定位由用户自己实现的UI类(用户基于java.awt.Component开发的子类)的工具

这年头还在写Swing，这辈子有了

## 使用方法

JVM参数加上

```shell
-javaagent:swing-spy.jar
```

如果成功加载此java-agent，启动程序的第一时间，控制台应该会输出

```shell
com.weiyuanstudio.swing.spy: LOADED
   _______          _______ _   _  _____        _____ _______     __
  / ____\ \        / /_   _| \ | |/ ____|      / ____|  __ \ \   / /
 | (___  \ \  /\  / /  | | |  \| | |  __ _____| (___ | |__) \ \_/ / 
  \___ \  \ \/  \/ /   | | | . ` | | |_ |______\___ \|  ___/ \   /  
  ____) |  \  /\  /   _| |_| |\  | |__| |      ____) | |      | |   
 |_____/    \/  \/   |_____|_| \_|\_____|     |_____/|_|      |_|   
```

按住CTRL并左键点击自己实现的SWING UI类，即可显示出此UI类的类名，以及UI"堆栈"。
鼠标左键单击悬浮窗关闭，右键单击悬浮窗复制此类的完整类名
