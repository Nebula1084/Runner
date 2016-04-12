package se.runner.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;

public class MapActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener,GeocodeSearch.OnGeocodeSearchListener,AMap.OnMarkerDragListener,
        AMap.OnMarkerClickListener
{
    final public static String LONGITUDE = "longitude";
    final public static String LATITUDE = "latitude";

    final private static boolean show_blue_arrow = false;

    private AMap aMap;
    private MapView mapView;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private ProgressDialog progDialog = null;
    private GeocodeSearch geocoderSearch;
    private String addressName;
    private Marker geoMarker;
    private LatLonPoint latLonPoint = new LatLonPoint(40.003662, 116.465271);

    private Double selfLatitude,selfLongtitude;

    private MaterialEditText address_edit;

    private Intent result;
    @Bind(R.id.tool_bar)
    Toolbar tool_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        setSupportActionBar(tool_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        AMapLocationClient.setApiKey("6fb01cc4afb8c3c461b106a89d16d558");


        address_edit = (MaterialEditText) findViewById(R.id.address_edit_text);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        init();


        result = new Intent();

        Log.e("Map","sha1="+sHA1(this));
    }

    private void init()
    {
        if( aMap == null)
        {
            aMap = mapView.getMap();
            geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f,0.5f)
                    .title("hello")
                    .position(new LatLng(30.761609, 120.122645))
                    .draggable(true));

            setUpMap();
        }

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        progDialog = new ProgressDialog(this);
    }

    private void setUpMap()
    {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();

        if( show_blue_arrow )
        {
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                    .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
            myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
            myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
            // myLocationStyle.anchor(int,int)  //设置小蓝点的锚点
            myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细

            aMap.setMyLocationStyle(myLocationStyle);
        }

        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false


        // set marker

        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        aMap.setOnMarkerClickListener(this);


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation)
    {
        if (mListener != null && amapLocation != null)
        {
//            Log.e("onLocationChanged","mListener and amapLocation are not null");

            if (amapLocation != null && amapLocation.getErrorCode() == 0)
            {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点

                selfLatitude = amapLocation.getLatitude();
                selfLongtitude = amapLocation.getLongitude();

                geoMarker.setPosition(new LatLng(selfLatitude,selfLongtitude));
            }
            else
            {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /*
    * 开始定位
    * */

    @Override
    public void activate(OnLocationChangedListener listener)
    {
        mListener = listener;
        if (mlocationClient == null)
        {
//            Log.e("activate","mlocationClient is null");
            if(this == null)
                Log.e("activate","this context is null");

            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
        else
            Log.e("activate","mlocationClient is not null");
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate()
    {
        mListener = null;
        if (mlocationClient != null)
        {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    public void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在获取地址");
        progDialog.show();
    }

    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    public void getLatlon(final String name)
    {
        showDialog();
        GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }

    public void getAddress(final LatLonPoint latLonPoint)
    {
        showDialog();
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode)
    {
        dismissDialog();
        if (rCode == 1000)
        {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0)
            {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(convertToLatLng(address.getLatLonPoint()), 15));

                geoMarker.setPosition(convertToLatLng(address
                        .getLatLonPoint()));
                addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
                        + address.getFormatAddress();
                show(MapActivity.this, addressName);
            }
            else
            {
                show(MapActivity.this, "no result");
            }

        }
        else
        {
            showerror(MapActivity.this, rCode);
        }
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode)
    {
        dismissDialog();
        if (rCode == 1000)
        {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null)
            {
                addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        convertToLatLng(latLonPoint), 15));
                geoMarker.setPosition(convertToLatLng(latLonPoint));
                geoMarker.setTitle("您的位置");
                geoMarker.setSnippet(addressName);
                show(MapActivity.this, addressName);
            }
            else
            {
                show(MapActivity.this, "no result");
            }
        }
        else
        {
            showerror(MapActivity.this, rCode);
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker)
    {
        jumpPoint(marker);
        return true;
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker)
    {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        Point startPoint = proj.toScreenLocation(geoMarker.getPosition());
        startPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run()
            {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * geoMarker.getPosition().longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * geoMarker.getPosition().latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                aMap.invalidate();// 刷新地图
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });

    }

    @Override
    public void onMarkerDragStart(Marker marker)
    {
        Log.e("marker","you start drag at ("+marker.getPosition().latitude+","+marker.getPosition().longitude+")");
    }

    @Override
    public void onMarkerDrag(Marker marker)
    {
        latLonPoint.setLatitude(marker.getPosition().latitude);
        latLonPoint.setLongitude(marker.getPosition().longitude);
    }

    @Override
    public void onMarkerDragEnd(Marker marker)
    {
        Log.e("marker","you end dragging at ("+marker.getPosition().latitude+","+marker.getPosition().longitude+")");
        getAddress(latLonPoint);
    }



    public static LatLng convertToLatLng(LatLonPoint latLonPoint)
    {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }



    // for debug
    public static void show(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    public static void showerror(Context context, int rCode){
        try {
            switch (rCode)
            {
                //服务错误码
                case 1001:
                    throw new AMapException(AMapException.AMAP_SIGNATURE_ERROR);
                case 1002:
                    throw new AMapException(AMapException.AMAP_INVALID_USER_KEY);
                case 1003:
                    throw new AMapException(AMapException.AMAP_SERVICE_NOT_AVAILBALE);
                case 1004:
                    throw new AMapException(AMapException.AMAP_DAILY_QUERY_OVER_LIMIT);
                case 1005:
                    throw new AMapException(AMapException.AMAP_ACCESS_TOO_FREQUENT);
                case 1006:
                    throw new AMapException(AMapException.AMAP_INVALID_USER_IP);
                case 1007:
                    throw new AMapException(AMapException.AMAP_INVALID_USER_DOMAIN);
                case 1008:
                    throw new AMapException(AMapException.AMAP_INVALID_USER_SCODE);
                case 1009:
                    throw new AMapException(AMapException.AMAP_USERKEY_PLAT_NOMATCH);
                case 1010:
                    throw new AMapException(AMapException.AMAP_IP_QUERY_OVER_LIMIT);
                case 1011:
                    throw new AMapException(AMapException.AMAP_NOT_SUPPORT_HTTPS);
                case 1012:
                    throw new AMapException(AMapException.AMAP_INSUFFICIENT_PRIVILEGES);
                case 1013:
                    throw new AMapException(AMapException.AMAP_USER_KEY_RECYCLED);
                case 1100:
                    throw new AMapException(AMapException.AMAP_ENGINE_RESPONSE_ERROR);
                case 1101:
                    throw new AMapException(AMapException.AMAP_ENGINE_RESPONSE_DATA_ERROR);
                case 1102:
                    throw new AMapException(AMapException.AMAP_ENGINE_CONNECT_TIMEOUT);
                case 1103:
                    throw new AMapException(AMapException.AMAP_ENGINE_RETURN_TIMEOUT);
                case 1200:
                    throw new AMapException(AMapException.AMAP_SERVICE_INVALID_PARAMS);
                case 1201:
                    throw new AMapException(AMapException.AMAP_SERVICE_MISSING_REQUIRED_PARAMS);
                case 1202:
                    throw new AMapException(AMapException.AMAP_SERVICE_ILLEGAL_REQUEST);
                case 1203:
                    throw new AMapException(AMapException.AMAP_SERVICE_UNKNOWN_ERROR);
                    //sdk返回错误
                case 1800:
                    throw new AMapException(AMapException.AMAP_CLIENT_ERRORCODE_MISSSING);
                case 1801:
                    throw new AMapException(AMapException.AMAP_CLIENT_ERROR_PROTOCOL);
                case 1802:
                    throw new AMapException(AMapException.AMAP_CLIENT_SOCKET_TIMEOUT_EXCEPTION);
                case 1803:
                    throw new AMapException(AMapException.AMAP_CLIENT_URL_EXCEPTION);
                case 1804:
                    throw new AMapException(AMapException.AMAP_CLIENT_UNKNOWHOST_EXCEPTION);
                case 1806:
                    throw new AMapException(AMapException.AMAP_CLIENT_NETWORK_EXCEPTION);
                case 1900:
                    throw new AMapException(AMapException.AMAP_CLIENT_UNKNOWN_ERROR);
                case 1901:
                    throw new AMapException(AMapException.AMAP_CLIENT_INVALID_PARAMETER);
                case 1902:
                    throw new AMapException(AMapException.AMAP_CLIENT_IO_EXCEPTION);
                case 1903:
                    throw new AMapException(AMapException.AMAP_CLIENT_NULLPOINT_EXCEPTION);
                    //云图和附近错误码
                case 2000:
                    throw new AMapException(AMapException.AMAP_SERVICE_TABLEID_NOT_EXIST);
                case 2001:
                    throw new AMapException(AMapException.AMAP_ID_NOT_EXIST);
                case 2002:
                    throw new AMapException(AMapException.AMAP_SERVICE_MAINTENANCE);
                case 2003:
                    throw new AMapException(AMapException.AMAP_ENGINE_TABLEID_NOT_EXIST);
                case 2100:
                    throw new AMapException(AMapException.AMAP_NEARBY_INVALID_USERID);
                case 2101:
                    throw new AMapException(AMapException.AMAP_NEARBY_KEY_NOT_BIND);
                case 2200:
                    throw new AMapException(AMapException.AMAP_CLIENT_UPLOADAUTO_STARTED_ERROR);
                case 2201:
                    throw new AMapException(AMapException.AMAP_CLIENT_USERID_ILLEGAL);
                case 2202:
                    throw new AMapException(AMapException.AMAP_CLIENT_NEARBY_NULL_RESULT);
                case 2203:
                    throw new AMapException(AMapException.AMAP_CLIENT_UPLOAD_TOO_FREQUENT);
                case 2204:
                    throw new AMapException(AMapException.AMAP_CLIENT_UPLOAD_LOCATION_ERROR);
                    //路径规划
                case 3000:
                    throw new AMapException(AMapException.AMAP_ROUTE_OUT_OF_SERVICE);
                case 3001:
                    throw new AMapException(AMapException.AMAP_ROUTE_NO_ROADS_NEARBY);
                case 3002:
                    throw new AMapException(AMapException.AMAP_ROUTE_FAIL);
                case 3003:
                    throw new AMapException(AMapException.AMAP_OVER_DIRECTION_RANGE);
                    //短传分享
                case 4000:
                    throw new AMapException(AMapException.AMAP_SHARE_LICENSE_IS_EXPIRED);
                case 4001:
                    throw new AMapException(AMapException.AMAP_SHARE_FAILURE);
                default:
                    Toast.makeText(context,"错误码："+rCode , Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public static String sHA1(Context context)
    {
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);

            byte[] cert = info.signatures[0].toByteArray();

            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++)
            {
                String appendString = Integer.toHexString(0xFF & publicKey[i]).toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            return hexString.toString();
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.map_confirm)
    void confirm()
    {
        String addr = address_edit.getText().toString();

        result.putExtra("address", addr);
        setResult(RESULT_OK, result);

        finish();
    }
}
