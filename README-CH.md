# jc-mybatis-extension
## 简介
A extension module of mybatis . Used to generate Service Class of corresponding Mapper Class
基于Mybatis generator的一个扩展模块，用于自动生成和Mapper对应的service层代码。

## 使用
### 步骤1
将如下groovy代码添加的项目的gradle.build
<allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}>

### 步骤2
添加依赖

dependencies {
        implementation 'com.github.zerochen2016:jc-mybatis-extension:{VERSION}'
}
你可以在 https://jitpack.io 搜索 https://github.com/zerochen2016/jc-mybatis-extension.git 查看版本

## 怎么使用

1. 在 src/resources/mybatis 目录下添加mybatis-extension.yml，修改配置
2. 像示例一样运行
### 示例
	<public static void main(String args[]) throws Exception {ServiceGenerator.generate("table_name");}>
