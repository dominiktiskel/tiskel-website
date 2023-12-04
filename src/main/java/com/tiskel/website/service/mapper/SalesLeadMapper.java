package com.tiskel.website.service.mapper;

import com.tiskel.website.domain.SalesLead;
import com.tiskel.website.service.dto.SalesLeadDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SalesLead} and its DTO {@link SalesLeadDTO}.
 */
@Mapper(componentModel = "spring")
public interface SalesLeadMapper extends EntityMapper<SalesLeadDTO, SalesLead> {}
