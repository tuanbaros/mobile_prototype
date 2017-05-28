package com.framgia.mobileprototype.comment;

import android.text.TextUtils;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.framgia.mobileprototype.data.model.Comment;
import com.framgia.mobileprototype.data.model.User;
import com.framgia.mobileprototype.data.remote.ApiService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by tuannt on 5/28/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.comment
 */
public class CommentPresenter implements CommentContract.Presenter {
    private CommentContract.View mCommentView;
    public CommentPresenter(CommentContract.View commentView) {
        mCommentView = commentView;
    }

    @Override
    public void start() {
    }

    @Override
    public void getComments(String projectId, int offset) {
        AndroidNetworking.get(ApiService.getApi(ApiService.COMMENTS))
            .addPathParameter(ApiService.Param.PROJECT_ENTRY_ID, projectId)
            .addPathParameter(ApiService.Param.OFFSET, String.valueOf(offset))
            .doNotCacheResponse()
            .build()
            .getAsJSONArray(new JSONArrayRequestListener() {
                @Override
                public void onResponse(JSONArray response) {
                    if (response == null) {
                        mCommentView.getCommentsError();
                        return;
                    }
                    if (response.length() == 0) {
                        mCommentView.emptyComments();
                        return;
                    }
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Comment>>(){}.getType();
                    List<Comment> comments = gson.fromJson(response.toString(), listType);
                    mCommentView.getCommentsSuccess(comments);
                }

                @Override
                public void onError(ANError anError) {
                    mCommentView.getCommentsError();
                }
            });
    }



    @Override
    public void prepare() {
        mCommentView.prepareGetComments();
    }

    @Override
    public void refresh(String projectId) {
        getComments(projectId, 0);
    }

    @Override
    public void comment(String projectId, String content, String lastCommentId) {
        if (User.getCurrent() == null) {
            mCommentView.showDialogRequestLogin();
            return;
        }
        if (TextUtils.isEmpty(content)) {
            return;
        }
        User user = User.getCurrent();
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setProjectId(projectId);
        AndroidNetworking.post(ApiService.getApi(ApiService.COMMENT))
            .addBodyParameter(ApiService.Param.OPEN_ID, user.getOpenId())
            .addBodyParameter(ApiService.Param.TOKEN, user.getToken())
            .addBodyParameter(ApiService.Param.LAST_ID, lastCommentId)
            .addBodyParameter(ApiService.Param.COMMENT, new Gson().toJson(comment))
            .doNotCacheResponse()
            .build()
            .getAsJSONArray(new JSONArrayRequestListener() {
                @Override
                public void onResponse(JSONArray response) {
                    if (response == null || response.length() == 0) {
                        mCommentView.commentError();
                        return;
                    }
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Comment>>(){}.getType();
                    List<Comment> comments = gson.fromJson(response.toString(), listType);
                    mCommentView.commentSuccess(comments);
                }

                @Override
                public void onError(ANError anError) {
                    mCommentView.commentError();
                }
            });
    }
}
