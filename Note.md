# parser
    program -> statement* EOF
强制性的结束标记EOF可以确保解析器能够消费所有输入内容，而不会默默地忽略脚本结尾处错误的、未消耗的标记。
## 语法脱糖
```C
for (int i = 0; i < n; i++) {
    foo();
}
```
for循环可以脱糖为完全等价的while循环
```C
{
    int i = 0;
    while (i < n) {
        foo();
        i++;
    }
}
```

# Java
泛型中需要传入类型为void时，需要使用Void作为类型参数。

# 编程
递归或设计函数时，可是先当作已经实现了设计函数或者基础功能的模块，并借此理清各个函数或模块之间的逻辑关系。
