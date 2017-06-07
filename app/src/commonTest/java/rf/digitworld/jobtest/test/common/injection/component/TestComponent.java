package rf.digitworld.jobtest.test.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import rf.digitworld.jobtest.test.common.injection.module.ApplicationTestModule;
import rf.digitworld.jobtest.injection.component.ApplicationComponent;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}
