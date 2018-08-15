package com.cjh;


import java.lang.reflect.InvocationTargetException;
import java.sql.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //连接数据库
       String driver="com.mysql.jdbc.Driver";
       String url="jdbc:mysql://localhost:3306/news";
       String userName="root";
       String password=".";
       //创建JDBC的API
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        //通过反射 创建实例
        Object obj=null;
        try {
            obj= Class.forName("com.cjh.entity.Users").newInstance();
            Class.forName(driver);
            try {
                con= DriverManager.getConnection(url,userName,password);
                String sql="select id,name,password,describes,type from news_userinfo";
                ps=con.prepareStatement(sql);
                rs=ps.executeQuery();
                while(rs.next()){
                    ResultSetMetaData data=rs.getMetaData();
                    int count=data.getColumnCount();
                    for (int i=1;i<=count;i++){
                        String columnName=data.getColumnName(i);
                        String methodName=changeName(columnName);
                        String columeType=data.getColumnTypeName(i);
                        if (columeType.equalsIgnoreCase("int")){
                            obj.getClass().getMethod(methodName,int.class).invoke(obj,rs.getInt(columnName));
                        }else if (columeType.equalsIgnoreCase("varchar")){
                            obj.getClass().getMethod(methodName,String.class).invoke(obj,rs.getString(columnName));
                        }
                    }
                    System.out.println(obj);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
    private  static String changeName(String columnName){
        return  "set"+columnName.substring(0,1).toUpperCase()+columnName.substring(1);
    }
}
