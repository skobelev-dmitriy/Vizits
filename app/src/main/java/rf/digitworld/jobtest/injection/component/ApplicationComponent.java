package rf.digitworld.jobtest.injection.component;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;
import rf.digitworld.jobtest.data.local.PreferencesHelper;
import rf.digitworld.jobtest.injection.ApplicationContext;
import rf.digitworld.jobtest.injection.module.ApplicationModule;
import rf.digitworld.jobtest.data.DataManager;
import rf.digitworld.jobtest.data.local.DatabaseHelper;
import rf.digitworld.jobtest.data.remote.NetworkService;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {



    @ApplicationContext
    Context context();
    Application application();
    NetworkService ribotsService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    Bus eventBus();

}
