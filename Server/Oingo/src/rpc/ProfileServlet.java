package rpc;

import bean.User;
import com.google.gson.Gson;
import db.DBConnection;
import db.DBConnectionFactory;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int uid = Integer.parseInt(request.getParameter("uid"));
        DBConnection connection = DBConnectionFactory.getConnection();
        Gson gson = new Gson();
        try {
            User user = connection.getUserInfo(uid);
            String userStr = gson.toJson(user);
            RpcHelper.writeJsonObject(response, new JSONObject().put("result", userStr));
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
