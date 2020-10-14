package com.dmitry.pisarevskiy.abovezero.weather;

import com.dmitry.pisarevskiy.abovezero.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Current {
    @SerializedName("temp")
    @Expose
    private float temp;

    @SerializedName("wind_speed")
    @Expose
    private float wind_speed;

    @SerializedName("weather")
    @Expose
    private Weather[] weather;

    public float getTemp() {
        return temp;
    }

    public float getWind_speed() {
        return wind_speed;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public int getImage() {
        int img;
        switch (getWeather()[0].getMain()){
            case "Drizzle":
                img = R.drawable.week_rain;
                break;
            case "Rain":
                if (getWeather()[0].getDescription().equals("light rain") ||
                        getWeather()[0].getDescription().equals("moderate rain")) {
                    img =R.drawable.week_rain;
                } else {
                    img =R.drawable.strong_rain;
                }
                break;
            case "Clouds":
                if (getWeather()[0].getDescription().equals("overcast clouds")) {
                    img=R.drawable.cloudly;
                } else {
                    img=R.drawable.week_cloudly;
                }
                break;
            case "Clear":
                img=R.drawable.sunny;
                break;
            default:
                img = R.drawable.cloudly;
        }
        return img;
    }
}
