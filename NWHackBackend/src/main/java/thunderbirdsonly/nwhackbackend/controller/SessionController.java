package thunderbirdsonly.nwhackbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import thunderbirdsonly.nwhackbackend.DOT.Session;
import thunderbirdsonly.nwhackbackend.Pojo.Result;
import thunderbirdsonly.nwhackbackend.service.SessionService;

import java.util.List;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    @Autowired
    SessionService sessionService;

    @PostMapping("/start")
    public Result startSession(@RequestParam(name = "Id") int userId) {
        try {
            sessionService.makeNew(userId);
            return Result.success("Start successful");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }


    @PostMapping("/update")
    public Result updateSession(
            @RequestParam(name = "Id") int userId,
            @RequestParam(name = "focus") int focus,
            @RequestParam(name = "unfocus") int unfocus) {
        try {
            // Pass the additional parameters to your service method if needed
            sessionService.updateLatest(userId, focus, unfocus);
            return Result.success("Update Successful");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }


    @PostMapping("/end")
    public Result endSession(@RequestParam(name = "Id") int userId) {
        try {
            sessionService.endLatest(userId);
            return Result.success("End successful");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }


    @PostMapping("/report")
    public Result reportSession(@RequestParam(name = "Id") int userId) {
        try {
            List<Session> result =  sessionService.report(userId);
            return Result.success(result);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

}
