package net.afl.aflstats.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.afl.aflstats.model.entity.AflTeamRef;
import net.afl.aflstats.model.repository.AflTeamRefRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AflReferenceService {
    
    private final AflTeamRefRepository aflTeamRefRepository;

    @Cacheable("reference")
    public Map<String, String> getAflTeamRefMapLongNameToShort() {
        List<AflTeamRef> aflTeamRef = aflTeamRefRepository.findAll();

        Map<String, String> refMap = aflTeamRef.stream()
            .collect(Collectors.toMap(AflTeamRef::getLongTeamName, AflTeamRef::getShortTeamName));

        log.info("Loading AflTeamRef long name to short name map: map={}", refMap);

        return refMap;
    }
}
