package rpc;

import bean.Filter;
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

/**
 * project: Oingo
 *
 * @author Yubai Tao on 12/14/2018
 */

@WebServlet("/filters")
public class FiltersServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Receive post on /filters");
        DBConnection connection = DBConnectionFactory.getConnection();
        Gson gson = new Gson();
        JSONObject input = RpcHelper.readJSONObject(request);
        try {
            List<Filter> filterList = connection.getFilterList(input.getInt("uid"));
            String filterListStr = gson.toJson(filterList);
            System.out.println(filterListStr);
            RpcHelper.writeJsonObject(response, new JSONObject().put("result", filterListStr));
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
