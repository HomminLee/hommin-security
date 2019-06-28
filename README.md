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


## session
### 配置
#### 必选
hommin.security.browser.session.sessionInvalidUrl: session失效后的跳转. json格式请求会直接跳转url进入controller, html请求会在url后加上".html"然后跳转页面. 修改session失效可以自定义整体逻辑并使得此配置失效. 另外, 如果是并发使得session失效, 会在url后添加"?concurrency=true".

#### 可选
server.session.timeout: session失效时间, 单位: s. 若设置小于60, 则为60.

hommin.security.browser.session.maximumSessions: 设置同一个用户同一时刻在线的数量, 默认为1

hommin.security.browser.session.maxSessionsPreventsLogin: 并发数登录超过maximumSessions时, 是否保留session存在时间长的. 默认为false, 即后登录者会将先登录者踢下线.

### 策略
session过期失效策略: 继承InvalidSessionStrategy. 请参考: MyInvalidSessionStrategy.class和BrowserSecurityBeanConfig.class

session并发失效策略: 继承SessionInformationExpiredStrategy. 请参考ExpiredSessionStrategy.class和BrowserSecurityBeanConfig.class
