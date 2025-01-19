package thunderbirdsonly.nwhackbackend.mapper;


import org.apache.ibatis.annotations.*;
import thunderbirdsonly.nwhackbackend.DOT.Session;

import java.util.List;

@Mapper
public interface SessionMapper {

    @Select("SELECT COUNT(*) FROM nwhack.user_focus_history WHERE user_id = #{userId}")
    int find(@Param("userId") int userId);


    @Select("SELECT COUNT(*) FROM nwhack.user_focus_history WHERE user_id = #{userId} AND is_finished = 0")
    int howMany(@Param("userId") int userId);



    @Insert("INSERT INTO nwhack.user_focus_history (user_id) " +
            "VALUES (#{userId})")
    void makeNew(@Param("userId") int userId);


    @Update("UPDATE nwhack.user_focus_history " +
            "SET focus = focus + #{focus}, unfocus = unfocus + #{unfocus}, updated_at = NOW() " +
            "WHERE user_id = #{userId} AND is_finished = 0")
    void updateSession(@Param("userId") int userId, @Param("focus") int focus, @Param("unfocus") int unfocus);

    @Update("UPDATE nwhack.user_focus_history " +
            "SET is_finished = 1, updated_at = NOW() " +
            "WHERE user_id = #{userId} AND is_finished = 0 " +
            "ORDER BY created_at DESC " +
            "LIMIT 1")
    void endSession(@Param("userId") int userId);


    @Select("SELECT * " +
            "FROM nwhack.user_focus_history " +
            "WHERE user_id = #{userId} " +
            "ORDER BY created_at DESC")
    List<Session> getAllSessions(@Param("userId") int userId);














}
