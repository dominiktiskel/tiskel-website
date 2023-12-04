package com.tiskel.website.service.impl;

import com.tiskel.website.domain.SalesLead;
import com.tiskel.website.repository.SalesLeadRepository;
import com.tiskel.website.service.SalesLeadService;
import com.tiskel.website.service.dto.SalesLeadDTO;
import com.tiskel.website.service.mapper.SalesLeadMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.tiskel.website.domain.SalesLead}.
 */
@Service
@Transactional
public class SalesLeadServiceImpl implements SalesLeadService {

    private final Logger log = LoggerFactory.getLogger(SalesLeadServiceImpl.class);

    private final SalesLeadRepository salesLeadRepository;

    private final SalesLeadMapper salesLeadMapper;

    public SalesLeadServiceImpl(SalesLeadRepository salesLeadRepository, SalesLeadMapper salesLeadMapper) {
        this.salesLeadRepository = salesLeadRepository;
        this.salesLeadMapper = salesLeadMapper;
    }

    @Override
    public SalesLeadDTO save(SalesLeadDTO salesLeadDTO) {
        log.debug("Request to save SalesLead : {}", salesLeadDTO);
        SalesLead salesLead = salesLeadMapper.toEntity(salesLeadDTO);
        salesLead = salesLeadRepository.save(salesLead);
        return salesLeadMapper.toDto(salesLead);
    }

    @Override
    public SalesLeadDTO update(SalesLeadDTO salesLeadDTO) {
        log.debug("Request to update SalesLead : {}", salesLeadDTO);
        SalesLead salesLead = salesLeadMapper.toEntity(salesLeadDTO);
        salesLead = salesLeadRepository.save(salesLead);
        return salesLeadMapper.toDto(salesLead);
    }

    @Override
    public Optional<SalesLeadDTO> partialUpdate(SalesLeadDTO salesLeadDTO) {
        log.debug("Request to partially update SalesLead : {}", salesLeadDTO);

        return salesLeadRepository
            .findById(salesLeadDTO.getId())
            .map(existingSalesLead -> {
                salesLeadMapper.partialUpdate(existingSalesLead, salesLeadDTO);

                return existingSalesLead;
            })
            .map(salesLeadRepository::save)
            .map(salesLeadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalesLeadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SalesLeads");
        return salesLeadRepository.findAll(pageable).map(salesLeadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SalesLeadDTO> findOne(Long id) {
        log.debug("Request to get SalesLead : {}", id);
        return salesLeadRepository.findById(id).map(salesLeadMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SalesLead : {}", id);
        salesLeadRepository.deleteById(id);
    }
}
