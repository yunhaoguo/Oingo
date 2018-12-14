package rpc;

import bean.Note;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/addnote")
public class AddNoteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            Note note = gson.fromJson(sBuilder.toString(), Note.class);
            int result = connection.addNote(note);
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
