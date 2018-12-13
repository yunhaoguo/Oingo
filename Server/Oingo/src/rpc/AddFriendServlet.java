package rpc;

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

@WebServlet("/addfriend")
public class AddFriendServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBConnection connection = DBConnectionFactory.getConnection();
        JSONObject input = RpcHelper.readJSONObject(request);
        try {
            int uid = input.getInt("uid");
            int fuid = input.getInt("fuid");

            boolean result = connection.addFriend(uid, fuid);
            RpcHelper.writeJsonObject(response, new JSONObject().put("result", result ? 1 : 0));
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
