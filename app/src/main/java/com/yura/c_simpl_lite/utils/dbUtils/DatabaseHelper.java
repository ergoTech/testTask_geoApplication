package com.yura.c_simpl_lite.utils.dbUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.yura.c_simpl_lite.dao.CoordinateDao;
import com.yura.c_simpl_lite.dao.CropFieldDao;
import com.yura.c_simpl_lite.dao.PolygonDao;
import com.yura.c_simpl_lite.domainEntities.Coordinate;
import com.yura.c_simpl_lite.domainEntities.CropField;
import com.yura.c_simpl_lite.domainEntities.Polygon;

import java.sql.SQLException;

/**
 * Created by Yuriy S on 11.07.2016.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "myLog";

    //имя файла базы данных который будет храниться в /data/data/APPNAME/DATABASE_NAME.db
    private static final String DATABASE_NAME = "myappname.db";

    //с каждым увеличением версии, при нахождении в устройстве БД с предыдущей версией будет выполнен метод onUpgrade();
    private static final int DATABASE_VERSION = 1;

    //ссылки на DAO соответсвующие сущностям, хранимым в БД
    private PolygonDao polygonDao = null;
    private CropFieldDao cropFieldDao = null;
    private CoordinateDao coordinateDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Выполняется, когда файл с БД не найден на устройстве
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, CropField.class);
            TableUtils.createTable(connectionSource, Polygon.class);
            TableUtils.createTable(connectionSource, Coordinate.class);
        } catch (SQLException e) {
            Log.e(TAG, "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    //Выполняется, когда БД имеет версию отличную от текущей
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer,
                          int newVer) {
        try {
            //Так делают ленивые, гораздо предпочтительнее не удаляя БД аккуратно вносить изменения
            TableUtils.dropTable(connectionSource, Polygon.class, true);
            TableUtils.dropTable(connectionSource, CropField.class, true);
            TableUtils.dropTable(connectionSource, Coordinate.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "error upgrading db " + DATABASE_NAME + "from ver " + oldVer);
            throw new RuntimeException(e);
        }
    }

    //синглтон для PolygonDao
    public PolygonDao getPolygonDao() throws SQLException {
        if (polygonDao == null) {
            polygonDao = new PolygonDao(getConnectionSource(), Polygon.class);
        }
        return polygonDao;
    }

    //    синглтон для CropFieldDao
    public CropFieldDao getCropFieldDao() throws SQLException {
        if (cropFieldDao == null) {
            cropFieldDao = new CropFieldDao(getConnectionSource(), CropField.class);
        }
        return cropFieldDao;
    }

    //    синглтон для CoordinateDao
    public CoordinateDao getCoordinateDao() throws SQLException {
        if (coordinateDao == null) {
            coordinateDao = new CoordinateDao(getConnectionSource(), Coordinate.class);
        }
        return coordinateDao;
    }

    //выполняется при закрытии приложения
    @Override
    public void close() {
        super.close();
        cropFieldDao = null;
        polygonDao = null;
        coordinateDao = null;
    }
}



