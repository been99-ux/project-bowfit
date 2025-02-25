package com.example.finaln.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Controller
public class KaKaoLoginPageController {

    //카카오 rest api key
    private String kakaoKey = "7d541892904af3321d67d3663459a5a1";

    //카카오로그인시 2차 인증 비밀번호!
    private String clientSecret ="tBijhS7YDoQqeIcLxALkF6bfacvm9ucN";

    // 카카오 인가코드 url (Get요청)
    private String kakaoUrl = "https://kauth.kakao.com/oauth/authorize";

    //카카오에서 응답받은 Redirect URI 요청을 처리하는
    // 메서드를 만들기!

    @GetMapping("/kakaoresp")
    public String kakaoresp(Model model ,String code) {
        System.out.println("카카오에서 받은 인가코드:"+ code);
        model.addAttribute("code",code);

        try{
            //1.url
            String tokenUrl ="https://kauth.kakao.com/oauth/token";

            // 2. http객체생성
            RestTemplate restTemplate = new RestTemplate();

            // 3. 자바의 map 자동으로 key,value 문자열로 자동변환
            MultiValueMap<String,String> body = new LinkedMultiValueMap<>();

            // 4. 상자안에 데이터 넣기
            body.add("grant_type", "authorization_code");
            body.add("client_id",kakaoKey);
            body.add("client_secret",clientSecret);
            body.add("code",code);
            body.add("redirect_uri","http://localhost:9020/main");

            //5.안에 데이터를 담았으니 이제는 header를 만든다.
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            //6.body와 헤더를 묶는다.
            HttpEntity<MultiValueMap<String,String >> req= new HttpEntity<>(body,headers);

            //7. 전송!
            ResponseEntity<String> resp = restTemplate
                    .postForEntity(tokenUrl,req,String.class);
            //8. 응답받은 내용 꺼내기!
            System.out.println(resp.getBody());

            // 카카오는 토큰 발급 받으면 몇시간 동안은 동의화면이
            // 안 뜬다. 위에서 발급 받은 access_token 값 꺼내오기
            // key와 value을 이용해서 문자열로 변환된 데이터를
            // 자바 객체로 변경하는 작업!
            // ObjectMapper json데이터를 다루는 도구!
            ObjectMapper mapper = new ObjectMapper();

            // 문자열로 되어있는 것을 읽어온다.
            JsonNode root = mapper.readTree(resp.getBody());

            // 변환한 데이터를 꺼내기!
            String accessToken = root.get("access_token").asText();

            System.out.println("토큰값:"+ accessToken);

            // 정보 가져오기!
            String infoUrl = "https://kapi.kakao.com/v2/user/me";

            //카카오서버한테 정보달라고 한다!
            RestTemplate restTemplate2 = new RestTemplate();

            // 헤더만 생성
            //"Authorization: Bearer ${ACCESS_TOKEN}"
            HttpHeaders headers2 = new HttpHeaders();

            headers2.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers2.set("Authorization","Bearer "+accessToken);

            // 헤더를 entity안에 저장
            // Void 없다!(body가 없다!)
            HttpEntity<Void> req2 = new HttpEntity<>(headers2);

            /*
             * ForEntity() post로 보낸 상태코드,헤더,바디 모두 확인해야
             *            될 경우 (자세히 응답을 처리)
             *            택배요청 후 응답!
             *           <html></html> xml , key,value (json), String
             * ForObject() get요청을 이용해서 헤더만,혹은 바디만 확인
             *            할때 (간단하게 응답할 때 )
             *            온라인 주문확인
             * */

            ResponseEntity<String> resp2 =
                    restTemplate2.exchange(
                            infoUrl,
                            HttpMethod.GET,
                            req2,
                            String.class
                    );
//            getForObject(infoUrl,req2,String.class);

            System.out.println(resp2.getBody());

            //json형태로 온 것을 자바객체로 변환
            ObjectMapper mapper2 = new ObjectMapper();

            //readTree
            JsonNode root2 = mapper2.readTree(resp2.getBody());
            String imageUrl = root2.get("properties").get("profile_image").asText();

            System.out.println("imgUrl:"+ imageUrl);
            //타임리프로 보내기
            // 자바 코드에서 html코드로 데이터를 전달하는 객체!
            model.addAttribute("imageUrl",imageUrl);


        }catch (Exception e){
            e.printStackTrace();
        }
        return "/loginPage/login";
    }

}