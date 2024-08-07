package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.model.PreApplication;
import com.isyeriegitimi.backend.repository.PreApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreApplicationService {
    @Autowired
    private PreApplicationRepository preApplicationRepository;

    public PreApplication savePreApplication(PreApplication application) {
        return preApplicationRepository.save(application);
    }

    public List<PreApplication> getAllFormEntries() {
        return preApplicationRepository.findAll();
    }

}

