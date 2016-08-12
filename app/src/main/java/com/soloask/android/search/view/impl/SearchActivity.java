package com.soloask.android.search.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.soloask.android.MainApplication;
import com.soloask.android.R;
import com.soloask.android.common.base.BaseActivity;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;
import com.soloask.android.question.view.impl.QuestionDetailActivity;
import com.soloask.android.question.view.impl.UserProfileActivity;
import com.soloask.android.search.adapter.SearchPersonAdapter;
import com.soloask.android.search.adapter.SearchQuestionAdapter;
import com.soloask.android.search.injection.SearchModule;
import com.soloask.android.search.presenter.SearchPresenter;
import com.soloask.android.search.view.SearchView;
import com.soloask.android.util.Constant;
import com.soloask.android.view.MaterialProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Lebron on 2016/7/18.
 */
public class SearchActivity extends BaseActivity implements SearchView
        , TextView.OnEditorActionListener {
    @BindView(R.id.edit_search)
    EditText mSearchEdit;
    @BindView(R.id.btn_clear)
    ImageButton mClearButton;
    @BindView(R.id.progressbar_loading)
    MaterialProgressBar mLoadingBar;
    @BindView(R.id.rl_empty)
    RelativeLayout mEmptyLayout;
    @BindView(R.id.network_layout)
    RelativeLayout mNoNetWorkLayout;
    @BindView(R.id.tv_retry)
    TextView mRetryView;
    @BindView(R.id.tv_hint_empty)
    TextView mEmptyDescribe;
    @BindView(R.id.recycler_search_person_view)
    RecyclerView mPersonsView;
    @BindView(R.id.recycler_search_question_view)
    RecyclerView mQuestionsView;
    @BindView(R.id.tv_persons_more)
    TextView mPersonMoreView;
    @BindView(R.id.tv_questions_more)
    TextView mQuestionMoreView;
    @BindView(R.id.ll_search_persons)
    LinearLayout mPersonContainer;
    @BindView(R.id.ll_search_questions)
    LinearLayout mQuestionContainer;

    @Inject
    SearchPresenter mPresenter;

    private SearchPersonAdapter mPersonAdapter;
    private SearchQuestionAdapter mQuestionAdapter;
    private List<User> mUsers = new ArrayList<>();
    private List<Question> mQuestions = new ArrayList<>();
    private String mSearchContent;

    @OnClick(R.id.btn_clear)
    public void clear() {
        mSearchEdit.setText("");
    }

    @OnClick(R.id.tv_retry)
    public void retry() {
        showNetworkError(false);
        searchData();
    }

    @OnClick(R.id.tv_persons_more)
    public void toPersonsMore() {
        Intent intent = new Intent(SearchActivity.this, SearchMoreActivity.class);
        intent.putExtra(Constant.KEY_SEARCH_CONTENT, mSearchContent);
        intent.putExtra(Constant.KEY_FROM_SEARCH, true);
        SearchActivity.this.startActivity(intent);
    }


    @OnClick(R.id.tv_questions_more)
    public void toQuestionsMore() {
        Intent intent = new Intent(SearchActivity.this, SearchMoreActivity.class);
        intent.putExtra(Constant.KEY_SEARCH_CONTENT, mSearchContent);
        intent.putExtra(Constant.KEY_FROM_SEARCH, false);
        SearchActivity.this.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_search;
    }

    @Override
    protected void initViewsAndData() {
        mPersonAdapter = new SearchPersonAdapter(mUsers);
        mPersonAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(getViewContext(), UserProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", mUsers.get(i));
                intent.putExtras(bundle);
                getViewContext().startActivity(intent);
            }
        });
        mPersonsView.setLayoutManager(new LinearLayoutManager(this));
        mPersonsView.setAdapter(mPersonAdapter);

        mQuestionAdapter = new SearchQuestionAdapter(mQuestions);
        mQuestionAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(getViewContext(), QuestionDetailActivity.class);
                intent.putExtra(Constant.KEY_QUESTION_ID, mQuestions.get(i).getObjectId());
                getViewContext().startActivity(intent);
            }
        });
        mQuestionsView.setLayoutManager(new LinearLayoutManager(this));
        mQuestionsView.setAdapter(mQuestionAdapter);

        mLoadingBar.setVisibility(View.GONE);
        mEmptyDescribe.setText(R.string.notice_no_search_result);
        addListener();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(this).getAppComponent()
                .plus(new SearchModule(this))
                .inject(this);
    }

    private void searchData() {
        mUsers.clear();
        mQuestions.clear();
        if (mPresenter != null) {
            mPresenter.getPersonsData(mSearchContent, 3);
            mPresenter.getQuestionsData(mSearchContent, 3);
        }
    }

    private void addListener() {
        mSearchEdit.setOnEditorActionListener(this);
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = mSearchEdit.getText().toString();
                if (text.length() > 0) {
                    mClearButton.setVisibility(View.VISIBLE);
                } else {
                    mClearButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            mSearchContent = mSearchEdit.getText().toString();
            searchData();
        }
        return true;
    }

    @Override
    public void showNetworkError(boolean show) {
        mNoNetWorkLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showEmptyLayout(boolean show) {
        mEmptyLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showProgress(boolean show) {
        mLoadingBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showQuestions(List<Question> questionList) {
        mQuestionContainer.setVisibility(questionList.size() > 0 ? View.VISIBLE : View.GONE);
        mQuestionAdapter.notifyDataChangedAfterLoadMore(questionList, false);
    }

    @Override
    public void showPersons(List<User> personList) {
        mPersonContainer.setVisibility(personList.size() > 0 ? View.VISIBLE : View.GONE);
        mPersonAdapter.notifyDataChangedAfterLoadMore(personList, false);
    }

    @Override
    public void showToast(int stringId) {
        Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getViewContext() {
        return this;
    }
}
