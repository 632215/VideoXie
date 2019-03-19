package com.weis.videoxie.moudle.activity;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.weis.videoxie.R;
import com.weis.videoxie.base.BaseActivity;
import com.weis.videoxie.bean.JPushBean;
import com.weis.videoxie.bean.PatrolDeviceBean;
import com.weis.videoxie.bean.PatrolMsgBean;
import com.weis.videoxie.bean.WeatherBean;
import com.weis.videoxie.moudle.adapter.PicAdaper;
import com.weis.videoxie.moudle.custom.LoadingLayout;
import com.weis.videoxie.moudle.custom.ShowImagesDialog;
import com.weis.videoxie.presenter.PatrolDetailPresenter;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.utils.ToastUtils;
import com.weis.videoxie.view.PatrolDetailView;

import butterknife.BindView;

public class PatrolDetailActivity extends BaseActivity<PatrolDetailView, PatrolDetailPresenter> implements PatrolDetailView, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    @BindView(R.id.tx_name)
    TextView txName;
    @BindView(R.id.rv_pic)
    RecyclerView rvPic;
    @BindView(R.id.tx_basic)
    TextView txBasic;
    @BindView(R.id.tx_environment_temp)
    TextView txEnvironmentTemp;
    @BindView(R.id.tx_hum_temp)
    TextView txHumTemp;
    @BindView(R.id.tx_see_area)
    TextView txSeeArea;
    @BindView(R.id.tx_cond)
    TextView txCond;
    @BindView(R.id.tx_wind_speed)
    TextView txWindSpeed;
    @BindView(R.id.tx_wind_dec)
    TextView txWindDec;
    @BindView(R.id.tx_wind_power)
    TextView txWindPower;
    @BindView(R.id.tx_rain)
    TextView txRain;
    @BindView(R.id.tx_hum)
    TextView txHum;
    @BindView(R.id.tx_pres)
    TextView txPres;
    @BindView(R.id.tx_aqi)
    TextView txAqi;
    @BindView(R.id.tx_aqi_value)
    TextView txAqiValue;
    @BindView(R.id.tx_air_aqi)
    TextView txAirAqi;
    @BindView(R.id.tx_no2)
    TextView txNo2;
    @BindView(R.id.tx_so2)
    TextView txSo2;
    @BindView(R.id.tx_co)
    TextView txCo;
    @BindView(R.id.tx_pm2)
    TextView txPm2;
    @BindView(R.id.tx_o3)
    TextView txO3;
    @BindView(R.id.cl_weather)
    ConstraintLayout clWeather;

    private PicAdaper picAdaper = null;
    private PatrolDeviceBean patrolDeviceBean = null;
    private PatrolMsgBean.LogListBean logListBean = null;
    private JPushBean jPushBean = null;

    @Override
    protected PatrolDetailPresenter initPresenter() {
        return new PatrolDetailPresenter(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_patrol_detail;
    }

    @Override
    protected void initView() {
        setTxTitle(getString(R.string.activity_patrol_detail));
        showBack();
        initData();
    }

    /**
     * 此页面可从巡检消息和报警消息跳转而来
     * 数据来源不同，操作方式不同
     */
    private void initData() {
        patrolDeviceBean = (PatrolDeviceBean) getIntent().getSerializableExtra("PatrolDeviceBean");
        logListBean = (PatrolMsgBean.LogListBean) getIntent().getSerializableExtra("MsgBean");
        jPushBean = (JPushBean) getIntent().getSerializableExtra("JPushBean");
        loadingLayout.showLoading();
        if (jPushBean != null)
            presenter.getPushInfo(jPushBean);//报警消息跳转
        else
            setPatrolMsg();//巡检消息跳转
    }

    private void setPatrolMsg() {
        if (patrolDeviceBean != null)
            txName.setText(patrolDeviceBean.getName());
        if (logListBean != null) {
            if (logListBean.getPicUrlList() != null) {
                if (picAdaper == null) {
                    picAdaper = new PicAdaper(R.layout.item_pic, logListBean.getPicUrlList());
                    picAdaper.bindToRecyclerView(rvPic);
                    picAdaper.setEnableLoadMore(false);
                    picAdaper.setOnItemClickListener(this);
                    picAdaper.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                    rvPic.setLayoutManager(gridLayoutManager);
                    rvPic.setAdapter(picAdaper);
                }
            }
            if (logListBean.getWeather() == null ||
                    StringUtils.isEmpty(logListBean.getWeather().getAqi())
                    || StringUtils.isEmpty(logListBean.getWeather().getFl()))
                presenter.getweather(patrolDeviceBean);
            else
                setWeather(logListBean.getWeather());
        }
    }

    @Override
    public void getPushInfoNext(PatrolMsgBean.LogListBean info) {
        if (patrolDeviceBean == null)
            patrolDeviceBean = new PatrolDeviceBean();
        patrolDeviceBean.setName(jPushBean.getDeviceName());
        patrolDeviceBean.setId(jPushBean.getDeviceSerial());
        if (logListBean == null)
            logListBean = new PatrolMsgBean.LogListBean();
        logListBean.setPushTime(jPushBean.getAlarmTime());
        logListBean = info;
        setPatrolMsg();//巡检消息跳转
    }

    @Override
    public void getPushInfoError(int i, String msg) {
        showMessage(i, msg);
    }

    public void setWeather(PatrolMsgBean.LogListBean.WeatherBean weather) {
        txEnvironmentTemp.setText("环境温度：" + weather.getTmp() + "℃");
        txHumTemp.setText("体感温度：" + weather.getFl() + "℃");
        txCond.setText("天气状况：" + weather.getTxt());
        txSeeArea.setText("能见度：" + weather.getVis() + "km");
        txWindSpeed.setText("风速：" + weather.getSpd() + "m/s");
        txWindDec.setText("风向：" + weather.getDir());
        txWindPower.setText("风力：" + weather.getSc());
        txRain.setText("降雨量：" + weather.getPcpn() + "mm");
        txHum.setText("湿度：" + weather.getHum() + "%");
        txPres.setText("大气压强：" + weather.getPres() + "pa");
        txAqiValue.setText("空气质量指数：" + weather.getAqi());
        txAirAqi.setText("空气质量：" + weather.getQlty());
        txNo2.setText("二氧化氮：" + weather.getNo2());
        txSo2.setText("二氧化硫：" + weather.getSo2());
        txCo.setText("一氧化碳：" + weather.getCo());
        txPm2.setText("PM2.5：" + weather.getPm25());
        txO3.setText("臭氧：" + weather.getO3());
        loadingLayout.showContent();
    }

    @Override
    public void showMessage(int i, final String s) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(PatrolDetailActivity.this, s);
            }
        });
    }

    @Override
    public void getWeatherNext(final WeatherBean weatherBean) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (weatherBean == null
                        || weatherBean.getHeWeather5() == null
                        || weatherBean.getHeWeather5().get(0) == null
                        || weatherBean.getHeWeather5().get(0).getBasic() == null
                        || weatherBean.getHeWeather5().get(0).getNow() == null
                        || weatherBean.getHeWeather5().get(0).getAqi() == null
                        || weatherBean.getHeWeather5().get(0).getAqi().getCity() == null) {
                    clWeather.setVisibility(View.GONE);
                } else {
                    final WeatherBean.HeWeather5Bean.NowBean nowBean = weatherBean.getHeWeather5().get(0).getNow();
                    final WeatherBean.HeWeather5Bean.AqiBean aqiBean = weatherBean.getHeWeather5().get(0).getAqi();
                    clWeather.setVisibility(View.VISIBLE);
                    txEnvironmentTemp.setText("环境温度：" + nowBean.getTmp() + "℃");
                    txHumTemp.setText("体感温度：" + nowBean.getFl() + "℃");
                    txCond.setText("天气状况：" + nowBean.getCond().getTxt());
                    txSeeArea.setText("能见度：" + nowBean.getVis() + "km");
                    txWindSpeed.setText("风速：" + nowBean.getWind().getSpd() + "m/s");
                    txWindDec.setText("风向：" + nowBean.getWind().getDir());
                    txWindPower.setText("风力：" + nowBean.getWind().getDeg());
                    txRain.setText("降雨量：" + nowBean.getPcpn() + "mm");
                    txHum.setText("湿度：" + nowBean.getHum() + "%");
                    txPres.setText("大气压强：" + nowBean.getPres() + "pa");
                    txAqiValue.setText("空气质量指数：" + aqiBean.getCity().getAqi());
                    txAirAqi.setText("空气质量：" + aqiBean.getCity().getQlty());
                    txNo2.setText("二氧化氮：" + aqiBean.getCity().getNo2());
                    txSo2.setText("二氧化硫：" + aqiBean.getCity().getSo2());
                    txCo.setText("一氧化碳：" + aqiBean.getCity().getCo());
                    txPm2.setText("PM2.5：" + aqiBean.getCity().getPm25());
                    txO3.setText("臭氧：" + aqiBean.getCity().getO3());
                }
                loadingLayout.showContent();
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        new ShowImagesDialog(this, adapter.getData(),position, metrics).show();
    }

    @Override
    protected void onBack(View view) {
        super.onBack(view);
        finish();
    }
}
