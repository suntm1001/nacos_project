package com.stm.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.util.CollectionUtils;

public class MysqlDataSource {
    private Connection connection;
    private String url;
    private String user;
    private String password;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public MysqlDataSource(String url,String user,String password){
        this.url=url;
        this.user=user;
        this.password=password;
        this.connection=getConnect();
    }
    
    private Connection getConnect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url,user,password);
        }catch (Exception e){
            
        }
        return connection;
    }

    /**
     * 查询-用于sql拼接
     * @param sql
     * @param params
     * @param page
     * @return
     * @throws SQLException
     */
    public List<HashMap<String,Object>> select(String sql,List<String> params,Page page)throws SQLException {
        if(null != page){
            sql = sql +"limit ? offset ?";
        }
        preparedStatement = connection.prepareStatement(sql);
        int size;
        if(!CollectionUtils.isEmpty(params)){
            size=params.size();
            for (int i=0;i<size;i++){
                preparedStatement.setString(i+1,params.get(i));
            }
        }else{
            size=0;
        }
        if(page != null){
            preparedStatement.setInt(size+1,(int)(page.getSize()));
            preparedStatement.setInt(size+2,(int)((page.getCurrent()-1)*page.getSize()));
        }
        resultSet = preparedStatement.executeQuery();
        List<HashMap<String,Object>> mapList=new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        while(resultSet.next()){
            HashMap<String,Object> map= new HashMap<>();
            for(int i=0;i<metaData.getColumnCount();i++){
                String columnName = metaData.getColumnName(i+1);
                Object object=resultSet.getObject(columnName);
                map.put(columnName,object);
            }
            mapList.add(map);
        }
        
        return mapList;
    }
    
    public void connClose()throws SQLException{
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
