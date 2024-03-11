package com.example.demo.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Grade;

import demo.ConnString;

@RestController
@RequestMapping(value = "/customApi",method = RequestMethod.POST)
public class Postcontroller {

	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/Grade")
	public String postGrade(@RequestBody Grade grade) {
		String returnString = "預設";
		String sql = "INSERT INTO [APITest].[dbo].[grade] (name, score, classname) VALUES (?, ?, ?)";
        try(
        	    Connection connection = ConnString.getConnection();
        		PreparedStatement insertStatement = connection.prepareStatement(sql);
        	)  {

            // 設定參數值
        	insertStatement.setString(1, grade.name != null && !grade.name.isEmpty() ? grade.name : "Default");
        	insertStatement.setInt(2, grade.score != null ? grade.score : 0); // 或者其他預設值
        	insertStatement.setString(3, grade.classname != null && !grade.classname.isEmpty() ? grade.classname : "Default");
            int rowsAffected = insertStatement.executeUpdate();

            if (rowsAffected > 0) {
            	returnString = "資料插入成功！";
            } else {
            	returnString = "資料插入失敗！";
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } 

		return returnString;
	}
}
