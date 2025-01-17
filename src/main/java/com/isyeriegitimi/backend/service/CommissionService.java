package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.CommissionDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.Commission;
import com.isyeriegitimi.backend.repository.CommissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommissionService {

    private final CommissionRepository commissionRepository;

    @Autowired
    public CommissionService(CommissionRepository commissionRepository) {
        this.commissionRepository = commissionRepository;
    }

    public Commission getCommissionByCommissionNo(String commissionNo) {
        try {
            Optional<Commission> commission = commissionRepository.findByCommissionNumber(commissionNo);
            if (commission.isEmpty()) {
                throw new ResourceNotFoundException("Commission", "commissionNo", commissionNo);
            }
            return commission.get();
        } catch (Exception e) {
            throw  new InternalServerErrorException("An error occurred while fetching the commission: " + e.getMessage());
        }
    }
    public CommissionDto getCommissionById(UUID commissionId) {
        try {
            Optional<Commission> commission = commissionRepository.findById(commissionId);
            if (commission.isEmpty()) {
                throw new ResourceNotFoundException("Commission", "id", commissionId.toString());
            }
            return mapToDto(commission.get());
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the commission: " + e.getMessage());
        }
    }
    public void update(UUID commissionId, CommissionDto commissionDto) {
        try {
            Optional<Commission> commission = commissionRepository.findById(commissionId);
            if (commission.isEmpty()) {
                throw new ResourceNotFoundException("Commission", "id", commissionId.toString());
            }
            commissionRepository.save(mapToEntity(commissionDto));

        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while updating the commission: " + e.getMessage());
        }
    }
    public List<Commission> getAllCommissions() {
        try {
            List<Commission> commissions = commissionRepository.findAll();
            if (commissions.isEmpty()) {
                throw new ResourceNotFoundException("Commissions");
            }
            return commissions;
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the commissions: " + e.getMessage());
        }
    }
    public CommissionDto mapToDto(Commission commission){
        CommissionDto commissionDto=new CommissionDto();
        commissionDto.setCommissionId(commission.getCommissionId());
        commissionDto.setFirstName(commission.getFirstName());
        commissionDto.setLastName(commission.getLastName());
        commissionDto.setEmail(commission.getEmail());
        commissionDto.setCommissionNumber(commission.getCommissionNumber());
        commissionDto.setAbout(commission.getAbout());
        return commissionDto;
    }
    public Commission mapToEntity(CommissionDto commissionDto){
        Commission commission=new Commission();
        commission.setCommissionId(commissionDto.getCommissionId());
        commission.setFirstName(commissionDto.getFirstName());
        commission.setLastName(commissionDto.getLastName());
        commission.setCommissionNumber(commissionDto.getCommissionNumber());
        commission.setEmail(commissionDto.getEmail());
        commission.setAbout(commissionDto.getAbout());

        return  commission;
    }


}


