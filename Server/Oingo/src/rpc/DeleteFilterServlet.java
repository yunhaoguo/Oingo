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

@WebServlet("/deletefilter")
public class DeleteFilterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBConnection connection = DBConnectionFactory.getConnection();
        JSONObject input = RpcHelper.readJSONObject(request);
        int result = 0;
        try {
            result = connection.deleteFilter(input.getInt("fid"));
            RpcHelper.writeJsonObject(response, new JSONObject().put("result", result));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
