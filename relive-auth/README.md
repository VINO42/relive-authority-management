## 权限管理

### 登录方式
- 用户名密码登录（basic登录，form登录，json登录），内部使用fromLogin()方便作为授权服务器时跳转登录
- 手机号验证码登录
- 扫码登录
- 第三方登录

### 功能菜单
- 用户管理
- 角色管理
- 菜单管理
- API管理
- 页面管理


### 统一Json返回
````
{
    "code":200,
    "message":"",
    "data":null
}

token过期返回新token
{
    "code":200,
    "message":"",
    "data":null,
    "accessToken":""
}
````
### token存储

````
auth-server: 
    usernameipmd5: 
      token: dafewfwer923r2
      captcha: YT76E 
      authorize: 权限信息
      errorCount: 
    blackIP: 黑名单IP
````

### 登录成功返回格式
````
{
    "access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiYXV0aC1zZXJ2ZXIiXSwidXNlcl9uYW1lIjoiYWRtaW4iLCJzY29wZSI6WyJhbGwiXSwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJleHAiOjE2MzQ2NTA0NzEsInVzZXJJZCI6MSwiYXV0aG9yaXRpZXMiOlsiaG9tZS1wYWdlIl0sImp0aSI6ImMxNTY2MDdmLTBjMTYtNDhkMi1iODE3LTEwYmM2ZDY1YmZjOCIsImNsaWVudF9pZCI6InJlbGl2ZS1jbGllbnQifQ.TI0A_tnb42PD1A6l6-2rDD3V1xFPRDHqt76rsk9hWFE",
    "token_type":"bearer",
    "refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiYXV0aC1zZXJ2ZXIiXSwidXNlcl9uYW1lIjoiYWRtaW4iLCJzY29wZSI6WyJhbGwiXSwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJhdGkiOiJjMTU2NjA3Zi0wYzE2LTQ4ZDItYjgxNy0xMGJjNmQ2NWJmYzgiLCJleHAiOjE2MzQ2ODI4NzEsInVzZXJJZCI6MSwiYXV0aG9yaXRpZXMiOlsiaG9tZS1wYWdlIl0sImp0aSI6IjNhYzY3ZTM3LWJkYzktNDRiMS1iYzU1LTZlMTI3ZDczZTZjOCIsImNsaWVudF9pZCI6InJlbGl2ZS1jbGllbnQifQ.6MBZc9g6wFcg6o1zjPyEj4aGVxcCxFOBUvASKU53P_s",
    "userId":1,
    "username":"admin",
    "nickname":"超级管理员",
    "roles":[
        "ROLE_ADMIN"
    ],
    "avatar":"http://avatar.com",
    "goTo":"https://localhost/home",
    "jti":"c156607f-0c16-48d2-b817-10bc6d65bfc8"
}
````

### Token续签方案
- 网关验证token失效,走刷新token流程
- 根据token获取刷新token,请求刷新接口，接口验证通过后会重新返回assess_token和refresh_token
- 存储access_token和refresh_token,并将access_token存入此请求线程ThreadLocal
- 携带access_token访问资源
- 请求返回是网关将从ThreadLocal中获取access_token并更新cookie
- 下一个请求从cookie中获取token
注意：
  1.网关更新token时设置secure和httpOnly为true
  2.http返回给https时cookie更新失败
  3.cookie设置httpOnly为true时，前端如何获取token