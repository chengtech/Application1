package com.chengtech.chengtechmt.activity.monitoremergency.weathermonitor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;


import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.util.MyConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherMonitorLocationActivity extends BaseActivity implements AMap.OnInfoWindowClickListener {
    private MapView mapView;
    private String mapData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_weather_monitor_location);

        setNavigationIcon(true);
        hidePagerNavigation(true);
        toolbar.setTitle("气象站位置信息");

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMap().setOnInfoWindowClickListener(this);

        mapData = getIntent().getStringExtra("mapData");

        if (!TextUtils.isEmpty(mapData)) {
            try {
                JSONArray jsonArray = new JSONArray(mapData);
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String x = jsonObject.getString("x");
                        String y = jsonObject.getString("y");
                        String name = jsonObject.getString("name");
                        LatLng latLng = new LatLng(Double.parseDouble(y), Double.parseDouble(x));
                        CoordinateConverter converter = new CoordinateConverter();
                        converter.from(CoordinateConverter.CoordType.GPS);
                        converter.coord(latLng);
                        LatLng convert = converter.convert();
                        mapView.getMap().addMarker(new MarkerOptions().position(convert).title(name));
                        if (i == 0) {
                            mapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(mapView.getMap().getMaxZoomLevel() - 8));
                            mapView.getMap().moveCamera(CameraUpdateFactory.changeLatLng(convert));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public static void startAction(Context context, String mapData) {
        Intent intent = new Intent(context, WeatherMonitorLocationActivity.class);
        intent.putExtra("mapData", mapData);
        context.startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String url1 = MyConstants.PRE_URL + "mt/monitoremergency/weathermonitor/weathermonitordata/listWeatherMonitorDataVisPhone.jsp";
        WeatherMonitorTypeActivity.startAction(WeatherMonitorLocationActivity.this, marker.getTitle().trim() + "-能见度", url1);
    }
}
