package com.tiskel.website.service.mapper;

import com.tiskel.website.domain.DemoMeeting;
import com.tiskel.website.service.dto.DemoMeetingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DemoMeeting} and its DTO {@link DemoMeetingDTO}.
 */
@Mapper(componentModel = "spring")
public interface DemoMeetingMapper extends EntityMapper<DemoMeetingDTO, DemoMeeting> {}
