# 第2小组学习报告

## 第2周

### 个人学习报告链接

- [金帆](http://toyhouse.cc/wiki/index.php/%E9%87%91%E5%B8%86-%E5%AD%A6%E4%B9%A0%E6%8A%A5%E5%91%8A-%E7%AC%AC2%E5%91%A8)
- [李浩源](http://toyhouse.cc/wiki/index.php/2017%E5%B9%B4%E7%A7%8B%E8%AE%A1%E7%AE%97%E6%80%9D%E7%BB%B4%E5%92%8C%E7%B3%BB%E7%BB%9F%E8%AE%BE%E8%AE%A1%E7%AC%AC%E4%BA%8C%E5%91%A8%E5%AD%A6%E4%B9%A0%E6%8A%A5%E5%91%8A_%E6%9D%8E%E6%B5%A9%E6%BA%90)
- [李闫涛](http://toyhouse.cc/wiki/index.php/User:2016012807#week_2)

### 逻辑模型

- 输入
	- 课程网站的课件、作业提示、上课内容
- 输出
	- 做 9、10、11 章作业
- 过程
	- 学习汇编语言和 Jack 语言
	- 观察已有样例程序，将参考程序反汇编，学习Java源码
	- 尝试写作业（to do）
- 验证
	- 利用样例文件，测试程序
	- 上课展示工作，听取反馈

### 工作汇总

1. 队长金帆划分了工作任务如下：
	- 李浩源：project 9
	- 李闫涛：project 10
	- 金帆：project 11

2. 学习了9-11三章的课件、教材，浏览了作业的要求。为了加深理解，还反汇编了 Jack Compiler 的 ```.class``` 文件，了解编译器的原理。在做作业时，上手时遇到了很大的困难，不知道是使用 Java 等高级语言，还是使用 Jack 语言来开发编译器。但是我们知道，整个编译器的构建被分为3个部分：符号提取（tokenization）、语法树构建（parsing）、代码生成（code generation）。其中，符号提取的工作使用 Java 很容易完成。

3. 队长和队员之间探讨 wiki 和 git 的使用方法，队员学会了利用 git 进行版本控制。

### 关于编译器的理解

以以下样例代码的片段为例：
![图1](https://i.loli.net/2017/10/11/59de3b5045870.png)
![图2](https://i.loli.net/2017/10/11/59de3b54e28ef.png)

- 原始代码是 ```sum = sum + a[i];```。首先是词法分析，将单词切分并标注词性，之后生成语法树，如图1所示。  

- 选中的部分就是等号的右值表达式（r-value expression），在语法树（XML格式）中是一个树状结构。  

- 在代码生成阶段，编译器将语法树的中序遍历结构，转化为基于栈的后序遍历形式，并生成对应代码（如图2）。

- 在图2的选中部分，首先是将 ```sum```（存于 local 3）入栈，再将 ```&a[0]```（存于 local 0）入栈，再将 ```i```（存于 local 2）入栈，之后做加法，栈上还剩 ```sum``` 以及新生成的 ```&a[i]```。

- 之后两行字节码是取指针操作，取出 ```&a[i]``` 位置的元素值（即 ```a[i]```），存放在栈上。

- 最后栈上只剩 ```sum``` 和 ```a[i]```，执行一次 add 操作，得到表达式的求值结果 ```sum + a[i]```。至此选中部分执行完毕。

- 选中部分的后续代码，将该结果 pop 回 ```sum``` 所在的 local 3 区域，就完成了一个语句（statement）的执行。

### To Do List

- 完成 projects 9 - 11 的作业（使用 Java 语言编写三个程序，分别实现 tokenization、parsing、code generation）

- 制作逻辑模型

- 预习

## 第3周

### 逻辑模型

- 输入
	- 课程网站的课件、作业提示、上课内容
- 输出
	- 做 9、10、11 章作业
- 过程
	- 继续学习 VM 字节码、 Jack 语言、git（branch、rebase）
	- 学习 project 3 & 4 课本
	- 改用 Java 写编译器的第1个部分（tokenizer）
- 验证
	- 单元测试通过（样例文件 KeyboardTest.jack）
	- 上课展示工作，听取反馈

### 工作汇总

1. 学习 git 的 branch 和 rebase 功能，使用 ```pull``` 命令实现版本合并，实现独立于服务器端的异步编辑。

2. 使用 Java 语言开发 JackCompiler，已完成 ```JackTokenizer``` 模块。
- [项目主地址](https://github.com/kingium/JackCompiler)
- 改动会定期合并到[主仓库](https://github.com/jisuansiwei2017/SDCT-2017-Fall/tree/master/src/group2/JackCompiler)
- ```JackTokenizer``` 类完全依照课本214页的设计合约制作，开放的接口可供上层的 ```Parser``` 类调用。

3. 李浩源学习了 project 4，并思考了该章的作业。

### 选择 Java 语言的原因

- 运行于 Java 虚拟机的高级语言
	- 跨平台
	- 底层被抽象成 Java API
- Object-oriented
	- 适合构建复杂系统
- 强类型
	- Runtime stability
- 模块化
	- 整合编译器的各模块
	- Unit test

### Tokenizer 本质是 Iterator（迭代器）

- 流式读取
	- Step-by-step
	- 适合读取大文件
- 顺序遍历（iterator）
	- 迭代器相当于单向移动的光标，永远指向两个 token 之间
	- ```hasMoreTokens()```
	- ```advance()```
	- getters
	- 便于嵌入其他模块（逐次调用）
- 状态机（state machine）
	- 类比范畴论

### ToDo List

参考编译原理的有关资料，讨论 ```Parser``` 类怎么写。
