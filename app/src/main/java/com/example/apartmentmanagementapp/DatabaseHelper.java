package com.example.apartmentmanagementapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "apartment_management.db";
    private static final int DATABASE_VERSION = 2;

    // Users table constants
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";
    private static final String COLUMN_PASSPORT_URI = "passport_uri";
    private static final String COLUMN_ID_FRONT_URI = "id_front_uri";
    private static final String COLUMN_ID_BACK_URI = "id_back_uri";

    // Estate table constants
    private static final String TABLE_ESTATE = "estate";
    private static final String COLUMN_ESTATE_ID = "estate_id";
    private static final String COLUMN_ESTATE_NAME = "estate_name";

    // Apartment complex table constants
    public static final String TABLE_APARTMENT_COMPLEX = "apartment_complex";
    public static final String COLUMN_COMPLEX_ID = "complex_id";
    public static final String COLUMN_COMPLEX_NAME = "complex_name";
    public static final String COLUMN_FLOORS = "floors";
    public static final String COLUMN_PARKING_SLOTS = "parking_slots";
    public static final String COLUMN_SECURITY_PERSONNEL = "security_personnel";
    public static final String COLUMN_CCTV_PRESENT = "cctv_present";



    // Columns for the apartments table
    public static final String TABLE_APARTMENT = "Apartment";
    public static final String APARTMENT_ID = "apartment_id";
    public static final String APARTMENT_COMPLEX_NAME = "complex_name";
    public static final String COLUMN_FLOOR = "floor";
    public static final String COLUMN_APARTMENT_NUMBER = "apartment_number";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGES = "images";
    public static final String COLUMN_FURNITURE = "furniture";
    public static final String COLUMN_ELECTRONICS = "electronics";


    // Table and columns
    public static final String TABLE_APPLIANCES = "appliances";
    public static final String APPLIANCES_ID = "id";
    public static final String APPLIANCES_NAME = "name";
    public static final String APPLIANCES_TYPE = "type";

    private static DatabaseHelper instance;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createUsersTable(db);
        createEstateTable(db);
        CreateApartment_ComplexTable(db);
        insertDefaultAdminUser(db);
        createApartmentTable(db);
        createAppliancesTable(db);
    }



    public void CreateApartment_ComplexTable(SQLiteDatabase db) {
        String CREATE_APARTMENT_COMPLEX_TABLE = "CREATE TABLE " + TABLE_APARTMENT_COMPLEX + " ("
                + COLUMN_COMPLEX_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_COMPLEX_NAME + " TEXT, "
                + COLUMN_FLOORS + " INTEGER, "
                + COLUMN_PARKING_SLOTS + " INTEGER, "
                + COLUMN_SECURITY_PERSONNEL + " TEXT, "
                + COLUMN_CCTV_PRESENT + " TEXT)";
        db.execSQL(CREATE_APARTMENT_COMPLEX_TABLE);

    }


    private void createUsersTable(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ROLE + " TEXT, " +
                COLUMN_PASSPORT_URI + " TEXT, " +
                COLUMN_ID_FRONT_URI + " TEXT, " +
                COLUMN_ID_BACK_URI + " TEXT)";
        db.execSQL(createUsersTable);
    }


    private void createAppliancesTable(SQLiteDatabase db) {
        String createAppliancesTable = "CREATE TABLE " + TABLE_APPLIANCES + " (" +
                APPLIANCES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                APPLIANCES_NAME + " TEXT NOT NULL, " +
                APPLIANCES_TYPE + " TEXT NOT NULL)";
        db.execSQL(createAppliancesTable);
    }



    private void createApartmentTable(SQLiteDatabase db) {
        String createApartmentTable = "CREATE TABLE " + TABLE_APARTMENT + " ("
                + APARTMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + APARTMENT_COMPLEX_NAME + " TEXT, "
                + COLUMN_FLOOR + " INTEGER, "
                + COLUMN_APARTMENT_NUMBER + " TEXT, "
                + COLUMN_STATUS + " TEXT, "
                + COLUMN_TYPE + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_IMAGES + " TEXT, "
                + COLUMN_FURNITURE + " TEXT, "
                + COLUMN_ELECTRONICS + " TEXT)";
        db.execSQL(createApartmentTable);
    }







    private void createEstateTable(SQLiteDatabase db) {
        String createEstateTable = "CREATE TABLE " + TABLE_ESTATE + " (" +
                COLUMN_ESTATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ESTATE_NAME + " TEXT NOT NULL)";
        db.execSQL(createEstateTable);
    }



    private void insertDefaultAdminUser(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, "Admin");
        values.put(COLUMN_LAST_NAME, "User");
        values.put(COLUMN_EMAIL, "admin@gmail.com");
        values.put(COLUMN_PASSWORD, "1");
        values.put(COLUMN_ROLE, "Admin");
        db.insert(TABLE_USERS, null, values);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Only drop the tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESTATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APARTMENT_COMPLEX);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APARTMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPLIANCES);
        onCreate(db);  // Recreate tables
    }



    public Cursor getComplexNamesAndFloors() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_COMPLEX_NAME + ", " + COLUMN_FLOORS + " FROM " + TABLE_APARTMENT_COMPLEX;
        return db.rawQuery(query, null);  // Return the cursor with complex name and floor count
    }




    // Method to get appliance by ID
    // Method to get appliance by ID
    public Appliance getAppliance(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_APPLIANCES,
                new String[]{APPLIANCES_ID, APPLIANCES_NAME, APPLIANCES_TYPE},
                APPLIANCES_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null) {
            // Log column names and their indexes
            int idColumnIndex = cursor.getColumnIndex(APPLIANCES_ID);
            int nameColumnIndex = cursor.getColumnIndex(APPLIANCES_NAME);
            int typeColumnIndex = cursor.getColumnIndex(APPLIANCES_TYPE);

            Log.d("DatabaseHelper", "ID Column Index: " + idColumnIndex);
            Log.d("DatabaseHelper", "Name Column Index: " + nameColumnIndex);
            Log.d("DatabaseHelper", "Type Column Index: " + typeColumnIndex);

            // Validate column indexes
            if (idColumnIndex >= 0 && nameColumnIndex >= 0 && typeColumnIndex >= 0) {
                cursor.moveToFirst();
                Appliance appliance = new Appliance(
                        cursor.getInt(idColumnIndex),
                        cursor.getString(nameColumnIndex),
                        cursor.getString(typeColumnIndex)
                );
                cursor.close();
                return appliance;
            } else {
                Log.e("DatabaseHelper", "One or more columns are missing from the table!");
            }
        } else {
            Log.e("DatabaseHelper", "Cursor is null or no appliance found with ID: " + id);
        }
        db.close();
        return null;
    }

    public List<Appliance> getAppliancesByType(String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Appliance> applianceList = new ArrayList<>();

        // Modify the query based on your database structure
        Cursor cursor = db.rawQuery("SELECT * FROM appliances WHERE type = ?", new String[]{type});
        if (cursor != null) {
            // Get the column indices for the necessary fields
            int idIndex = cursor.getColumnIndex("id");  // Assuming "id" is the column name for the appliance ID
            int nameIndex = cursor.getColumnIndex("name");  // Assuming "name" is the column name for the appliance name
            int typeIndex = cursor.getColumnIndex("type");  // Assuming "type" is the column name for the appliance type

            // Iterate through the cursor to fetch all appliances of the given type
            while (cursor.moveToNext()) {
                // Get the appliance details
                int id = cursor.getInt(idIndex);  // Retrieve the appliance ID as an int
                String name = cursor.getString(nameIndex);  // Retrieve the appliance name as a String
                String applianceType = cursor.getString(typeIndex);  // Retrieve the appliance type as a String

                // Add the appliance to the list using the correct constructor
                applianceList.add(new Appliance(id, name, applianceType));  // Create Appliance using id, name, and type
            }
            cursor.close();
        }
        return applianceList;
    }




    public boolean updateAppliance(int id, String name, String type) {
        SQLiteDatabase db = this.getWritableDatabase();  // Get writable database

        // Prepare the ContentValues with new data
        ContentValues values = new ContentValues();
        values.put(APPLIANCES_NAME, name);
        values.put(APPLIANCES_TYPE, type);

        // Perform the update operation on the appliances table
        int rowsAffected = db.update(TABLE_APPLIANCES,
                values,
                APPLIANCES_ID + " = ?",  // Where condition
                new String[]{String.valueOf(id)});  // The appliance ID to update

        // Close the database connection
        db.close();

        // Return true if at least one row was updated (i.e., appliance was found and updated)
        return rowsAffected > 0;
    }






    // Method to add an appliance
    public void addAppliance(String name, String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create ContentValues to store the appliance data
        ContentValues values = new ContentValues();
        values.put(APPLIANCES_NAME, name);
        values.put(APPLIANCES_TYPE, type);

        // Insert the appliance into the table
        long result = db.insert(TABLE_APPLIANCES, null, values);

        // Check if the insertion was successful
        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert appliance");
        } else {
            Log.d("DatabaseHelper", "Appliance added successfully with ID: " + result);
        }

        // Close the database after operation
        db.close();
    }


    // Method to get all appliances
    public ArrayList<Appliance> getAllAppliances() {
        ArrayList<Appliance> applianceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_APPLIANCES, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Validate column indexes
            int idColumnIndex = cursor.getColumnIndex(APPLIANCES_ID);
            int nameColumnIndex = cursor.getColumnIndex(APPLIANCES_NAME);
            int typeColumnIndex = cursor.getColumnIndex(APPLIANCES_TYPE);

            // Check if the column indexes are valid
            if (idColumnIndex >= 0 && nameColumnIndex >= 0 && typeColumnIndex >= 0) {
                do {
                    int id = cursor.getInt(idColumnIndex);
                    String name = cursor.getString(nameColumnIndex);
                    String type = cursor.getString(typeColumnIndex);
                    applianceList.add(new Appliance(id, name, type));
                } while (cursor.moveToNext());
            } else {
                Log.e("DatabaseHelper", "One or more column indexes are invalid! Please check your table schema.");
            }
            cursor.close();
        } else {
            Log.e("DatabaseHelper", "Cursor is null or no records found.");
        }
        db.close();
        return applianceList;
    }


    // Method to delete an appliance
    public void deleteAppliance(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_APPLIANCES, APPLIANCES_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }





    public Cursor getComplexFloors(String complexName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COLUMN_FLOORS + " FROM " + TABLE_APARTMENT_COMPLEX +
                " WHERE " + COLUMN_COMPLEX_NAME + " = ?", new String[]{complexName});
    }




    // Insert apartment data
    public long insertApartment(String complexName, int floor, String apartmentNumber, String status,
                                String type, String description, String images, String furniture,
                                String electronics) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(APARTMENT_COMPLEX_NAME, complexName);
        values.put(COLUMN_FLOOR, floor);
        values.put(COLUMN_APARTMENT_NUMBER, apartmentNumber);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGES, images);
        values.put(COLUMN_FURNITURE, furniture);
        values.put(COLUMN_ELECTRONICS, electronics);

        return db.insert(TABLE_APARTMENT, null, values);
    }

    // Update apartment data
    public int updateApartment(int id, String complexName, int floor, String apartmentNumber, String status,
                               String type, String description, String images, String furniture,
                               String electronics) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(APARTMENT_COMPLEX_NAME, complexName);
        values.put(COLUMN_FLOOR, floor);
        values.put(COLUMN_APARTMENT_NUMBER, apartmentNumber);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGES, images);
        values.put(COLUMN_FURNITURE, furniture);
        values.put(COLUMN_ELECTRONICS, electronics);

        return db.update(TABLE_APARTMENT, values, APARTMENT_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Retrieve all apartments
    public Cursor getAllApartments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_APARTMENT, null, null, null, null, null, null);
    }

    // Retrieve a single apartment by ID
    public Cursor getApartmentById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_APARTMENT, null, APARTMENT_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }






    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_LAST_NAME, user.getLastName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_ROLE, user.getRole());
        values.put(COLUMN_PASSPORT_URI, user.getPassportUri());
        values.put(COLUMN_ID_FRONT_URI, user.getIdFrontUri());
        values.put(COLUMN_ID_BACK_URI, user.getIdBackUri());

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Method to insert apartment complex data into the database
    // Method to insert apartment complex data into the database
    public boolean insertApartmentComplex(String complexName, int floors, int parkingSlots, String securityPersonnel, String cctvPresent) {
        SQLiteDatabase db = this.getWritableDatabase();  // Get writable database
        ContentValues contentValues = new ContentValues();  // Create a contentValues object to store the data

        // Put values into the contentValues object
        contentValues.put(COLUMN_COMPLEX_NAME, complexName);  // Use COLUMN_COMPLEX_NAME constant
        contentValues.put(COLUMN_FLOORS, floors);  // Use COLUMN_FLOORS constant
        contentValues.put(COLUMN_PARKING_SLOTS, parkingSlots);  // Use COLUMN_PARKING_SLOTS constant
        contentValues.put(COLUMN_SECURITY_PERSONNEL, securityPersonnel.equals("Yes") ? "1" : "0");  // Use "1" or "0"
        contentValues.put(COLUMN_CCTV_PRESENT, cctvPresent.equals("Yes") ? "1" : "0");  // Use "1" or "0" for CCTV column

        // Insert the data into the apartment_complex table
        long result = db.insert(TABLE_APARTMENT_COMPLEX, null, contentValues);
        db.close();  // Close the database connection
        Log.d("ApartmentInsert", "Inserting: " + complexName + ", " + floors + ", " + parkingSlots + ", " + securityPersonnel + ", " + cctvPresent);

        return result != -1;  // Return true if the insertion was successful, otherwise false
    }



    public ApartmentComplex getApartmentComplexDetails(String complexName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM apartment_complex WHERE complex_name=?", new String[]{complexName});

        if (cursor != null && cursor.moveToFirst()) {
            // Print all column names to verify correct names
            String[] columnNames = cursor.getColumnNames();
            for (String columnName : columnNames) {
                Log.d("DatabaseDebug", "Column name: " + columnName);
            }

            // Create a new ApartmentComplex object
            ApartmentComplex complex = new ApartmentComplex();

            // Retrieve each field by its column name
            int complexNameIndex = cursor.getColumnIndex("complex_name");
            int floorsIndex = cursor.getColumnIndex("floors");
            int parkingSlotsIndex = cursor.getColumnIndex("parking_slots");
            int securityPersonnelIndex = cursor.getColumnIndex("security_personnel");
            int cctvIndex = cursor.getColumnIndex(COLUMN_CCTV_PRESENT);  // Use 'COLUMN_CCTV_PRESENT' for the correct column name

            // Check if column indexes are valid before assigning values
            if (complexNameIndex >= 0) {
                complex.setName(cursor.getString(complexNameIndex));
            } else {
                Log.e("DatabaseError", "Column 'complex_name' not found");
            }

            if (floorsIndex >= 0) {
                complex.setFloors(cursor.getInt(floorsIndex));
            } else {
                Log.e("DatabaseError", "Column 'floors' not found");
            }

            if (parkingSlotsIndex >= 0) {
                complex.setParkingSlots(cursor.getInt(parkingSlotsIndex));
            } else {
                Log.e("DatabaseError", "Column 'parking_slots' not found");
            }

            if (securityPersonnelIndex >= 0) {
                complex.setSecurityPersonnel(cursor.getInt(securityPersonnelIndex) == 1);
            } else {
                Log.e("DatabaseError", "Column 'security_personnel' not found");
            }

            if (cctvIndex >= 0) {
                complex.setCCTV(cursor.getInt(cctvIndex) == 1);
            } else {
                Log.e("DatabaseError", "Column 'cctv_present' not found");
            }

            cursor.close();
            return complex;
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }






    public boolean updateApartmentComplex(String originalComplexName, String newName, int floors, int parkingSlots, boolean securityPersonnel, boolean cctv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPLEX_NAME, newName);  // Use constant
        values.put(COLUMN_FLOORS, floors);  // Use constant
        values.put(COLUMN_PARKING_SLOTS, parkingSlots);  // Use constant
        values.put(COLUMN_SECURITY_PERSONNEL, securityPersonnel ? "1" : "0");  // Convert to TEXT ("1" or "0")
        values.put(COLUMN_CCTV_PRESENT, cctv ? "1" : "0");  // Correctly referencing COLUMN_CCTV_PRESENT


        int rowsAffected = db.update(TABLE_APARTMENT_COMPLEX, values, COLUMN_COMPLEX_NAME + "=?", new String[]{originalComplexName});
        db.close();
        Log.d("DatabaseDebug", "Rows affected: " + rowsAffected);

        return rowsAffected > 0;  // Return true if at least one row was updated

    }


    public boolean deleteApartmentComplex(String complexName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("apartment_complex", "complex_name = ?", new String[]{complexName});
        db.close();
        return result > 0; // If deletion is successful, result will be > 0
    }


    public Cursor getAllApartmentComplexes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM apartment_complex", null);
    }


    public User validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email, password});

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int firstNameIndex = cursor.getColumnIndex(COLUMN_FIRST_NAME);
            int lastNameIndex = cursor.getColumnIndex(COLUMN_LAST_NAME);
            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            int roleIndex = cursor.getColumnIndex(COLUMN_ROLE);
            int passportUriIndex = cursor.getColumnIndex(COLUMN_PASSPORT_URI);
            int idFrontUriIndex = cursor.getColumnIndex(COLUMN_ID_FRONT_URI);
            int idBackUriIndex = cursor.getColumnIndex(COLUMN_ID_BACK_URI);

            // Safely set the user properties using the provided code snippet
            user.setId(idIndex >= 0 ? cursor.getInt(idIndex) : 0);
            user.setFirstName(firstNameIndex >= 0 ? cursor.getString(firstNameIndex) : null);
            user.setLastName(lastNameIndex >= 0 ? cursor.getString(lastNameIndex) : null);
            user.setEmail(emailIndex >= 0 ? cursor.getString(emailIndex) : null);
            user.setPassword(passwordIndex >= 0 ? cursor.getString(passwordIndex) : null);
            user.setRole(roleIndex >= 0 ? cursor.getString(roleIndex) : null);
            user.setPassportUri(passportUriIndex >= 0 ? cursor.getString(passportUriIndex) : null);
            user.setIdFrontUri(idFrontUriIndex >= 0 ? cursor.getString(idFrontUriIndex) : null);
            user.setIdBackUri(idBackUriIndex >= 0 ? cursor.getString(idBackUriIndex) : null);
        }

        cursor.close();
        db.close();
        return user;
    }




    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int firstNameIndex = cursor.getColumnIndex(COLUMN_FIRST_NAME);
                int lastNameIndex = cursor.getColumnIndex(COLUMN_LAST_NAME);
                int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
                int roleIndex = cursor.getColumnIndex(COLUMN_ROLE);
                int passportUriIndex = cursor.getColumnIndex(COLUMN_PASSPORT_URI);
                int idFrontUriIndex = cursor.getColumnIndex(COLUMN_ID_FRONT_URI);
                int idBackUriIndex = cursor.getColumnIndex(COLUMN_ID_BACK_URI);

                user.setId(idIndex >= 0 ? cursor.getInt(idIndex) : 0);
                user.setFirstName(firstNameIndex >= 0 ? cursor.getString(firstNameIndex) : null);
                user.setLastName(lastNameIndex >= 0 ? cursor.getString(lastNameIndex) : null);
                user.setEmail(emailIndex >= 0 ? cursor.getString(emailIndex) : null);
                user.setPassword(passwordIndex >= 0 ? cursor.getString(passwordIndex) : null);
                user.setRole(roleIndex >= 0 ? cursor.getString(roleIndex) : null);
                user.setPassportUri(passportUriIndex >= 0 ? cursor.getString(passportUriIndex) : null);
                user.setIdFrontUri(idFrontUriIndex >= 0 ? cursor.getString(idFrontUriIndex) : null);
                user.setIdBackUri(idBackUriIndex >= 0 ? cursor.getString(idBackUriIndex) : null);

                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userList;
    }


    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int firstNameIndex = cursor.getColumnIndex(COLUMN_FIRST_NAME);
            int lastNameIndex = cursor.getColumnIndex(COLUMN_LAST_NAME);
            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            int roleIndex = cursor.getColumnIndex(COLUMN_ROLE);
            int passportUriIndex = cursor.getColumnIndex(COLUMN_PASSPORT_URI);
            int idFrontUriIndex = cursor.getColumnIndex(COLUMN_ID_FRONT_URI);
            int idBackUriIndex = cursor.getColumnIndex(COLUMN_ID_BACK_URI);

            user.setId(idIndex >= 0 ? cursor.getInt(idIndex) : 0);
            user.setFirstName(firstNameIndex >= 0 ? cursor.getString(firstNameIndex) : null);
            user.setLastName(lastNameIndex >= 0 ? cursor.getString(lastNameIndex) : null);
            user.setEmail(emailIndex >= 0 ? cursor.getString(emailIndex) : null);
            user.setPassword(passwordIndex >= 0 ? cursor.getString(passwordIndex) : null);
            user.setRole(roleIndex >= 0 ? cursor.getString(roleIndex) : null);
            user.setPassportUri(passportUriIndex >= 0 ? cursor.getString(passportUriIndex) : null);
            user.setIdFrontUri(idFrontUriIndex >= 0 ? cursor.getString(idFrontUriIndex) : null);
            user.setIdBackUri(idBackUriIndex >= 0 ? cursor.getString(idBackUriIndex) : null);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return user;
    }

    public long addEstate(String estateName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ESTATE_NAME, estateName);
        long result = db.insert(TABLE_ESTATE, null, values); // Capture the result of the insert operation
        db.close();
        return result; // Return the result
    }


    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(user);

        // Update the row with the corresponding user ID
        int rowsAffected = db.update("users", values, "id = ?", new String[]{String.valueOf(user.getId())});
        db.close();

        return rowsAffected > 0; // Return true if the update was successful
    }

    private static @NonNull ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();

        // Update user information
        values.put("first_name", user.getFirstName());
        values.put("last_name", user.getLastName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("role", user.getRole());
        values.put("passport_uri", user.getPassportUri());
        values.put("id_front_uri", user.getIdFrontUri());
        values.put("id_back_uri", user.getIdBackUri());
        return values;
    }

    // In DatabaseHelper.java
    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("users", "id = ?", new String[]{String.valueOf(userId)});
        db.close();
        return result > 0; // Return true if at least one row was deleted
    }



    public List<String> getAllEstates() {
        List<String> estateList = new ArrayList<>();

        try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.query(TABLE_ESTATE, new String[]{COLUMN_ESTATE_NAME}, null, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(COLUMN_ESTATE_NAME);
                if (columnIndex != -1) {
                    do {
                        String estateName = cursor.getString(columnIndex);
                        estateList.add(estateName);
                    } while (cursor.moveToNext());
                } else {
                    Log.e("DatabaseHelper", "Column " + COLUMN_ESTATE_NAME + " does not exist.");
                }
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error getting estates: " + e.getMessage(), e);
        }

        return estateList;
    }




    public boolean updateEstate(String originalEstateName, String newEstateName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ESTATE_NAME, newEstateName); // Assuming COLUMN_ESTATE_NAME is the correct column name

        int rowsAffected = db.update(TABLE_ESTATE, values, COLUMN_ESTATE_NAME + "=?", new String[]{originalEstateName});
        return rowsAffected > 0; // Returns true if at least one row was updated
    }



    public boolean deleteEstate(String estateName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_ESTATE, COLUMN_ESTATE_NAME + "=?", new String[]{estateName});
        return rowsAffected > 0; // Returns true if at least one row was deleted
    }
    // Method to get the count of estates
    public int getEstateCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_ESTATE; // Use TABLE_ESTATE here
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

}
