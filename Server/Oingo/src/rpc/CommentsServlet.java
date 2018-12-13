package rpc;

import bean.Comment;
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

@WebServlet("/comments")
public class CommentsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBConnection connection = DBConnectionFactory.getConnection();
        JSONObject input = RpcHelper.readJSONObject(request);
        try {
            int uid = input.getInt("uid");
            int nid = input.getInt("nid");
            String ctime = input.getString("ctime");
            if (input.getInt("op") == 1) {
                String ccontent = input.getString("ccontent");
                Comment comment = new Comment();
                comment.setUid(uid);
                comment.setNid(nid);
                comment.setCtime(ctime);
                comment.setCcontent(ccontent);
                boolean result = connection.addComment(comment);
                RpcHelper.writeJsonObject(response, new JSONObject().put("result", result ? 1 : 0));
            } else {
                Comment comment = new Comment();
                comment.setUid(uid);
                comment.setNid(nid);
                comment.setCtime(ctime);
                boolean result = connection.deleteComment(comment);
                RpcHelper.writeJsonObject(response, new JSONObject().put("result", result ? 1 : 0));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int nid = Integer.parseInt(request.getParameter("nid"));
        DBConnection connection = DBConnectionFactory.getConnection();
        Gson gson = new Gson();
        try {
            List<Comment> commentsList = connection.getCommentsList(nid);
            String commentsListStr = gson.toJson(commentsList);
            RpcHelper.writeJsonObject(response, new JSONObject().put("result", commentsListStr));
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
