### 关于Integer的那些事

#### 先来看一段代码

```java
public class IntegerTest {
    public static void main(String[] args) {
        //第一段
        int a = 1;
        int b = 1;
        System.out.println(a == b);

        //第二段
        Integer a1 = 1;
        Integer b1 = 1;
        System.out.println(a1 == b1);

        //第三段
        int a2 = 1;
        Integer b2 = 1;
        System.out.println(a2 == b2);

        //第四段
        int c = 1000;
        int d = 1000;
        System.out.println(c == d);

        //第五段
        Integer c1 = 1000;
        Integer d1 = 1000;
        System.out.println(c1 == d1);

        //第六段
        int c2 = 1000;
        Integer d2 = 1000;
        System.out.println(c2 == d2);

    }
}
```

> 代码很简单，就是几个值之间来回比较，看看他们是否相等

#### 结果是什么呢？

这还用说，两两之间大小都一样，肯定都是`true`嘛....

好，咱们大胆假设，小心求证。让程序跑一跑不就知道了....

3，2，1，编译--运行--走你。

跑完了，结果是这样子的。（为了方便观看，在这里我给每个结果加个注释）

```java
//第一段
true
//第二段
true
//第三段    
true
//第四段    
true
//第五段    
false
//第六段    
true
```

#### 结果分析

第一、二、三、四、六段代码的结果都是`true`，偏偏第五段代码的结果是`false`....

咦，剧本不是这样啊？

难道是，执行的时候执行错了...

于是乎，再执行一次。

结果仍然一样。

这就奇怪了...

莫慌，咱们一段段分析

首先在分析之前，先了解下`java`自动拆装箱机制。

##### java自动拆装箱

> 首先java内置了一些基本类型，但是java是一门面向对象的语言。这些基本类型不是面向对象的，这样自然就会有很多不方便的地方。于是在设计类的时候，就为每一个基本类型设计了一个对应的类，这些类统称为包装类，他们之间的对应关系如下表所示

| 基本数据类型 | 包装类    |
| ------------ | --------- |
| byte         | Byte      |
| boolean      | Boolean   |
| short        | Short     |
| char         | Character |
| int          | Integer   |
| long         | Long      |
| float        | Float     |
| double       | Double    |

那么，问题来了，他们之间是怎么转换的呢？

且听我细细道来

1. 在`javaSE5`之前，可以通过如下代码进行转换

```java
//装箱
Integer i1 = new Integer(5);
//拆箱
int i2 = i1.intValue();
```

通过上边的代码，`int`和`Integer`就完成了相互转换

2. 从`javaSE5`开始，转换就变的简单多了

```java
//自动装箱
Integer i1=5;
//自动拆箱
int i2=i1;
```

那么他是怎么实现自动拆箱装箱的呢？他是这么实现的

```java
Integer i1 = Integer.valueOf(5); 
int i2=integer.intValue(); 
```

显然，通过自动拆装箱之后，我们写起代码也就变的简单多了

##### Integer自动装箱

通过上面分析可得知，`Integer`自动装箱是通过`Integer.valueOf`方法实现的

下面，看看他的源码，做到既知其然，也知其所以然

```java
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
```

简答分析可知，他并不是通过直接`new Integer()`来实现自动装箱的，而是搞了一个`IntegerCache`

那么，问题来了

`IntegerCache`是个什么鬼？

来看源码

```java
private static class IntegerCache {
    static final int low = -128;
    static final int high;
    static final Integer cache[];

    static {
        // high value may be configured by property
        int h = 127;
        String integerCacheHighPropValue =
            sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
        if (integerCacheHighPropValue != null) {
            try {
                int i = parseInt(integerCacheHighPropValue);
                i = Math.max(i, 127);
                // Maximum array size is Integer.MAX_VALUE
                h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
            } catch( NumberFormatException nfe) {
                // If the property cannot be parsed into an int, ignore it.
            }
        }
        high = h;

        cache = new Integer[(high - low) + 1];
        int j = low;
        for(int k = 0; k < cache.length; k++)
            cache[k] = new Integer(j++);

        // range [-128, 127] must be interned (JLS7 5.1.7)
        assert IntegerCache.high >= 127;
    }

    private IntegerCache() {}
}
```

莫慌

其实很简单，且听我细细道来

`IntegerCache`是`Integer`的一个内部类；

`IntegerCache`有三个常量；

- `low`：定值`-128`
- `high`：默认`0`
- `cache`：存放`Integer`对象的数组

`IntegerCache`还有一个静态代码块，这个静态代码块的作用就是预先生成好`low`到`high`对应的`Integer`对象，并且把生成好的对象存放至`cache`中

