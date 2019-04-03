package kj.x.recruit.loader;

import com.x.core.http.ObjectLoader;
import com.x.core.http.RetrofitServiceManager;

import java.io.File;

import kj.x.recruit.model.BooleanResult;
import kj.x.recruit.model.PayLoad;
import kj.x.recruit.model.RankResult;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

public class PublicLoader extends ObjectLoader {
    private ApiService apiService;

    public PublicLoader() {
        apiService = RetrofitServiceManager.getInstance().create(ApiService.class);
    }

    public Observable<RankResult> loadRank() {
        return observable(apiService.loadRankList())
                .map(new PayLoad<>());
    }

    public Observable<BooleanResult> uploadFile(File file, String nickName) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody nickNameBody = RequestBody.create(MediaType.parse("text/plain"), nickName);
        return observable(apiService.uploadSingleFile(fileBody, nickNameBody))
                .map(new PayLoad<>());
    }
}
