package com.memtrip.sqlking.sample.comment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.memtrip.sqlking.gen.Q;
import com.memtrip.sqlking.operation.function.Insert;
import com.memtrip.sqlking.operation.function.Select;
import com.memtrip.sqlking.operation.keyword.OrderBy;
import com.memtrip.sqlking.sample.App;
import com.memtrip.sqlking.sample.R;
import com.memtrip.sqlking.sample.model.Comment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CommentActivity extends AppCompatActivity {

    @Bind(R.id.enter_comment)
    EditText editText;

    @Bind(R.id.comments)
    RecyclerView recyclerView;

    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);
        ButterKnife.bind(this);

        commentAdapter = new CommentAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    insert();
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshComments();
    }

    @OnClick(R.id.insert_comment)
    public void insert() {
        Comment comment = new Comment();
        comment.setBody(editText.getText().toString());
        comment.setTimestamp(System.currentTimeMillis());

        Insert.getBuilder().values(comment)
                .rx(Comment.class, App.getInstance().getSQLProvider())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        refreshComments();
                    }
                });

        editText.getText().clear();
    }

    private void refreshComments() {
        Select.getBuilder()
                .orderBy(Q.Comment.TIMESTAMP, OrderBy.Order.DESC)
                .rx(Comment.class, App.getInstance().getSQLProvider())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Comment[]>() {
                    @Override
                    public void call(Comment[] comments) {
                        commentAdapter.addAll(comments);
                    }
                });
    }
}
