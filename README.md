# jc-mybatis-extension
## Introduction
A extension module of mybatis . Used to generate Service Class of corresponding Mapper Class

## How to get 
### Step 1. 
Add the JitPack repository to your build file

allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
### Step 2. 
Add the dependency

dependencies {
        implementation 'com.github.zerochen2016:jc-mybatis-extension:{VERSION}'
}
You can get Version in https://jitpack.io Visit and search https://github.com/zerochen2016/jc-mybatis-extension.git

## How to use

1. Create generatorConfig.xml And mybatis-extension.yml in src/resources/mybatis
2. Run like demo
Demo 

	public static void main(String args[]) throws Exception {
		ServiceGenerator.generate("table_name");
	}
