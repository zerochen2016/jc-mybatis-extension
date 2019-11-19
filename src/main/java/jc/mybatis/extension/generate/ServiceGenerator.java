package jc.mybatis.extension.generate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import jc.mybatis.extension.entity.DBInfo;
import jc.mybatis.extension.entity.IndexInfo;
import jc.mybatis.extension.util.DBUtil;
import jc.mybatis.extension.util.StringUtil;

public class ServiceGenerator {

	public static void generate(String tableName, String projectPath, String servicePackage, String mapperPackage,
			String entityPackage, String entityExamplePackage, String url, String user, String password, String driverClassName) 
					throws IOException {
		
		String servicePackagePath = servicePackage.replace(".", "/");
		String servicePath = new StringBuffer(projectPath).append("/src/main/java/")
				.append(servicePackagePath).append("/").toString();
		String serviceImplPath = new StringBuffer(servicePath).append("impl/").toString();
		System.out.println("----------------------------------------");
		System.out.println("servicePackagePath = " + servicePackagePath);
		System.out.println("servicePath = " + servicePath);
		System.out.println("serviceImplPath = " + serviceImplPath);
		File serviceDir = new File(serviceImplPath);
		if (!serviceDir.exists()) {
			serviceDir.mkdirs();
		}
		String tableNameCamel = StringUtil.underlineToCamel(tableName);
		String tableNameClass = StringUtil.firstLetterUpper(tableNameCamel);
		File serviceFile = new File(
				new StringBuffer(servicePath).append(tableNameClass).append("Service.java").toString());
		File serviceImplFile = new File(
				new StringBuffer(serviceImplPath).append(tableNameClass).append("ServiceImpl.java").toString());
		
		if (!serviceFile.exists()) {
			serviceFile.createNewFile();
		}
		if (!serviceImplFile.exists()) {
			serviceImplFile.createNewFile();
		} 
		List<IndexInfo> indexes = DBUtil.listIndexInfo(new DBInfo(url, user, password, driverClassName), tableName);
		List<String> serviceLines = getServiceContent(tableNameClass, entityPackage, servicePackage, indexes);
		List<String> serviceImplLines = getServiceImplContent(tableNameClass,tableNameCamel, entityPackage, entityExamplePackage,
				mapperPackage, servicePackage, indexes);
		
		FileUtils.writeLines(serviceFile, serviceLines, false);
		FileUtils.writeLines(serviceImplFile, serviceImplLines, false);
	}
	
