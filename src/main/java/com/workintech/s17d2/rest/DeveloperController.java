package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import com.workintech.s17d2.tax.Taxable;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DeveloperController {
	public Map<Integer, Developer> developers;
	private Taxable developerTax;

	@PostConstruct
	public void init() {
		developers = new HashMap<>();
	}

	@Autowired
	public DeveloperController(Taxable developerTax) {
		this.developerTax = developerTax;
	}

	@GetMapping("/developers")
	public List<Developer> getAllDevelopers(){
		return developers.values().stream().toList();
	}

	@GetMapping("/developers/{id}")
	public Developer getDeveloperById(@PathVariable("id") Integer id){
		if(!developers.containsKey(id)){
			return null;
		}
		return developers.get(id);
	}

	@PostMapping("/developers")
	@ResponseStatus(HttpStatus.CREATED)
	public void addDevelopers(@RequestBody Developer developer){

		switch (developer.getExperience()){
			case JUNIOR:
				Developer developerJ = new JuniorDeveloper(developer, developer.getSalary() - developerTax.getSimpleTaxRate());
				developers.put(developer.getId(), developerJ);
				break;
			case MID:
				Developer developerM = new MidDeveloper(developer.getId(), developer.getName(), developer.getSalary()- developerTax.getMiddleTaxRate());
				developers.put(developer.getId(), developerM);
				break;
			case SENIOR:
				Developer developerS = new SeniorDeveloper(developer.getId(), developer.getName(), developer.getSalary() - developerTax.getUpperTaxRate());
				developers.put(developer.getId(), developerS);
				break;
		}
	}

	@PutMapping("/developers/{id}")
	public void updateDeveloper(@PathVariable Integer id, @RequestBody Developer developer){
		developers.replace(id, developer);
	};

	@DeleteMapping("/developers/{id}")
	public void deleteDeveloper(@PathVariable Integer id){
		developers.remove(id);
	};

}
