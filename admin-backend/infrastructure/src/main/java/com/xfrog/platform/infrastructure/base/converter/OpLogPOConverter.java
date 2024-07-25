package com.xfrog.platform.infrastructure.base.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.framework.oplog.domain.OpLog;
import com.xfrog.platform.infrastructure.base.dataobject.OpLogPO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OpLogPOConverter extends DomainAndPOConverter<OpLog, OpLogPO> {
    OpLogPOConverter INSTANCE = Mappers.getMapper(OpLogPOConverter.class);
}