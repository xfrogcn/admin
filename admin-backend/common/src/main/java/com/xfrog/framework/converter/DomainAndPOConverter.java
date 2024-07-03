package com.xfrog.framework.converter;


import java.util.List;

public interface DomainAndPOConverter<D, P> {
    D toDomain(P po);

    P toPO(D domain);

    List<D> toDomainList(List<P> pos);

    List<P> toPOList(List<D> domains);
}
