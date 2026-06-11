
# 高校志愿者团学组织人员管理系统 - 第一阶段开发计划

---

## 阶段概述

**阶段名称**：基础架构与核心模块  
**周期**：第1-2周  
**核心目标**：搭建系统基础架构，完成用户认证和成员信息管理核心功能

---

## 1. 技术方案设计

### 1.1 技术选型

| 分类 | 技术 | 版本 | 说明 |
| :--- | :--- | :--- | :--- |
| 前端框架 | Vue.js | 3.x | 渐进式JavaScript框架 |
| UI组件库 | Element Plus | 2.x | 基于Vue3的组件库，支持红色主题 |
| 后端框架 | Spring Boot | 3.x | Java企业级开发框架 |
| 数据库 | MySQL | 8.0+ | 关系型数据库 |
| 缓存 | Redis | 7.x | 缓存用户会话和热点数据 |
| 构建工具 | Maven | 3.8+ | Java项目管理工具 |
| 前端构建 | Vite | 6.x | 快速构建工具 |

### 1.2 架构设计

**架构风格**：前后端分离，RESTful API设计

**模块划分**：
- `controller`：对外REST API控制层
- `service`：业务逻辑层
- `repository`：数据访问层（基于Spring Data JPA）
- `entity`：数据库实体模型
- `dto`：数据传输对象
- `config`：配置类
- `security`：安全认证相关

### 1.3 数据库设计

#### 1.3.1 用户表（user）

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 用户ID |
| username | VARCHAR(50) | NOT NULL, UNIQUE | 用户名（学号） |
| password | VARCHAR(255) | NOT NULL | 加密后的密码 |
| role | VARCHAR(20) | NOT NULL | 角色：ADMIN/LEADER/CAPTAIN/MEMBER |
| status | VARCHAR(20) | NOT NULL | 状态：ACTIVE/INACTIVE |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

#### 1.3.2 成员信息表（member）

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 成员ID |
| user_id | BIGINT | FOREIGN KEY, UNIQUE | 关联用户ID |
| student_id | VARCHAR(20) | NOT NULL, UNIQUE | 学号 |
| name | VARCHAR(50) | NOT NULL | 姓名 |
| gender | VARCHAR(10) | | 性别 |
| grade | VARCHAR(20) | | 年级 |
| college | VARCHAR(100) | | 学院 |
| major | VARCHAR(100) | | 专业 |
| phone | VARCHAR(20) | | 手机号 |
| email | VARCHAR(100) | | 邮箱 |
| volunteer_hours | DECIMAL(10,2) | DEFAULT 0 | 志愿时长 |
| join_date | DATE | | 加入时间 |
| status | VARCHAR(20) | NOT NULL | 状态：ACTIVE/LEFT |

#### 1.3.3 服务队表（service_team）

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 服务队ID |
| name | VARCHAR(50) | NOT NULL, UNIQUE | 服务队名称 |
| description | VARCHAR(500) | | 服务宗旨 |
| icon | VARCHAR(200) | | 图标路径 |
| status | VARCHAR(20) | NOT NULL | 状态：ACTIVE/INACTIVE |

**初始化数据**：
| ID | 名称 | 描述 |
| :--- | :--- | :--- |
| 1 | 爱心家园服务队 | 关爱孤寡老人、残障人士，传递温暖 |
| 2 | 绿色环保服务队 | 环保宣传、校园美化、节能减排 |
| 3 | 文化之旅服务队 | 文化传承、艺术交流、知识普及 |
| 4 | 安全使者服务队 | 校园安全、消防宣传、应急协助 |

#### 1.3.4 成员-服务队关联表（member_team）

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| member_id | BIGINT | FOREIGN KEY | 成员ID |
| team_id | BIGINT | FOREIGN KEY | 服务队ID |
| join_date | DATE | NOT NULL | 加入服务队时间 |
| role | VARCHAR(20) | DEFAULT 'MEMBER' | 队内角色：CAPTAIN/MEMBER |

---

## 2. 功能开发清单

### 2.1 用户认证模块

