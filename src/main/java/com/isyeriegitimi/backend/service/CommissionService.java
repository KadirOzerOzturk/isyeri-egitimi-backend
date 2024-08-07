package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.CommissionDto;
import com.isyeriegitimi.backend.model.Commission;
import com.isyeriegitimi.backend.repository.CommissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommissionService {

    private final CommissionRepository commissionRepository;

    @Autowired
    public CommissionService(CommissionRepository commissionRepository) {
        this.commissionRepository = commissionRepository;
    }








    public Optional<Commission> getCommissionByCommissionNo(String komisyonNo) {
        return commissionRepository.findByKomisyonNo(komisyonNo);
    }

    public CommissionDto getCommissionById(Long commissionId) {
        Optional<Commission>  commission =commissionRepository.findById(commissionId);
        if (commission.isEmpty()){
            return null
                    ;
        }
        return mapToDto(commission.get());
    }


    public void  update(Long commissionId, CommissionDto commissionDto) {
        Optional<Commission> commission=commissionRepository.findById(commissionId);
        if (commission.isEmpty()){
            return  ;
        }
        commissionRepository.save(mapToEntity(commissionDto));

    }


    public CommissionDto mapToDto(Commission commission){
        CommissionDto commissionDto=new CommissionDto();
        commissionDto.setKomisyonAd(commission.getKomisyonAd());
        commissionDto.setKomisyonId(commission.getKomisyonId());
        commissionDto.setKomisyonEposta(commission.getKomisyonEposta());
        commissionDto.setKomisyonNo(commission.getKomisyonNo());
        commissionDto.setKomisyonSoyad(commission.getKomisyonSoyad());
        commissionDto.setKomisyonParola(commission.getKomisyonParola());
        commissionDto.setKomisyonHakkinda(commission.getKomisyonHakkinda());
        return commissionDto;
    }
    public Commission mapToEntity(CommissionDto commissionDto){
        Commission commission=new Commission();
        commission.setKomisyonId(commissionDto.getKomisyonId());
        commission.setKomisyonAd(commissionDto.getKomisyonAd());
        commission.setKomisyonSoyad(commissionDto.getKomisyonSoyad());
        commission.setKomisyonEposta(commissionDto.getKomisyonEposta());
        commission.setKomisyonParola(commissionDto.getKomisyonParola());
        commission.setKomisyonNo(commissionDto.getKomisyonNo());
        commission.setKomisyonHakkinda(commissionDto.getKomisyonHakkinda());
        return  commission;
    }
}


