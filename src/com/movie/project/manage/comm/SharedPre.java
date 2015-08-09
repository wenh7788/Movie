package com.movie.project.manage.comm;

import android.content.SharedPreferences;



public class SharedPre
{
    private static String CONFIG_FILE_NAME = "chehui";

    private volatile static SharedPre instance = null;

    public static SharedPre getInstance()
    {
        if (instance == null)
        {
            instance = new SharedPre();
            
        }

        return instance;
    }

    private SharedPreferences sp = null;


    private SharedPre()
    {
        
    }
    
    
}
