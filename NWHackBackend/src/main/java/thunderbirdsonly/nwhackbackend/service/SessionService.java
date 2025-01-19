package thunderbirdsonly.nwhackbackend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thunderbirdsonly.nwhackbackend.DOT.Session;
import thunderbirdsonly.nwhackbackend.mapper.SessionMapper;
import thunderbirdsonly.nwhackbackend.mapper.UserMapper;

import java.util.List;

@Service
public class SessionService {
    @Autowired
    SessionMapper sessionMapper;
    @Autowired
    UserMapper userMapper;

    public void makeNew(int userId) {
        if (userMapper.find(userId) == 0) {
            throw new IllegalArgumentException("Invalid userID");
        } else if (sessionMapper.howMany(userId) != 0) {
            throw new IllegalArgumentException("You Cannot Start a New Task Before Finished The Current One");
        } else {
            sessionMapper.makeNew(userId);
        }
    }

    public void updateLatest(int userId,int focus,int unfocus) {
        if (sessionMapper.find(userId) != 0) {
            throw new IllegalArgumentException("Invalid userID");
        }else if (sessionMapper.howMany(userId) == 0) {
            throw new IllegalArgumentException("You Cannot Update Task Before Start One");
        } else {
            sessionMapper.updateSession(userId, focus, unfocus);
        }
    }

    public void endLatest(int userId) {
        if (sessionMapper.find(userId) != 0) {
            throw new IllegalArgumentException("Invalid userID");
        }else if (sessionMapper.howMany(userId) == 0) {
            throw new IllegalArgumentException("You Cannot Close Task Before Start One");
        } else {
            sessionMapper.endSession(userId);
        }
    }


    public List<Session> report(int userId) {
        if (sessionMapper.find(userId) != 0) {
            throw new IllegalArgumentException("Invalid userID");
        }else if (sessionMapper.howMany(userId) !=0 ) {
            throw new IllegalArgumentException("You Have to Close Task First");
        } else {
            return sessionMapper.getAllSessions(userId);
        }
    }
}
