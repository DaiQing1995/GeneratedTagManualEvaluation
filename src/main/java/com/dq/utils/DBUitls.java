package com.dq.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dq.constant.FileName;
import com.dq.entity.ContainerInfo;
import com.dq.entity.TagType;

public class DBUitls {

	// MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/dockerhub_info";

	// MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
	// static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	// static final String DB_URL =
	// "jdbc:mysql://localhost:3306/RUNOOB?useSSL=false&serverTimezone=UTC";

	// 数据库的用户名与密码，需要根据自己的设置
	private static final String USER = "root";
	private static final String PASS = "daiqing123";

	public static List<ContainerInfo> Get_Tags() {
		Connection conn = null;
		List<ContainerInfo> ret = new ArrayList<ContainerInfo>();
		Statement stmt = null;
		try {
			FileReader fReader = new FileReader(new File(FileName.TAG_FINISHED_CONTAINER));
			BufferedReader br = new BufferedReader(fReader);
			Set<String> finishedContainer = new HashSet<String>();
			String ctnname;
			while ((ctnname = br.readLine()) != null && ctnname.length() != 0) {
				finishedContainer.add(ctnname);
			}
			// 注册 JDBC 驱动
			Class.forName(JDBC_DRIVER);

			// 打开链接
			System.out.println("连接数据库...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// 执行查询
			System.out.println(" 实例化Statement对象...");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT container_tags.`name`, container_info.short_desc, container_tags.tf_idf_tags_033, container_tags.tf_idf_tags_050, container_tags.tf_idf_tags_060, container_tags.tf_idf_tags_080, container_tags.hierarchical_cluster_tags "
					+ "FROM container_tags JOIN container_info ON container_info.name = container_tags.name";
			ResultSet rs = stmt.executeQuery(sql);

			// 展开结果集数据库
			while (rs.next()) {
				// 通过字段检索

				String name = rs.getString("name");
				String shortDesc = rs.getString("short_desc");
				// 输出数据
//				System.out.print("名称: " + name);
//				System.out.print(", 短描述: " + shortDesc);
//				System.out.print("\n");

				// if tag marking finished, let it go.
				if (finishedContainer.contains(name))
					continue;

				ContainerInfo ctnInfo = new ContainerInfo(name, shortDesc);
				ctnInfo.setTags(rs.getString("tf_idf_tags_033"), TagType.TF_IDF_033);
				ctnInfo.setTags(rs.getString("tf_idf_tags_050"), TagType.TF_IDF_050);
				ctnInfo.setTags(rs.getString("tf_idf_tags_060"), TagType.TF_IDF_060);
				ctnInfo.setTags(rs.getString("tf_idf_tags_080"), TagType.TF_IDF_080);
				ctnInfo.setTags(rs.getString("hierarchical_cluster_tags"), TagType.HIER);
				ret.add(ctnInfo);
			}
			System.out.println("load " + ret.size() + " container infos");
			// 完成后关闭
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// 处理 JDBC 错误
			se.printStackTrace();
		} catch (Exception e) {
			// 处理 Class.forName 错误
			e.printStackTrace();
		} finally {
			// 关闭资源
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // 什么都不做
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return ret;
	}
}