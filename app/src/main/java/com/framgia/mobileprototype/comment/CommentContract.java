package com.framgia.mobileprototype.comment;

import com.framgia.mobileprototype.BasePresenter;
import com.framgia.mobileprototype.BaseView;
import com.framgia.mobileprototype.data.model.Comment;

import java.util.List;

/**
 * Created by tuannt on 5/28/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.comment
 */
public interface CommentContract {
    interface View extends BaseView {
        void prepareGetComments();
        void getCommentsSuccess(List<Comment> comments);
        void getCommentsError();
        void emptyComments();
        void showDialogRequestLogin();
        void commentSuccess(List<Comment> comments);
        void commentError();
    }

    interface Presenter extends BasePresenter {
        void getComments(String projectId, int offset);
        void prepare();
        void refresh(String projectId);
        void comment(String projectId, String content, String lastCommentId);
    }
}
