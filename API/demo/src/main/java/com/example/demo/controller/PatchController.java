package com.example.demo.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import demo.ConnString;

@RestController
@RequestMapping(value = "/customApi", method = RequestMethod.PATCH)
public class PatchController {

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/Grade/{id}")
    public String updateGrade(@PathVariable(name = "id") int id, @RequestBody Map<String, Object> updateData) {
        String returnString = "預設";

        // 更新的 SQL 语句
        String sql = "UPDATE [APITest].[dbo].[grade] SET ";
        boolean isFirst = true;

        // 根据传入的字段构建 SQL 语句
        for (Map.Entry<String, Object> entry : updateData.entrySet()) {
            if (!isFirst) {
                sql += ", ";
            }
            sql += entry.getKey() + " = ?";
            isFirst = false;
        }

        // 加上 WHERE 子句
        sql += " WHERE id = ?";

        try (
            Connection connection = ConnString.getConnection();
            PreparedStatement updateStatement = connection.prepareStatement(sql);
        ) {
            // 设置参数值
            int parameterIndex = 1;
            for (Object value : updateData.values()) {
                updateStatement.setObject(parameterIndex++, value);
            }
            updateStatement.setInt(parameterIndex, id);

            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                returnString = "資料更新成功！";
            } else {
                returnString = "找不到對應的資料，更新失敗！";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnString;
    }
}




//StringBuilder sqlBuilder = new StringBuilder("UPDATE [APITest].[dbo].[grade] SET");
//
//// 構建 SET 子句
//updates.forEach((key, value) -> {
//    sqlBuilder.append(" ").append(key).append(" = ?,");
//});
//
//// 移除最後一個逗號
//sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
//
//// WHERE 子句
//sqlBuilder.append(" WHERE id = ?");