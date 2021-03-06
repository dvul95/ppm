package com.dvulovic.ppmtool.web;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dvulovic.ppmtool.domain.ProjectTask;
import com.dvulovic.ppmtool.services.MapValidationErrorService;
import com.dvulovic.ppmtool.services.ProjectTaskService;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

	@Autowired
	private ProjectTaskService projectTaskService;

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	// actions
	@PostMapping("/{backlog_id}")
	public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
			@PathVariable String backlog_id, Principal principal) {

		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if (mapError != null)
			return mapError;

		ProjectTask pt = projectTaskService.addProjectTask(backlog_id, projectTask, principal.getName());

		return new ResponseEntity<ProjectTask>(pt, HttpStatus.CREATED);
	}

	@GetMapping("/{backlog_id}")
	public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlog_id, Principal principal) {
		return projectTaskService.findBacklogById(backlog_id, principal.getName());

	}

	@GetMapping("/{backlog_id}/{pt_id}")
	public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal) {
		ProjectTask projectTask = projectTaskService.findProjectTaskBySequence(backlog_id, pt_id, principal.getName());
		return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
	}

	@PatchMapping("/{backlog_id}/{pt_id}")
	public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
			@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal) {
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if (mapError != null)
			return mapError;

		ProjectTask updatedProjectTask = projectTaskService.updateByProjectSequence(projectTask, backlog_id, pt_id, principal.getName());

		return new ResponseEntity<ProjectTask>(updatedProjectTask, HttpStatus.OK);
	}

	@DeleteMapping("/{backlog_id}/{pt_id}")
	public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal) {
		projectTaskService.deleteProjectTaskBySequence(backlog_id, pt_id, principal.getName());

		return new ResponseEntity<String>("Project task with the id: '" + pt_id + "' was deleted successfully!",
				HttpStatus.OK);
	}

}
