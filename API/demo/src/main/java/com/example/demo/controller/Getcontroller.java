package com.example.demo.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.*;
import com.example.demo.model.model.*;
import com.example.demo.model.Grade;

import demo.ConnString;

@RestController
@RequestMapping(value = "/customApi" , method = RequestMethod.GET)
public class Getcontroller {
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/getBook")
	public Book getBook() {
		// 使用 Book 模型
		model.Book book = new model.Book();

		book.bookId = 1;
		book.name = "Java Programming";
		book.author = "John Doe";

		return book;
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/getAuthor")
	public Author getAuthor() {
		// 使用 Author 模型
		model.Author author = new model.Author();
		author.authorId = 101;
		author.name = "Jane Doe";

		return author;
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/Grade")
	public List<modelgrade> getGrade() {
		// 使用 Grade 模型
		List<modelgrade> gradeList = new ArrayList();
		String sql = "SELECT name,score FROM [APITest].[dbo].[grade]";
        try(
        	    Connection connection = ConnString.getConnection();
        		PreparedStatement selectStatement = connection.prepareStatement(sql);
        	)  {

            // 設定參數值
//        	selectStatement.setString(1, "John");
            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
            	modelgrade grade = new modelgrade();
                grade.name = resultSet.getString("name");
                grade.score = resultSet.getInt("score");
                gradeList.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 

		return gradeList;
	}
	
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/Grade/{id}")
	public List<modelgrade> getGradebyid(@PathVariable(name = "id") Integer id) {
		// 使用 Grade 模型
		List<modelgrade> gradeList = new ArrayList();
		String sql = "SELECT name,score FROM [APITest].[dbo].[grade] where id = ?";
        try(
        	    Connection connection = ConnString.getConnection();
        		PreparedStatement selectStatement = connection.prepareStatement(sql);
        	)  {

            // 設定參數值
        	selectStatement.setInt(1, id);
            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
            	modelgrade grade = new modelgrade();
                grade.name = resultSet.getString("name");
                grade.score = resultSet.getInt("score");
                gradeList.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 

		return gradeList;
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/Grade/detail")
	public List<Grade> getdetail() {
		// 使用 Grade 模型
		List<Grade> gradeList = new ArrayList();
		String sql = "SELECT * FROM [APITest].[dbo].[grade] left join teacherforclass on classname = class";
        try(
        	    Connection connection = ConnString.getConnection();
        		PreparedStatement selectStatement = connection.prepareStatement(sql);
        	)  {

            // 設定參數值
//        	selectStatement.setString(1, "John");
            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
            	Grade grade = new Grade();
                grade.name = resultSet.getString("name");
                grade.score = resultSet.getInt("score");
                grade.classname = resultSet.getString("classname");
                grade.teacher = resultSet.getString("teacher");
                gradeList.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 

		return gradeList;
	}
}