package com.example.genesis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

@Service
public class PersonIdService {
    
    private final Set<String> authorizedPersonIds = new HashSet<>();
    private final ResourceLoader resourceLoader;
 
    @Value("${genesis.resources.person-id-file}")
    private String personIdFileName;
    
    public PersonIdService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    @PostConstruct
    public void loadPersonIds() {
        Resource resource = resourceLoader.getResource(personIdFileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    authorizedPersonIds.add(line.trim());
                }
            }
            System.out.println(">>> Genesis Resources: Nahráno " + authorizedPersonIds.size() + " autorizovaných PersonID.");
        } catch (IOException e) {
            System.err.println("Chyba při načítání PersonID souboru! Spouštění se nezdaří: " + e.getMessage());
            throw new RuntimeException("Kritická chyba: Nelze načíst autorizovaná PersonID.", e);
        }
    }
    
    public boolean isPersonIdValid(String personId) {
        return authorizedPersonIds.contains(personId);
    }
}