package thunderbirdsonly.nwhackbackend.mapper;
import org.apache.ibatis.annotations.*;
import thunderbirdsonly.nwhackbackend.DOT.User;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO nwhack.user (username, email, password) " +
            "VALUES (#{username}, #{email}, #{password})")
    public void insertUser(User user);


    @Select("SELECT * FROM nwhack.user WHERE username = #{username}")
    public User getUserByUsername(String username);


    @Select("SELECT * FROM nwhack.user WHERE username = #{username} and password = #{password}")
    User getbyUsernameAndPass(User user);


    @Select("SELECT COUNT(*) FROM thunderbirds.user WHERE username = #{username} OR email = #{email}")
    int countByUsernameOrEmail(User user);

    @Update("UPDATE thunderbirds.user SET password = #{password}, update_time = NOW() WHERE id = #{id}")
    void updatePassword(User user);


    @Select("SELECT COUNT(*) FROM nwhack.user WHERE user_id = #{userId}")
    int find(@Param("userId") int userId);

}
