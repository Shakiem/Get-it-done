package com.shakiemsaunders.get_it_done.DB.DAO;

import android.content.Context;

import com.shakiemsaunders.get_it_done.DB.DBHelper;

public class AbstractDAO {
    protected DBHelper dbHelper;

    public AbstractDAO(Context context){
        dbHelper = DBHelper.getInstance(context);
    }
}