	public static List<String> getServiceImplContent(String tableNameClass, String tableNameCamel, String entityPackage,
			String entityExamplePackage, String mapperPackage, String servicePackage, List<IndexInfo> indexes){
		List<String> serviceImplLines = new ArrayList<String>();
		serviceImplLines.add(new StringBuffer("package ").append(servicePackage).append(".impl;").toString());
		serviceImplLines.add("\n");
		serviceImplLines.add("import jc.mybatis.extension.util.ExampleBuildUtil;");
		serviceImplLines.add("import jc.mybatis.extension.util.PageModel;");
		serviceImplLines.add("\n");
		serviceImplLines.add("import java.util.List;");
		serviceImplLines.add("\n");
		serviceImplLines.add("import org.springframework.beans.factory.annotation.Autowired;");
		serviceImplLines.add("import org.springframework.util.CollectionUtils;");
		serviceImplLines.add(new StringBuffer("import ").append(entityPackage).append(".").append(tableNameClass).append(";").toString());
		serviceImplLines.add(new StringBuffer("import ").append(entityExamplePackage).append(".").append(tableNameClass).append("Example;").toString());
		serviceImplLines.add(new StringBuffer("import ").append(mapperPackage).append(".").append(tableNameClass).append("Mapper;").toString());
		serviceImplLines.add(new StringBuffer("import ").append(servicePackage).append(".").append(tableNameClass).append("Service;").toString());
		serviceImplLines.add("\n");
		serviceImplLines.add(new StringBuffer("public class ").append(tableNameClass).append("ServiceImpl implements ").append(tableNameClass).append("Service {").toString());
		serviceImplLines.add("\n");
		serviceImplLines.add("\t@Autowired");
		serviceImplLines.add(new StringBuffer("\t").append(tableNameClass).append("Mapper ").append(tableNameCamel).append("Mapper;").toString());
		serviceImplLines.add("\n");
		
		String newExample = new StringBuffer("\t\t").append(tableNameClass).append("Example example = new ")
				.append(tableNameClass).append("Example();").toString();
		String buildExample = new StringBuffer("\t\t").append("ExampleBuildUtil.buildExample(").append(tableNameClass)
				.append(".class, record, example);").toString();
		String list = new StringBuffer("\t\tList<").append(tableNameClass).append("> list = this.").append(tableNameCamel)
				.append("Mapper.selectByExample(example);").toString();
		//page
		serviceImplLines.add("\t@Override");
		serviceImplLines.add(new StringBuffer("\tpublic ").append("PageModel<").append(tableNameClass).append("> page")
				.append(tableNameClass).append("(").append(tableNameClass).append(" record, PageModel<")
				.append(tableNameClass).append("> pageModel) {").toString());
		serviceImplLines.add(newExample);
		serviceImplLines.add(buildExample);
		serviceImplLines.add("\t\tExampleBuildUtil.setPageParam(pageModel, example);");
		serviceImplLines.add(list);
		serviceImplLines.add(new StringBuffer("\t\tlong totalCount = this.").append(tableNameCamel).append("Mapper.countByExample(example);").toString());
		serviceImplLines.add("\t\treturn pageModel.build(list, totalCount, pageModel);");
		serviceImplLines.add("\t}");
		serviceImplLines.add("\n");
		
		//list
		serviceImplLines.add("\t@Override");
		serviceImplLines.add(new StringBuffer("\tpublic List<").append(tableNameClass).append("> list").append(tableNameClass)
				.append("(").append(tableNameClass).append(" record) {").toString());
		serviceImplLines.add(newExample);
		serviceImplLines.add(buildExample);
		serviceImplLines.add(list);
		serviceImplLines.add("\t\treturn list;");
		serviceImplLines.add("\t}");
		serviceImplLines.add("\n");

		//get
		serviceImplLines.add("\t@Override");
		serviceImplLines.add(new StringBuffer("\tpublic ").append(tableNameClass).append(" get").append(tableNameClass).append("(").append(tableNameClass)
				.append(" record) {").toString());
		serviceImplLines.add(newExample);
		serviceImplLines.add(buildExample);
		serviceImplLines.add("\t\texample.setLimitStart(0);\n\t\texample.setLimitLength(1);");
		serviceImplLines.add(list);
		serviceImplLines.add("\t\tif (CollectionUtils.isEmpty(list)) {\n\t\t\treturn null;\n\t\t}\n\t\treturn list.get(0);");
		serviceImplLines.add("\t}");
		serviceImplLines.add("\n");
		
		indexes.stream().forEach(x->{
			if(x.getUnique()) {
				//get by
				serviceImplLines.add("\t@Override");
				serviceImplLines.add(new StringBuffer("\tpublic ").append(tableNameClass).append(" get").append(tableNameClass).append("By")
						.append(StringUtil.underlineToCamelFirstLetterUpper(x.getColumnName())).append("(").append(x.getColumnType())
						.append(" ").append(StringUtil.underlineToCamel(x.getColumnName())).append(") {").toString());
				serviceImplLines.add(newExample);
				serviceImplLines.add(new StringBuffer("\t\texample.createCriteria().and")
						.append(StringUtil.underlineToCamelFirstLetterUpper(x.getColumnName()))
						.append("EqualTo(").append(StringUtil.underlineToCamel(x.getColumnName())).append(");").toString());
				serviceImplLines.add("\t\texample.setLimitStart(0);\n\t\texample.setLimitLength(1);");
				serviceImplLines.add(list);
				serviceImplLines.add("\t\tif (CollectionUtils.isEmpty(list)) {\n\t\t\treturn null;\n\t\t}\n\t\treturn list.get(0);");
				serviceImplLines.add("\t}");
				serviceImplLines.add("\n");
				
			}else {
				//list by
				serviceImplLines.add("\t@Override");
				serviceImplLines.add(new StringBuffer("\tpublic ").append("List<").append(tableNameClass).append("> list").append(tableNameClass).append("By")
						.append(StringUtil.underlineToCamelFirstLetterUpper(x.getColumnName())).append("(").append(x.getColumnType())
						.append(" ").append(StringUtil.underlineToCamel(x.getColumnName())).append(") {").toString());
				serviceImplLines.add(newExample);
				serviceImplLines.add(new StringBuffer("\t\texample.createCriteria().and")
						.append(StringUtil.underlineToCamelFirstLetterUpper(x.getColumnName()))
						.append("EqualTo(").append(StringUtil.underlineToCamel(x.getColumnName())).append(");").toString());
				serviceImplLines.add(list);
				serviceImplLines.add("\t\treturn list;");
				serviceImplLines.add("\t}");
				serviceImplLines.add("\n");
				
			}
			
			//update by
			serviceImplLines.add("\t@Override");
			serviceImplLines.add(new StringBuffer("\tpublic ").append("int").append(" update").append(tableNameClass).append("By")
					.append(StringUtil.underlineToCamelFirstLetterUpper(x.getColumnName())).append("(").append(tableNameClass).append(" record,")
					.append(x.getColumnType()).append(" ").append(StringUtil.underlineToCamel(x.getColumnName())).append(") {").toString());
			serviceImplLines.add(newExample);
			serviceImplLines.add(new StringBuffer("\t\texample.createCriteria().and")
					.append(StringUtil.underlineToCamelFirstLetterUpper(x.getColumnName()))
					.append("EqualTo(").append(StringUtil.underlineToCamel(x.getColumnName())).append(");").toString());
			serviceImplLines.add(new StringBuffer("\t\treturn this.").append(tableNameCamel)
					.append("Mapper.updateByExampleSelective(record, example);").toString());
			serviceImplLines.add("\t}");
			serviceImplLines.add("\n");
			
			//delete by
			serviceImplLines.add("\t@Override");
			serviceImplLines.add(new StringBuffer("\tpublic ").append("int").append(" delete").append(tableNameClass).append("By")
					.append(StringUtil.underlineToCamelFirstLetterUpper(x.getColumnName())).append("(").append(x.getColumnType())
					.append(" ").append(StringUtil.underlineToCamel(x.getColumnName())).append(") {").toString());
			serviceImplLines.add(newExample);
			serviceImplLines.add(new StringBuffer("\t\texample.createCriteria().and")
					.append(StringUtil.underlineToCamelFirstLetterUpper(x.getColumnName()))
					.append("EqualTo(").append(StringUtil.underlineToCamel(x.getColumnName())).append(");").toString());
			serviceImplLines.add(new StringBuffer("\t\treturn this.").append(tableNameCamel)
					.append("Mapper.deleteByExample(example);").toString());
			serviceImplLines.add("\t}");
			serviceImplLines.add("\n");
			
		});
		
		serviceImplLines.add("\t@Override");
		serviceImplLines.add(new StringBuffer("\tpublic int insert").append(tableNameClass).append("(")
				.append(tableNameClass).append(" record) {").toString());
		serviceImplLines.add(new StringBuffer("\t\treturn this.").append(tableNameCamel)
				.append("Mapper.insertSelective(record);").toString());
		serviceImplLines.add("\t}");
		serviceImplLines.add("\n");
		
		serviceImplLines.add("\t@Override");
		serviceImplLines.add(new StringBuffer("\tpublic int update").append(tableNameClass).append("(").append(tableNameClass).append(" record) {").toString());
		serviceImplLines.add(newExample);
		serviceImplLines.add(buildExample);
		serviceImplLines.add(new StringBuffer("\t\treturn this.").append(tableNameCamel)
				.append("Mapper.updateByExampleSelective(record, example);").toString());
		serviceImplLines.add("\t}");
		serviceImplLines.add("\n");
		
		serviceImplLines.add("\t@Override");
		serviceImplLines.add(new StringBuffer("\tpublic int delete").append(tableNameClass).append("(").append(tableNameClass).append(" record) {").toString());
		serviceImplLines.add(newExample);
		serviceImplLines.add(buildExample);
		serviceImplLines.add(new StringBuffer("\t\treturn this.").append(tableNameCamel)
				.append("Mapper.deleteByExample(example);").toString());
		serviceImplLines.add("\t}");
		serviceImplLines.add("\n");
		
		serviceImplLines.add("}");

		return serviceImplLines;
	}
	
