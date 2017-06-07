package rf.digitworld.jobtest.data.model;


import com.google.gson.annotations.Expose;

import io.realm.RealmObject;
import rf.digitworld.jobtest.R;

public class Vizit extends RealmObject {

    @Expose
    private String title;
    @Expose
    private String organizationId;

    private Organization organization;
    private boolean isSelected=false;

    public Vizit() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
