package com.tiskel.website.service.impl;

import com.tiskel.website.domain.DemoMeeting;
import com.tiskel.website.repository.DemoMeetingRepository;
import com.tiskel.website.service.DemoMeetingService;
import com.tiskel.website.service.dto.DemoMeetingDTO;
import com.tiskel.website.service.mapper.DemoMeetingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.tiskel.website.domain.DemoMeeting}.
 */
@Service
@Transactional
public class DemoMeetingServiceImpl implements DemoMeetingService {

    private final Logger log = LoggerFactory.getLogger(DemoMeetingServiceImpl.class);

    private final DemoMeetingRepository demoMeetingRepository;

    private final DemoMeetingMapper demoMeetingMapper;

    public DemoMeetingServiceImpl(DemoMeetingRepository demoMeetingRepository, DemoMeetingMapper demoMeetingMapper) {
        this.demoMeetingRepository = demoMeetingRepository;
        this.demoMeetingMapper = demoMeetingMapper;
    }

    @Override
    public DemoMeetingDTO save(DemoMeetingDTO demoMeetingDTO) {
        log.debug("Request to save DemoMeeting : {}", demoMeetingDTO);
        DemoMeeting demoMeeting = demoMeetingMapper.toEntity(demoMeetingDTO);
        demoMeeting = demoMeetingRepository.save(demoMeeting);
        return demoMeetingMapper.toDto(demoMeeting);
    }

    @Override
    public DemoMeetingDTO update(DemoMeetingDTO demoMeetingDTO) {
        log.debug("Request to update DemoMeeting : {}", demoMeetingDTO);
        DemoMeeting demoMeeting = demoMeetingMapper.toEntity(demoMeetingDTO);
        demoMeeting = demoMeetingRepository.save(demoMeeting);
        return demoMeetingMapper.toDto(demoMeeting);
    }

    @Override
    public Optional<DemoMeetingDTO> partialUpdate(DemoMeetingDTO demoMeetingDTO) {
        log.debug("Request to partially update DemoMeeting : {}", demoMeetingDTO);

        return demoMeetingRepository
            .findById(demoMeetingDTO.getId())
            .map(existingDemoMeeting -> {
                demoMeetingMapper.partialUpdate(existingDemoMeeting, demoMeetingDTO);

                return existingDemoMeeting;
            })
            .map(demoMeetingRepository::save)
            .map(demoMeetingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DemoMeetingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DemoMeetings");
        return demoMeetingRepository.findAll(pageable).map(demoMeetingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DemoMeetingDTO> findOne(Long id) {
        log.debug("Request to get DemoMeeting : {}", id);
        return demoMeetingRepository.findById(id).map(demoMeetingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DemoMeeting : {}", id);
        demoMeetingRepository.deleteById(id);
    }
}
