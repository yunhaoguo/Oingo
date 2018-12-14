package rpc;

import bean.User;
import com.google.gson.Gson;
import db.DBConnection;
import db.DBConnectionFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/requests")
public class RequestsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBConnection connection = DBConnectionFactory.getConnection();
        JSONObject input = RpcHelper.readJSONObject(request);
        try {
            int uid = input.getInt("uid");
            int ruid = input.getInt("ruid");
            System.out.println(uid + " " + ruid + "ididi");
            int accept = input.getInt("accept");
            if (connection.updateRequestsList(uid, ruid, accept)) {
                RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
            } else {
                RpcHelper.writeJsonObject(response, new JSONObject().put("result", "FAIL"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int uid = Integer.parseInt(request.getParameter("uid"));
        DBConnection connection = DBConnectionFactory.getConnection();
        Gson gson = new Gson();
        try {
            List<User> requestsList = connection.getRequestsList(uid);
            String friendListStr = gson.toJson(requestsList);
            RpcHelper.writeJsonObject(response, new JSONObject().put("result", friendListStr));
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
