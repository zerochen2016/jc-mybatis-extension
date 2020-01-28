package jc.mybatis.extension.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jc.mybatis.extension.entity.DBInfo;
import jc.mybatis.extension.entity.IndexInfo;

/**
 * util of db
 * @author JC
 * @Date 2019年11月17日
 * @since 1.0.0
 */
public class DBUtil {
	/**
	 * 
	 * @param url: jdbc:mysql://IP:PORT/DATABASE?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&autoReconnect=true&useSSL=false
	 * @param user: user
	 * @param password: password
	 * @param driverClassName: com.mysql.jdbc.Driver
	 * @return
	 */
	private static Connection getConnection(String url, String user, String password, String driverClassName) {
		try {
			Class.forName(driverClassName);
			Connection conn = DriverManager.getConnection(url, user, password);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void closeAll(AutoCloseable ...autoCloseables) {
		for(AutoCloseable ac:autoCloseables) {
			if(ac!=null) {
				try {
					ac.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static List<IndexInfo> listIndexInfo(DBInfo dbInfo, String schema, String tableName) {
		List<IndexInfo> list = new ArrayList<>();
		Connection conn = getConnection(dbInfo.getUrl(), dbInfo.getUser(), dbInfo.getPassword(), dbInfo.getDriverClassName());
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(new StringBuffer("select * from information_schema.columns where  TABLE_NAME = '").append(tableName).append("'")
					.append(" AND TABLE_SCHEMA = '").append(schema).append("'").toString());
			rs = ps.executeQuery();
			while(rs.next()) {
				String columnKey = rs.getString("COLUMN_KEY");
				if(columnKey == null || columnKey.length() <= 0) {
					continue;
				}
				IndexInfo ii = new IndexInfo();
				if("PRI".equals(columnKey) || "UNI".equals(columnKey)) {
					ii.setUnique(true);
				}else {
					ii.setUnique(false);
				}
				ii.setColumnName(rs.getString("COLUMN_NAME"));
				String columnType = rs.getString("COLUMN_TYPE");
				columnType = columnType.contains("(") ? columnType.substring(0, columnType.indexOf("(")) : columnType;
				ii.setColumnType(sqlTypeToJavaType(columnType));
				list.add(ii);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeAll(rs,ps,conn);
		}
		return list;
	}
	
	private static String sqlTypeToJavaType(String sqlType) {
		if( sqlType == null || sqlType.trim().length() == 0 ) {
			return sqlType;
		}
        sqlType = sqlType.toLowerCase();
        switch(sqlType){
        	case "tinyint":
        		return "Integer";
        	case "smallint":
        		return "Integer";
        	case "mediumint":
        		return "Integer";
        	case "int":
        		return "Integer";
        	case "integer":
        		return "Integer";        		
        	case "bigint":
        		return "Long";
        	case "serial":
        		return "Long";
            case "float":
            	return "Fload";        		
            case "real":
            	return "java.math.BigDecimal";        		
            case "double":
            	return "Double";
            case "decimal":
            	return "java.math.BigDecimal";            	
            case "bit":
            	return "Boolean";            	
            case "boolean":	
            	return "Boolean";
            case "bool":	
            	return "Boolean";            	
            case "date":
            	return "java.util.Date";
            case "datetime":
            	return "java.util.Date";            	
            case "timestamp":
            	return "java.sql.Timestamp";            	
            case "year":
            	return "java.util.Date";
            case "time":
            	return "java.sql.Time";
            case "char":
            	return "String";
            case "varchar":
            	return "String";
            case "tinytext":
            	return "String";
            case "mediumtext":
            	return "String";            	
            case "text":
            	return "String";
            case "longtext":
            	return "String";            	
            default:
                System.out.println("-----------------》转化失败：未发现的类型"+sqlType);
                break;
        }
        return sqlType;
	}
}
