package test.dao;

import oracle.jdbc.driver.OracleDriver;
import test.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OracleDAOConnection implements DAOConnection {

    private static OracleDAOConnection oracleDAOConnection;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private OracleDAOConnection () {
        super();
    }

    public static OracleDAOConnection getInstance() {
        if (oracleDAOConnection == null) {
            oracleDAOConnection = new OracleDAOConnection();
        }
        return oracleDAOConnection;
    }


    @Override
    public void connect() {
        Driver driver = new OracleDriver();
        try {
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "student");
            if (!connection.isClosed()) {
                System.out.println("Connected successful!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void disconnect() {
        try {
            connection.close();
            if (resultSet != null) {
                resultSet.close();
            }
            statement.close();
            System.out.println("Connection was closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //---------READ-------------
    @Override
    public List<Student> selectAllStudents() {
        connect();
        List<Student> studentList = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM STUDENTS ORDER BY STUDENT_NAME ASC");
            while (resultSet.next()){
                studentList.add(parseStudent(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return studentList;
    }

    //---------Create-------------
    @Override
    public void createStudent(String name, float scholarShip) {
        connect();
        try {
            statement = connection.prepareStatement("INSERT INTO STUDENTS (STUDENT_ID, STUDENT_NAME, STUDENT_SCHOLARSHIP)" +
                    "VALUES (NULL, ?, ?)");
            ((PreparedStatement)statement).setString(1, name);
            ((PreparedStatement)statement).setFloat(2, scholarShip);
            ((PreparedStatement) statement).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    //---------Update-------------
    @Override
    public void updateStudent(int id, float sum) {
        connect();
        try {
            statement = connection.prepareStatement("UPDATE STUDENTS SET STUDENT_SCHOLARSHIP = ? WHERE STUDENT_ID = ?");
            ((PreparedStatement)statement).setFloat(1, sum);
            ((PreparedStatement)statement).setInt(2, id);
            ((PreparedStatement) statement).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    //---------Delete-------------
    @Override
    public void deleteStudent(int id) {
        connect();
        try {
            statement = connection.prepareStatement("DELETE FROM STUDENTS WHERE STUDENT_ID = ?");
            ((PreparedStatement)statement).setInt(1, id);
            ((PreparedStatement) statement).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }


    private Student parseStudent(ResultSet resultSet) {
        Student student = null;
        try {
            int id = resultSet.getInt("STUDENT_ID");
            String name = resultSet.getString("STUDENT_NAME");
            float scholarship = resultSet.getFloat("STUDENT_SCHOLARSHIP");
            student = new Student(id, name, scholarship);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

}
