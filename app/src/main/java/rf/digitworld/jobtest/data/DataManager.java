package rf.digitworld.jobtest.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import rf.digitworld.jobtest.data.model.Organization;
import rf.digitworld.jobtest.data.model.Vizit;
import rf.digitworld.jobtest.data.remote.NetworkService;
import rx.Observable;
import rx.functions.Action0;
import rf.digitworld.jobtest.data.local.PreferencesHelper;
import rf.digitworld.jobtest.util.EventPosterHelper;
import rf.digitworld.jobtest.data.local.DatabaseHelper;

@Singleton
public class DataManager {

    private final NetworkService mNetworkService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final EventPosterHelper mEventPoster;
    final Realm realm = Realm.getDefaultInstance();

    @Inject
    public DataManager(NetworkService networkService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper, EventPosterHelper eventPosterHelper) {
        mNetworkService = networkService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
        mEventPoster = eventPosterHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<List<Organization>> syncOrganizations() {
        return mNetworkService.getOrganizationListTest();
    }
    public Observable<List<Vizit> >syncVizit() {
        return mNetworkService.getVisitsListTest();

    }




    /// Helper method to post events from doOnCompleted.
    private Action0 postEventAction(final Object event) {
        return new Action0() {
            @Override
            public void call() {
                mEventPoster.postEventSafely(event);
            }
        };
    }

}
