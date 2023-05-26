package com.example.mytestmenu.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ActivityCollector {
    //存放activity的列表
    public static HashMap<Class<?>, Activity> activities = new LinkedHashMap<>();

    public static void addActivity(Activity activity, Class<?> clz) {
        activities.put(clz, activity);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static <T extends Activity> boolean isActivityExist(Class<T> clz) {
        boolean res;
        Activity activity = getActivity(clz);
        if (activity == null) {
            res = false;
        } else {
            if (activity.isFinishing() || activity.isDestroyed()) {
                res = false;
            } else {
                res = true;
            }
        }
        return res;
    }

    public static <T extends Activity> T getActivity(Class<T> clazz) {
        return (T) activities.get(clazz);
    }

    public static void removeActivity(Activity activity) {
        if (activities.containsValue(activity)) {
            activities.remove(activity.getClass());
        }
    }

    public static void removeAllActivity() {
        if (activities != null && activities.size() > 0) {
            Set<Map.Entry<Class<?>, Activity>> sets = activities.entrySet();
            for (Map.Entry<Class<?>, Activity> s : sets) {
                if (!s.getValue().isFinishing()) {
                    s.getValue().finish();
                }
            }
        }
        activities.clear();
    }
}
