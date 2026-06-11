
# 高校志愿者团学组织人员管理系统 - 第二阶段开发计划

---

## 阶段概述

**阶段名称**：业务功能模块  
**周期**：第3-4周  
**核心目标**：完成活动报名管理、值班管理和服务队加入功能，实现核心业务流程

---

## 1. 数据库设计（续）

### 1.1 活动表（activity）

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 活动ID |
| name | VARCHAR(100) | NOT NULL | 活动名称 |
| theme | VARCHAR(200) | | 活动主题 |
| start_time | DATETIME | NOT NULL | 活动开始时间 |
| end_time | DATETIME | NOT NULL | 活动结束时间 |
| location | VARCHAR(200) | | 活动地点 |
| description | TEXT | | 活动描述 |
| max_participants | INT | DEFAULT 0 | 招募人数上限 |
| signup_deadline | DATETIME | | 报名截止时间 |
| team_id | BIGINT | FOREIGN KEY | 所属服务队ID |
| status | VARCHAR(20) | NOT NULL | 状态：PREPARING/IN_PROGRESS/ENDED |
| created_by | BIGINT | FOREIGN KEY | 创建人ID |
| created_at | DATETIME | NOT NULL | 创建时间 |

### 1.2 活动报名表（activity_signup）

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 报名ID |
| activity_id | BIGINT | FOREIGN KEY | 活动ID |
| member_id | BIGINT | FOREIGN KEY | 报名成员ID |
| status | VARCHAR(20) | NOT NULL | 状态：PENDING/APPROVED/REJECTED/CANCELLED |
| apply_time | DATETIME | NOT NULL | 报名时间 |
| approve_time | DATETIME | | 审核时间 |
| approve_by | BIGINT | FOREIGN KEY | 审核人ID |

### 1.3 值班表（duty）

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 值班ID |
| date | DATE | NOT NULL | 值班日期 |
| period | VARCHAR(20) | NOT NULL | 时段：MORNING/AFTERNOON/EVENING |
| location | VARCHAR(200) | | 值班地点 |
| member_id | BIGINT | FOREIGN KEY | 值班人员ID |
| team_id | BIGINT | FOREIGN KEY | 所属服务队ID |
| status | VARCHAR(20) | NOT NULL | 状态：SCHEDULED/CHECKED_IN/ABSENT |
| check_in_time | DATETIME | | 签到时间 |
| created_by | BIGINT | FOREIGN KEY | 创建人ID |

### 1.4 通知表（notification）

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 通知ID |
| user_id | BIGINT | FOREIGN KEY | 目标用户ID |
| type | VARCHAR(20) | NOT NULL | 类型：SYSTEM/ACTIVITY/DUTY/TEAM |
| title | VARCHAR(100) | NOT NULL | 通知标题 |
| content | TEXT | | 通知内容 |
| is_read | BOOLEAN | DEFAULT FALSE | 是否已读 |
| created_at | DATETIME | NOT NULL | 创建时间 |

---

## 2. 功能开发清单

### 2.1 活动报名管理模块

| 功能点 | 描述 | 接口设计 | 关联模块 |
| :--- | :--- | :--- | :--- |
| 获取活动列表 | 分页获取活动，支持筛选 | `GET /api/activities` | activity |
| 获取活动详情 | 获取活动详细信息及报名情况 | `GET /api/activities/{id}` | activity, activity_signup |
| 创建活动 | 创建新活动 | `POST /api/activities` | activity |
| 更新活动 | 修改活动信息 | `PUT /api/activities/{id}` | activity |
| 删除活动 | 删除活动记录 | `DELETE /api/activities/{id}` | activity |
| 报名活动 | 成员报名参加活动 | `POST /api/activities/{id}/signup` | activity_signup |
| 审核报名 | 管理员审核报名申请 | `PUT /api/activities/{id}/signup/{signupId}/approve` | activity_signup |
| 取消报名 | 成员取消报名 | `DELETE /api/activities/{id}/signup` | activity_signup |
| 活动统计 | 统计活动参与人数 | `GET /api/activities/{id}/statistics` | activity_signup |

**活动创建请求体**：
```json
{
  "name": "string (必填)",
  "theme": "string",
  "startTime": "string (yyyy-MM-dd HH:mm:ss, 必填)",
  "endTime": "string (yyyy-MM-dd HH:mm:ss, 必填)",
  "location": "string",
  "description": "string",
  "maxParticipants": "number",
  "signupDeadline": "string (yyyy-MM-dd HH:mm:ss)",
  "teamId": "number"
}
```

### 2.2 值班管理模块

| 功能点 | 描述 | 接口设计 | 关联模块 |
| :--- | :--- | :--- | :--- |
| 获取值班列表 | 获取值班安排列表 | `GET /api/duties` | duty |
| 获取值班详情 | 获取单个值班记录 | `GET /api/duties/{id}` | duty |
| 创建值班 | 管理员创建值班安排 | `POST /api/duties` | duty |
| 更新值班 | 修改值班信息 | `PUT /api/duties/{id}` | duty |
| 删除值班 | 删除值班记录 | `DELETE /api/duties/{id}` | duty |
| 值班签到 | 成员签到确认值班 | `PUT /api/duties/{id}/check-in` | duty |
| 申请换班 | 成员申请调换值班 | `POST /api/duties/{id}/swap` | duty |
| 值班统计 | 统计成员值班时长 | `GET /api/duties/statistics/{memberId}` | duty |

