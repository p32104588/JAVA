package com.example.demo;

public class comment {
			// 舊版可用 不會抓model的function 至55行
			/*		
			String url = ConnString.getJdbcUrl();
	        String username = ConnString.getUsername();
	        String password = ConnString.getPassword();
	        
	        Connection connection = null;
	        PreparedStatement preparedStatement = null;
	        
	        try {
	            // 載入JDBC驅動程式
	            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

	            // 連接資料庫
	            connection = DriverManager.getConnection(url, username, password);

	            // SQL INSERT語句
	            String sql = "INSERT INTO [APITest].[dbo].[grade] (name, score) VALUES (?, ?)";

	            // 建立PreparedStatement
	            preparedStatement = connection.prepareStatement(sql);

	            // 設定參數值
	            preparedStatement.setString(1, "John");
	            preparedStatement.setInt(2, 77);

	            // 執行INSERT語句
	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("資料插入成功！");
	            } else {
	                System.out.println("資料插入失敗！");
	            }
	        } catch (ClassNotFoundException | SQLException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                // 關閉PreparedStatement
	                if (preparedStatement != null) {
	                    preparedStatement.close();
	                }

	                // 關閉資料庫連線
	                if (connection != null) {
	                    connection.close();
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        */
			
			// 新版(JAVA 6以上)可用 不會抓model的function 至78行
			/*
			        try(
	        	    Connection connection = DatabaseHelper.getConnection();
	        	    PreparedStatement preparedStatement = DatabaseHelper.prepareInsertStatement(connection);
	        	)  {

	            // 設定參數值
	            preparedStatement.setString(1, "John");
	            preparedStatement.setInt(2, 77);

	            // 執行INSERT語句
	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("資料插入成功！");
	            } else {
	                System.out.println("資料插入失敗！");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } 
	        */
}