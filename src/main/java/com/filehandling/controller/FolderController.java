package com.filehandling.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filehandling.model.Folder;
import com.filehandling.service.FolderService;

import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/folder")
public class FolderController {

	@Autowired
	private FolderService folderService;
	
    @PostMapping("/createfolder")
    public String createFolder(@RequestBody Folder request) {
        return folderService.createFolder(request);
    }

    @GetMapping("/getAllFolders")
    public List<Folder> getAllFolders() {
        return folderService.getAllFolders();
    }
    
    @GetMapping("/getFolderById/{id}")
    public ResponseEntity<Folder> getFolderById(@PathVariable Long id) {
        Folder folder = folderService.getFolderById(id);
        return ResponseEntity.ok(folder);
    }
//    @PutMapping("/updateFolder/{id}/update")
//    public ResponseEntity<Folder> updateFolder(@PathVariable Long id, @RequestBody Folder request) {
//        String newFolderName = request.getName();
//        String newFolderPath = request.getPath();
//        Folder updatedFolder = folderService.updateFolder(id, newFolderName, newFolderPath);
//        return ResponseEntity.ok(updatedFolder);
//    }
    
//    @PatchMapping("/updateFolder/{id}/update")
//    public ResponseEntity<Folder> updateFolder(@PathVariable Long id, @RequestBody Folder request) {
//        try {
//        	   String newFolderName = request.getName();
//               String newFolderPath = request.getPath();
//               Folder updatedFolder = folderService.updateFolder(id, newFolderName, newFolderPath);
//               return ResponseEntity.ok(updatedFolder);
//        } catch (RuntimeException e) {
//        	System.err.println(e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }
    
    @PatchMapping("/updateFolder/{id}/update")
    public ResponseEntity<Folder> updateFolder(@PathVariable Long id, @RequestBody Folder request) {
        try {
            String newFolderName = request.getName();
            String newFolderPath = request.getPath();
            Folder updatedFolder = folderService.updateFolder(id, newFolderName, newFolderPath);
            return ResponseEntity.ok(updatedFolder);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @DeleteMapping("/deleteFolder/{id}")
    public ResponseEntity<String> deleteFolder(@PathVariable Long id) {
        try {
            String result = folderService.deleteFolder(id);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
