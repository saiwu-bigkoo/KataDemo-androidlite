package com.bigkoo.billlite.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bigkoo.billlite.constants.DBConstant;
import com.bigkoo.billlite.manager.AppInfoManager;
import com.bigkoo.kataframework.bean.HttpResult;
import com.bigkoo.kataframework.http.constants.HttpStatusConstants;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by sai on 2018/4/29.
 */

public class DBServiceGenerator {
    private final static String DBNAME = "billlite.db";

    static LiteOrm liteOrm;


    private DBServiceGenerator() {
        if (liteOrm == null) {
            liteOrm = LiteOrm.newSingleInstance(AppInfoManager.getInstance().getContext(), DBServiceGenerator.DBNAME);
        }
    }

    private static class DBServiceGeneratorInstance {
        private static final DBServiceGenerator INSTANCE = new DBServiceGenerator();
    }

    public static DBServiceGenerator getInstance() {
        return DBServiceGeneratorInstance.INSTANCE;
    }

    private Observable createObservable(ObservableOnSubscribe observableOnSubscribe) {
        return Observable.create(observableOnSubscribe);
    }

    public <R> Observable<HttpResult<R>> update(final Object data) {
        return createObservable(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                HttpResult<R> result = new HttpResult();

                Integer row = liteOrm.update(data, ConflictAlgorithm.Rollback);
                //row 小于 0 插入错误
                if (row < 0) {
                    result.setCode(DBConstant.DBRESULTCODE_FAIL);
                    result.setMsg("数据更新失败");
                } else {
                    result.setCode(HttpStatusConstants.CODE_SUCCESS);
                    result.setData((R) row);
                }
                emitter.onNext(result);
                emitter.onComplete();
            }
        });
    }

    public <R, T> Observable<HttpResult<R>> update(final Collection<T> data) {
        return createObservable(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                HttpResult<R> result = new HttpResult();

                Integer row = liteOrm.update(data, ConflictAlgorithm.Rollback);
                //row 小于 0 插入错误
                if (row < 0) {
                    result.setCode(DBConstant.DBRESULTCODE_FAIL);
                    result.setMsg("数据更新失败");
                } else {
                    result.setCode(HttpStatusConstants.CODE_SUCCESS);
                    result.setData((R) row);
                }
                emitter.onNext(result);
                emitter.onComplete();
            }
        });
    }

    public <R> Observable<HttpResult<R>> insert(final Object data) {
        return createObservable(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                HttpResult<R> result = new HttpResult();

                Long row = liteOrm.insert(data, ConflictAlgorithm.Rollback);
                //row 小于 0 插入错误
                if (row < 0) {
                    result.setCode(DBConstant.DBRESULTCODE_FAIL);
                    result.setMsg("数据保存失败");
                } else {
                    result.setCode(HttpStatusConstants.CODE_SUCCESS);
                    result.setData((R) row);
                }
                emitter.onNext(result);
                emitter.onComplete();
            }
        });
    }

    public <R> Observable<HttpResult<R>> save(final Object data) {
        return createObservable(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                HttpResult<R> result = new HttpResult();

                Long row = liteOrm.save(data);
                //row 小于 0 插入错误
                if (row < 0) {
                    result.setCode(DBConstant.DBRESULTCODE_FAIL);
                    result.setMsg("数据保存失败");
                } else {
                    result.setCode(HttpStatusConstants.CODE_SUCCESS);
                    result.setData((R) row);
                }
                emitter.onNext(result);
                emitter.onComplete();
            }
        });
    }

    public <R,T> Observable<HttpResult<R>> insert(final Collection<T> data) {
        return createObservable(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                HttpResult<R> result = new HttpResult();

                Integer row = liteOrm.insert(data, ConflictAlgorithm.Rollback);
                //row 小于 0 插入错误
                if (row < 0) {
                    result.setCode(DBConstant.DBRESULTCODE_FAIL);
                    result.setMsg("数据保存失败");
                } else {
                    result.setCode(HttpStatusConstants.CODE_SUCCESS);
                    result.setData((R) row);
                }
                emitter.onNext(result);
                emitter.onComplete();
            }
        });
    }

    public <R> Observable<HttpResult<R>> delete(final Object data) {
        return createObservable(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                HttpResult<R> result = new HttpResult();

                Integer row = liteOrm.delete(data);
                //row 小于 0 插入错误
                if (row < 0) {
                    result.setCode(DBConstant.DBRESULTCODE_FAIL);
                    result.setMsg("数据删除失败");
                } else {
                    result.setCode(HttpStatusConstants.CODE_SUCCESS);
                    result.setData((R) row);
                }
                emitter.onNext(result);
                emitter.onComplete();
            }
        });
    }

    public <R> Observable<HttpResult<R>> query(final Class data) {
        return createObservable(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                HttpResult<R> result = new HttpResult();

                ArrayList arrayList = liteOrm.query(data);

                result.setCode(HttpStatusConstants.CODE_SUCCESS);
                result.setData((R) arrayList);
                emitter.onNext(result);
                emitter.onComplete();
            }
        });
    }

    public <R> Observable<HttpResult<R>> query(final QueryBuilder builder) {
        return createObservable(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                HttpResult<R> result = new HttpResult();

                ArrayList arrayList = liteOrm.query(builder);

                result.setCode(HttpStatusConstants.CODE_SUCCESS);
                result.setData((R) arrayList);
                emitter.onNext(result);
                emitter.onComplete();
            }
        });
    }

    public <R> Observable<HttpResult<R>> queryById(final long id, final Class<R> clazz) {
        return createObservable(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                HttpResult<R> result = new HttpResult();

                R r = liteOrm.queryById(id, clazz);

                result.setCode(HttpStatusConstants.CODE_SUCCESS);
                result.setData(r);
                emitter.onNext(result);
                emitter.onComplete();
            }
        });
    }


    public <R> Observable<HttpResult<R>> queryCount(final Class data) {
        return createObservable(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                HttpResult<R> result = new HttpResult();

                Long row = liteOrm.queryCount(data);

                result.setCode(HttpStatusConstants.CODE_SUCCESS);
                result.setData((R) row);
                emitter.onNext(result);
                emitter.onComplete();
            }
        });
    }

    public <T> Observable<HttpResult<List<T>>> rawQuery(final String sql, final Class<T> clazz) {
        return createObservable(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                HttpResult<List<T>> result = new HttpResult();

                 SQLiteDatabase db= liteOrm.getReadableDatabase();
                Cursor cursor = db.rawQuery(sql, null);
                List<T> resultList = null;
                try {
                    resultList = getCursorToList(cursor, clazz);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } finally {
                    cursor.close();
                }

                result.setCode(HttpStatusConstants.CODE_SUCCESS);
                result.setData(resultList);
                emitter.onNext(result);
                emitter.onComplete();
            }
        });
    }

    private  <T> List<T> getCursorToList(Cursor cursor,Class<T> clazz) throws InstantiationException, IllegalAccessException {
        ArrayList resultList = new ArrayList();
        while (cursor.moveToNext()) {
            T result = getCursorToObject(cursor, clazz);
            resultList.add(result);
        }

        return resultList;
    }

    private  <T> T getCursorToObject(Cursor cursor, Class clazz) throws IllegalAccessException, InstantiationException {
        T result = (T) clazz.newInstance();

        HashMap<String, Field> fieldsMap = new HashMap<>();
        //反射获取属性并迭代获取到注解，如果没有注解默认用属性名
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
//            //如果注解是DBNever，则不要保存字段
//            if (field.getAnnotation(Ignore.class) != null) continue;

            String fieldName = field.getAnnotation(Column.class) != null ? field.getAnnotation(Column.class).value() : field.getName();
            fieldsMap.put(fieldName, field);
        }

        for (String fieldName : fieldsMap.keySet()) {
            int index = cursor.getColumnIndex(fieldName);
            if (index < 0) continue;
            Field field = fieldsMap.get(fieldName);
            field.setAccessible(true);
            Class clazzType = field.getType();
            if (clazzType == String.class) {
                field.set(result, cursor.getString(index));
            } else if (clazzType == Integer.class || clazzType == int.class) {
                field.set(result, cursor.getInt(index));
            } else if( clazzType == Boolean.class || clazzType == boolean.class) {
                field.set(result, Boolean.valueOf(cursor.getString(index)));
            } else if (clazzType == Long.class || clazzType == long.class) {
                field.set(result, cursor.getLong(index));
            } else if (clazzType == Double.class || clazzType == double.class) {
                field.set(result, cursor.getDouble(index));
            } else if (clazzType == Float.class || clazzType == float.class) {
                field.set(result, cursor.getFloat(index));
            } else if (clazzType == byte[].class) {
                field.set(result, cursor.getBlob(index));
            }
        }
        return result;
    }

}