再回到`Integer.valueOf`方法，他的源码是这样的

```java
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
```

到这

奥，明白了

当调用`Integer.valueOf`方法，传入的值为`-128`到`127`时，并不会重新生成一个对象，而是直接从`IntegerCache.cache`中拿一个已经生成好的对象返回。

这样带来最直接的好处就是效率会提高，但是也占用了一部分内存。总体来说，还是好处大于坏处。

#### 回到文章开始部分

- 第一段

```java
int a = 1;
int b = 1;
System.out.println(a == b);
```

> 两个int类型比较大小，结果为true

- 第二段

```java
Integer a1 = 1;
Integer b1 = 1;
System.out.println(a1 == b1);
```

> a1,b1发生了自动装箱，比较的是内存地址。因为1在-128到127之间，b1并没用重新生成一个对象，而是从缓存中拿已有的对象，所以两个integer对象内存地址一样，所以结果为true

- 第三段

```java
int a2 = 1;
Integer b2 = 1;
System.out.println(a2 == b2);
```

> 当包装类与基本数据类型进行比较运算，是先将包装类进行拆箱成基本数据类型，然后进行比较的。所以最后实质上是两个int类型比较大小，结果自然为true

- 第四段

```java
int c = 1000;
int d = 1000;
System.out.println(c == d);
```

> 两个int类型比较大小，结果为true

- 第五段

```java
Integer c1 = 1000;
Integer d1 = 1000;
System.out.println(c1 == d1);
```

> c1,d1发生了自动装箱，比较的是内存地址。因为1000不在-128到127之间，d1重新生成一个对象，所以两个integer对象内存地址不一样，所以结果自然为false

- 第六段

```java
int c2 = 1000;
Integer d2 = 1000;
System.out.println(c2 == d2);
```

> 当包装类与基本数据类型进行比较运算，是先将包装类进行拆箱成基本数据类型，然后进行比较的。所以最后实质上是两个int类型比较大小，结果自然为true

#### 阿里开发手册有话说

> 【强制】 所有整型包装类对象之间值的比较， 全部使用 equals 方法比较。
> 说明： 对于 Integer var = ? 在-128 至 127 之间的赋值， Integer 对象是在 IntegerCache.cache 产生，会复用已有对象，这个区间内的 Integer 值可以直接使用==进行判断，但是这个区间之外的所有数据，都会在堆上产生，并不会复用已有对象，这是一个大坑，推荐使用 equals 方法进行判断。  

以上内容，引用自《阿里开发手册嵩山版》

为什么用`equals `方法比较就没问题呢？

来，看源码

- `Integer`的`equals`方法

```java
public boolean equals(Object obj) {
    if (obj instanceof Integer) {
        return value == ((Integer)obj).intValue();
    }
    return false;
}
```

- `Integer`的`intValue`方法

```java
public int intValue() {
    return value;
}
```

最后，转换为两个`int`比较大小，自然没有问题。

再把第五段代码稍微改动

```java
Integer c11 = 1000;
Integer d11 = 1000;
System.out.println(c11.equals(d11));
```

结果为`true`

完美~

#### 总结

通过这篇文章，我们知道了

- `java`的基本数据类型与其对应的包装类型分别是什么
- `java`自动装箱，拆箱的历史以及原理
- 所有整型包装类对象之间值的比较，应全部使用 `equals` 方法比较

#### 拓展

下面是`Integer`的一些常用方法

| 方法                                        | 说明                                                         |
| ------------------------------------------- | ------------------------------------------------------------ |
| int intValue()                              | 以int的形式返回Integer对象的值                               |
| static String toString(int i)               | 以一个新String对象的形式返回给定数值 i 的十进制表示          |
| static String toString(int i ,int radix )   | 返回数值i的基于给定radix参数进制的表示                       |
| static int parselnt( String s)              | 返回字符串s表示的整型数值，给定字符串表示的是十进制的整数    |
| static int parseInt( String s,int radix )   | 返回字符串s表示的整型数值，给定字符串表示的是radix参数进制的整数 |
| static Integer valueOf(String s)            | 返回用s表示的整型数值进行初始化后的一个新 Integer对象，给定字符串表示的是十进制的整数 |
| Static Integer valueOf(String s, int radix) | 返回用s表示的整型数值进行初始化后的一个新 Integer 对象，给定字符串表示的是radix 参数进制的整数 |

#### 写在最后

好了，这期的内容到这里就结束了。

我是小码哥，一个朴实的打工人，始终相信实践是检验真理的唯一标准。

欢迎您的点赞，转发，分享，在看，关注。

我们下期不见不散。

拜拜~