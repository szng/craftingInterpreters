# parser
    program -> statement* EOF
强制性的结束标记EOF可以确保解析器能够消费所有输入内容，而不会默默地忽略脚本结尾处错误的、未消耗的标记。

# Java
泛型中需要传入类型为void时，需要使用Void作为类型参数