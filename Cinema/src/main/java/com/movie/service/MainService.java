package com.movie.service;

import com.movie.domain.Events;
import com.movie.mapper.MainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    @Autowired
    private final MainMapper mainMapper;

    public List<Events> Mainevent() {
        return mainMapper.mainevent();  // Mapper 메서드 호출
    }


}