**值班创建请求体**：
```json
{
  "date": "string (yyyy-MM-dd, 必填)",
  "period": "string (MORNING/AFTERNOON/EVENING, 必填)",
  "location": "string",
  "memberId": "number (必填)",
  "teamId": "number"
}
```

### 2.3 服务队功能完善

| 功能点 | 描述 | 接口设计 | 关联模块 |
| :--- | :--- | :--- | :--- |
| 加入服务队 | 成员申请加入 | `POST /api/teams/{id}/join` | member_team |
| 退出服务队 | 成员退出 | `DELETE /api/teams/{id}/leave` | member_team |
| 审核申请 | 队长审核加入申请 | `PUT /api/teams/{id}/applications/{memberId}/approve` | member_team |
| 获取成员列表 | 获取服务队成员 | `GET /api/teams/{id}/members` | member_team, member |
| 服务队统计 | 统计服务队活动和时长 | `GET /api/teams/{id}/statistics` | activity, duty |

### 2.4 通知消息模块

| 功能点 | 描述 | 接口设计 | 关联模块 |
| :--- | :--- | :--- | :--- |
| 获取通知列表 | 获取当前用户通知 | `GET /api/notifications` | notification |
| 获取通知详情 | 获取单个通知 | `GET /api/notifications/{id}` | notification |
| 标记已读 | 标记通知为已读 | `PUT /api/notifications/{id}/read` | notification |
| 批量已读 | 批量标记已读 | `PUT /api/notifications/read-all` | notification |
| 发送通知 | 管理员发送通知 | `POST /api/notifications` | notification |

---

## 3. 前端页面开发

### 3.1 活动管理页面

**页面路径**：`/activities`  
**功能**：
- 活动列表（时间线形式）
- 活动卡片展示（名称、时间、地点、招募状态）
- 创建活动按钮
- 报名/取消报名操作

### 3.2 活动详情页面

**页面路径**：`/activities/{id}`  
**功能**：
- 活动详细信息展示
- 报名按钮（未报名时）
- 报名列表（管理员视角）
- 审核操作（管理员视角）

### 3.3 值班管理页面

**页面路径**：`/duties`  
**功能**：
- 日历视图（标注值班日期）
- 值班表格（日期、时段、人员）
- 签到按钮（当天值班）
- 创建值班按钮

### 3.4 服务队页面

**页面路径**：`/teams`  
**功能**：
- 四个服务队卡片展示
- 服务队详情弹窗
- 加入/退出按钮
- 成员列表展示

### 3.5 消息中心页面

**页面路径**：`/notifications`  
**功能**：
- 消息列表（按时间倒序）
- 消息分类标签
- 未读消息红点提示
- 批量已读按钮

---

## 4. 业务流程实现

### 4.1 活动报名流程

```
成员查看活动列表 → 选择活动 → 提交报名申请 → 管理员审核 → 
├─ 通过 → 创建通知 → 成员参加活动 → 更新志愿时长
└─ 拒绝 → 创建通知 → 报名失败
```

### 4.2 值班安排流程

```
管理员创建值班计划 → 分配值班人员 → 创建通知 → 
成员查看通知 → 确认值班 → 值班当天签到 → 更新值班状态
```

### 4.3 服务队加入流程

```
成员选择服务队 → 提交申请 → 创建通知 → 服务队队长审核 → 
├─ 通过 → 更新成员-服务队关联 → 创建通知 → 加入成功
└─ 拒绝 → 创建通知 → 申请失败
```

---

## 5. 数据一致性保障

### 5.1 事务管理
- 活动报名时：检查活动状态、检查报名人数、创建报名记录
- 值班签到时：检查时间有效性、更新签到状态、累计值班时长
- 服务队加入时：检查申请状态、更新关联关系、发送通知

### 5.2 状态一致性
- 活动状态流转：PREPARING → IN_PROGRESS → ENDED
- 报名状态流转：PENDING → APPROVED/REJECTED/CANCELLED
- 值班状态流转：SCHEDULED → CHECKED_IN/ABSENT

### 5.3 消息触发时机
| 场景 | 消息类型 | 接收人 |
| :--- | :--- | :--- |
| 报名提交 | ACTIVITY | 管理员 |
| 报名审核通过 | ACTIVITY | 报名成员 |
| 报名审核拒绝 | ACTIVITY | 报名成员 |
| 活动即将开始 | ACTIVITY | 已报名成员 |
| 值班安排 | DUTY | 值班成员 |
| 值班签到提醒 | DUTY | 值班成员 |
| 服务队申请提交 | TEAM | 服务队队长 |
| 服务队申请通过 | TEAM | 申请成员 |
| 服务队申请拒绝 | TEAM | 申请成员 |

---

## 6. 阶段交付物

| 交付物 | 说明 | 负责人 |
| :--- | :--- | :--- |
| 数据库脚本 | 活动表、报名表、值班表、通知表 | 开发工程师 |
| 后端API接口 | 活动管理、值班管理、通知管理 | 后端开发 |
| 前端页面 | 活动管理、值班管理、服务队、消息中心 | 前端开发 |
| 业务流程测试 | 报名流程、值班流程、服务队流程 | 测试工程师 |

---

## 7. 阶段验收标准

| 验收项 | 标准 |
| :--- | :--- |
| 活动创建 | 成功创建活动，数据持久化 |
| 活动报名 | 成员报名、管理员审核流程正常 |
| 值班安排 | 管理员创建值班，成员签到正常 |
| 服务队加入 | 申请、审核流程正常 |
| 消息通知 | 各场景消息正确发送和接收 |
| 数据一致性 | 状态流转正确，关联数据同步 |
| 界面交互 | 操作流畅，响应及时 |
