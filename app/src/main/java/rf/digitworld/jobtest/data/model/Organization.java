package rf.digitworld.jobtest.data.model;


import com.google.gson.annotations.Expose;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import rf.digitworld.jobtest.R;

public class Organization extends RealmObject {
    @Expose
    private String title;
    @Expose
    @PrimaryKey
    private String organizationId;
    @Expose
    private Double latitude;
    @Expose
    private Double longitude;

    private boolean isSelected;

    public Organization() {
        isSelected=false;
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

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public int getMarker(){
        int i=0;
        switch (getTitle()){
            case "Сбербанк-Технологии":
                i= R.mipmap.sber_off;
                break;
            case "Тинькофф-банк":
                i= R.mipmap.tincoff_off;
                break;

        }
        return i;

    }
    public int getMarkerActive(){
        int i=0;
        switch (getTitle()){
            case "Сбербанк-Технологии":
                i=R.mipmap.sber_on;
                break;
            case "Тинькофф-банк":
                i= R.mipmap.tincoff_on;
                break;

        }
        return i;

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
