package term.rjb.x2l.lessoncheck.manager;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

public class ActivityManager {
    private static List<Activity> activityList;
    private static ActivityManager instance;
    private PendingIntent restartIntent;

    private ActivityManager() {
    }

    public static ActivityManager getAppManager() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        if (activityList == null) {
            activityList = new CopyOnWriteArrayList<Activity>();
        }
        activityList.add(activity);
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityList) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public Activity getActivity(Class<?> cls){
        for (Activity activity : activityList) {
            if (activity.getClass().equals(cls)) {
               return activity;
            }
        }
        return null;
    }

    public void finishAllActivity() {
        for (int i = 0, size = activityList.size(); i < size; i++) {
            if (null != activityList.get(i)) {
                activityList.get(i).finish();
            }
        }
        activityList.clear();
    }

    public void exitApp(Context context) {
        try {
            finishAllActivity();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
        }
    }

}
