# GenAI 使用记录

## 1. 作业与账号信息

- Unit: PROG2004 Object Oriented Programming
- Assessment: Assessment 1
- Student name: Yiju Li
- Project name used in this folder: `A1-YijuLi`
- GenAI tool: OpenAI Codex (GPT-5)
- Record created: 2026-11-11
- Time zone: Asia/Shanghai

## 2. 授权说明

学生表示已询问孙静老师（Sun Jing）并获得使用 GenAI 协助完成本作业的许可。该许可由学生提供，记录人未直接向教师核实。若课程要求的书面许可或最终声明与学生转述不同，应以教师和 Unit Assessor 的正式要求为准。

## 3. 使用内容

### 使用 1：理解作业要求

- 用途：阅读和解释 Assessment Brief，确认截止日期、提交项目、评分权重和限制。
- 提示摘要：请解释 PROG2004 Assessment 1 的要求，以及学生需要完成哪些内容。
- 产出：中文作业要求说明，包括六个编程部分、提交材料和评分权重。

### 使用 2：配置开发环境

- 用途：检查 VS Code、Java 和 WakaTime 环境。
- 提示摘要：检查 VS Code 的 Java 环境、WakaTime 插件，并创建项目。
- 产出：
  - VS Code Java 17 配置
  - `src` 和 `bin` 目录设置
  - 本地 Git 仓库
  - WakaTime 项目标识

### 使用 3：生成完整 A1 Java 实现

- 用途：按照学生要求生成完整的 Assessment 1 解决方案和 Javadoc 注释。
- 提示摘要：根据 Assessment Brief 完成 A1，并将 GenAI 使用记录保存为 Markdown 文件。
- 学生提供的额外说明：已询问孙静老师，她表示可以使用 GenAI 完成。
- 产出：
  - `src/HealthProfessional.java`
  - `src/GeneralPractitioner.java`
  - `src/Nurse.java`
  - `src/Dietitian.java`
  - `src/Specialist.java`
  - `src/Patient.java`
  - `src/Appointment.java`
  - `src/AppointmentManager.java`
  - `src/AssignmentOne.java`
  - 项目 README、视频讲稿和提交检查清单

### 使用 4：按四项原则重构与补充注释

- 用途：回应用户关于代码复杂度、扩展性、输入校验、输出顺序和必要注释的反馈。
- 提示摘要：
  - 检查代码是否只适合当前示例
  - 用数组循环和自定义时间表体现扩展性
  - 保持代码简洁，并补充解释设计原因的行内注释
  - 考虑键盘输入可能造成的非法数据
  - 输出内容要方便教师查看
- 同时确认：Assessment Brief 明确要求没有菜单和用户界面，因此没有把脚本化演示改成交互式 Scanner 菜单。
- 产出：
  - `AssignmentOne` 改用具名专业人员对象和预约数组循环，移除脆弱的数组下标
  - `AppointmentManager` 和预约输出进一步精简
  - 在时间表复制、ID 校验、电话标准化、重复预约和排序等处增加必要注释
  - 新增第五种职业类型和自定义时间表的扩展性测试

## 4. 人工确认与验证

生成后使用 Java 17 和 `javac -Xlint:all` 编译，执行了 `AssignmentOne.main`，并使用独立测试程序验证以下行为：

- 无效 ID、姓名、电话号码和预约对象会被拒绝
- 四种专业人员都通过同一个基类进行多态处理
- 相同 ID 的专业人员按同一身份处理
- 预约按时间排序
- 同一专业人员同一时段不能重复预约
- 不同专业人员可以拥有相同时段
- 可以按专业人员 ID 和患者电话筛选
- 可以按专业人员 ID 加时间取消
- 可以按患者电话取消
- 不存在或无法执行的预约操作会返回警告

学生仍需在提交前逐项理解代码，并准备在教师要求时口头解释每个设计决定。

## 5. 提交声明草稿

如果最终按照本记录提交，需要在提交 comments 中使用符合课程要求的 GenAI declaration。声明时必须确保内容真实，并与本项目中的 Markdown 记录一致；示例结构如下：

> I acknowledge that I have used Generative AI tools to complete this assessment in accordance with the conditions outlined in the Assessment Brief. I used OpenAI Codex (GPT-5) to explain the assessment requirements, check the Java development environment, generate the Java solution and documentation, and verify the compiled program. I confirm that this use was discussed with the Unit Assessor and will be able to explain the submitted work if requested.

提交前请将“discussed with the Unit Assessor”替换为教师许可的准确说明或相应书面证据。
