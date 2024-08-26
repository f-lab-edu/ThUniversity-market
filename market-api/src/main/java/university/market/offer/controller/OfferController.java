package university.market.offer.controller;

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

@RestController
@RequestMapping("/api/offer")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;
    private final HttpRequest httpRequest;

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PostMapping("/")
    public ResponseEntity<Void> sendOffer(@RequestBody OfferRequest offerRequest) {
        offerService.createOffer(offerRequest, httpRequest.getCurrentMember());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/{offerId}")
    public ResponseEntity<OfferResponse> getOfferById(@PathVariable Long offerId) {
        OfferResponse offer = offerService.getOfferById(offerId, httpRequest.getCurrentMember());
        return ResponseEntity.ok(offer);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PatchMapping("/{offerId}/price")
    public ResponseEntity<Void> updateOffer(@PathVariable Long offerId,
                                            @RequestBody int price) {
        offerService.updatePriceOffer(offerId, price, httpRequest.getCurrentMember());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PatchMapping("/{offerId}/status")
    public ResponseEntity<Void> updateOffer(@PathVariable Long offerId,
                                            @RequestBody String status) {
        offerService.updateStatusOffer(offerId, status, httpRequest.getCurrentMember());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PostMapping("/item/{itemId}")
    public ResponseEntity<List<OfferResponse>> getOffersByItemId(@PathVariable Long itemId) {
        List<OfferResponse> offers = offerService.getOffersByItemId(itemId, httpRequest.getCurrentMember());
        return ResponseEntity.ok(offers);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/member")
    public ResponseEntity<List<OfferResponse>> getOffersByMemberId() {
        List<OfferResponse> offers = offerService.getOffersByMemberId(httpRequest.getCurrentMember());
        return ResponseEntity.ok(offers);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @DeleteMapping("/{offerId}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long offerId) {
        offerService.deleteOffer(offerId, httpRequest.getCurrentMember());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
