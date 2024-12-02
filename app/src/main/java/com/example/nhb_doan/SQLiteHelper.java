package com.example.nhb_doan; // Add this line at the top

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.example.nhb_doan.model.Dish;
import com.example.nhb_doan.model.Order;
import com.example.nhb_doan.model.User;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "userDB";
    private static final int DATABASE_VERSION = 3;

    // Tên bảng và cột cho bảng users
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Tên bảng và cột cho bảng dishes
    public static final String TABLE_DISHES = "dishes";
    public static final String COLUMN_DISH_NAME = "name";
    public static final String COLUMN_DISH_DESCRIPTION = "description";
    public static final String COLUMN_DISH_IMAGE_URI = "image_uri";

    // Tên bảng và cột cho bảng orders
    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDER_USER_ID = "user_id";
    public static final String COLUMN_ORDER_DISH_ID = "dish_id";
    public static final String COLUMN_ORDER_QUANTITY = "quantity";
    public static final String COLUMN_ORDER_STATUS = "status";
    public static final String COLUMN_ORDER_ADDRESS = "shipping_address";
    public static final String COLUMN_ORDER_PHONENUMBER = "phone_number";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("SQLiteHelper", "Creating database...");

        // Tạo bảng users
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);
        Log.d("SQLiteHelper", "Created table: " + TABLE_USERS);

        // Thêm tài khoản mặc định
        String INSERT_DEFAULT_USER = "INSERT INTO " + TABLE_USERS + " ("
                + COLUMN_USERNAME + ", "
                + COLUMN_PASSWORD + ") VALUES ('admin', '123')";
        db.execSQL(INSERT_DEFAULT_USER);
        Log.d("SQLiteHelper", "Inserted default user.");

        // Tạo bảng dishes
        String CREATE_DISHES_TABLE = "CREATE TABLE " + TABLE_DISHES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DISH_NAME + " TEXT,"
                + COLUMN_DISH_DESCRIPTION + " TEXT,"
                + COLUMN_DISH_IMAGE_URI + " TEXT"
                + ")";
        db.execSQL(CREATE_DISHES_TABLE);
        Log.d("SQLiteHelper", "Created table: " + TABLE_DISHES);

        // Thêm dữ liệu món ăn mặc định
        String INSERT_DISHES = "INSERT INTO " + TABLE_DISHES + " ("
                + COLUMN_DISH_NAME + ", "
                + COLUMN_DISH_DESCRIPTION + ", "
                + COLUMN_DISH_IMAGE_URI + ") VALUES "
                + "('Bánh mì', 'Bánh mì nóng giòn, đầy đủ thịt và rau sống', 'uri://images/banhmi.jpg'), "
                + "('Phở bò', 'Phở bò đặc biệt với nước dùng thơm ngon, thịt bò mềm', 'uri://images/phobobo.jpg')";
        db.execSQL(INSERT_DISHES);
        Log.d("SQLiteHelper", "Inserted default dishes.");

        // Tạo bảng orders
        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ORDER_USER_ID + " INTEGER,"
                + COLUMN_ORDER_DISH_ID + " INTEGER,"
                + COLUMN_ORDER_QUANTITY + " INTEGER,"
                + COLUMN_ORDER_STATUS + " TEXT,"
                + COLUMN_ORDER_ADDRESS + " TEXT,"  // Thêm cột shipping_address
                + COLUMN_ORDER_PHONENUMBER + " TEXT,"      // Thêm cột phone_number
                + "FOREIGN KEY(" + COLUMN_ORDER_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "),"
                + "FOREIGN KEY(" + COLUMN_ORDER_DISH_ID + ") REFERENCES " + TABLE_DISHES + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(CREATE_ORDERS_TABLE);
        Log.d("SQLiteHelper", "Created table: " + TABLE_ORDERS);

        // Thêm dữ liệu đơn hàng mặc định
        String INSERT_ORDERS = "INSERT INTO " + TABLE_ORDERS + " ("
                + COLUMN_ORDER_USER_ID + ", "
                + COLUMN_ORDER_DISH_ID + ", "
                + COLUMN_ORDER_QUANTITY + ", "
                + COLUMN_ORDER_STATUS + ", "
                + COLUMN_ORDER_ADDRESS + ", "
                + COLUMN_ORDER_PHONENUMBER + ") VALUES "
                + "(1, 1, 2, 'CHUA_GIAO', '123 Main Street', '0123456789'), "
                + "(1, 2, 1, 'DA_GIAO', '456 Elm Street', '0987654321')";
        db.execSQL(INSERT_ORDERS);
        Log.d("SQLiteHelper", "Inserted default orders.");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("SQLiteHelper", "Upgrading database from " + oldVersion + " to " + newVersion);

        // Xóa bảng cũ nếu tồn tại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        Log.d("SQLiteHelper", "Dropped table: " + TABLE_USERS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISHES);
        Log.d("SQLiteHelper", "Dropped table: " + TABLE_DISHES);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        Log.d("SQLiteHelper", "Dropped table: " + TABLE_ORDERS);

        // Tạo lại bảng
        onCreate(db);
        Log.d("SQLiteHelper", "Recreated all tables.");
    }

    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        return false;
    }

    public boolean isUsernameExist(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + "=?",
                new String[]{username}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        return false;
    }

    // Lấy món ăn theo ID
    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + "=?",
                new String[]{String.valueOf(username)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String usernameUser = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
            String passwordUser = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            String userIdUser = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));

            cursor.close();
            return new User(Integer.parseInt(userIdUser), usernameUser, passwordUser);
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public void addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        db.insert(TABLE_USERS, null, values);
    }

    public void addDish(Context context, String name, String description, Uri imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DISH_NAME, name);
        values.put(COLUMN_DISH_DESCRIPTION, description);
        values.put(COLUMN_DISH_IMAGE_URI, imageUri.toString());

        db.insert(TABLE_DISHES, null, values);
    }

    // Lấy món ăn theo ID
    public Dish getDishById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DISHES, null, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISH_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISH_DESCRIPTION));
            String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISH_IMAGE_URI));

            cursor.close();
            return new Dish(id, name, description, imageUri);
        }

        if (cursor != null) {
            cursor.close();
        }
        return null; // Không tìm thấy món ăn
    }

    public List<Dish> getAllDishes() {
        List<Dish> dishList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_DISHES, null);

        if (cursor.moveToFirst()) {
            do {
                dishList.add(new Dish(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISH_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISH_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISH_IMAGE_URI))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dishList;
    }

    // Cập nhật món ăn
    public void updateDish(int id, String name, String description, Uri imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DISH_NAME, name);
        values.put(COLUMN_DISH_DESCRIPTION, description);
        values.put(COLUMN_DISH_IMAGE_URI, imageUri.toString());

        db.update(TABLE_DISHES, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Xóa món ăn
    public void deleteDish(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DISHES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Thêm đơn hàng
    public void addOrder(int userId, int dishId, int quantity, String status, String address, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_USER_ID, userId);
        values.put(COLUMN_ORDER_DISH_ID, dishId);
        values.put(COLUMN_ORDER_QUANTITY, quantity);
        values.put(COLUMN_ORDER_STATUS, status);
        values.put(COLUMN_ORDER_ADDRESS, address);
        values.put(COLUMN_ORDER_PHONENUMBER, phoneNumber);

        db.insert(TABLE_ORDERS, null, values);
    }

    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM orders", null);

        if (cursor.moveToFirst()) {
            do {
                orderList.add(new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_USER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DISH_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PHONENUMBER))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orderList;
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orderList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_ORDERS + " WHERE " + COLUMN_ORDER_USER_ID + " = ?",
                new String[]{String.valueOf(userId)}
        );

        if (cursor.moveToFirst()) {

            do {
                orderList.add(new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_USER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DISH_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PHONENUMBER))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orderList;
    }


    public void deleteOrder(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDERS, COLUMN_ID + "=?", new String[]{String.valueOf(orderId)});
    }

    public void updateOrderStatus(int orderId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_STATUS, newStatus);

        db.update(TABLE_ORDERS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(orderId)});
        db.close();
    }
}
