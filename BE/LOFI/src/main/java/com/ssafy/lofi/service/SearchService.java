package com.ssafy.lofi.service;

import com.ssafy.lofi.db.repository.MissingAnlmalRepository;
import com.ssafy.lofi.db.repository.MissingPersonRepository;
import com.ssafy.lofi.dto.response.MissingAnimalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final FoundArticleService foundArticleService;
    private final LostArticleService lostArticleService;
    private final MissingAnlmalRepository missingAnlmalRepository;
    private final MissingPersonRepository missingPersonRepository;

    public String[] keywordtoPython(String keyword) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "j7b102.p.ssafy.io:8000/api/keyword/" + keyword;
        String s = restTemplate.getForObject(url,String.class);
        return s.split(" ");
    }

    public void getAnimal(String[] keywords, String category) {
        List<MissingAnimalDto> missingAnimalDtos = new ArrayList<>();
        //List<MissingAnimal> missingAnimals = missingAnlmalRepository.findBykeywords;
//        for (MissingAnimal missingAnimal: missingAnimals) {
//            MissingAnimalDto missingAnimalDto = new MissingAnimalDto(missingAnimal);
//        }
        //return missingAnimalDtos;
    }

    public void getPerson(String[] keywords, String category) {
        // 중복 제거로 set 사용
        //set<MissingPerson> missingPerson = missingPersonRepository;
    }

    public void getFound(String[] keywords, String category) {

    }

    public void getArticle(String[] keywords, String category) {

    }

}