	public static List<String> getServiceContent(String tableNameClass, String entityPackage,
			String servicePackage, List<IndexInfo> indexes){
		List<String> serviceLines = new ArrayList<String>();
		serviceLines.add(new StringBuffer("package ").append(servicePackage).append(";").toString());
		serviceLines.add("\n");
		serviceLines.add("import jc.mybatis.extension.util.PageModel;");
		serviceLines.add("\n");
		serviceLines.add("import java.util.List;");
		serviceLines.add(new StringBuffer("import ").append(entityPackage).append(".").append(tableNameClass).append(";").toString());
		serviceLines.add("\n");
		serviceLines.add(new StringBuffer("public interface ").append(tableNameClass).append("Service {").toString());
		serviceLines.add("\n");
		serviceLines.add(new StringBuffer("\tPageModel<").append(tableNameClass).append("> page")
				.append(tableNameClass).append("(").append(tableNameClass).append(" record, PageModel<")
				.append(tableNameClass).append("> pageModel);").toString());
		serviceLines.add("\n");
		serviceLines.add(new StringBuffer("\tList<").append(tableNameClass).append("> list").append(tableNameClass)
				.append("(").append(tableNameClass).append(" record);").toString());
		serviceLines.add("\n");
		serviceLines.add(new StringBuffer("\t").append(tableNameClass).append(" get").append(tableNameClass).append("(").append(tableNameClass)
				.append(" record);").toString());
		serviceLines.add("\n");
		indexes.stream().forEach(x->{
			if(x.getUnique()) {
				serviceLines.add(new StringBuffer("\t").append(tableNameClass).append(" get").append(tableNameClass).append("By")
						.append(StringUtil.underlineToCamelFirstLetterUpper(x.getColumnName())).append("(").append(x.getColumnType())
						.append(" ").append(StringUtil.underlineToCamel(x.getColumnName())).append(");").toString());
				serviceLines.add("\n");
			}else {
				serviceLines.add(new StringBuffer("\t").append("List<").append(tableNameClass).append("> list").append(tableNameClass).append("By")
						.append(StringUtil.underlineToCamelFirstLetterUpper(x.getColumnName())).append("(").append(x.getColumnType())
						.append(" ").append(StringUtil.underlineToCamel(x.getColumnName())).append(");").toString());
				serviceLines.add("\n");
			}
			serviceLines.add(new StringBuffer("\t").append("int").append(" update").append(tableNameClass).append("By")
					.append(StringUtil.underlineToCamelFirstLetterUpper(x.getColumnName())).append("(").append(tableNameClass).append(" record,")
					.append(x.getColumnType()).append(" ").append(StringUtil.underlineToCamel(x.getColumnName())).append(");").toString());
			serviceLines.add("\n");
			serviceLines.add(new StringBuffer("\t").append("int").append(" delete").append(tableNameClass).append("By")
					.append(StringUtil.underlineToCamelFirstLetterUpper(x.getColumnName())).append("(").append(x.getColumnType())
					.append(" ").append(StringUtil.underlineToCamel(x.getColumnName())).append(");").toString());
			serviceLines.add("\n");
			
		});
		serviceLines.add(new StringBuffer("\tint insert").append(tableNameClass).append("(").append(tableNameClass).append(" record);").toString());
		serviceLines.add("\n");
		serviceLines.add(new StringBuffer("\tint update").append(tableNameClass).append("(").append(tableNameClass).append(" record);").toString());
		serviceLines.add("\n");
		serviceLines.add(new StringBuffer("\tint delete").append(tableNameClass).append("(").append(tableNameClass).append(" record);").toString());
		serviceLines.add("\n");
		serviceLines.add("}");
		return serviceLines;
	}

}
