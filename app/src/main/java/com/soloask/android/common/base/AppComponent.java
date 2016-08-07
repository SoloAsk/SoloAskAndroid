package com.soloask.android.common.base;


import com.soloask.android.account.injection.EditUserComponent;
import com.soloask.android.account.injection.EditUserModule;
import com.soloask.android.account.injection.LoginComponent;
import com.soloask.android.account.injection.LoginModule;
import com.soloask.android.account.injection.UserComponent;
import com.soloask.android.account.injection.UserModule;
import com.soloask.android.account.injection.WithDrawComponent;
import com.soloask.android.account.injection.WithDrawModule;
import com.soloask.android.main.component.HotComponent;
import com.soloask.android.main.component.PersonComponent;
import com.soloask.android.main.module.HotModule;
import com.soloask.android.main.module.PersonModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by lebron on 16-8-4.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    HotComponent plus(HotModule module);

    PersonComponent plus(PersonModule module);

    UserComponent plus(UserModule module);

    EditUserComponent plus(EditUserModule module);

    LoginComponent plus(LoginModule module);

    WithDrawComponent plus(WithDrawModule module);
}
