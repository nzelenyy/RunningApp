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

    @Query("SELECT * FROM user WHERE seconds=='a'")
    List<User> getAUsers();

    @Query("SELECT MAX(CAST(seconds AS INTEGER)) FROM user")
    Integer getMax();

    @Query("SELECT track_id FROM user WHERE seconds==(SELECT  MAX(CAST(seconds AS INTEGER)) FROM user)")
    Integer getMaxNew();

    @Query ("SELECT seconds FROM user WHERE track_id like :given_id ORDER BY uid ASC")
    List<Integer> getFirstNames(int given_id);

    @Query ("SELECT distance FROM user WHERE track_id == :given_id ORDER BY uid ASC")
    List<Double> getDistances(int given_id);


    @Query ("SELECT seconds FROM user WHERE seconds==(SELECT MAX(CAST (seconds AS INTEGER)) FROM USER WHERE (track_id LIKE :given_id))")
    Integer getSecond(int given_id); //ищет среди записей с заданным track_id максимальный first_name

    @Query("SELECT distance FROM user WHERE distance==(SELECT MAX(CAST (distance AS DOUBLE)) FROM user WHERE (track_id LIKE :given_id))")
    Double getDistance(int given_id);

    @Query("SELECT DISTINCT track_id FROM user")
    List<Integer> getIDs();

    @Query("SELECT track_id FROM user WHERE track_id==(SELECT MAX(CAST (track_id AS INT)) FROM user)")
    Integer getMaxTrackID();

    @Query ("DELETE FROM user WHERE track_id like :given_id")
    Void DeleteGivenTrack(int given_id);

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
