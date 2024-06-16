package university.market.offer.domain;

public enum OfferStatus {
    OFFER, ACCEPT, DECLINE, CANCEL;

    public static OfferStatus fromValue(String value) {
        return switch (value.toUpperCase()) {
            case "OFFER" -> OFFER;
            case "ACCEPT" -> ACCEPT;
            case "DECLINE" -> DECLINE;
            case "CANCEL" -> CANCEL;
            default -> throw new IllegalArgumentException("없는 OfferStatus 입니다. value = " + value);
        };
    }
}
