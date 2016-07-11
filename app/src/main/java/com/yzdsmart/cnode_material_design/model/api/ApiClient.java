package com.yzdsmart.cnode_material_design.model.api;


import com.yzdsmart.cnode_material_design.model.util.EntityUtils;
import com.yzdsmart.cnode_material_design.model.util.HttpUtils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {

    private ApiClient() {
    }

    public static final ApiService service = new Retrofit.Builder()
            .baseUrl(ApiDefine.API_BASE_URL)
            .client(HttpUtils.client)
            .addConverterFactory(GsonConverterFactory.create(EntityUtils.gson))
            .build()
            .create(ApiService.class);

}
