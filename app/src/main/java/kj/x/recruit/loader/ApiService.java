package kj.x.recruit.loader;

import java.util.Map;

import kj.x.recruit.model.BooleanResult;
import kj.x.recruit.model.RankResult;
import kj.x.recruit.model.ResponseModel;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

public interface ApiService {
    @GET("dd/loadRank")
    Observable<ResponseModel<RankResult>> loadRankList();

    //此处@Part(“file\”; filename=\”avatar.png\”“)注释的含义是该RequestBody 的名称为file，上传的文件名称为avatar.png。
    //@Path注解中的filename与上传文件的真实名称可以不匹配。
    @POST("/file")
    @Multipart
    Observable<ResponseModel<BooleanResult>> uploadSingleFile(@Part("file\";fileName=\"avatar.png\"") RequestBody file, @Part("nickName") RequestBody nickName);
}