| 功能点 | 描述 | 接口设计 | 状态 |
| :--- | :--- | :--- | :--- |
| 用户登录 | 用户名密码登录 | `POST /api/auth/login` | 待开发 |
| 用户登出 | 退出登录，清除会话 | `POST /api/auth/logout` | 待开发 |
| 密码修改 | 修改登录密码 | `PUT /api/auth/password` | 待开发 |
| 获取当前用户 | 获取登录用户信息 | `GET /api/auth/me` | 待开发 |

**登录请求体**：
```json
{
  "username": "string (学号)",
  "password": "string (密码)"
}
```

**登录响应体**：
```json
{
  "token": "string (JWT令牌)",
  "user": {
    "id": "number",
    "username": "string",
    "role": "string",
    "name": "string"
  }
}
```

### 2.2 成员信息管理模块

| 功能点 | 描述 | 接口设计 | 状态 |
| :--- | :--- | :--- | :--- |
| 获取成员列表 | 分页获取成员列表，支持筛选 | `GET /api/members` | 待开发 |
| 获取成员详情 | 根据ID获取成员详细信息 | `GET /api/members/{id}` | 待开发 |
| 添加成员 | 创建新成员记录 | `POST /api/members` | 待开发 |
| 更新成员 | 修改成员信息 | `PUT /api/members/{id}` | 待开发 |
| 删除成员 | 软删除成员（标记状态） | `DELETE /api/members/{id}` | 待开发 |
| 搜索成员 | 按姓名/学号搜索 | `GET /api/members/search` | 待开发 |
| 导出成员 | 导出Excel文件 | `GET /api/members/export` | 待开发 |

**成员创建请求体**：
```json
{
  "studentId": "string (必填)",
  "name": "string (必填)",
  "gender": "string",
  "grade": "string",
  "college": "string",
  "major": "string",
  "phone": "string",
  "email": "string",
  "joinDate": "string (yyyy-MM-dd)"
}
```

### 2.3 服务队管理模块（基础功能）

| 功能点 | 描述 | 接口设计 | 状态 |
| :--- | :--- | :--- | :--- |
| 获取服务队列表 | 获取所有服务队 | `GET /api/teams` | 待开发 |
| 获取服务队详情 | 获取服务队信息及成员 | `GET /api/teams/{id}` | 待开发 |

---

## 3. 前端页面开发

### 3.1 登录页面

**页面路径**：`/login`  
**功能**：
- 用户名/密码输入框
- 登录按钮
- 红色主题风格

### 3.2 首页/仪表盘

**页面路径**：`/dashboard`  
**功能**：
- 统计卡片：总成员数、今日活动、待处理通知
- 快捷入口：成员管理、活动报名、值班安排、消息中心
- 红色主题导航栏

### 3.3 成员管理页面

**页面路径**：`/members`  
**功能**：
- 搜索框（姓名/学号）
- 筛选条件（学院、服务队、年级）
- 成员列表（卡片式）
- 添加成员按钮
- 编辑/删除操作按钮

---

## 4. 数据一致性保障

### 4.1 事务管理
- 使用Spring @Transactional注解管理数据库事务
- 成员创建时同时创建用户账户

### 4.2 数据校验
- 使用JSR-380进行参数校验
- 前端表单验证与后端双重校验

### 4.3 数据初始化
- 初始化四个服务队基础数据
- 创建默认管理员账户

---

## 5. 阶段交付物

| 交付物 | 说明 | 负责人 |
| :--- | :--- | :--- |
| 技术方案文档 | 架构设计、数据库设计 | 架构师 |
| 数据库脚本 | 建表语句、初始化数据 | 开发工程师 |
| 后端API接口 | 用户认证、成员管理、服务队管理 | 后端开发 |
| 前端页面 | 登录、仪表盘、成员管理 | 前端开发 |
| 单元测试 | 用户认证、成员管理测试用例 | 测试工程师 |

---

## 6. 阶段验收标准

| 验收项 | 标准 |
| :--- | :--- |
| 用户登录 | 正确验证用户名密码，返回JWT令牌 |
| 成员列表 | 支持分页、筛选、搜索功能 |
| 成员CRUD | 增删改查操作正常，数据持久化 |
| 服务队展示 | 显示四个服务队信息 |
| 界面风格 | 红色主题，符合设计规范 |
| 响应时间 | 接口响应时间 < 1秒 |
