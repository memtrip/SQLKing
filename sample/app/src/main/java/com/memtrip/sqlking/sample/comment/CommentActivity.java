package com.memtrip.sqlking.sample.comment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.memtrip.sqlking.operation.function.Count;
import com.memtrip.sqlking.operation.function.Insert;
import com.memtrip.sqlking.operation.function.Select;
import com.memtrip.sqlking.operation.keyword.OrderBy;
import com.memtrip.sqlking.sample.App;
import com.memtrip.sqlking.sample.R;
import com.memtrip.sqlking.sample.model.Comment;
import com.memtrip.sqlking.sample.model.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.memtrip.sqlking.operation.clause.On.on;
import static com.memtrip.sqlking.operation.join.InnerJoin.innerJoin;

public class CommentActivity extends AppCompatActivity {

    @Bind(R.id.comment_count)
    TextView count;

    @Bind(R.id.enter_comment)
    EditText editText;

    @Bind(R.id.comments)
    RecyclerView recyclerView;

    private CommentAdapter commentAdapter;

    private CompositeDisposable disposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);
        ButterKnife.bind(this);

        commentAdapter = new CommentAdapter();

        disposables = new CompositeDisposable();

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
        checkUserExists();
        countComments();
    }

    @Override
    protected void onStop() {
        super.onStop();

        disposables.dispose();
    }

    private void checkUserExists() {
        Count.getBuilder()
                .rx(User.class, App.getInstance().getSQLProvider())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onSuccess(Long aLong) {
                        if (aLong == 0) {
                            insertUser();
                        } else {
                            refreshComments();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void insertUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("Sam");

        Insert.getBuilder().values(user)
                .rx(App.getInstance().getSQLProvider())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onComplete() {
                        refreshComments();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void countComments() {
        Count.getBuilder()
                .rx(Comment.class, App.getInstance().getSQLProvider())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onSuccess(Long aLong) {
                        count.setText(getResources().getString(R.string.count, aLong.toString()));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @OnClick(R.id.insert_comment)
    public void insert() {
        Comment comment = new Comment();
        comment.setBody(editText.getText().toString());
        comment.setTimestamp(System.currentTimeMillis());
        comment.setUserId(1);

        Insert.getBuilder().values(comment)
                .rx(App.getInstance().getSQLProvider())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onComplete() {
                        refreshComments();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

        editText.getText().clear();
    }

    private void refreshComments() {
        Select.getBuilder()
                .join(innerJoin(User.class, on("Comment.userId","User.id")))
                .orderBy("Comment.timestamp", OrderBy.Order.DESC)
                .rx(Comment.class, App.getInstance().getSQLProvider())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Comment>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onSuccess(List<Comment> comments) {
                        commentAdapter.addAll(comments);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
