package com.example.demo.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import demo.ConnString;

@RestController
@RequestMapping(value = "/customApi",method = RequestMethod.DELETE)
public class Deletecontroller {
	
	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping(value = "/Grade/{id}")
	public String deleteGrade(@PathVariable(name = "id") int id) {
        String returnString = "預設";
        String sql = "DELETE FROM [APITest].[dbo].[grade] WHERE id = ?";
        try (
                Connection connection = ConnString.getConnection();
                PreparedStatement deleteStatement = connection.prepareStatement(sql);
        ) {
            // 設定參數值
            deleteStatement.setInt(1, id);
            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected > 0) {
                returnString = "資料刪除成功！";
            } else {
                returnString = "找不到對應的資料，刪除失敗！";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnString;
    }
}