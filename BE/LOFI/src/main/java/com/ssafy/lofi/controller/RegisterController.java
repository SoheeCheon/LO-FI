package com.ssafy.lofi.controller;

import com.ssafy.lofi.config.security.UserDetailsImpl;
import com.ssafy.lofi.db.entity.User;
import com.ssafy.lofi.dto.request.*;
import com.ssafy.lofi.dto.response.UserDto;
import com.ssafy.lofi.service.RegisterService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sun.java2d.pipe.RegionIterator;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/register")
public class RegisterController {

    private final RegisterService registerService;

    @ApiOperation(value = "회원가입", notes = "이메일과 비밀번호 등록")
    @PostMapping(value = "/signUp")
    public ResponseEntity<?> signup(@RequestBody SignUpDto signUpDto){
        User user = registerService.signUp(signUpDto);
        if(user.getId() == null){
            return ResponseEntity.badRequest().build();
        }else {
            return ResponseEntity.ok().build();
        }
    }

    @ApiOperation(value = "마이페이지", notes = "유저Id, 이메일, 보유 포인트를 불러온다.", response = UserDto.class)
    @GetMapping(value = "/myPage")
    public ResponseEntity<?> myPage(@AuthenticationPrincipal UserDetailsImpl userInfo){
        UserDto user = registerService.mypage(userInfo.getName());
        return ResponseEntity.ok().body(user);
    }

    @ApiOperation(value = "회원탈퇴", notes = "유저 정보 삭제")
    @PostMapping(value = "/delete")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetailsImpl userInfo){
        registerService.deleteuser(userInfo.getId());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "실종동물 등록", notes = "입력된 실종동물 데이터 등록")
    @PostMapping(value = "/missingAnimal")
    public ResponseEntity<?> registerMissingAnimal(@RequestBody MissingAnimalRequest missingAnimalRequest,@AuthenticationPrincipal UserDetailsImpl userInfo){
        Long id = registerService.registerMissingAnimal(missingAnimalRequest,userInfo.getId());
        return ResponseEntity.ok().body(id);
    }

    @ApiOperation(value = "실종자등록", notes = "입력된 실종자 데이터 등록")
    @PostMapping(value = "/missingPerson")
    public ResponseEntity<?> registerMissingPerson(@RequestBody MissingPersonRequest missingPersonRequest,@AuthenticationPrincipal UserDetailsImpl userInfo){
        Long id = registerService.registerMissingPerson(missingPersonRequest,userInfo.getId());
        return ResponseEntity.ok().body(id);
    }

    @ApiOperation(value = "분실물 등록", notes = "입력된 분실물 등록")
    @PostMapping(value = "/lostArticle")
    public ResponseEntity<?> registerMissingPerson(@RequestBody LostArticleRequest lostArticleRequest,@AuthenticationPrincipal UserDetailsImpl userInfo){
        Long id = registerService.registerLostArticle(lostArticleRequest,userInfo.getId());
        return ResponseEntity.ok().body(id);
    }

    @ApiOperation(value = "습득물 등록", notes = "입력된 습득물 등록")
    @PostMapping(value = "/foundArticle")
    public ResponseEntity<?> registerMissingPerson(@RequestBody FoundArticleRequest foundArticleRequest,@AuthenticationPrincipal UserDetailsImpl userInfo){
        Long id = registerService.registerFoundArticle(foundArticleRequest,userInfo.getId());
        return ResponseEntity.ok().body(id);
    }

    @ApiOperation(value = "내가 등록한 게시물", notes = "사용자가 등록한 게시물들 반환")
    @GetMapping("/myboard")
    public ResponseEntity<?> getMyBoard(@AuthenticationPrincipal UserDetailsImpl userInfo){
        Map<String,Object> result = registerService.selectMyboard(userInfo.getId());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    
}
