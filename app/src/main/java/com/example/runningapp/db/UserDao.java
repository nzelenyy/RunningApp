package com.example.runningapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE first_name=='a'")
    List<User> getAUsers();

    @Query("SELECT MAX(CAST(first_name AS INTEGER)) FROM user")
    Integer getMax();

    @Query("SELECT track_id FROM user WHERE first_name==(SELECT  MAX(CAST(first_name AS INTEGER)) FROM user)")
    Integer getMaxNew();

    @Query ("SELECT first_name FROM user WHERE track_id like :given_id ORDER BY uid ASC")
    List<Integer> getFirstNames(int given_id);

    @Query ("SELECT last_name FROM user WHERE track_id == :given_id ORDER BY uid ASC")
    List<Double> getLastNames(int given_id);


    @Query ("SELECT first_name FROM user WHERE first_name==(SELECT MAX(CAST (first_name AS INTEGER)) FROM USER WHERE (track_id LIKE :given_id))")
    Integer getFirstName(int given_id); //ищет среди записей с заданным track_id максимальный first_name

    @Query("SELECT last_name FROM user WHERE last_name==(SELECT MAX(CAST (last_name AS DOUBLE)) FROM user WHERE (track_id LIKE :given_id))")
    Double getLastName(int given_id);

    @Query("SELECT DISTINCT track_id FROM user")
    List<Integer> getIDs();

    @Query("SELECT track_id FROM user WHERE track_id==(SELECT MAX(CAST (track_id AS INT)) FROM user)")
    Integer getMaxTrackID();

    /*@Query(
            "SELECT * FROM user" +
                    "JOIN book ON user.id = book.user_id"
    )
    //public Map<User, List<Book>> loadUserAndBookNames();
*/
    @Insert
    void insertUser(User... users);

    @Delete
    void delete(User user);
}
