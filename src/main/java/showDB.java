import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

public class showDB extends HttpServlet {

    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/test_utf8?useUnicode=true&characterEncoding=utf8";

    /**
     * User and Password
     */
    static final String USER = "root";
    static final String PASSWORD = "12345678";

    public static String resultHTML = "";

    public static String main() throws ClassNotFoundException, SQLException {
        Connection connection = null;
        Statement statement = null;


        Class.forName("com.mysql.jdbc.Driver");

        connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

        statement = connection.createStatement();

        String sql;
        sql = "SELECT * FROM persons";

        ResultSet resultSet = statement.executeQuery(sql);

        int i = 0;
        int size = 0;
        String ends = "";
        resultSet.last();
        size = resultSet.getRow();
        resultSet.beforeFirst();

        resultHTML = "[\n";

        while (resultSet.next()) {
            i++;
            ends = "";
            if (i < size) {
                ends = ",";
            }
            int id = resultSet.getInt("id");
            String firstname = resultSet.getString("firstname");
            String lastname = resultSet.getString("lastname");
            String city = resultSet.getString("city");
            String dataR = resultSet.getString("dataR");

            //System.out.println("id: " + id);

            resultHTML = resultHTML + ("  {\n");
            resultHTML = resultHTML + ("    \"id\": " + id + ",\n");
            resultHTML = resultHTML + ("    \"firstName\": \"" + firstname + "\",\n");
            resultHTML = resultHTML + ("    \"lastName\": \"" + lastname + "\",\n");
            resultHTML = resultHTML + ("    \"city\": \"" + city + "\",\n");
            resultHTML = resultHTML + ("    \"dataR\": \"" + dataR + "\"\n");
            resultHTML = resultHTML + ("  }" + ends + "\n");
        }

        resultHTML = resultHTML + "]\n";
        resultSet.close();
        statement.close();
        connection.close();

        return resultHTML;
    }

    /**
     * Функция выполнения запроса
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static String mysqlQuery(String SQL) throws ClassNotFoundException, SQLException {

        Connection connection = null;
        Statement statement = null;
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.executeUpdate();

        resultHTML = "{\n";
        resultHTML = resultHTML + "\"result\": \"Ok\"\n";
        resultHTML = resultHTML + "}\n";

        connection.close();

        return resultHTML;
    }

    /**
     * Функция выполнения запроса
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static String mysqlQueryR(String SQL, String Col) throws ClassNotFoundException, SQLException {
        String ret = "";

        Connection connection = null;
        Statement statement = null;
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL);
        resultSet.next();

        ret = resultSet.getString(Col);

        connection.close();

        return ret;
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        //String action = req.getParameter("action");

        resp.setHeader("Content-Type", "application/json; charset=utf-8");
        //resp.getWriter().write("метод POST");

        // Проверяем если есть параметр ?persons=add то выполняем запрос на добавление
        if (req.getParameter("persons").equals("add")) {

            // считываем буфер POST в строку
            StringBuffer jb = new StringBuffer();
            String line = null;
            try {
                BufferedReader reader = req.getReader();
                while ((line = reader.readLine()) != null)
                    jb.append(line);
            } catch (Exception e) { /*report an error*/ }

            // преобразовываем строку в объект JSON
            JSONObject obj = new JSONObject(jb.toString());

            // получаем переменные
            String firstName = obj.getString("firstName");
            String lastName = obj.getString("lastName");
            String city = obj.getString("city");
            String dataR = obj.getString("dataR");

            try {
                String ret, ret2;
                ret = mysqlQuery("INSERT INTO persons (firstName, LastName, city, dataR) VALUES (\"" + firstName + "\", \"" + lastName + "\", \"" + city + "\", \"" + dataR + "\");");

                ret = mysqlQueryR("SELECT MAX(id) AS insert_id FROM persons;", "insert_id");
                ret2 = "{" +
                        "\"insert_id\": \"" + ret + "\"" +
                        "}";
                resp.getWriter().write(ret2);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        // Проверяем если есть параметр ?persons=edit то выполняем запрос на редактирование
        if (req.getParameter("persons").equals("edit")) {

            // считываем буфер POST в строку
            StringBuffer jb = new StringBuffer();
            String line = null;
            try {
                BufferedReader reader = req.getReader();
                while ((line = reader.readLine()) != null)
                    jb.append(line);
            } catch (Exception e) { /*report an error*/ }

            // преобразовываем строку в объект JSON
            JSONObject obj = new JSONObject(jb.toString());

            // получаем переменные
            String id = obj.getString("id");
            String firstName = obj.getString("firstName");
            String lastName = obj.getString("lastName");
            String city = obj.getString("city");
            String dataR = obj.getString("dataR");

            try {
                resp.getWriter().write(mysqlQuery("UPDATE persons SET firstName = \"" + firstName + "\", LastName = \"" + lastName + "\", city = \"" + city + "\", dataR = \"" + dataR + "\" WHERE id = " + id + ";"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        // Проверяем если есть параметр ?persons=delete то выполняем запрос на редактирование
        if (req.getParameter("persons").equals("delete")) {

            // считываем буфер POST в строку
            StringBuffer jb = new StringBuffer();
            String line = null;
            try {
                BufferedReader reader = req.getReader();
                while ((line = reader.readLine()) != null)
                    jb.append(line);
            } catch (Exception e) { /*report an error*/ }

            // преобразовываем строку в объект JSON
            JSONObject obj = new JSONObject(jb.toString());

            // получаем переменные
            String id = obj.getString("id");

            try {
                resp.getWriter().write(mysqlQuery("DELETE FROM persons WHERE id = " + id + ";"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setHeader("Content-Type", "application/json; charset=utf-8");

        //resp.getWriter().write("connect DB (соединение с БД)");


        try {
            resp.getWriter().write(main());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}