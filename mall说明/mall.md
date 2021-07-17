#	mall项目文档

## 1.	项目结构和数据库结构

![项目结构](C:\Users\Administrator\Desktop\mall\mall说明\img\Snipaste_2021-02-04_13-57-40.png)

![](C:\Users\Administrator\Desktop\mall\mall说明\img\Snipaste_2021-02-04_13-59-13.png)

## 2.项目文件介绍

### 2.1 org.group.mall.common包

- ```
  CategoryLevelEnum 获取分类等级
  ```

- ```
  Constants 一些全局配置信息
  ```

- ```
  IndexConfigTypeEnum 首页配置项 1-搜索框热搜 2-搜索下拉框热搜 3-(首页)热销商品 4-(首页)新品上线 5-(首页)为你推荐
  ```

- ```
  MyException 自定义注解
  ```

- ```
  OrderStatusEnum 订单状态配置:0.待支付 1.已支付 2.配货完成 3:出库成功 4.交易成功 -1.手动关闭 -2.超时关闭 -3.商家关闭
  ```

- ``` 
  PayStatusEnum 支付状态配置:0.支付中 1.支付成功 -1.支付失败
  ```

- ``` 
  PayTypeEnum 支付类型配置:0.无 1.支付宝 2.微信支付
  ```

- ```
  ServiceResultEnum 全局返回信息配置
  ```



### 2.2 org.group.mall.interceptor包

- ```
  AdminLoginInterceptor 管理端系统身份验证拦截器
  ```

- ```
  CartNumberInterceptor 购物车数量处理拦截器
  ```

- ```
  LoginInterceptor 用户端系统身份验证拦截器
  ```



### 2.3 org.group.mall.config包

- ```
  MyWebMvcConfigurer 全局拦截器配置
  ```

- ```
  RedisConfig 使用Redis配置
  ```



### 2.4 org.group.mall.util包

- ```
  BeanUtil 读取配置文件
  ```

- ``` 
  JsonUtil 操作json
  ```

- ``` 
  MallUtils 解析页面
  ```

- ``` 
  MD5Util 加密解密
  ```

- ``` 
  NumberUtil 生成指定位数的数字	
  ```

- ```
  PageQueryUtil 分页查询
  ```

- ``` 
  PageResult 分页总信息
  ```

- ```
  PatternUtil 生成正则
  ```

- ```
  RedisUtil 操作Redis
  ```

- ``` 
  Result 自定义结果集
  ```

- ```
  ResultGenerator 封装全局结果集
  ```

- ```
  SystemUtil 生成token
  ```



###  2.5  org.group.mall.entity包

- ```
  AdminUser 管理员
  ```

- ```
  Carousel 轮播图
  ```

- ```
  Goods 商品
  ```

- ```
  GoodsCategory 商品分类
  ```

- ```
  GroupToRedis 拼团商品到Redis
  ```

- ```
  GroupWork 拼团商品
  ```

- ``` 
  IndexConfig 首页
  ```

- ```
  Notice 公告
  ```

- ```
  Order 订单
  ```

- ```
  OrderItem 订单项
  ```

- ```
  ShoppingCartItem 购物车项
  ```

- ```
  StockNumDTO 修改库存
  ```

- ```
  User 用户
  ```



### 2.6  org.group.mall.dao包

- ```
  AdminUserMapper 关联AdminUserMapper.xml
  ```

- ```
  CarouselMapper 关联CarouselMapper.xml
  ```

- ```
  GoodsCategoryMapper 关联GoodsCategoryMapper.xml
  ```

- ```
  GoodsMapper 关联GoodsMapper.xml
  ```

- ```
  GroupMapper 关联GroupMapper.xml
  ```

- ```
  GroupToRedisMapper 关联GroupToRedisMapper.xml
  ```

- ```
  GroupWorkUMapper 关联GroupWorkUMapper.xml
  ```

- ```
  IndexConfigMapper 关联IndexConfigMapper.xml
  ```

- ```
  MallNoticeMapper 关联MallNoticeMapper.xml
  ```

- ```
  MallUserMapper 关联MallUserMapper.xml
  ```

- ```
  OrderItemMapper 关联OrderItemMapper.xml
  ```

- ```
  OrderMapper 关联OrderMapper.xml
  ```

- ```
  ShoppingCartItemMapper 关联ShoppingCartItemMapper.xml
  ```



### 2.7  org.group.mall.service.impl包

- ```
  AdminUserServiceImpl 实现 AdminUserService
  ```

- ```
  CarouselServiceImpl 实现 CarouselService
  ```

- ```
  CategoryServiceImpl 实现 CategoryService
  ```

- ```
  GoodsServiceImpl 实现 GoodsService
  ```

- ```
  GroupServiceImpl 实现 GroupService
  ```

- ```
  GroupWorkServiceImpl 实现 GroupWorkService
  ```

- ```
  IndexConfigServiceImpl 实现 IndexConfigService
  ```

- ```
  NoticeServiceImpl 实现 NoticeService
  ```

- ```
  OrderServiceImpl 实现 OrderService
  ```

- ```
  ShoppingCartServiceImpl 实现 ShoppingCartService
  ```

- ```
  UserServiceImpl 实现 MallUserService
  ```



### 2.8  org.group.mall.controller包

##### 2.8.1 admin包(管理员管理)

- ```
  AdminController
  ```

- ```
  CarouselController
  ```

- ```
  GoodsCategoryController
  ```

- ```
  GoodsIndexConfigController
  ```

- ```
  GroupController
  ```

- ```
  MallGoodsController
  ```

- ```
  MallOrderController
  ```

- ```
  MallUserController
  ```

- ```
  NoticeController
  ```

##### 2.8.2 common包
- ```
  CommonController 验证码
  ```
- ```
  ErrorPageController 错误页面
  ```
- ```
   GlobalExceptionHandler 全局异常
  ```
- ```
  UploadController 文件上传下载
  ```

##### 2.8.3 mall包(用户使用)
- ```
  GoodsController
  ```
- ```
  GroupWorkController
  ```
- ```
  IndexController
  ```
- ```
 MallNoticeController
  ```
- ```
  OrderController
  ```
- ```
  PersonalController
  ```
- ```
  ShoppingCartController
  ```
##### 2.8.4 vo包(返回数据的封装)



### 2.9  static文件  管理端和用户端的静态资源



### 2.10  template文件  管理端和用户端的页面












