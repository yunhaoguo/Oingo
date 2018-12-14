package rpc;

import bean.Note;
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

@WebServlet("/notes")
public class NotesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBConnection connection = DBConnectionFactory.getConnection();
        Gson gson = new Gson();
        JSONObject input = RpcHelper.readJSONObject(request);
        try {
            boolean isFilter = input.getBoolean("is_filter");
            if (isFilter) {
                int fid = input.getInt("fid");
                List<Note> noteList = connection.getFilteredNoteList(fid);
                String noteListStr = gson.toJson(noteList);
                RpcHelper.writeJsonObject(response, new JSONObject().put("result", noteListStr));

            } else {
                List<Note> noteList = connection.getAllNoteList();
                String noteListStr = gson.toJson(noteList);
                RpcHelper.writeJsonObject(response, new JSONObject().put("result", noteListStr));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
