package com.roshanadke.mvvmexpensedairy.domain.mapper;

public interface DomainMapper<Entity, DomainModel> {

    DomainModel mapToDomainModel(Entity entity);

    Entity mapFromDomainModel(DomainModel domainModel);

}
