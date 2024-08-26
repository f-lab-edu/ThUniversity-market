package university.market.offer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.auth.AuthType;
import university.market.member.utils.http.HttpRequest;
import university.market.offer.service.OfferService;
import university.market.offer.service.dto.request.OfferRequest;
import university.market.offer.service.dto.response.OfferResponse;

@Tag(name = "Offer", description = "가격 제안 관련 API")
@RestController
@RequestMapping("/api/offer")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;
    private final HttpRequest httpRequest;

    @Operation(summary = "가격 제안")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PostMapping("/")
    public ResponseEntity<Void> sendOffer(@RequestBody OfferRequest offerRequest) {
        offerService.createOffer(offerRequest, httpRequest.getCurrentMember());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "가격 제안 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/{offerId}")
    public ResponseEntity<OfferResponse> getOfferById(@PathVariable Long offerId) {
        OfferResponse offer = offerService.getOfferById(offerId, httpRequest.getCurrentMember());
        return ResponseEntity.ok(offer);
    }

    @Operation(summary = "가격 제안 - 가격 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PatchMapping("/{offerId}/price")
    public ResponseEntity<Void> updateOffer(@PathVariable Long offerId,
                                            @RequestBody int price) {
        offerService.updatePriceOffer(offerId, price, httpRequest.getCurrentMember());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "가격 제안 - 제안 상태 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PatchMapping("/{offerId}/status")
    public ResponseEntity<Void> updateOffer(@PathVariable Long offerId,
                                            @RequestBody String status) {
        offerService.updateStatusOffer(offerId, status, httpRequest.getCurrentMember());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "상품별 가격 제안 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<OfferResponse>> getOffersByItemId(@PathVariable Long itemId) {
        List<OfferResponse> offers = offerService.getOffersByItemId(itemId, httpRequest.getCurrentMember());
        return ResponseEntity.ok(offers);
    }

    @Operation(summary = "자신의 가격 제안 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/member")
    public ResponseEntity<List<OfferResponse>> getOffersByMemberId() {
        List<OfferResponse> offers = offerService.getOffersByMemberId(httpRequest.getCurrentMember());
        return ResponseEntity.ok(offers);
    }

    @Operation(summary = "가격 제안 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @DeleteMapping("/{offerId}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long offerId) {
        offerService.deleteOffer(offerId, httpRequest.getCurrentMember());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
