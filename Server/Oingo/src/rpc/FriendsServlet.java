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

@WebServlet("/friends")
public class FriendsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DBConnection connection = DBConnectionFactory.getConnection();
        JSONObject input = RpcHelper.readJSONObject(request);
        try {
            int uid = input.getInt("uid");
            int fuid = input.getInt("fuid");
            RpcHelper.writeJsonObject(response,
                    new JSONObject().put("result", connection.deleteFriend(uid, fuid)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBConnection connection = DBConnectionFactory.getConnection();
        Gson gson = new Gson();
        int uid = Integer.parseInt(request.getParameter("uid"));
        try {
            List<User> friendList = connection.getFriendList(uid);
            String friendListStr = gson.toJson(friendList);
            RpcHelper.writeJsonObject(response, new JSONObject().put("result", friendListStr));
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
