package com.ssafy.lofi.controller;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.ssafy.lofi.dto.response.*;
import com.ssafy.lofi.service.FoundArticleService;
import com.ssafy.lofi.service.LostArticleService;
import com.ssafy.lofi.service.MainService;
import com.ssafy.lofi.service.MissingPersonService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;



import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import java.util.*;


@RestController
@RequestMapping("api/main")
@CrossOrigin("*")
@RequiredArgsConstructor
public class MainController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final MissingPersonService missingPersonService;
    private final LostArticleService lostArticleService;
    private final FoundArticleService foundArticleService;
    private final MainService mainService;
    @Transactional
    @GetMapping("/person")
    public void getLostPerson(
            @RequestParam("esntlId") int esntlId,
            @RequestParam("authKey") String authKey,
            @RequestParam("rowSize") int rowSize
    ) throws ParseException, java.text.ParseException {

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("esntlId", esntlId);
        params.add("authKey", authKey);
        params.add("rowSize", rowSize);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://www.safe182.go.kr/api/lcm/findChildList.do",
                HttpMethod.POST,
                entity,
                String.class
                );
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
        Long totalCount = (Long) jsonObject.get("totalCount");
        Long pageNo = (totalCount / rowSize) + 1;

        List<MissingPersonAPIResponse> apiResponses = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        for (int i = 1; i <= pageNo; i++){
            params.set("page", i);
            response = rt.exchange(
                    "https://www.safe182.go.kr/api/lcm/findChildList.do",
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            jsonObject = (JSONObject) jsonParser.parse(response.getBody());
            apiResponses.addAll(mapper.convertValue(jsonObject.get("list"), TypeFactory.defaultInstance().constructCollectionType(List.class, MissingPersonAPIResponse.class)));
        }

        // apiResponse ??? ????????? ????????? ???????????? ?????????

        // ?????? db??? ??????????????????.
        missingPersonService.saveMissingPersonAPIData(apiResponses);
    }

    @Transactional
    @DeleteMapping("/person")
    public void deleteMissingPerson(){
        missingPersonService.deleteMissingPersonAPIData();
    }

    @Transactional
    @GetMapping("/article")
    public void getLostArticle(String startDate, String endDate) throws IOException, java.text.ParseException {
        int numOfRows = 1000;
        int pageNo = 1;

        // ????????? ????????? ?????? API
        List<String> idList = new ArrayList<>();
        Map<String, Integer> idMap = new HashMap<>();
        boolean flag = true;
        while (flag){
            flag = lostArticleService.getLostArticleList(numOfRows, pageNo++, idMap, startDate, endDate);
        }

        // idList ????????? ?????? ????????? ??????
        List<String> insertList = new ArrayList<>();
        List<String> deleteList = new ArrayList<>();
        lostArticleService.checkIdExist(idMap, insertList, deleteList);

        // insertList ????????? ????????? ???????????? API ???????????? db??? ????????????
        lostArticleService.callDetailAPIAndSaveLostArticle(insertList);

        // deleteList ????????? db?????? deleted update?????????
        lostArticleService.deleteLostArticle(deleteList);
    }

    @Transactional
    @GetMapping("/found")
    public void getFoundArticle(String startDate, String endDate) throws IOException, java.text.ParseException {
        int numOfRows = 1000;
        int pageNo = 1;

        // ????????? ????????? ?????? API
        List<String> idList = new ArrayList<>();
        Map<String, Integer> idMap = new HashMap<>();
        boolean flag = true;
        while (flag) {
            flag = foundArticleService.getFoundArticleList(numOfRows, pageNo++, idMap, startDate, endDate);
        }

        // idList ????????? ?????? ????????? ??????
        List<String> insertList = new ArrayList<>();
        List<String> deleteList = new ArrayList<>();
        foundArticleService.checkIdExist(idMap, insertList, deleteList);

        // idList ????????? ????????? ???????????? API ???????????? db??? ????????????
        foundArticleService.callDetailAPIAndSaveFoundArticle(insertList);

        // deleteList ????????? db?????? deleted update?????????
        foundArticleService.deleteFoundArticle(deleteList);
    }

    @ApiOperation(value = "?????? ???????????? ?????????", notes = "?????? ????????? ????????? ??? ????????? ??????")
    @GetMapping("/list")
    public ResponseEntity<?> getList(@RequestParam String category, @RequestParam Double lat, @RequestParam Double lon){
        String[] categorys = category.split(",");
        Map<String,Object> result = new HashMap<>();

        for (String s: categorys) {
            switch (s){
                case "animal":
                    List<MissingAnimalDto> missingAnimalDtos = mainService.selectAnimalsBylatlon(lat,lon);
                    result.put(s,missingAnimalDtos);
                    break;
                case "article":
                    List<LostArticleDto> lostArticleDtos = mainService.selectArticlesBylatlon(lat,lon);
                    result.put(s,lostArticleDtos);
                    break;
                case "found":
                    List<FoundArticleDto> foundArticleDtos = mainService.selectFoundsArticleBylatlon(lat,lon);
                    result.put(s,foundArticleDtos);
                    break;
                case "person":
                    List<MissingPersonDto> missingPersonDtos = mainService.selectPersonsBylatlon(lat,lon);
                    result.put(s,missingPersonDtos);
                    break;
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
