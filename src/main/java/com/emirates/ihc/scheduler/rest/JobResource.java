package com.emirates.ihc.scheduler.rest;

import com.emirates.ihc.framework.exceptions.InvalidInputException;
import com.emirates.ihc.scheduler.domain.JobInstanceTO;
import com.emirates.ihc.scheduler.domain.JobTO;
import com.emirates.ihc.scheduler.exception.JobNotRunningException;
import com.emirates.ihc.scheduler.exception.JobSchedulingException;
import com.emirates.ihc.scheduler.service.IJobService;
import io.swagger.annotations.*;
import org.quartz.UnableToInterruptJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.HttpURLConnection;


@Api(value="jobs", description = "Job Resource", produces="application/json")
@RestController
@RequestMapping("/jobs")
public class JobResource {

    @Autowired
    IJobService srv;

    @ApiOperation(value = "Perform action on alreay running job", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Successfully performed the requested operation for a running job" ),
            @ApiResponse(code = HttpURLConnection.HTTP_GONE, message="The job is not running "),
            @ApiResponse(code = 417, message="Not able to interrupt the job"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message="Bad Request. Action Code cannot be null or should be either INTERRUPT")
        }
    )
    @RequestMapping(value = "/{batchId}", method= RequestMethod.PUT)
    public ResponseEntity<JobInstanceTO> interrupt(
            @ApiParam("Batch id of the job for which required action to be performed") @PathVariable("batchId") String batchId
            , @ApiParam("Type of the action as query parameter. Allowed value is 'INTERRUPT'") @RequestParam("action") String action) {
        try {
            if (action == null || !action.equalsIgnoreCase("INTERRUPT")) {
                return new ResponseEntity<JobInstanceTO>(new JobInstanceTO(), HttpStatus.BAD_REQUEST);
            }
            JobInstanceTO to = srv.interruptRunningJob(batchId);
            return new ResponseEntity<JobInstanceTO>(to, HttpStatus.OK);
        } catch(JobNotRunningException ex) {
            return new ResponseEntity<JobInstanceTO>(new JobInstanceTO(), HttpStatus.GONE);
        } catch(UnableToInterruptJobException ex) {
            return new ResponseEntity<JobInstanceTO>(new JobInstanceTO(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @ApiOperation(value = "Schedule a Job using provided job detail", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Successfully scheduled a job" ),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message="Bad Request. Action Code cannot be null or should be either INTERRUPT"),
            @ApiResponse(code = 417, message="Not able to schedule the job")
        }
    )
    @PostMapping
    public ResponseEntity<JobInstanceTO> schedule(@RequestBody JobTO job) {
        try {
            JobInstanceTO to = srv.scheduleJob(job);
            return new ResponseEntity<JobInstanceTO>(to, HttpStatus.OK);
        } catch(InvalidInputException ex) {
            return new ResponseEntity<JobInstanceTO>(new JobInstanceTO(), HttpStatus.BAD_REQUEST);
        } catch(JobSchedulingException ex) {
            return new ResponseEntity<JobInstanceTO>(new JobInstanceTO(), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
