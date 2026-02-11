package com.union.demo.controller;

import com.union.demo.service.DropdownService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dropdown")
@RequiredArgsConstructor
public class DropdownController {
    private final DropdownService dropdownService;
    //1. 대학 드롭다운 /api/dropdown/university?q=이화
    @GetMapping("/universities")
    public ResponseEntity<?> getUniversities(@RequestParam(name = "q") String keyword){
        return ResponseEntity.ok(dropdownService.dropdownUniversity(keyword));
    }

    //2. field, role 드롭다운 /api/dropdown/roles?fieldId=100
    @GetMapping("/roles")
    public ResponseEntity<?> getRoles(@RequestParam Long fieldId){
        return ResponseEntity.ok(dropdownService.dropdownRole(fieldId));
    }

    //3. field, skill 드롭다운 /api/dropdown/skills?fieldId=100
    @GetMapping("/skills")
    public ResponseEntity<?> getSkills(@RequestParam Integer fieldId){
        return  ResponseEntity.ok(dropdownService.dropdownSkill(fieldId));
    }

    //4. domain 드롭다운 /api/dropdown/domains
    @GetMapping("/domains")
    public ResponseEntity<?> getDomains(){
        return ResponseEntity.ok(dropdownService.dropdownDomain());
    }
}
