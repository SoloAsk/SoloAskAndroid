package com.soloask.android.common.base;


import com.soloask.android.account.injection.EditUserComponent;
import com.soloask.android.account.injection.EditUserModule;
import com.soloask.android.account.injection.LoginComponent;
import com.soloask.android.account.injection.LoginModule;
import com.soloask.android.account.injection.MyCommonComponent;
import com.soloask.android.account.injection.MyCommonModule;
import com.soloask.android.account.injection.UserComponent;
import com.soloask.android.account.injection.UserModule;
import com.soloask.android.account.injection.WithDrawComponent;
import com.soloask.android.account.injection.WithDrawModule;
import com.soloask.android.main.component.HotComponent;
import com.soloask.android.main.component.MainComponent;
import com.soloask.android.main.component.PersonComponent;
import com.soloask.android.main.module.HotModule;
import com.soloask.android.main.module.MainModule;
import com.soloask.android.main.module.PersonModule;
import com.soloask.android.question.injection.AnswerComponent;
import com.soloask.android.question.injection.AnswerModule;
import com.soloask.android.question.injection.AskComponent;
import com.soloask.android.question.injection.AskModule;
import com.soloask.android.question.injection.QuestionDetailComponent;
import com.soloask.android.question.injection.QuestionDetailModule;
import com.soloask.android.search.injection.SearchComponent;
import com.soloask.android.search.injection.SearchModule;
import com.soloask.android.search.injection.SearchMoreComponent;
import com.soloask.android.search.injection.SearchMoreModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by lebron on 16-8-4.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    MainComponent plus(MainModule module);

    HotComponent plus(HotModule module);

    PersonComponent plus(PersonModule module);

    UserComponent plus(UserModule module);

    EditUserComponent plus(EditUserModule module);

    LoginComponent plus(LoginModule module);

    WithDrawComponent plus(WithDrawModule module);

    MyCommonComponent plus(MyCommonModule module);

    SearchComponent plus(SearchModule module);

    SearchMoreComponent plus(SearchMoreModule module);

    QuestionDetailComponent plus(QuestionDetailModule module);

    AnswerComponent plus(AnswerModule module);

    AskComponent plus(AskModule module);
}
