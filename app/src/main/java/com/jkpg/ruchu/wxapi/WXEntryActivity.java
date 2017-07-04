package com.jkpg.ruchu.wxapi;

import com.umeng.socialize.weixin.view.WXCallbackActivity;

/**
 * Created by qindi on 2017/6/29.
 */

public class WXEntryActivity extends WXCallbackActivity  /* implements IWXAPIEventHandler*/ {

  /*  private IWXAPI api;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int errorCode = baseResp.errCode;
        switch (errorCode) {
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                String code = ((SendAuth.Resp) baseResp).code;
                OkGo
                        .post("https://api.weixin.qq.com/sns/oauth2/access_token")
                        .tag(this)
                        .params("appid", Constants.APP_ID)
                        .params("secret", Constants.SECRET)
                        .params("code", code)
                        .params("grant_type", "authorization_code")
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                finish();
                            }
                        });

                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                break;
            default:
                break;
        }
//        ToastUtil.showMessageLong(this, resp.errStr);
    }*/
}
