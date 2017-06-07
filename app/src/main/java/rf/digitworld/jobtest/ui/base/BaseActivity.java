package rf.digitworld.jobtest.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rf.digitworld.jobtest.TestApplication;
import rf.digitworld.jobtest.injection.component.ActivityComponent;
import rf.digitworld.jobtest.injection.module.ActivityModule;
import rf.digitworld.jobtest.injection.component.DaggerActivityComponent;

public class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(TestApplication.get(this).getComponent())
                    .build();
        }
        return mActivityComponent;
    }

}
