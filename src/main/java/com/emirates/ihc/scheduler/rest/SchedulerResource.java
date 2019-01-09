package com.emirates.ihc.scheduler.rest;

import com.emirates.ihc.scheduler.domain.SchedulerTO;
import com.emirates.ihc.scheduler.exception.OrchSchedulerException;
import com.emirates.ihc.scheduler.exception.SchedulerNotRunningException;
import com.emirates.ihc.scheduler.service.ISchedulerService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;


@Api(value="scheduler", description = "Scheduler Resource", produces="application/json")
@RestController
@RequestMapping("/scheduler")
public class SchedulerResource {

    @Autowired
    ISchedulerService srv;

    @ApiOperation(value = "Stop the scheduler if it is running", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Successfully stopped the running scheduler" ),
            @ApiResponse(code = HttpURLConnection.HTTP_GONE, message="The scheduler is not running "),
            @ApiResponse(code = 417, message="Not able to stop the scheduler due to some server errors")
        }
    )
    @DeleteMapping
    public ResponseEntity<SchedulerTO> stopScheduler() {
        try {
            SchedulerTO to = srv.stopScheduler();
            return new ResponseEntity<SchedulerTO>(to, HttpStatus.OK);
        } catch(SchedulerNotRunningException ex) {
            return new ResponseEntity<SchedulerTO>(new SchedulerTO(), HttpStatus.GONE);
        } catch(OrchSchedulerException ex) {
            return new ResponseEntity<SchedulerTO>(new SchedulerTO(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @ApiOperation(value = "Start the scheduler if it is not running", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Successfully started the running scheduler" ),
            @ApiResponse(code = 417, message="Not able to start the scheduler due to some server errors")
    }
    )
    @PutMapping
    public ResponseEntity<SchedulerTO> startScheduler() {
        try {
            SchedulerTO to = srv.startScheduler();
            return new ResponseEntity<SchedulerTO>(to, HttpStatus.OK);
        } catch(OrchSchedulerException ex) {
            return new ResponseEntity<SchedulerTO>(new SchedulerTO(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @ApiOperation(value = "Get status of scheduler", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "The status of the scheduler")
    }
    )
    @GetMapping
    public ResponseEntity<SchedulerTO> getStatus() {
        SchedulerTO to = srv.getStatus();

        return new ResponseEntity<SchedulerTO>(to, HttpStatus.OK);
    }

}
