# hommin-security
慕课网上学习spring-security的记录代码

## 登录

### 配置
#### 必填配置

#### 可选配置

- 登录页url

默认: /hommin-sign-in.html

自定义: hommin.security.browser.loginPage=YOUR_LOGIN_PAGE

## 第三方
### 绑定和解绑
主要关注`ConnectController.class`

#### 常用url

- GET /connect                 当前登录user的所有第三方绑定
- GET /connect/{providerId}    当前登录user在{providerId}的绑定信息
- POST /connect/{providerId}   跳转页面到{providerId}为当前登录user进行绑定
- DELETE /connect/{providerId} 当前登录user与{providerId}解绑(删除数据库)

使用示例: `demo-binding.html`或直接访问`http://host/connect`

#### 配置 

第三方绑定信息汇总的返回信息和格式的配置: 创建类继承AbstractView, 以之创建名为"connect/status"的实例. 可查看`ConnectStatusView.class`和`SocialConfig.connectStatusView()`.

绑定/解绑后的跳转的配置: 创建类继承AbstractView, 以之创建名为"qqConnectView","connect/qqConnect", "connect/qqConnected"的实例. 可以查看`ConnectView.class`和`SocialBeanConfig.qqConnectView()`



