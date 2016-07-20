package com.soloask.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
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

import com.soloask.android.R;
import com.soloask.android.adapter.SearchPersonAdapter;
import com.soloask.android.adapter.SearchQuestionAdapter;
import com.soloask.android.data.bmob.SearchManager;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;
import com.soloask.android.view.MaterialProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lebron on 2016/7/18.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    private EditText mSearchEdit;
    private ImageButton mClearButton;
    private MaterialProgressBar mLoadingBar;
    private RelativeLayout mEmptyLayout;
    private TextView mEmptyDescribe, mEmptyBtn;
    private RecyclerView mPersonsView, mQuestionsView;
    private TextView mPersonMoreView, mQuestionMoreView;
    private LinearLayout mPersonContainer, mQuestionContainer;

    private SearchPersonAdapter mPersonAdapter;
    private SearchQuestionAdapter mQuestionAdapter;
    private List<User> mUsers = new ArrayList<>();
    private List<Question> mQuestions = new ArrayList<>();
    private boolean isUserOK, isQuestionOK;
    private String mSearchContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        mSearchEdit = (EditText) findViewById(R.id.edit_search);
        mClearButton = (ImageButton) findViewById(R.id.btn_clear);
        mLoadingBar = (MaterialProgressBar) findViewById(R.id.progressbar_loading);
        mEmptyLayout = (RelativeLayout) findViewById(R.id.rl_empty);
        mEmptyDescribe = (TextView) findViewById(R.id.tv_hint_empty);
        mEmptyBtn = (TextView) findViewById(R.id.btn_empty);
        mPersonsView = (RecyclerView) findViewById(R.id.recycler_search_person_view);
        mQuestionsView = (RecyclerView) findViewById(R.id.recycler_search_question_view);
        mPersonMoreView = (TextView) findViewById(R.id.tv_persons_more);
        mQuestionMoreView = (TextView) findViewById(R.id.tv_questions_more);
        mPersonContainer = (LinearLayout) findViewById(R.id.ll_search_persons);
        mQuestionContainer = (LinearLayout) findViewById(R.id.ll_search_questions);

        mPersonAdapter = new SearchPersonAdapter(this, mUsers);
        mQuestionAdapter = new SearchQuestionAdapter(this, mQuestions);
        mPersonsView.setLayoutManager(new LinearLayoutManager(this));
        mPersonsView.setAdapter(mPersonAdapter);
        mQuestionsView.setLayoutManager(new LinearLayoutManager(this));
        mQuestionsView.setAdapter(mQuestionAdapter);
        mLoadingBar.setVisibility(View.GONE);
        mEmptyBtn.setVisibility(View.GONE);
        mEmptyDescribe.setText(R.string.notice_no_search_result);
        addListener();
    }

    private void addListener() {
        mClearButton.setOnClickListener(this);
        mPersonMoreView.setOnClickListener(this);
        mQuestionMoreView.setOnClickListener(this);
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

    private void initData(final String text) {
        mLoadingBar.setVisibility(View.VISIBLE);
        mQuestions.clear();
        mUsers.clear();
        SearchManager searchManager = new SearchManager();
        searchManager.setOnSearchUserListener(new SearchManager.OnSearchUserListener() {
            @Override
            public void onSuccess(List users) {
                mLoadingBar.setVisibility(View.GONE);
                mEmptyLayout.setVisibility(View.GONE);
                if (users != null && users.size() > 0) {
                    mUsers.addAll(users);
                    mPersonContainer.setVisibility(View.VISIBLE);
                    mPersonAdapter.notifyDataSetChanged();
                } else {
                    mPersonContainer.setVisibility(View.GONE);
                    isUserOK = true;
                    if (isQuestionOK) {
                        mEmptyLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailed() {
                mLoadingBar.setVisibility(View.GONE);
                isUserOK = true;
                if (isQuestionOK) {
                    mEmptyLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        searchManager.getSearchUsers(text, 3);

        searchManager.setOnSearchQuestionListener(new SearchManager.OnSearchQuestionListener() {
            @Override
            public void onSuccess(List questions) {
                mLoadingBar.setVisibility(View.GONE);
                mEmptyLayout.setVisibility(View.GONE);
                if (questions != null && questions.size() > 0) {
                    mQuestions.addAll(questions);
                    mQuestionContainer.setVisibility(View.VISIBLE);
                    mQuestionAdapter.notifyDataSetChanged();
                } else {
                    mQuestionContainer.setVisibility(View.GONE);
                    isQuestionOK = true;
                    if (isUserOK) {
                        mEmptyLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailed() {
                mLoadingBar.setVisibility(View.GONE);
                isQuestionOK = true;
                if (isUserOK) {
                    mEmptyLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        searchManager.getSearchQuestions(text, 3);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_clear) {
            mSearchEdit.setText("");
        } else if (v.getId() == R.id.tv_persons_more) {
            Intent intent = new Intent(SearchActivity.this, SearchMoreActivity.class);
            intent.putExtra(Constant.KEY_SEARCH_CONTENT, mSearchContent);
            intent.putExtra(Constant.KEY_FROM_SEARCH, true);
            SearchActivity.this.startActivity(intent);
        } else if (v.getId() == R.id.tv_questions_more) {
            Intent intent = new Intent(SearchActivity.this, SearchMoreActivity.class);
            intent.putExtra(Constant.KEY_SEARCH_CONTENT, mSearchContent);
            intent.putExtra(Constant.KEY_FROM_SEARCH, false);
            SearchActivity.this.startActivity(intent);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            mSearchContent = mSearchEdit.getText().toString();
            initData(mSearchContent);
        }
        return true;
    }
}
