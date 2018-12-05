package rpc;

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
            if (connection.verifyLogin(userName, password)) {
                RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
            } else {
                RpcHelper.writeJsonObject(response, new JSONObject().put("result", "FAIL"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

}
