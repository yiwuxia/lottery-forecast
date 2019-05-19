# lzhpo-shiro

## 项目简介

>   `lzhpo-shiro`是一款非常漂亮、美观的权限管理系统，只保留了最精简的功能，旨在帮助开发人员减少造轮子，能够快速使用它，并改造成升级自己的权限管理系统，代码无过度封装，简洁易懂，开发利器，非常非常非常容易上手，甚至连`Redis`都没有使用，只要安装了`JDK`、有`MySQL`数据库，有开发工具`idea`或者`eclipse`就能运行，让想搭建后台的开发者上手即可使用，可以自己随意开发。

## 图片预览

### 登录界面

![](http://cdn.liuzhaopo.top/lzhpo-shiro-login.png)

### 全局概览

![](http://cdn.liuzhaopo.top/lzhpo-shiro-%E6%9B%B4%E6%8D%A2%E7%9A%AE%E8%82%A4.png)

个人喜欢顶部颜色值`#00a65a`，可以自己去`static/admin/js/index.js`下设计自己喜欢的颜色主题。

### 权限管理

![](http://cdn.liuzhaopo.top/lzhpo-shiro-%E6%9D%83%E9%99%90%E7%AE%A1%E7%90%86.png)

### 分配角色权限

![](http://cdn.liuzhaopo.top/lzhpo-shiro-%E5%88%86%E9%85%8D%E8%A7%92%E8%89%B2%E6%9D%83%E9%99%902.png)

### 个人资料

![](http://cdn.liuzhaopo.top/lzhpo-shiro-%E4%B8%AA%E4%BA%BA%E8%B5%84%E6%96%99.png)

## 技术栈

-   JDK 1.8
-   SpringBoot 2.0.3.RELEASE
-   shiro 1.4.0
-   Mybatis-Plus 3.0-RC1
-   layui 2.4.5
-   fastjson 1.2.41
-   MySQL
-   cuisongliu(验证码)

## 使用说明

### 下载

```
git clone https://github.com/liuzhaopo/lzhpo-shiro.git
```

不使用Git就直接直接下载。

### 导入项目

直接导入到`idea`或者`eclipse`。

### 导入sql脚本

说明：脚本已经包含创建数据库的命令，所以直接导入sql脚本即可，sql脚本在`sql/lzhpo-shiro.sql`。

### 运行项目

启动`com.lzhpo.AdminApplication`

### 登录

打开浏览器，访问[http://localhost:8080](http://localhost:8080)即可。
登录账号：admin
登录密码：123456

## 打赏作者

喜欢的话，先star，然后可以小额打赏一下哈~

### 支付宝

![](http://cdn.liuzhaopo.top/%E6%94%AF%E4%BB%98%E5%AE%9D.png)

### 微信

![](http://cdn.liuzhaopo.top/%E5%BE%AE%E4%BF%A1.png)
