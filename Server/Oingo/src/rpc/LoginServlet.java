package rpc;

import bean.User;
import com.google.gson.Gson;
import db.DBConnection;
import db.DBConnectionFactory;

import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;



@WebServlet("/login")
public class LoginServlet extends HttpServlet {



    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();
        out.print("{\"name\": \"guo\"}");
        out.close();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        DBConnection connection = DBConnectionFactory.getConnection();
        try {
            JSONObject input = RpcHelper.readJSONObject(request);
            String userName = input.getString("userName");
            String password = input.getString("password");
            User user = connection.verifyLogin(userName, password);
            Gson gson = new Gson();
            String userStr = gson.toJson(user);
            System.out.println(userStr);
            RpcHelper.writeJsonObject(response, new JSONObject().put("result", userStr));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

}
