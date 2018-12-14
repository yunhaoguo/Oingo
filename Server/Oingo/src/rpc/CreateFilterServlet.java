package rpc;

import bean.Filter;
import com.google.gson.Gson;
import db.DBConnection;
import db.DBConnectionFactory;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * project: Oingo
 *
 * @author YubaiTao on 12/14/2018
 */
@WebServlet("/createfilter")
public class CreateFilterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Receive post on /createfilter");
        DBConnection connection = DBConnectionFactory.getConnection();
        Gson gson = new Gson();
        try {
            StringBuilder sBuilder = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sBuilder.append(line);
                }
            }
            Filter filter = gson.fromJson(sBuilder.toString(), Filter.class);
            int result = connection.createFilter(filter);
            RpcHelper.writeJsonObject(response, new JSONObject().put("result", result));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
