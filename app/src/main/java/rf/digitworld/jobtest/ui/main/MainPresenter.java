package rf.digitworld.jobtest.ui.main;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import rf.digitworld.jobtest.data.model.Organization;
import rf.digitworld.jobtest.data.model.Vizit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import rf.digitworld.jobtest.data.DataManager;
import rf.digitworld.jobtest.data.model.Ribot;
import rf.digitworld.jobtest.ui.base.BasePresenter;

public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    final Realm realm = Realm.getDefaultInstance();

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadVizits() {
        checkViewAttached();
        mSubscription = realm.where(Vizit.class).findAll().asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                //.subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Vizit>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<Vizit> vizits) {
                        if (vizits.isEmpty()) {
                            getMvpView().showVizitsEmpty();
                        } else {
                            getMvpView().showVizits(realm.copyFromRealm(vizits));
                        }
                    }
                });
    }
    /*public void loadOgranizations() {
        checkViewAttached();
        mSubscription = realm.where(Organization.class).findAll().asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                //.subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Organization>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<Organization> organizations) {
                        if (organizations.isEmpty()) {
                            getMvpView().showOrganizationsEmpty();
                        } else {
                            getMvpView().showOrganizations(organizations);
                        }
                    }
                });
    }*/
    public void syncVizits() {
        checkViewAttached();
        mSubscription = mDataManager.syncVizit()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Vizit>>() {
                    @Override
                    public void onCompleted() {

                        loadVizits();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<Vizit> vizits) {
                        List <Organization> organizations=realm.where(Organization.class).findAll();
                        realm.beginTransaction();
                        realm.delete(Vizit.class);
                        for (Vizit vizit:vizits){
                            String vizitId=vizit.getOrganizationId();
                            for (Organization organization:organizations){
                                if(organization.getOrganizationId().equals(vizitId)){
                                    vizit.setOrganization(organization);
                                }
                            }
                        }
                        realm.copyToRealmOrUpdate(vizits);
                        realm.commitTransaction();
                        Log.d("StartActivity","Сделано "+vizits.size()+" записей");
                        //realm.close();

                    }
                });
    }
    public void syncOrganizations() {
        checkViewAttached();
        mSubscription = mDataManager.syncOrganizations()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Organization>>() {
                    @Override
                    public void onCompleted() {
                        syncVizits();
                        //loadOgranizations();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<Organization> organizations) {
                        realm.beginTransaction();
                        realm.delete(Organization.class);
                        realm.copyToRealmOrUpdate(organizations);
                        realm.commitTransaction();
                        Log.d("StartActivity","Сделано "+organizations.size()+" записей");
                        realm.close();
                    }
                });
    }
    public void setActiveOrg(final String id){
        setActiveVizit(null);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                List<Organization> list=realm.where(Organization.class).findAll();
                for (Organization org:list) {
                    org.setSelected(false);
                }

                if(id!=null){
                    Organization organization=realm.where(Organization.class).equalTo("organizationId", id).findFirst();
                    organization.setSelected(true);
                }

            }
        });
    }
    public void setActiveVizit(final Vizit vizit){
        //setActiveOrg(null);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                List<Vizit> list=realm.where(Vizit.class).findAll();
                for (Vizit vizit0:list) {
                    vizit0.setSelected(false);
                    vizit0.getOrganization().setSelected(false);
                }
                if(vizit!=null){
                    Vizit vizit1=realm.where(Vizit.class).equalTo("title",vizit.getTitle()).findFirst();
                    vizit1.setSelected(true);
                    //vizit1.getOrganization().setSelected(true);
                }

            }
        });
    }
}
