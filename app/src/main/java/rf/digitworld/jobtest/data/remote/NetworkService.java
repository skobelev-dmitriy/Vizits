package rf.digitworld.jobtest.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import rf.digitworld.jobtest.data.model.Organization;
import rf.digitworld.jobtest.data.model.Vizit;
import rx.Observable;

public interface NetworkService {

    String ENDPOINT = "http://demo3526062.mockable.io/";

    @GET("getVisitsListTest")
    Observable<List<Vizit>> getVisitsListTest();
    @GET("getOrganizationListTest")
    Observable<List<Organization>> getOrganizationListTest();

    /******** Helper class that sets up a new services *******/
    class Creator {

        public static NetworkService newRibotsService() {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(NetworkService.class);
        }
    }
}
