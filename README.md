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
hommin.security.browser.session.sessionInvalidUrl: session失效后的跳转页面, 必须以".html"结尾(json格式请求会直接返回一个json数据). 修改session失效策略可以自定义整体逻辑并使得此配置失效. 另外, 如果是并发使得session失效, 会在url后添加"?concurrency=true".

#### 可选
server.session.timeout: session失效时间, 单位: s. 若设置小于60, 则为60.

hommin.security.browser.session.maximumSessions: 设置同一个用户同一时刻在线的数量, 默认为1

hommin.security.browser.session.maxSessionsPreventsLogin: 并发数登录超过maximumSessions时, 是否保留session存在时间长的. 默认为false, 即后登录者会将先登录者踢下线.

### 策略
session过期失效策略: 继承并实现InvalidSessionStrategy. 请参考: MyInvalidSessionStrategy.class和BrowserSecurityBeanConfig.class

session并发失效策略: 继承并实现SessionInformationExpiredStrategy. 请参考ExpiredSessionStrategy.class和BrowserSecurityBeanConfig.class

## 退出登录
### 可选配置

hommin.security.browser.logout-success-url: 退出登录成功后的跳转页面, 默认为登录页面

hommin.security.browser.deleteCookie: 退出登录后要删除的cookie的数组, 格式为 cookie1,cookie2,...

### 可配置策略

退出登录后的处理: 继承并实现LogoutSuccessHandler. 请参考: MyLogoutSuccessHandler.class和BrowserSecurityBeanConfig.class