package test.dao;

import test.model.Student;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class WebLogicDAOConnection implements DAOConnection {

    private static WebLogicDAOConnection webLogicDAOConnection;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private WebLogicDAOConnection() {
        super();
    }
    public static WebLogicDAOConnection getInstance() {
        if (webLogicDAOConnection == null) {
            webLogicDAOConnection = new WebLogicDAOConnection();
        }
        return webLogicDAOConnection;
    }

    @Override
    public void connect() {
        String sp = "weblogic.jndi.WLInitialContextFactory";
        String file = "t3://localhost:7001";
        String dataSourceName = "AjaxTestJNDI";
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, sp);
        env.put(Context.PROVIDER_URL, file);
        Context ctx = null;
        DataSource ds = null;
        try {
            ctx = new InitialContext(env);
            ds = (DataSource) ctx.lookup(dataSourceName);
        } catch (NamingException e) {

            e.printStackTrace();
        }
        connection = null;
        try {
            connection = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection != null) {
            System.out.println("Connected successful!");
        }
    }

    @Override
    public void disconnect() {
        try {
            connection.close();
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
