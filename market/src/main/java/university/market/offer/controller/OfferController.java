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
import university.market.offer.service.OfferService;
import university.market.offer.service.dto.request.OfferRequest;
import university.market.offer.service.dto.request.UpdateOfferRequest;
import university.market.offer.service.dto.response.OfferResponse;

@RestController
@RequestMapping("/api/offer")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PostMapping("/")
    public ResponseEntity<Void> sendOffer(@RequestBody OfferRequest offerRequest) {
        offerService.createOffer(offerRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/{offerId}")
    public ResponseEntity<OfferResponse> getOfferById(@PathVariable Long offerId) {
        OfferResponse offer = offerService.getOfferById(offerId);
        return ResponseEntity.ok(offer);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/accept/{offerId}")
    public ResponseEntity<Void> acceptOffer(@PathVariable Long offerId) {
        offerService.acceptOffer(offerId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PostMapping("/item/{itemId}")
    public ResponseEntity<List<OfferResponse>> getOffersByItemId(@PathVariable Long itemId) {
        List<OfferResponse> offers = offerService.getOffersByItemId(itemId);
        return ResponseEntity.ok(offers);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PostMapping("/member/{memberId}")
    public ResponseEntity<List<OfferResponse>> getOffersByMemberId(@PathVariable Long memberId) {
        List<OfferResponse> offers = offerService.getOffersByMemberId(memberId);
        return ResponseEntity.ok(offers);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @DeleteMapping("/{offerId}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long offerId) {
        offerService.deleteOffer(offerId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PatchMapping("/")
    public ResponseEntity<Void> updateOffer(@RequestBody UpdateOfferRequest offerRequest) {
        offerService.updateOffer(offerRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
