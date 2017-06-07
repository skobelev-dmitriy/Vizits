package rf.digitworld.jobtest.ui.main;

import java.util.List;

import rf.digitworld.jobtest.data.model.Organization;
import rf.digitworld.jobtest.data.model.Ribot;
import rf.digitworld.jobtest.data.model.Vizit;
import rf.digitworld.jobtest.ui.base.MvpView;

public interface MainMvpView extends MvpView {

    void showVizits(List<Vizit> vizits);

    void showVizitsEmpty();

    void showError();

}
